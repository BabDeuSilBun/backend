package com.zerobase.babdeusilbun.domain;


import com.zerobase.babdeusilbun.enums.EvaluateBadge;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 평가
 */
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@Table(name = "evaluate",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"meeting_id", "evaluateeId", "evaluatorId", "content"}
        )
    })
public class Evaluate extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "evaluate_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meeting_id", nullable = false)
  private Meeting meeting;

  @Column(nullable = false)
  private Long evaluateeId;
  @Column(nullable = false)
  private Long evaluatorId;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private EvaluateBadge content;

}
