package Promotion.service;

import Promotion.model.Order;
import Promotion.model.PaymentDecision;
import Promotion.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.*;

public class PaymentOptimizer {
    private final CalculateDiscount calculator = new CalculateDiscount();

    public List<PaymentDecision> optimizeAll(List<Order> orders, Map<PaymentMethod, BigDecimal> availableMethods) {
        List<PaymentDecision> results = new ArrayList<>();

        for (Order order : orders) {
            List<Map<PaymentMethod, BigDecimal>> possibleCombinations = generateValidCombinations(order, availableMethods);
            PaymentDecision bestDecision = null;
            BigDecimal maxDiscount = BigDecimal.ZERO;

            for (Map<PaymentMethod, BigDecimal> option : possibleCombinations) {
                PaymentDecision decision = calculator.calculateDiscounts(order, option);
                if (decision == null || decision.getDiscount() == null) continue;
                int cmp = decision.getDiscount().compareTo(maxDiscount);
                if (cmp > 0) {
                    maxDiscount = decision.getDiscount();
                    bestDecision = decision;
                } else if (cmp == 0 && bestDecision != null) {
                    BigDecimal pointsUsedNow = getPointsUsed(decision);
                    BigDecimal pointsUsedBest = getPointsUsed(bestDecision);
                    if (pointsUsedNow.compareTo(pointsUsedBest) > 0) {
                        bestDecision = decision;
                    }
                }
            }
            if (bestDecision != null) {
                for (Map.Entry<PaymentMethod, BigDecimal> e : bestDecision.getUsedMethods().entrySet()) {
                    BigDecimal old = availableMethods.get(e.getKey());
                    availableMethods.put(e.getKey(), old.subtract(e.getValue()));
                }
                results.add(bestDecision);
            } else {
                System.err.println("No special discount for " + order.getID());
            }
        }
        return results;
    }

    private List<Map<PaymentMethod, BigDecimal>> generateValidCombinations(Order order, Map<PaymentMethod, BigDecimal> availableMethods) {
        List<Map<PaymentMethod, BigDecimal>> combinations = new ArrayList<>();
        BigDecimal orderValue = order.getValue();
        for (PaymentMethod method : availableMethods.keySet()) {
            BigDecimal available = availableMethods.get(method);
            if (available.compareTo(orderValue) >= 0) {
                Map<PaymentMethod, BigDecimal> single = new HashMap<>();
                single.put(method, orderValue);
                combinations.add(single);
            }
        }
        PaymentMethod pointsMethod = getPointsMethod(availableMethods);
        if (pointsMethod != null) {
            BigDecimal pointsAvailable = availableMethods.get(pointsMethod);
            BigDecimal tenPercent = orderValue.multiply(new BigDecimal("0.10"));
            BigDecimal remaining = orderValue.subtract(tenPercent);

            if (pointsAvailable.compareTo(tenPercent) >= 0) {
                for (PaymentMethod card : availableMethods.keySet()) {
                    if ("PUNKTY".equals(card.getId())) continue;
                    BigDecimal cardAvailable = availableMethods.get(card);
                    if (cardAvailable.compareTo(remaining) >= 0) {
                        Map<PaymentMethod, BigDecimal> combo = new HashMap<>();
                        combo.put(pointsMethod, tenPercent);
                        combo.put(card, remaining);
                        combinations.add(combo);
                    }
                }
            }
        }
        return combinations;
    }

    private PaymentMethod getPointsMethod(Map<PaymentMethod, BigDecimal> map) {
        for (PaymentMethod method : map.keySet()) {
            if ("PUNKTY".equals(method.getId())) {
                return method;
            }
        }
        return null;
    }

    private BigDecimal getPointsUsed(PaymentDecision decision) {
        return decision.getUsedMethods().entrySet().stream()
                .filter(e -> "PUNKTY".equals(e.getKey().getId()))
                .map(Map.Entry::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
