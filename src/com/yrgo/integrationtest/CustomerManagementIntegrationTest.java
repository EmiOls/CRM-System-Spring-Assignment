package com.yrgo.integrationtest;

import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/other-tiers.xml", "/datasource-test.xml"})
@Transactional
public class CustomerManagementIntegrationTest {

    private final CustomerManagementService customerManagementService;
    @Autowired
    public CustomerManagementIntegrationTest(CustomerManagementService customerManagementService) {
        this.customerManagementService = customerManagementService;
    }
    @Test
    public void testCreatingNewCustomer() {
        assertDoesNotThrow(() -> customerManagementService.newCustomer(new Customer("CS03939", "Acme", "Good Customer")));
    }

    @Test
    public void testFindingExistingCustomer() {
        customerManagementService.newCustomer(new Customer("CS03939", "Acme", "Good Customer"));
        assertDoesNotThrow(() -> customerManagementService.findCustomerById("CS03939"));
    }
}
