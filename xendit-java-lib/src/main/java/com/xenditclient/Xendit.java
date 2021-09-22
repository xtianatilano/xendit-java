package com.xenditclient;

import com.xenditclient.network.BaseRequest;
import com.xenditclient.network.NetworkClient;

public class Xendit {

  private static volatile NetworkClient requestClient = new BaseRequest();

  public static String apiKey;

  public static Option Opt = new Option();

  public static NetworkClient getRequestClient() {
    return requestClient;
  }

  public static void setRequestClient(NetworkClient requestClient) {
    Xendit.requestClient = requestClient;
  }

  public static class Option {

    private String apiKey;

    public String getApiKey() {
      return apiKey;
    }

    public Option setApiKey(String secretKey) {
      this.apiKey = secretKey;
      return this;
    }

    public String getXenditURL() {
      return "https://api.xendit.co";
    }
  }
}
