package ua.in.checkbox.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ua.in.checkbox.api.client.dto.*;
import ua.in.checkbox.api.client.dto.cashier.DetailedCashierModel;
import ua.in.checkbox.api.client.dto.good.GoodModel;
import ua.in.checkbox.api.client.dto.receipt.ReceiptModel;
import ua.in.checkbox.api.client.dto.receipt.ReceiptSellPayload;
import ua.in.checkbox.api.client.dto.receipt.ReceiptServicePayload;
import ua.in.checkbox.api.client.dto.shift.ShiftWithCashRegisterModel;
import ua.in.checkbox.api.client.dto.shift.ShiftWithCashierAndCashRegister;
import ua.in.checkbox.api.client.utils.CheckboxApiCallException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

@Slf4j
public class CheckboxApiClient
{
    private static final String API_PREFIX_TEMPLATE = "%s/api/v%d";
    private static final String SHIFTS_END_POINT = "/shifts";
    private static final String GOODS_END_POINT = "/goods";
    private static final String CASHIER_END_POINT = "/cashier";
    private static final String RECEIPTS_END_POINT = "/receipts";

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
                .uri(URI.create(apiPrefix + CASHIER_END_POINT + "/signin"))
                .header("Content-Type", "application/json")
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpURLConnection.HTTP_OK)
            {
                Bearer bearer = mapper.readValue(response.body(), Bearer.class);
                token = bearer.getAccessToken();
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
        return getForObject(DetailedCashierModel.class, URI.create(apiPrefix + CASHIER_END_POINT + "/me"));
    }

    public ReceiptModel findReceiptById(String id)
    {
        return getForObject(ReceiptModel.class, URI.create(apiPrefix + RECEIPTS_END_POINT + "/" + id));
    }

    public ShiftWithCashierAndCashRegister findShiftById(String id)
    {
        return getForObject(ShiftWithCashierAndCashRegister.class, URI.create(apiPrefix + SHIFTS_END_POINT + "/" + id));
    }

    public GoodModel findGoodById(String id)
    {
        return getForObject(GoodModel.class, URI.create(apiPrefix + GOODS_END_POINT + "/" + id));
    }

    public ReceiptModel sell(ReceiptSellPayload receipt)
    {
        return postForObject(ReceiptModel.class, receipt,
            URI.create(apiPrefix + RECEIPTS_END_POINT + "/sell"), HttpURLConnection.HTTP_CREATED);
    }

    public ReceiptModel service(ReceiptServicePayload receipt)
    {
        return postForObject(ReceiptModel.class, receipt,
            URI.create(apiPrefix + RECEIPTS_END_POINT + "/service"), HttpURLConnection.HTTP_CREATED);
    }

    public ShiftWithCashierAndCashRegister openShift(String xLicenseKey)
    {
        return postForObject(
            ShiftWithCashierAndCashRegister.class,
            null,
            builder -> builder.header("X-License-Key", xLicenseKey),
            URI.create(apiPrefix + SHIFTS_END_POINT),
            HttpURLConnection.HTTP_ACCEPTED);
    }

    public ShiftWithCashierAndCashRegister closeShift()
    {
        return postForObject(
            ShiftWithCashierAndCashRegister.class,
            URI.create(apiPrefix + SHIFTS_END_POINT + "/close"),
            HttpURLConnection.HTTP_ACCEPTED);
    }

    public ShiftWithCashRegisterModel getCurrentShift()
    {
        return getForObject(ShiftWithCashRegisterModel.class, URI.create(apiPrefix + CASHIER_END_POINT + "/shift"));
    }

    public PaginatedResult<ShiftWithCashierAndCashRegister> getShifts()
    {
        return getForObject(new TypeReference<>(){}, URI.create(apiPrefix + SHIFTS_END_POINT));
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
                log.error("API call error", e);
                throw CheckboxApiCallException.builder().build();
            }
        }, uri);
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
        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
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
