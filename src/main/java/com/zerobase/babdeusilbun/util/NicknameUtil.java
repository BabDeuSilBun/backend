package com.zerobase.babdeusilbun.util;

public class NicknameUtil {

  public static String createRandomNickname() {
    String[] modifiers = {
        "배고픈", "굶주린", "예민한", "소심한", "대담한", "인기많은", "섬세한"
    };
    String[] nouns = {
        "돼지", "병아리", "고양이", "강아지", "플라톤",
        "아리스토텔레스", "소크라테스", "학생", "대학원생",
        "예비대학원생", "탈출자", "기숙사생", "망령"
    };

    StringBuilder nickname = new StringBuilder();

    nickname.append(modifiers[(int) (Math.random() * modifiers.length)]);
    nickname.append(nouns[(int) (Math.random() * nouns.length)]);
    for(int i = 0; i < 3; i++) {
      nickname.append((int) (Math.random() * 10));
    }

    return nickname.toString();
  }

}
