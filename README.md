# Checkbox APIs Client Library for Java

This API Client is a wrapper around https://checkbox.in.ua/ REST API. Please see the [API documentation](https://dev-api.checkbox.in.ua/api/redoc) for more information.

Example of usage:
```Java
CheckboxApiClient client = new CheckboxApiClient(
    "login",
    "password",
    "https://dev-api.checkbox.in.ua",
    apiVersion);
client.signIn();
ShiftWithCashierAndCashRegister shift = client.openShift("X-License-Key");
```
