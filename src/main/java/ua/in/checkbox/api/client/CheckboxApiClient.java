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
import java.util.StringJoiner;
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

    private static final int MIN_WIDTH_TEXT = 10;
    private static final int MAX_WIDTH_TEXT = 250;
    private static final int MIN_CHARS_PNG_RECEIPT = 22;
    private static final int MAX_CHARS_PNG_RECEIPT = 100;
    private static final int MIN_WIDTH_PNG_RECEIPT = 40;
    private static final int MAX_WIDTH_PNG_RECEIPT = 80;

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

    public String getReportTextById(String id,int width)
    {
        String widthParam =
                width >= MIN_WIDTH_TEXT && width <= MAX_WIDTH_TEXT
                        ? "?width=" + width
                        : "";
        return getForString(URI.create(apiPrefix + REPORTS_PATH + "/" + id + "/text" + widthParam));
    }

    public String getReportTextById(String id)
    {
        return getReportTextById(id, 0);
    }

    public String getReportXMLById(String id)
    {
        return getForString(URI.create(apiPrefix + REPORTS_PATH + "/" + id + "/xml"));
    }

    public String getReceiptHtmlById(String id)
    {
        return getReceiptHtmlById(id,false);
    }

    public String getReceiptHtmlById(String id,boolean isSimple)
    {
        return getForString(URI.create(apiPrefix + RECEIPTS_PATH + "/" + id+ "/html"+ (isSimple ? "?simple=true" : "" )));
    }

    public String getReceiptTextById(String id,int width)
    {
        String widthParam =
                width >= MIN_WIDTH_TEXT && width <= MAX_WIDTH_TEXT
                        ? "?width=" + width
                        : "";
        return getForString(URI.create(apiPrefix + RECEIPTS_PATH + "/" + id + "/text" + widthParam));
    }

    public String getReceiptTextById(String id)
    {
        return getReceiptTextById(id, 0);
    }

    public byte[] getReceiptPngById(String id)
    {
        return getReceiptPngById(id,0,0);
    }

    public byte[] getReceiptPngById(String id,int charsCount, int paperWidth)
    {
        StringJoiner parameters = new StringJoiner("&","?","");
        parameters.setEmptyValue("");
        if(charsCount >= MIN_CHARS_PNG_RECEIPT && MAX_CHARS_PNG_RECEIPT <= 100)
            parameters.add("width="+charsCount);
        if(paperWidth >= MIN_WIDTH_PNG_RECEIPT && MAX_WIDTH_PNG_RECEIPT <= 80)
            parameters.add("paper_width=" + paperWidth);
        return getForBytes(URI.create(apiPrefix + RECEIPTS_PATH + "/" + id + "/png" + parameters));
    }

    public byte[] getReceiptPdfById(String id)
    {
        return getForBytes(URI.create(apiPrefix + RECEIPTS_PATH + "/" + id + "/pdf"));
    }

    public byte[] getReceiptQRCodeById(String id)
    {
        return getForBytes(URI.create(apiPrefix + RECEIPTS_PATH + "/" + id + "/qrcode"));
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
            catch (Exception e)
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
            catch (Exception e)
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
            catch (Exception e)
            {
                log.error("API call error", e);
                throw CheckboxApiCallException.builder().build();
            }
        }, uri);
    }

    private String getForString(URI uri)
    {
        return getForObjectImpl(httpResponse -> new String(httpResponse.body(), StandardCharsets.UTF_8), uri);
    }
    private byte[] getForBytes(URI uri)
    {
        return getForObjectImpl(httpResponse -> httpResponse.body(), uri,null);
    }

    private <T> T getForObjectImpl(Function<HttpResponse<byte[]>, T> responseFunction, URI uri)
    {
        return getForObjectImpl(responseFunction, uri, null);
    }

    private <T> T getForObjectImpl(Function<HttpResponse<byte[]>, T> responseFunction, URI uri,  Function<HttpRequest.Builder,HttpRequest.Builder> httpRequestCustomBuilder)
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
            HttpResponse<byte[]> response = httpClient.send(request.build(), HttpResponse.BodyHandlers.ofByteArray());
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
