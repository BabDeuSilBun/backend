package com.zerobase.babdeusilbun.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Value("${mail.host}")
  private String host;
  @Value("${mail.username}")
  private String mail;
  @Value("${mail.port}")
  private Integer port;
  @Value("${mail.password}")
  private String password;

  @Bean
  public JavaMailSender javaMailService() {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    javaMailSender.setHost(host);
    javaMailSender.setUsername(mail);
    javaMailSender.setPassword(password);

    javaMailSender.setPort(port);

    javaMailSender.setJavaMailProperties(getMailProperties());

    return javaMailSender;
  }

  private Properties getMailProperties() {
    Properties properties = new Properties();
    properties.setProperty("mail.transport.protocol", "smtp");
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.starttls.enable", "true");
    properties.setProperty("mail.debug", "true");
    properties.setProperty("mail.smtp.ssl.trust",host);
//    properties.setProperty("mail.smtp.ssl.enable","true");
    return properties;
  }

}
