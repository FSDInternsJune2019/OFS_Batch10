package com.oracle;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.controller.EmployeeControllerImpl;
import com.oracle.dto.EmployeeDTO;
import com.oracle.service.EmployeeService;

@WebMvcTest(EmployeeControllerImpl.class)
public class EmployeeControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private EmployeeDTO employee;

    @BeforeEach
    public void setup() {
        employee = new EmployeeDTO();
        employee.setEmpId(101);
        employee.setEmpName("Sabbir");
        employee.setEmpSalary(50000.0);
    }

    @Test
    public void testGetAllEmployees_positive() throws Exception {
        when(employeeService.retrieveEmployees()).thenReturn(Arrays.asList(employee));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].empId").value(101));
    }
    
    @Test
    public void testGetAllEmployees_negative() throws Exception {
        when(employeeService.retrieveEmployees()).thenReturn(Arrays.asList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEmployeeByPathParam_Found() throws Exception {
        when(employeeService.retrieveEmployee(101)).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/101"))
               .andExpect(status().isFound())
               .andExpect(jsonPath("$.empName").value("Sabbir"));
    }

    @Test
    public void testGetEmployeeByPathParam_NotFound() throws Exception {
        when(employeeService.retrieveEmployee(999)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEmployeeByRequestParam_Found() throws Exception {
        when(employeeService.retrieveEmployee(101)).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employeesparam")
                .param("empId", "101"))
               .andExpect(status().isFound())
               .andExpect(jsonPath("$.empSalary").value(50000.0));
    }

    @Test
    public void testCreateEmployee_Success() throws Exception {
        when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenReturn("success");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
               .andExpect(status().isCreated())
               .andExpect(content().string("Employee Resource Created"));
    }

    @Test
    public void testUpdateEmployee_Success() throws Exception {
        when(employeeService.updateEmployee(101, 60000.0)).thenReturn("success");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/employees/101/60000.0"))
               .andExpect(status().isAccepted())
               .andExpect(content().string("Employee Resource Updated"));
    }

    @Test
    public void testDeleteEmployee_Success() throws Exception {
        when(employeeService.deleteEmployee(101)).thenReturn("success");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/101"))
               .andExpect(status().isAccepted())
               .andExpect(content().string("Employee Resource Deleted"));
    }

    @Test
    public void testDeleteEmployee_Failure() throws Exception {
        when(employeeService.deleteEmployee(999)).thenReturn("failure");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/999"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("Employee Resource Not Deleted"));
    }
}

