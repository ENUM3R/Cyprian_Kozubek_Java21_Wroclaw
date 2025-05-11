package Promotion.service;
import Promotion.model.Order;
import Promotion.model.PaymentMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

//The InputParser class is responsible for reading data from JSON files and
//converting them into Java objects using Jackson library
public class InputParser {

    //This method reads a JSON file containing a list of Order objects, converts the JSON into a List<Order>, and returns it.
    //If an error occurs during the reading or parsing process, it prints the error stack trace and returns an empty list.
    public List<Order> parseOrder(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.readValue(new File(filename), new TypeReference<List<Order>>(){});
        } catch (Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

    //This method reads a JSON file containing a list of PaymentMethod objects,
    //converts the JSON into a List<PaymentMethod>, and then creates a map (Map<PaymentMethod, BigDecimal>).
    //The map associates each PaymentMethod with its corresponding limit value.
    //If an error occurs, it prints the error stack trace and returns an empty map.
    public Map<PaymentMethod, BigDecimal> parsePaymentMethods(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<PaymentMethod> methods = mapper.readValue(new File(filename),
                    new TypeReference<List<PaymentMethod>>(){});
            Map<PaymentMethod,BigDecimal> map = new HashMap<>();
            for(PaymentMethod method : methods){
                map.put(method, method.getLimit());
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
}
