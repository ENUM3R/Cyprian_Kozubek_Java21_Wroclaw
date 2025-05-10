package Promotion.service;

import Promotion.model.Order;
import Promotion.model.PaymentDecision;
import Promotion.model.PaymentMethod;
import Promotion.util.MoneyUtil;

import java.math.BigDecimal;
import java.util.Map;

public class CalculateDiscount {

    public BigDecimal calculateDiscountFullCard(Order order, Map<PaymentMethod, BigDecimal> paymentBreakdown) {
        if (paymentBreakdown.size() != 1) {
            return null;
        }
        PaymentMethod method = paymentBreakdown.keySet().iterator().next();
        if (order.getPromotions() == null || !order.getPromotions().contains(method.getId())) {
            return null;
        }
        if (order.getValue().compareTo(method.getLimit()) > 0) {
            return null;
        }
        BigDecimal discountPercentage = method.getDiscount().divide(new BigDecimal("100"));
        BigDecimal discount = discountPercentage.multiply(order.getValue());

        MoneyUtil moneyUtil = new MoneyUtil();
        return moneyUtil.roundToTwoDecimalPlaces(discount);
    }

    public BigDecimal calculateDiscountPartPoints(Order order, Map<PaymentMethod, BigDecimal> paymentBreakdown) {
        PaymentMethod method = paymentBreakdown.keySet().iterator().next();
        BigDecimal amountPaidWihtPoints = BigDecimal.ZERO;
        for (Map.Entry<PaymentMethod, BigDecimal> entry : paymentBreakdown.entrySet()) {
            PaymentMethod paymentMethod = entry.getKey();
            BigDecimal amount = entry.getValue();
            if (paymentMethod.getId().equals("PUNKTY")) {
                amountPaidWihtPoints = amountPaidWihtPoints.add(amount);
                break;
            }
        }
        if (amountPaidWihtPoints.compareTo(BigDecimal.ZERO) == 0
        || amountPaidWihtPoints.compareTo(order.getValue().multiply(new BigDecimal("0.10"))) < 0) {
            return null;
        }
        BigDecimal discount = order.getValue().multiply(new BigDecimal("0.10"));

        MoneyUtil moneyUtil = new MoneyUtil();
        return moneyUtil.roundToTwoDecimalPlaces(discount);
    }

    public BigDecimal calculateDiscountFullPoints(Order order, Map<PaymentMethod, BigDecimal> paymentBreakdown) {
        if (paymentBreakdown.size() != 1) {
            return null;
        }
        PaymentMethod method = paymentBreakdown.keySet().iterator().next();
        if (!method.getId().equals("PUNKTY")) {
            return null;
        }
        if (order.getValue().compareTo(method.getLimit()) > 0) {
            return null;
        }
        BigDecimal discountPercentage = method.getDiscount().divide(new BigDecimal("100"));
        BigDecimal discount = discountPercentage.multiply(order.getValue());

        MoneyUtil moneyUtil = new MoneyUtil();
        return moneyUtil.roundToTwoDecimalPlaces(discount);
    }

    public PaymentDecision calculateDiscounts(Order order, Map<PaymentMethod, BigDecimal> paymentBreakdown) {

        return null;
    }
}
