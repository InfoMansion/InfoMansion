package com.infomansion.server.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApiResponseTestController.class)
class ApiResponseTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("ApiResponse가 지정된 형식에 맞게 잘 출력된다.")
    @WithMockUser
    @Test
    void ApiResponse_성공() throws Exception {
        mockMvc.perform(
                get("/test/success"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").value("test"));
    }

    @DisplayName("ErrorResponse가 지정된 형식에 맞게 잘 출력된다.")
    @WithMockUser
    @Test
    void ApiResponse_실패() throws Exception {
        mockMvc.perform(
                        get("/test/error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("지원하지 않는 Http Method 방식입니다."));
    }
}
