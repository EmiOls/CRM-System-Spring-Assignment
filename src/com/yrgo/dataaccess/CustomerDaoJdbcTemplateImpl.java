package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CustomerDaoJdbcTemplateImpl implements CustomerDao {
    private static final String CREATE_CUSTOMER_TABLE_SQL = "CREATE TABLE CUSTOMER (CUSTOMER_ID VARCHAR(255), COMPANY_NAME VARCHAR(255), EMAIL VARCHAR(255), TELEPHONE VARCHAR(255), NOTES VARCHAR(255))";
    private static final String CREATE_CALL_TABLE_SQL = "CREATE TABLE TBL_CALL (ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1), CUSTOMER_ID VARCHAR(255), TIME_AND_DATE VARCHAR(255), NOTES VARCHAR(255))";
    private static final String CREATE_CUSTOMER_SQL = "INSERT INTO CUSTOMER (CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES) " +
        "VALUES (?, ?, ?, ?, ?)";
    private static final String GET_CUSTOMER_BY_ID_SQL = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ?";
    private static final String GET_CUSTOMER_BY_NAME_SQL = "SELECT * FROM CUSTOMER WHERE COMPANY_NAME LIKE ?";
    private static final String UPDATE_CUSTOMER_SQL = "UPDATE CUSTOMER SET COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, " +
        "NOTES=? WHERE CUSTOMER_ID=?";
    private static final String DELETE_CUSTOMER_SQL = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?";
    private static final String GET_ALL_CUSTOMERS_SQL = "SELECT * FROM CUSTOMER";
    private static final String GET_ALL_CALLS_FOR_CUSTOMER_SQL = "SELECT * FROM TBL_CALL WHERE CUSTOMER_ID = ?";
    private static final String CREATE_CALL_SQL = "INSERT INTO TBL_CALL (CUSTOMER_ID, TIME_AND_DATE, NOTES) VALUES (?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;

    public CustomerDaoJdbcTemplateImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        createTables();
    }

    private void createTables() {
        try {
            this.jdbcTemplate.update(CREATE_CUSTOMER_TABLE_SQL);
            this.jdbcTemplate.update(CREATE_CALL_TABLE_SQL);
        } catch (org.springframework.jdbc.BadSqlGrammarException e) {
            System.out.println("The tables CUSTOMER and/or TBL_CALL table already exists");
        }
    }

    @Override
    public void create(Customer customer) {
        jdbcTemplate.update(CREATE_CUSTOMER_SQL,
            customer.getCustomerId(),
            customer.getCompanyName(),
            customer.getEmail(),
            customer.getTelephone(),
            customer.getNotes());
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        try {
            return jdbcTemplate.queryForObject(GET_CUSTOMER_BY_ID_SQL,
                new CustomerRowMapper(), customerId);
        } catch (Exception e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getByName(String name) {
        return jdbcTemplate.query(GET_CUSTOMER_BY_NAME_SQL, new CustomerRowMapper(), "%" + name + "%");
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        int updatedRowsCount = jdbcTemplate.update(UPDATE_CUSTOMER_SQL,
            customerToUpdate.getCompanyName(),
            customerToUpdate.getEmail(),
            customerToUpdate.getTelephone(),
            customerToUpdate.getNotes(),
            customerToUpdate.getCustomerId());
        if (updatedRowsCount == 0) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        int deletedRowsCount = jdbcTemplate.update(DELETE_CUSTOMER_SQL, oldCustomer.getCustomerId());
        if (deletedRowsCount == 0) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return jdbcTemplate.query(GET_ALL_CUSTOMERS_SQL, new CustomerRowMapper());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        try {
            Customer customer = jdbcTemplate.queryForObject(GET_CUSTOMER_BY_ID_SQL, new CustomerRowMapper(), customerId);
            if (customer == null) {
                throw new RecordNotFoundException();
            }
            List<Call> calls = jdbcTemplate.query(GET_ALL_CALLS_FOR_CUSTOMER_SQL,
                new CallRowMapper(),
                customerId);
            customer.setCalls(calls);

            return customer;
        } catch (Exception e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        try {
            jdbcTemplate.update(CREATE_CALL_SQL, customerId, Long.toString(newCall.getTimeAndDate().getTime()), newCall.getNotes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecordNotFoundException();
        }
    }

    static class CustomerRowMapper implements RowMapper<Customer> {
        public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
            String customerId = rs.getString("CUSTOMER_ID");
            String companyName = rs.getString("COMPANY_NAME");
            String email = rs.getString("EMAIL");
            String telephone = rs.getString("TELEPHONE");
            String notes = rs.getString("NOTES");

            return new Customer("" + customerId, companyName, email, telephone, notes);
        }
    }

    static class CallRowMapper implements RowMapper<Call> {
        @Override
        public Call mapRow(ResultSet rs, int arg1) throws SQLException {
            String notes = rs.getString("NOTES");
            Date timeAndDate = new Date(Long.parseLong(rs.getString("TIME_AND_DATE")));

            return new Call(notes, timeAndDate);
        }
    }
}