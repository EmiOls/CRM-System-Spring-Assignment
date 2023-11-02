package com.yrgo.client;

import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class SimpleClient {
    public static void main(String[] args) {
        System.out.println("Testing managing customers...\n");

        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");

        var custManService = container.getBean(CustomerManagementService.class);
        var customers = custManService.getAllCustomers();
        //Print all
        printAllCustomers(customers);

        System.out.println("\nDeleting one customer....");
        var customersToDelete = custManService.findCustomersByName("River Ltd");
        custManService.deleteCustomer(customersToDelete.get(0));
        customers = custManService.getAllCustomers();
        printAllCustomers(customers);

        container.close();
    }

    private static void printAllCustomers(List<Customer> customers) {
        System.out.println("All customers: ");
        for (var customer : customers) {
            System.out.printf("* %s (%s)%n", customer.getCompanyName(), customer.getCustomerId());
        }
    }
}