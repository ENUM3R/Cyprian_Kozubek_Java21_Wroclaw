package Promotion.service;
import Promotion.model.Order;
import Promotion.model.PaymentMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

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
