package com.viraj.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viraj.sample.entity.Employee;
import com.viraj.sample.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private EmployeeService employeeService;

    @Test
    void hello_ok() throws Exception {
        mockMvc.perform(get("/employee/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello boot"));
    }

    @Test
    void getAll_ok() throws Exception {
        Employee e1 = new Employee("Ana", "Dev"); e1.setEmployeeId(1L);
        when(employeeService.getAllEmployees()).thenReturn(List.of(e1));

        mockMvc.perform(get("/employee/getall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value(1));
    }

    @Test
    void save_ok() throws Exception {
        Employee req = new Employee("Ana", "Dev");
        Employee res = new Employee("Ana", "Dev"); res.setEmployeeId(5L);
        when(employeeService.saveEmployee(org.mockito.ArgumentMatchers.any(Employee.class))).thenReturn(res);

        mockMvc.perform(post("/employee/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(5));
    }
}

