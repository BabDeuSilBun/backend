package com.zerobase.babdeusilbun.util;

public class ChatUtility {
  public final static String SEND_TO_CLIENT_PREFIX = "/meeting";
  public final static String SEND_TO_SERVER_PREFIX = "/socket";
  public final static String STOMP_PREFIX = "/stomp";
  public final static String CHAT_SEPARATOR = "/chatroom";

  public static String makeSocketDestination(String prefix, String separator, Long id) {
    return prefix + separator + "/" + id;
  }
}
