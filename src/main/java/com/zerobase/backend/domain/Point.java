package com.zerobase.backend.domain;

import com.zerobase.backend.enums.PointStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "point") @Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class Point extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id", nullable = false)
    private Long id;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "order_payment_id", nullable = false)
    private OrderPayment orderPayment;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PointStatus status;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long amount;
}
