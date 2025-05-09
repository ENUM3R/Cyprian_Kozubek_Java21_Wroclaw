package Promotion.model;

import java.util.List;

public class Order {
    private String id;
    private double value;
    private List<String> promotions;

    public Order() {}

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public List<String> getPromotions() {
        return promotions;
    }
    public void setPromotions(List<String> promotions) {
        this.promotions = promotions;
    }
}
