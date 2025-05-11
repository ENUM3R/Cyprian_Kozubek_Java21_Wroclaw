package Promotion.model;

import java.util.Map;
import java.math.BigDecimal;

//This class represents the final decision on how a specific order was paid.
//It maps the chosen payment methods to the amounts used and records the total discount applied to the order.
public class PaymentDecision {
    private String orderId;
    private Map<PaymentMethod, BigDecimal> usedMethods;
    private BigDecimal discount;

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public Map<PaymentMethod, BigDecimal> getUsedMethods() {
        return usedMethods;
    }
    public void setUsedMethods(Map<PaymentMethod, BigDecimal> usedMethods) {
        this.usedMethods = usedMethods;
    }
    public BigDecimal getDiscount() {
        return discount;
    }
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

}
