package com.zerobase.babdeusilbun.config;

import static java.lang.String.format;

import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class CustomDataInitializer {
//  @Autowired
  private DataSource dataSource;
//  @Autowired
  private JdbcTemplate jdbcTemplate;
  private final List<String> schemas = List.of("table", "school", "major", "category");

//  @Bean
  @Transactional
  public ApplicationRunner initializeDatabase() {
    return args -> {
      try (Connection connection = dataSource.getConnection()) {
        schemas.forEach(schema -> {
          if (checkIfSchemaEmpty(schema)) {
            try {
              ScriptUtils.executeSqlScript(connection, new ClassPathResource(format("%s.sql", schema)));
            } catch (Exception e) {
              log.error("Failed to execute SQL file for {}. ", schema);
            }
          }
        });
      } catch (Exception e) {
        log.error("Failed to obtain database connection.");
      }
    };
  }

  private boolean checkIfSchemaEmpty(String schema) {
    //테이블이 비어있을 시 실행
    Integer count = jdbcTemplate.queryForObject(format("SELECT COUNT(*) FROM %s", schema), Integer.class);

    return count == null || count == 0;
  }
}
