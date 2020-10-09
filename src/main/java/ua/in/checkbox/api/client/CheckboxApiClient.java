package ua.in.checkbox.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ua.in.checkbox.api.client.dto.Bearer;
import ua.in.checkbox.api.client.dto.ErrorDetails;
import ua.in.checkbox.api.client.dto.PaginatedResult;
import ua.in.checkbox.api.client.dto.shift.Shift;
import ua.in.checkbox.api.client.dto.good.GoodModel;
import ua.in.checkbox.api.client.dto.receipt.ReceiptModel;
import ua.in.checkbox.api.client.dto.receipt.ReceiptSellPayload;
import ua.in.checkbox.api.client.dto.shift.ShiftWithCashierAndCashRegister;
import ua.in.checkbox.api.client.utils.CheckboxApiCallException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
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
    private HttpClient httpClient = HttpClient.newHttpClient();;
    private String apiPrefix;
    private String login;
    private String password;
    private ObjectMapper mapper = new ObjectMapper();

    public CheckboxApiClient(String login, String password, String apiUrl, int apiVersion)
    {
        apiPrefix = String.format(API_PREFIX_TEMPLATE, apiUrl, apiVersion);
        this.login = login;
        this.password = password;
    }

    public void authorize()
    {
        try
        {
            String json = new StringBuilder()
                .append("{")
                .append(String.format("\"login\":\"%s\",", login))
                .append(String.format("\"password\":\"%s\"", password))
                .append("}").toString();
            HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
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
            //TODO remove
            e.printStackTrace();
            log.error("API call error", e.getMessage());
            throw CheckboxApiCallException.builder().build();
        }
    }

    public boolean isAuthorized()
    {
        return token != null;
    }

    public Shift findShiftById(String id)
    {
        return getForObject(Shift.class, URI.create(apiPrefix + SHIFTS_END_POINT + "/" + id));
    }

    public GoodModel findGoodById(String id)
    {
        return getForObject(GoodModel.class, URI.create(apiPrefix + GOODS_END_POINT + "/" + id));
    }

    public ReceiptModel sell(ReceiptSellPayload receipt)
    {
        return postForObject(ReceiptModel.class, Optional.of(receipt),
            URI.create(apiPrefix + RECEIPTS_END_POINT + "/sell"), HttpURLConnection.HTTP_CREATED);
    }

    public ShiftWithCashierAndCashRegister openShift(String xLicenseKey)
    {
        return postForObject(
            ShiftWithCashierAndCashRegister.class,
            Optional.empty(),
            builder -> builder.header("X-License-Key", xLicenseKey),
            URI.create(apiPrefix + SHIFTS_END_POINT),
            HttpURLConnection.HTTP_ACCEPTED);
    }

    public ShiftWithCashierAndCashRegister closeShift()
    {
        return postForObject(
            ShiftWithCashierAndCashRegister.class,
            Optional.empty(),
            URI.create(apiPrefix + SHIFTS_END_POINT + "/close"),
            HttpURLConnection.HTTP_ACCEPTED);
    }

    public PaginatedResult<ShiftWithCashierAndCashRegister> getShifts()
    {
        return getForObject(new TypeReference<>(){}, URI.create(apiPrefix + SHIFTS_END_POINT));
    }

    private <T> T postForObject(Class<T> returnType, Optional postData, URI uri, int successHttpCode)
    {
        return postForObject(returnType, postData, null, uri, successHttpCode);
    }

    private <T> T postForObject(Class<T> returnType, Optional postData, Function<HttpRequest.Builder,HttpRequest.Builder> httpRequestCustomBuilder, URI uri, int successHttpCode)
    {
        try
        {
            HttpRequest.BodyPublisher publisher = postData.isPresent()
                ? HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(postData.get()))
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
            //TODO remove
            e.printStackTrace();
            log.error("API call error", e.getMessage());
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
                //TODO remove
                e.printStackTrace();
                log.error("API call error", e.getMessage());
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
                //TODO remove
                e.printStackTrace();
                log.error("API call error", e.getMessage());
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
            //TODO remove
            e.printStackTrace();
            log.error("API call error", e.getMessage());
            throw CheckboxApiCallException.builder().build();
        }
    }
}
