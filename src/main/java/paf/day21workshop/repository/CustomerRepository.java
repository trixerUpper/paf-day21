package paf.day21workshop.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import paf.day21workshop.model.Customer;
import paf.day21workshop.model.Order;

@Repository
public class CustomerRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Fetch all customers
    public List<Customer> getAllCustomers(int offset, int limit) {
        List<Customer> customers = new ArrayList<Customer>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(DBQueries.SELECT_ALL_CUSTOMERS, offset, limit);
        
        while(rowSet.next()){
            customers.add(Customer.create(rowSet));
        }
        return customers;
    }

    //Fetch customer using id
    public Customer findCustomerById (int id) {
    List<Customer> customers = jdbcTemplate.query(DBQueries.SELECT_CUSTOMERS_BY_ID, new CustomerRowMapper(), new Object[]{id});
    return customers.get(0);
    }

    //Fetch orders for a customer
    public List<Order> getCustomerOrders(int id) {
        List<Order> orders = new ArrayList<Order>();
         SqlRowSet rs = jdbcTemplate.queryForRowSet(DBQueries.SELECT_ORDERS_FOR_CUSTOMERS, new Object[]{id});
        
         while (rs.next()) {
            orders.add(Order.create(rs));
        }
        return orders;
    }
}
