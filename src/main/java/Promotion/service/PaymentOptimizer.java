package Promotion.service;

import Promotion.model.Order;
import Promotion.model.PaymentDecision;
import Promotion.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentOptimizer {
    private final CalculateDiscount calculateDiscount;

    public PaymentOptimizer() {
        this.calculateDiscount = new CalculateDiscount();
    }

    public PaymentDecision optimize(Order order, Map<PaymentMethod, BigDecimal> breakdown) {
        BigDecimal maxDiscount = BigDecimal.ZERO;
        PaymentDecision decision = null;

        if(isPaidFullWithPoints(breakdown)){
            BigDecimal discount = calculateDiscount.calculateDiscountFullPoints(order, breakdown);
            decision = buildDecision(order,breakdown,discount);
            maxDiscount = discount;
        }else if(breakdown.size() == 1){
            BigDecimal discount = calculateDiscount.calculateDiscountFullCard(order, breakdown);
            decision = buildDecision(order,breakdown,discount);
            maxDiscount = discount;
        }else{
            BigDecimal discount = calculateDiscount.calculateDiscountPartPoints(order, breakdown);
            if(discount != null && discount.compareTo(maxDiscount) > 0){
                decision = buildDecision(order,breakdown,discount);
            }
        }
        if(decision != null){
            return decision;
        } else {
            return buildDecision(order,breakdown,BigDecimal.ZERO);
        }
    }

    private boolean isPaidFullWithPoints(Map<PaymentMethod, BigDecimal> breakdown){
        return breakdown.size() == 1 && breakdown.keySet().iterator().next().getId().equals("PUNKTY");
    }

    private PaymentDecision buildDecision(Order order, Map<PaymentMethod, BigDecimal> breakdown, BigDecimal discount) {
        PaymentDecision decision = new PaymentDecision();
        decision.setOrderId(order.getID());
        decision.setUsedMethods(breakdown);
        decision.setDiscount(discount);
        return decision;
    }
}
