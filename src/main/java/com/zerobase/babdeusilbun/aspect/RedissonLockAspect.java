package com.zerobase.babdeusilbun.aspect;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static com.zerobase.babdeusilbun.util.RedissonLockUtil.*;

import com.zerobase.babdeusilbun.annotation.RedissonLockKeyType;
import com.zerobase.babdeusilbun.annotation.RedissonLock;
import com.zerobase.babdeusilbun.exception.CustomException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class RedissonLockAspect {

  private final RedissonClient redissonClient;

  @Around("@annotation(redissonLock)")
  public Object handleRedissonLockAnnotation(
      ProceedingJoinPoint joinPoint, RedissonLock redissonLock
  ) throws Throwable {

    // 메서드의 파라미터 이름
    String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
    // 각 파라미터 별 인자
    Object[] args = joinPoint.getArgs();

    // value로 넘어온 이름의 파라미터 찾음
    String parameterValue = null;
    for (int i = 0; i < parameterNames.length; i++) {
      if (parameterNames[i].equals(redissonLock.value())) {
        parameterValue = args[i].toString();
        break;
      }
    }
    validateParameterValue(parameterValue);

    RLock lock = redissonClient.getLock(getRedissonKey(redissonLock.key(), parameterValue));

    try {
      boolean isLocked = lock.tryLock(10, 10, TimeUnit.SECONDS);

      validateLockTimeout(isLocked);

      return joinPoint.proceed();

    } catch (RuntimeException | InterruptedException e) {
      throw new RuntimeException(e);

    } finally {
      lock.unlock();
    }
  }

  // 파라미터로 넘어온 값이 올바른지 확인
  private void validateParameterValue(String parameterValue) {
    if (parameterValue == null) {
      throw new CustomException(REDISSON_LOCK_FAIL_OBTAIN);
    }
  }

  // lock timeout 확인
  private void validateLockTimeout(boolean isLocked) {
    if (!isLocked) {
      throw new CustomException(REDISSON_LOCK_TIMEOUT);
    }
  }

  private String getRedissonKey(RedissonLockKeyType redissonLockKeyType, String parameterValue) {

    if (Objects.requireNonNull(redissonLockKeyType) == RedissonLockKeyType.PAYMENT) {
      return getPaymentLockKey(parameterValue);
    }

    throw new CustomException(REDISSON_LOCK_FAIL_OBTAIN);
  }


}
