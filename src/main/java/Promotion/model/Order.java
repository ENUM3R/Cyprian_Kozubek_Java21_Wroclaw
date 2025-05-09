package Promotion.model;

public class Order {
    private String orderID;
    private double value;
    private String promotions;

    public Order(String orderID, double value, String promotions) {
        this.orderID = orderID;
        this.value = value;
        this.promotions = promotions;
    }

    public String getOrderID() {
        return orderID;
    }
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public String getPromotions() {
        return promotions;
    }
    public void setPromotions(String promotions) {
        this.promotions = promotions;
    }
}
