package com.zerobase.babdeusilbun.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedissonLock {

  /**
   * 사용할 key 종류를 입력하면 됩니다.
   */
  RedissonLockKeyType key();

  /**
   * key의 생성값으로 넘겨줄 parameter의 이름을 넣으면 됩니다.
   */
  String value();
}
