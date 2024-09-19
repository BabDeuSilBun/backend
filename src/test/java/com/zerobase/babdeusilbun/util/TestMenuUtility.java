package com.zerobase.babdeusilbun.util;

import com.zerobase.babdeusilbun.domain.Menu;

public class TestMenuUtility {
    private static final Menu menu = Menu.builder()
            .id(1L)
            .store(TestStoreUtility.getStore())
            .name("가짜메뉴")
            .image("url")
            .description("가짜설명")
            .price(200L)
            .build();

    public static Menu getMenu() {
        return menu;
    }
}
