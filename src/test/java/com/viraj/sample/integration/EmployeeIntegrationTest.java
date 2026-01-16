package com.viraj.sample.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viraj.sample.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void create_then_get_ok() throws Exception {
        Employee req = new Employee("Luis", "QA");

        String body = mockMvc.perform(post("/employee/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").isNumber())
                .andReturn().getResponse().getContentAsString();

        Employee created = objectMapper.readValue(body, Employee.class);

        mockMvc.perform(get("/employee/getone/" + created.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeName").value("Luis"));
    }

    @Test
    void get_notFound_returns_404() throws Exception {
        mockMvc.perform(get("/employee/getone/999999"))
                .andExpect(status().isNotFound());
    }
}
