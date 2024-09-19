package com.zerobase.babdeusilbun.security.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class RefreshToken implements Serializable {

    private String email;

    private String refreshToken;

    private Date expiredDate;

}
