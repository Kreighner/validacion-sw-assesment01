package com.viraj.sample.service;

import com.viraj.sample.entity.Employee;
import com.viraj.sample.exception.ResourceNotFoundException;
import com.viraj.sample.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void saveEmployee_ok() {
        Employee e = new Employee("Ana", "Dev");
        when(employeeRepository.save(e)).thenReturn(e);

        Employee saved = employeeService.saveEmployee(e);

        assertEquals("Ana", saved.getEmployeeName());
        verify(employeeRepository).save(e);
    }

    @Test
    void getEmployee_exists_ok() {
        Employee e = new Employee("Ana", "Dev");
        e.setEmployeeId(10L);
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(e));

        Employee found = employeeService.getEmployee(10L);

        assertEquals(10L, found.getEmployeeId());
        verify(employeeRepository).findById(10L);
    }

    @Test
    void getEmployee_notFound_throws() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployee(99L));
    }

    @Test
    void updateEmployee_notFound_throws() {
        Employee e = new Employee("Ana", "Dev");
        e.setEmployeeId(77L);
        when(employeeRepository.existsById(77L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(e));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteEmployee_notFound_throws() {
        when(employeeRepository.existsById(55L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(55L));
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}