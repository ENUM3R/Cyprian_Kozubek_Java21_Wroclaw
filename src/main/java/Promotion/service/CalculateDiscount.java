package Promotion.service;

import Promotion.model.Order;
import Promotion.model.PaymentDecision;
import Promotion.model.PaymentMethod;
import Promotion.util.MoneyUtil;

import java.math.BigDecimal;
import java.util.Map;

//This class encapsulates the discount logic for different types of payments:
//full card payment, full loyalty points, or partial points.
//It determines the correct discount amount based on the order and payment breakdown and constructs a corresponding PaymentDecision.
public class CalculateDiscount {
    private final MoneyUtil moneyUtil = new MoneyUtil();

    //This method checks if the order is fully paid using one card method,
    //verifies that this method is eligible for promotion in the order.
    //If valid, applies the method’s discount to the full order value.
    public BigDecimal calculateDiscountFullCard(Order order, Map<PaymentMethod, BigDecimal> paymentBreakdown) {
        if (paymentBreakdown.size() != 1) {
            return null;
        }
        PaymentMethod method = paymentBreakdown.keySet().iterator().next();
        if (order.getPromotions() == null || !order.getPromotions().contains(method.getId())) {
            return BigDecimal.ZERO;
        }
        if (order.getValue().compareTo(method.getLimit()) > 0) {
            return null;
        }
        BigDecimal discount = method.getDiscount()
                .divide(new BigDecimal("100"))
                .multiply(order.getValue());
        return moneyUtil.roundToTwoDecimalPlaces(discount);
    }

    //This method checks if at least 10% of the order is paid with "PUNKTY".
    //If so, returns a 10% discount on the full order value (cannot be combined with card discounts).
    public BigDecimal calculateDiscountPartPoints(Order order, Map<PaymentMethod, BigDecimal> paymentBreakdown) {
        BigDecimal pointsPaid = BigDecimal.ZERO;
        BigDecimal orderValue = order.getValue();

        for (Map.Entry<PaymentMethod, BigDecimal> entry : paymentBreakdown.entrySet()) {
            if ("PUNKTY".equals(entry.getKey().getId())) {
                pointsPaid = pointsPaid.add(entry.getValue());
            }
        }
        if (pointsPaid.compareTo(BigDecimal.ZERO) == 0 ||
                pointsPaid.compareTo(orderValue.multiply(new BigDecimal("0.10"))) < 0) {
            return null;
        }

        BigDecimal discount = orderValue.multiply(new BigDecimal("0.10"));
        return moneyUtil.roundToTwoDecimalPlaces(discount);
    }

    //This method applies only when 100% of the order is paid using "PUNKTY".
    //Uses the discount defined for "PUNKTY" from the PaymentMethod.
    public BigDecimal calculateDiscountFullPoints(Order order, Map<PaymentMethod, BigDecimal> paymentBreakdown) {
        if (paymentBreakdown.size() != 1) {
            return null;
        }
        PaymentMethod method = paymentBreakdown.keySet().iterator().next();
        if (!"PUNKTY".equals(method.getId())) {
            return null;
        }
        if (order.getValue().compareTo(method.getLimit()) > 0) {
            return null;
        }
        BigDecimal discount = method.getDiscount()
                .divide(new BigDecimal("100"))
                .multiply(order.getValue());

        return moneyUtil.roundToTwoDecimalPlaces(discount);
    }

    //Central method that selects the proper discount calculation strategy:
    // full card, partial points, full points,or zero discount.
    //Builds and returns a PaymentDecision object with the calculated discount and the used payment breakdown.
    public PaymentDecision calculateDiscounts(Order order, Map<PaymentMethod, BigDecimal> paymentBreakdown) {
        BigDecimal discount;
        if (paymentBreakdown.size() == 1) {
            PaymentMethod method = paymentBreakdown.keySet().iterator().next();

            if ("PUNKTY".equals(method.getId())) {
                discount = calculateDiscountFullPoints(order, paymentBreakdown);
            } else {
                BigDecimal d = calculateDiscountFullCard(order, paymentBreakdown);
                if (d != null) {
                    discount = d;
                } else {
                    discount = BigDecimal.ZERO;
                }
            }
        } else {
            BigDecimal d = calculateDiscountPartPoints(order, paymentBreakdown);
            if (d != null) {
                discount = d;
            } else {
                discount = BigDecimal.ZERO;
            }
        }
        PaymentDecision decision = new PaymentDecision();
        decision.setOrderId(order.getID());
        decision.setUsedMethods(paymentBreakdown);
        decision.setDiscount(discount);
        return decision;
    }

}
