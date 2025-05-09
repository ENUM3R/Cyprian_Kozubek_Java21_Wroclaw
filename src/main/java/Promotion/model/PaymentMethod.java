package Promotion.model;

public class PaymentMethod {
    private String id;
    private int discount;
    private double limit;

    public PaymentMethod() {}


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getDiscount() {
        return discount;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }
    public double getLimit() {
        return limit;
    }
    public void setLimit(double limit) {
        this.limit = limit;
    }

}
