package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EntrepreneurDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateRequest {
        private String phoneNumber;
        private String postal;
        private String streetAddress;
        private String detailAddress;
        private String image;
        private String password;
    }

    public interface MyPage {
        Long getEntrepreneurId();

        String getEmail();

        String getName();

        String getPhoneNumber();
        String getBusinessNumber();

        Address getAddress();

        String getImage();
    }
}
