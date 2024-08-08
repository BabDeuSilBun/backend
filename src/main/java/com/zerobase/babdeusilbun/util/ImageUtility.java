package com.zerobase.babdeusilbun.util;

import java.util.Arrays;
import java.util.List;

public class ImageUtility {
  public static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
  public static final String MENU_IMAGE_FOLDER = "menu";
  public static final String STORE_IMAGE_FOLDER = "store";
  public static final String USER_IMAGE_FOLDER = "user";
  public static final String INQUIRY_IMAGE_FOLDER = "inquiry";

  public ImageUtility() {
  }

  public static boolean isImage(String imageFileName) {
    return hasExtension(imageFileName)
        && IMAGE_EXTENSIONS.contains(getExtension(imageFileName));
  }

  public static boolean hasExtension(String text) {
    return findExtensionStartIndex(text) > 0;
  }

  public static String getExtension(String text) {
    return text != null
        && findExtensionStartIndex(text) != 0 ? text.substring(findExtensionStartIndex(text)) : "";
  }

  public static int findExtensionStartIndex(String text) {
    return text.lastIndexOf(".") + 1;
  }
}