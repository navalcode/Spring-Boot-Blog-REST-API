package com.sopromadze.blogapi.controllerTest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.test.web.servlet.ResultMatcher;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseBodyMatchers {

    @Getter
    @Setter
    @NoArgsConstructor
    static public class ErrorResult {
        private String error;
        private int status;
        private List<String> messages;
        private String timestamp;
    }
}
