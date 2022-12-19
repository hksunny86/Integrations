package com.inov8.integration.vo.daewoo.json;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {

    @JsonProperty("CLIENT_TOKEN")
    private String token;

    @JsonProperty("API_OUT")
    private String apiOut;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApiOut() {
        return apiOut;
    }

    public void setApiOut(String apiOut) {
        this.apiOut = apiOut;
    }
}
