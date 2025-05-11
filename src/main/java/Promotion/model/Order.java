package Promotion.model;

import java.math.BigDecimal;
import java.util.List;

//This class represents a single customer order from the orders.json input file.
//It holds the order ID, total value, and the list of applicable promotion IDs.
public class Order {
    private String id;
    private BigDecimal value;
    private List<String> promotions;

    public Order() {}

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    public List<String> getPromotions() {
        return promotions;
    }
    public void setPromotions(List<String> promotions) {
        this.promotions = promotions;
    }
}
