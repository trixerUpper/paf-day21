package paf.day21workshop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import paf.day21workshop.model.Customer;
import paf.day21workshop.model.Order;
import paf.day21workshop.repository.CustomerRepository;

@RestController
@RequestMapping(path = "/api/customers", produces=MediaType.APPLICATION_JSON_VALUE)
public class CustomerRestController {

    //customer injection
    public CustomerRepository customerRepository;
    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping()
    public ResponseEntity<String> getAllCustomers(@RequestParam(required = false) String offset, @RequestParam(required = false) String limit) {
        if(Objects.isNull(offset)) 
            offset = "0";
        if(Objects.isNull(limit)) 
            limit = "5";
        
        List<Customer> customers = customerRepository.getAllCustomers(Integer.parseInt(offset), Integer.parseInt(limit));

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(Customer c : customers) {
            arrayBuilder.add(c.toJson());
        }
        JsonArray result = arrayBuilder.build();

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }

    //fetch customer using customer id
    @GetMapping(path = "{customerId}")
    public ResponseEntity<String> getCustomerById(@PathVariable int customerId){
        JsonObject result = null;    
        try{
            Customer customer = customerRepository.findCustomerById(customerId);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("customer", customer.toJson());
            result = objectBuilder.build();
        } catch(IndexOutOfBoundsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body("{\"error_msg\":\"Customer not found\"}");

        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }

    //Fetch orders for a customer
    @GetMapping(path = "{customer_id}/orders")
    public ResponseEntity<String> getOrdersForCustomer(@PathVariable Integer customer_id) {

        List<Order> orders = new ArrayList<Order>();
        JsonArray result = null;

        orders = customerRepository.getCustomerOrders(customer_id);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Order o : orders) {
            arrayBuilder.add(o.toJson());
        }

        result = arrayBuilder.build();
        if (result.size() == 0)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error_msg\": \"record not found \"}");

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }
    
}
