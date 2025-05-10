package Promotion.service;
import Promotion.model.Order;
import Promotion.model.PaymentMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class InputParser {

    public List<Order> parseOrder(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.readValue(new File(filename), new TypeReference<List<Order>>(){});
        } catch (Exception e){
            e.printStackTrace();
            return List.of();
        }
    }
    public List<PaymentMethod> parsePaymentMethods(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(filename), new TypeReference<List<PaymentMethod>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
