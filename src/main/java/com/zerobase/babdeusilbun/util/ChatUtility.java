package com.zerobase.babdeusilbun.util;

public class ChatUtility {
  public static String SEND_TO_CLIENT_PREFIX = "/meeting";
  public static String SEND_TO_SERVER_PREFIX = "/socket";
  public static String STOMP_PREFIX = "/stomp";
  public static String CHAT_SEPARATOR = "/chatroom";

  public static String makeSocketDestination(String prefix, String separator, Long id) {
    return prefix + separator + "/" + id;
  }
}
