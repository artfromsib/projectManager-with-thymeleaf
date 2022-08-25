package com.ym.projectManager.service.impl;

import com.ym.projectManager.model.Customer;
import com.ym.projectManager.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldReturnAllCustomersList(){
        List<Customer> customers = Arrays.asList(new Customer("Petrov Vasya", "Moscow"),
                new Customer("Ivanov Petya", "Ufa"));
        doReturn(customers).when(customerRepository).findAll();
        var actualResult = customerService.getAllCustomers();

        assertNotNull(actualResult);
        assertEquals(2, actualResult.size());
        assertEquals("Petrov Vasya", actualResult.get(0).getFullName());
        verify(customerRepository).findAll();
    }

    @Test
    void shouldReturnEmptyCustomersListIfCustomersNotFound(){
        doReturn(null)
                .when(customerRepository).findAll();
        var actualResult = customerService.getAllCustomers();
        assertNull(actualResult);
        verify(customerRepository).findAll();
    }

}
