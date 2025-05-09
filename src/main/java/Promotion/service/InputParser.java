package Promotion.service;
import Promotion.model.Order;
import Promotion.model.PaymentMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class InputParser {

    public void parseOrder(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            List<Order> orders = mapper.readValue(new File(filename), new TypeReference<List<Order>>(){});
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getID());
                System.out.println("Promotions: " + order.getPromotions());
                System.out.println("Value: " + order.getValue());
                System.out.println();
                System.out.println("-   -");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void parsePaymentMethods(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<PaymentMethod> methods = mapper.readValue(new File(filename), new TypeReference<List<PaymentMethod>>() {});
            for (PaymentMethod method : methods) {
                System.out.println("ID: " + method.getId());
                System.out.println("Discount: " + method.getDiscount());
                System.out.println("Limit: " + method.getLimit());
                System.out.println("-----");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
