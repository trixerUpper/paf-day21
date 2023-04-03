package paf.day21workshop.model;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonValue;

public class Order {
    private int id;
    private String shipName;
    private Double shippingFee;
    private Customer customer;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getShipName() {
        return shipName;
    }
    public void setShipName(String shipName) {
        this.shipName = shipName;
    }
    public Double getShippingFee() {
        return shippingFee;
    }
    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Order [customer=" + customer + ", id=" + id + ", shipName=" + shipName + ", shippingFee=" + shippingFee
                + "]";
    }

    // order_id,o.ship_name, o.shipping_fee
    public static Order create(SqlRowSet rs) {
        Order order = new Order();
        Customer customer = new Customer();

        customer.setId(rs.getInt("customer_id"));
        order.setCustomer(customer);
        order.setId(rs.getInt("order_id"));
        order.setShipName(rs.getString("ship_name"));
        order.setShippingFee(rs.getDouble("shipping_fee"));

        return order;
    }

    // c.company, o.id as order_id,o.ship_name, o.shipping_fee
    public JsonValue toJson() {
        return Json.createObjectBuilder()
                .add("customer_id", getCustomer().getId())
                .add("order_id", getId())
                .add("ship_name", getShipName())
                .add("Shipping_fee", getShippingFee())
                .build();
    }
}