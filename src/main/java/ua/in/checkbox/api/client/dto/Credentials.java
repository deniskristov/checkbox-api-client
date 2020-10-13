package ua.in.checkbox.api.client.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Credentials
{
    private String login;
    private String password;
}
