package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CustomerDaoJpaImpl implements CustomerDao {

    private static final String GET_CUSTOMER_BY_NAME_SQL = "SELECT c FROM Customer c WHERE c.companyName LIKE :name";
    private static final String GET_FULL_CUSTOMER_DETAILS_SQL = "SELECT c FROM Customer c LEFT JOIN FETCH c.calls WHERE c.customerId = :customerId";
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Customer customer) {
        em.persist(customer);
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        if (customer == null) {
            throw new RecordNotFoundException();
        }
        return customer;
    }

    @Override
    public List<Customer> getByName(String name) {
        return em
            .createQuery(GET_CUSTOMER_BY_NAME_SQL, Customer.class)
            .setParameter("name", "%" + name + "%")
            .getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        Customer currentCustomer = em.find(Customer.class, customerToUpdate.getCustomerId());
        if (currentCustomer == null) {
            throw new RecordNotFoundException();
        }
        currentCustomer.setCompanyName(customerToUpdate.getCompanyName());
        currentCustomer.setEmail(customerToUpdate.getEmail());
        currentCustomer.setTelephone(customerToUpdate.getTelephone());
        currentCustomer.setNotes(customerToUpdate.getNotes());
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        Customer currentCustomer = em.find(Customer.class, oldCustomer.getCustomerId());
        if (currentCustomer == null) {
            throw new RecordNotFoundException();
        }

        em.remove(currentCustomer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return em
            .createQuery("SELECT c FROM Customer c", Customer.class)
            .getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        try {
            Customer detailedCustomer = em
                .createQuery(GET_FULL_CUSTOMER_DETAILS_SQL, Customer.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
            if (detailedCustomer == null) {
                throw new RecordNotFoundException();
            }
            return detailedCustomer;
        } catch (Exception e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        Customer callOwner = getById(customerId);
        if (callOwner == null) {
            throw new RecordNotFoundException();
        }
        callOwner.getCalls().add(newCall);
    }
}
