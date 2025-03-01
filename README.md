# Checkbox REST API Client Library for Java
[![Java CI with Maven](https://github.com/deniskristov/checkbox-api-client/actions/workflows/maven.yml/badge.svg)](https://github.com/deniskristov/checkbox-api-client/actions/workflows/maven.yml)

This API Client is a wrapper around https://checkbox.ua/ REST API. Please see the [API documentation](https://dev-api.checkbox.in.ua/api/redoc) for more information.

Use as Maven dependency.
Add to your pom.xml repository and dependency:
```xml
<repository>
    <id>checkbox</id>
    <name>Checkbox API</name>
    <url>https://maven.pkg.github.com/deniskristov/checkbox-api-client</url>
    <releases>
        <enabled>true</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>

<dependency>
    <groupId>ua.in.checkbox</groupId>
    <artifactId>checkbox-api-client</artifactId>
    <version>{latest-version}</version>
</dependency>
```

Example of usage:
```Java
CheckboxApiClient client = new CheckboxApiClient(
    "login",
    "password",
    "https://dev-api.checkbox.in.ua",
    "X-Client-Name",
    apiVersion);
client.signIn();
ShiftWithCashierAndCashRegister shift = client.openShift("X-License-Key");
```
