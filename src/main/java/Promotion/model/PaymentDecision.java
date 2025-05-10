package Promotion.model;

import java.util.Map;
import java.math.BigDecimal;

public class PaymentDecision {
    private int orderId;
    private Map<String, BigDecimal> usedMethods;

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public Map<String, BigDecimal> getUsedMethods() {
        return usedMethods;
    }
    public void setUsedMethods(Map<String, BigDecimal> usedMethods) {
        this.usedMethods = usedMethods;
    }


}
