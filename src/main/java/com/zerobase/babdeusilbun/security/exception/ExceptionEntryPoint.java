package com.zerobase.babdeusilbun.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

//@Component
public class ExceptionEntryPoint  {


//  @Override
//  public void commence(HttpServletRequest request, HttpServletResponse response,
//      AuthenticationException authException) throws IOException, ServletException {
//
//    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    response.setCharacterEncoding("UTF-8");
//
//    PrintWriter writer = response.getWriter();
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    authException.ge
//
//    ApiResponse errorResponse = ApiResponse.error("페이지를 찾을 수 없습니다.");
//    writer.write(objectMapper.writeValueAsString(errorResponse));
//    writer.flush();
//
//  }
}
