package com.zerobase.backend.security.dto;

import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
//@RedisHash("redisRefreshToken")
public class RefreshToken implements Serializable {

//    @Id
//    private Long id;

    private String email;

    private String refreshToken;

    private String jwtToken;

    private Date expiredDate;

}
