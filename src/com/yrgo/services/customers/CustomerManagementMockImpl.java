package com.yrgo.services.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;

public class CustomerManagementMockImpl implements CustomerManagementService {
	private HashMap<String,Customer> customerMap;

	public CustomerManagementMockImpl() {
		customerMap = new HashMap<>();
		customerMap.put("OB74", new Customer("OB74" ,"Fargo Ltd", "some notes"));
		customerMap.put("NV10", new Customer("NV10" ,"North Ltd", "some other notes"));
		customerMap.put("RM210", new Customer("RM210" ,"River Ltd", "some more notes"));
	}


	@Override
	public void newCustomer(Customer newCustomer) {
		customerMap.put(newCustomer.getCustomerId(), newCustomer);
	}

	@Override
	public void updateCustomer(Customer changedCustomer) {
		customerMap.replace(changedCustomer.getCustomerId(), changedCustomer);
	}

	@Override
	public void deleteCustomer(Customer oldCustomer) {
		customerMap.remove(oldCustomer.getCustomerId());
	}

	@Override
	public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
		if (customerMap.get(customerId) != null) {
			return customerMap.get(customerId);
		}
		else {
			throw new CustomerNotFoundException();
		}
	}

	@Override
	public List<Customer> findCustomersByName(String name) {
		var customerList = new ArrayList<Customer>();
		for (var entry : customerMap.entrySet()) {
			if (entry.getValue().getCompanyName().equalsIgnoreCase(name)) {
				customerList.add(entry.getValue());
			}
		}
		return customerList;
	}

	@Override
	public List<Customer> getAllCustomers() {
		var customerList = new ArrayList<Customer>();
		for (var entry : customerMap.entrySet()) {
			customerList.add(entry.getValue());
		}
		return customerList;
	}

	@Override
	public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
		return findCustomerById(customerId);
	}

	@Override
	public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
		//First find the customer
		var customer = findCustomerById(customerId);
		//Call the addCall on the customer
		customer.addCall(callDetails);
	}

}
