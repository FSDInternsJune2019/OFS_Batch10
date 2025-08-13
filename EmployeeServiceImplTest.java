package com.oracle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oracle.dto.EmployeeDTO;
import com.oracle.entities.Employee;
import com.oracle.repositories.EmployeeRepository;
import com.oracle.service.EmployeeServiceImpl;

import java.util.List;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setEmpId(1);
        employee.setEmpName("John");
        employee.setEmpSalary(50000);

        employeeDTO = new EmployeeDTO();
        employeeDTO.setEmpId(1);
        employeeDTO.setEmpName("John");
        employeeDTO.setEmpSalary(50000);
    }

    @Test
    void testRetrieveEmployees() {
    	
    
    	   
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));
        when(modelMapper.map(employee, EmployeeDTO.class)).thenReturn(employeeDTO);

        List<EmployeeDTO> result = employeeService.retrieveEmployees();
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getEmpName());
    }

    @Test
    void testRetrieveEmployee_Found() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee, EmployeeDTO.class)).thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.retrieveEmployee(1);
        assertNotNull(result);
        assertEquals(1, result.getEmpId());
    }

    @Test
    void testRetrieveEmployee_NotFound() {
        when(employeeRepository.findById(2)).thenReturn(Optional.empty());

        EmployeeDTO result = employeeService.retrieveEmployee(2);
        assertNull(result);
    }

    @Test
    void testSaveEmployee_Success() {
        when(modelMapper.map(employeeDTO, Employee.class)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);

        String result = employeeService.saveEmployee(employeeDTO);
        assertEquals("success", result);
    }

    @Test
    void testSaveEmployee_Fail() {
        Employee savedEmployee = new Employee();
        savedEmployee.setEmpId(2); // Different ID
        when(modelMapper.map(employeeDTO, Employee.class)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(savedEmployee);

        String result = employeeService.saveEmployee(employeeDTO);
        assertEquals("fail", result);
    }

    @Test
    void testUpdateEmployee_Success() {
        when(employeeRepository.updateEmployeeSalary(1, 60000)).thenReturn(1);

        String result = employeeService.updateEmployee(1, 60000);
        assertEquals("success", result);
    }

    @Test
    void testUpdateEmployee_Fail() {
        when(employeeRepository.updateEmployeeSalary(1, 60000)).thenReturn(0);

        String result = employeeService.updateEmployee(1, 60000);
        assertEquals("fail", result);
    }

    @Test
    void testDeleteEmployee_Success() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById(1);

        String result = employeeService.deleteEmployee(1);
        assertEquals("success", result);
    }

    @Test
    void testDeleteEmployee_Fail() {
        when(employeeRepository.findById(2)).thenReturn(Optional.empty());

        String result = employeeService.deleteEmployee(2);
        assertEquals("fail", result);
    }
}
