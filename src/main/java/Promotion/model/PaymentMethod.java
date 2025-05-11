package Promotion.model;

import java.math.BigDecimal;
//This class represents a single payment method available to the customer, including its discount rate and spending limit.
//Used to match with applicable promotions and to determine how much can be spent using each method.
public class PaymentMethod {
    private String id;
    private BigDecimal discount;
    private BigDecimal limit;

    public PaymentMethod() {}


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public BigDecimal getDiscount() {
        return discount;
    }
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    public BigDecimal getLimit() {
        return limit;
    }
    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentMethod that = (PaymentMethod) o;
        return id != null && id.equalsIgnoreCase(that.id);
    }
    @Override
    public int hashCode() {
        if (id == null) return 0;
        return id.toLowerCase().hashCode();
    }
}
