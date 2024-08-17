package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Evaluate;
import com.zerobase.babdeusilbun.dto.EvaluateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EvaluateRepository extends JpaRepository<Evaluate, Long> {
    @Query(value=
            "select evaluate.content as content, ifnull(count(evaluate.content), 0) as count \n" +
                    "from com.zerobase.babdeusilbun.domain.User as user, \n" +
                    "com.zerobase.babdeusilbun.domain.Evaluate as evaluate \n" +
                    "where evaluate.evaluateeId = user.id and user.id = :userId and " +
                    "(evaluate.content = com.zerobase.babdeusilbun.enums.EvaluateBadge.GOOD_COMMUNICATION or \n" +
                    "evaluate.content = com.zerobase.babdeusilbun.enums.EvaluateBadge.GOOD_TIMECHECK or \n" +
                    "evaluate.content = com.zerobase.babdeusilbun.enums.EvaluateBadge.GOOD_TOGETHER or \n" +
                    "evaluate.content = com.zerobase.babdeusilbun.enums.EvaluateBadge.GOOD_RESPONSE) \n" +
                    "group by content \n")
    List<EvaluateDto.PositiveEvaluate> findPositiveEvaluatesByUserId(Long userId);

    @Query(value=
            "select evaluate.content as content, ifnull(count(evaluate.content), 0) as count \n" +
                    "from com.zerobase.babdeusilbun.domain.User as user, \n" +
                    "com.zerobase.babdeusilbun.domain.Evaluate as evaluate \n" +
                    "where evaluate.evaluateeId = user.id and user.id = :userId and " +
                    "(evaluate.content = com.zerobase.babdeusilbun.enums.EvaluateBadge.BAD_RESPONSE or \n" +
                    "evaluate.content = com.zerobase.babdeusilbun.enums.EvaluateBadge.BAD_TIMECHECK or \n" +
                    "evaluate.content = com.zerobase.babdeusilbun.enums.EvaluateBadge.BAD_TOGETHER) \n" +
                    "group by content \n")
    List<EvaluateDto.NegativeEvaluate> findNegativeEvaluatesByUserId(Long userId);

}
