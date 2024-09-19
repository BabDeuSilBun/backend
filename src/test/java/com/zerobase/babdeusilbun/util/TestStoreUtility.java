package com.zerobase.babdeusilbun.util;

import com.zerobase.babdeusilbun.domain.Address;
import com.zerobase.babdeusilbun.domain.Store;

import java.time.LocalTime;

public class TestStoreUtility {
    public static final Store store = Store.builder()
    .id(1L)
            .entrepreneur(TestEntrepreneurUtility.getEntrepreneur())
            .name("가짜가게")
            .description("가짜설명")
            .minPurchaseAmount(2L)
            .deliveryPrice(100L)
            .minDeliveryTime(10)
            .maxDeliveryTime(20)
            .address(Address.builder().postal("가짜주소1").streetAddress("가짜주소2").detailAddress("가짜주소3").build())
            .phoneNumber("000-111-2222")
            .openTime(LocalTime.MIN)
            .closeTime(LocalTime.MAX)
            .build();

    public static Store getStore() {
        return store;
    }
}
