package ua.in.checkbox.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import ua.in.checkbox.api.client.dto.*;
import ua.in.checkbox.api.client.dto.cashier.DetailedCashierModel;
import ua.in.checkbox.api.client.dto.cashier.SignatureTestResult;
import ua.in.checkbox.api.client.dto.cashregister.CashRegisterInfo;
import ua.in.checkbox.api.client.dto.cashregister.DetailedCashRegisterModel;
import ua.in.checkbox.api.client.dto.good.GoodModel;
import ua.in.checkbox.api.client.dto.receipt.ReceiptFilter;
import ua.in.checkbox.api.client.dto.receipt.ReceiptModel;
import ua.in.checkbox.api.client.dto.receipt.ReceiptSellPayload;
import ua.in.checkbox.api.client.dto.receipt.ReceiptServicePayload;
import ua.in.checkbox.api.client.dto.report.ReportModel;
import ua.in.checkbox.api.client.dto.shift.ShiftFilter;
import ua.in.checkbox.api.client.dto.shift.ShiftWithCashRegisterModel;
import ua.in.checkbox.api.client.dto.shift.ShiftWithCashierAndCashRegister;
import ua.in.checkbox.api.client.utils.CheckboxApiCallException;
import ua.in.checkbox.api.client.utils.DateDeserializer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Slf4j
public class CheckboxApiClient
{
    private static final String API_PREFIX_TEMPLATE = "%s/api/v%d";
    private static final String SHIFTS_PATH = "/shifts";
    private static final String GOODS_PATH = "/goods";
    private static final String CASHIER_PATH = "/cashier";
    private static final String RECEIPTS_PATH = "/receipts";
    private static final String RECEIPTS_SEARCH_PATH = RECEIPTS_PATH+"/search";
    private static final String REPORTS_PATH = "/reports";
    private static final String CASH_REGISTER_PATH = "/cash-registers";

    private String token;
    private HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiPrefix;
    private final String login;
    private final String password;
    private ObjectMapper mapper = new ObjectMapper();

    public CheckboxApiClient(String login, String password, String apiUrl, int apiVersion)
    {
        apiPrefix = String.format(API_PREFIX_TEMPLATE, apiUrl, apiVersion);
        this.login = login;
        this.password = password;
        SimpleModule dateModule = new SimpleModule();
        dateModule.addDeserializer(Date.class, new DateDeserializer());
        mapper.registerModule(dateModule);
    }

    public void signIn()
    {
        try
        {
            Credentials credentials = Credentials.builder()
                .login(login)
                .password(password)
                .build();
            HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(
                    mapper.writeValueAsString(credentials)))
                .uri(URI.create(apiPrefix + CASHIER_PATH + "/signin"))
                .header("Content-Type", "application/json")
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpURLConnection.HTTP_OK)
            {
                Bearer bearer = mapper.readValue(response.body(), Bearer.class);
                token = bearer.getAccessToken();
            }
            else if (response.statusCode() == 422)
            {
                // Validation error
                HTTPValidationError error = mapper.readValue(response.body(), HTTPValidationError.class);
                throw CheckboxApiCallException.builder()
                    .httpCode(response.statusCode())
                    .validationError(error)
                    .build();
            }
            else
            {
                ErrorDetails error = mapper.readValue(response.body(), ErrorDetails.class);
                throw CheckboxApiCallException.builder()
                    .httpCode(response.statusCode())
                    .error(error)
                    .build();
            }
        }
        catch (InterruptedException|IOException e)
        {
            log.error("API call error", e);
            throw CheckboxApiCallException.builder().build();
        }
    }

    public boolean isAuthorized()
    {
        return token != null;
    }

    public DetailedCashierModel me()
    {
        return getForObject(DetailedCashierModel.class, URI.create(apiPrefix + CASHIER_PATH + "/me"));
    }

    public SignatureTestResult checkSignature()
    {
        return getForObject(SignatureTestResult.class, URI.create(apiPrefix + CASHIER_PATH + "/check-signature"));
    }

    public ReceiptModel findReceiptById(String id)
    {
        return getForObject(ReceiptModel.class, URI.create(apiPrefix + RECEIPTS_PATH + "/" + id));
    }

    public PaginatedResult<ReceiptModel> findReceipts(ReceiptFilter receiptFilter)
    {
        URI uri = URI.create(apiPrefix + RECEIPTS_SEARCH_PATH+receiptFilter.toString());
        return getForObject(new TypeReference<>(){}, uri);
    }

    public CashRegisterInfo getCashRegisterInfo(String xLicenseKey)
    {
        return getForObject(CashRegisterInfo.class, URI.create(apiPrefix + CASH_REGISTER_PATH + "/info"), builder -> builder.header("X-License-Key", xLicenseKey));
    }

    public DetailedCashRegisterModel findCashRegisterById(String id)
    {
        return getForObject(DetailedCashRegisterModel.class, URI.create(apiPrefix + CASH_REGISTER_PATH + "/" + id));
    }

    public ShiftWithCashierAndCashRegister findShiftById(String id)
    {
        return getForObject(ShiftWithCashierAndCashRegister.class, URI.create(apiPrefix + SHIFTS_PATH + "/" + id));
    }

    public GoodModel findGoodById(String id)
    {
        return getForObject(GoodModel.class, URI.create(apiPrefix + GOODS_PATH + "/" + id));
    }

    /**
     * @param query is just a word (or phrase) that will be matched against all GoodModel fields
     * @return all GoodModels that contain query in any field
     */
    public PaginatedResult<GoodModel> findGoodByQuery(String query)
    {
        return getForObject(new TypeReference<>(){}, URI.create(apiPrefix + GOODS_PATH + "?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8)));
    }

    public ReceiptModel sell(ReceiptSellPayload receipt)
    {
        return postForObject(ReceiptModel.class, receipt,
            URI.create(apiPrefix + RECEIPTS_PATH + "/sell"), HttpURLConnection.HTTP_CREATED);
    }

    public ReceiptModel service(ReceiptServicePayload receipt)
    {
        return postForObject(ReceiptModel.class, receipt,
            URI.create(apiPrefix + RECEIPTS_PATH + "/service"), HttpURLConnection.HTTP_CREATED);
    }

    public ShiftWithCashierAndCashRegister openShift(String xLicenseKey)
    {
        return postForObject(
            ShiftWithCashierAndCashRegister.class,
            null,
            builder -> builder.header("X-License-Key", xLicenseKey),
            URI.create(apiPrefix + SHIFTS_PATH),
            HttpURLConnection.HTTP_ACCEPTED);
    }

    public ShiftWithCashierAndCashRegister closeShift()
    {
        return postForObject(
            ShiftWithCashierAndCashRegister.class,
            URI.create(apiPrefix + SHIFTS_PATH + "/close"),
            HttpURLConnection.HTTP_ACCEPTED);
    }

    public ShiftWithCashRegisterModel getCurrentShift()
    {
        return getForObject(ShiftWithCashRegisterModel.class, URI.create(apiPrefix + CASHIER_PATH + "/shift"));
    }

    public PaginatedResult<ShiftWithCashierAndCashRegister> getShifts(ShiftFilter shiftFilter)
    {
        return getForObject(new TypeReference<>(){}, URI.create(apiPrefix + SHIFTS_PATH + shiftFilter.toString()));
    }

    public ReportModel findReportById(String id)
    {
        return getForObject(ReportModel.class, URI.create(apiPrefix + REPORTS_PATH + "/" + id));
    }

    private <T> T postForObject(Class<T> returnType,  URI uri, int successHttpCode)
    {
        return postForObject(returnType, null, null, uri, successHttpCode);
    }

    private <T> T postForObject(Class<T> returnType, Object postData, URI uri, int successHttpCode)
    {
        return postForObject(returnType, postData, null, uri, successHttpCode);
    }

    private <T> T postForObject(Class<T> returnType, Object postData, Function<HttpRequest.Builder,HttpRequest.Builder> httpRequestCustomBuilder, URI uri, int successHttpCode)
    {
        try
        {
            HttpRequest.BodyPublisher publisher = postData != null
                ? HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(postData))
                : HttpRequest.BodyPublishers.noBody();
            HttpRequest.Builder request = HttpRequest.newBuilder()
                .POST(publisher)
                .uri(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token);
            if (httpRequestCustomBuilder != null)
            {
                request = httpRequestCustomBuilder.apply(request);
            }
            HttpResponse<String> response = httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == successHttpCode)
            {
                return mapper.readValue(response.body(), returnType);
            }
            else if (response.statusCode() == 422)
            {
                // Validation error
                HTTPValidationError error = mapper.readValue(response.body(), HTTPValidationError.class);
                throw CheckboxApiCallException.builder()
                    .httpCode(response.statusCode())
                    .validationError(error)
                    .build();
            }
            else
            {
                ErrorDetails error = mapper.readValue(response.body(), ErrorDetails.class);
                throw CheckboxApiCallException.builder()
                    .httpCode(response.statusCode())
                    .error(error)
                    .build();
            }
        }
        catch (InterruptedException|IOException e)
        {
            log.error("API call error", e);
            throw CheckboxApiCallException.builder().build();
        }
    }

    private <T> T getForObject(Class<T> returnType, URI uri)
    {
        return getForObjectImpl(response ->
        {
            try
            {
                return mapper.readValue(response.body(), returnType);
            }
            catch (JsonProcessingException e)
            {
                e.printStackTrace();
                log.error("API call error", e);
                throw CheckboxApiCallException.builder().build();
            }
        }, uri);
    }

    private <T> T getForObject(Class<T> returnType, URI uri, Function<HttpRequest.Builder,HttpRequest.Builder> httpRequestCustomBuilder)
    {
        return getForObjectImpl(response ->
        {
            try
            {
                return mapper.readValue(response.body(), returnType);
            }
            catch (JsonProcessingException e)
            {
                e.printStackTrace();
                log.error("API call error", e);
                throw CheckboxApiCallException.builder().build();
            }
        }, uri, httpRequestCustomBuilder);
    }

    private <T> T getForObject(TypeReference<T> returnType, URI uri)
    {
        return getForObjectImpl(response ->
        {
            try
            {
                return mapper.readValue(response.body(), returnType);
            }
            catch (JsonProcessingException e)
            {
                log.error("API call error", e);
                throw CheckboxApiCallException.builder().build();
            }
        }, uri);
    }

    private <T> T getForObjectImpl(Function<HttpResponse<String>, T> responseFunction, URI uri)
    {
        return getForObjectImpl(responseFunction, uri, null);
    }

    private <T> T getForObjectImpl(Function<HttpResponse<String>, T> responseFunction, URI uri,  Function<HttpRequest.Builder,HttpRequest.Builder> httpRequestCustomBuilder)
    {
        try
        {
            HttpRequest.Builder request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Authorization", "Bearer " + token);
            if (httpRequestCustomBuilder != null)
            {
                request = httpRequestCustomBuilder.apply(request);
            }
            HttpResponse<String> response = httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpURLConnection.HTTP_OK)
            {
                return responseFunction.apply(response);
            }
            else
            {
                ErrorDetails error = mapper.readValue(response.body(), ErrorDetails.class);
                throw CheckboxApiCallException.builder()
                    .httpCode(response.statusCode())
                    .error(error)
                    .build();
            }
        }
        catch (InterruptedException|IOException e)
        {
            log.error("API call error", e);
            throw CheckboxApiCallException.builder().build();
        }
    }
}
