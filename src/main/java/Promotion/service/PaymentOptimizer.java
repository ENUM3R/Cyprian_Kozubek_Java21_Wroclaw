package Promotion.service;

import Promotion.model.Order;
import Promotion.model.PaymentDecision;
import Promotion.model.PaymentMethod;
import Promotion.util.MoneyUtil;

import java.math.BigDecimal;
import java.util.*;

public class PaymentOptimizer {
    private final CalculateDiscount calculateDiscount;
    private final MoneyUtil moneyUtil;

    public PaymentOptimizer() {
        this.calculateDiscount = new CalculateDiscount();
        this.moneyUtil = new MoneyUtil();
    }

    public PaymentDecision optimize(Order order, Map<PaymentMethod, BigDecimal> wallet) {
        List<PaymentDecision> possibleDecisions = new ArrayList<>();

        BigDecimal orderValue = order.getValue();

        for (PaymentMethod method : wallet.keySet()) {
            if ("PUNKTY".equals(method.getId()) && wallet.get(method).compareTo(orderValue) >= 0) {
                Map<PaymentMethod, BigDecimal> breakdown = Map.of(method, orderValue);
                BigDecimal discount = calculateDiscount.calculateDiscountFullPoints(order, breakdown);
                if (discount != null) {
                    possibleDecisions.add(buildDecision(order, breakdown, discount));
                }
            }
        }

        for (PaymentMethod method : wallet.keySet()) {
            if (!"PUNKTY".equals(method.getId())
                    && wallet.get(method).compareTo(orderValue) >= 0
                    && (order.getPromotions() != null && order.getPromotions().contains(method.getId()))
            ) {
                Map<PaymentMethod, BigDecimal> breakdown = Map.of(method, orderValue);
                BigDecimal discount = calculateDiscount.calculateDiscountFullCard(order, breakdown);
                if (discount != null) {
                    possibleDecisions.add(buildDecision(order, breakdown, discount));
                }
            }
        }

        for (PaymentMethod card : wallet.keySet()) {
            if ("PUNKTY".equals(card.getId())) continue;

            BigDecimal pointsAvailable = wallet.entrySet().stream()
                    .filter(e -> "PUNKTY".equals(e.getKey().getId()))
                    .map(Map.Entry::getValue)
                    .findFirst().orElse(BigDecimal.ZERO);

            BigDecimal minPoints = orderValue.multiply(new BigDecimal("0.10"));
            if (pointsAvailable.compareTo(minPoints) >= 0) {
                BigDecimal pointsUsed = pointsAvailable.min(orderValue);
                BigDecimal remaining = orderValue.subtract(pointsUsed);

                if (wallet.get(card).compareTo(remaining) >= 0) {
                    Map<PaymentMethod, BigDecimal> breakdown = new LinkedHashMap<>();
                    PaymentMethod pointsMethod = getPointsMethod(wallet);
                    breakdown.put(pointsMethod, pointsUsed);
                    breakdown.put(card, remaining);

                    BigDecimal discount = calculateDiscount.calculateDiscountPartPoints(order, breakdown);
                    if (discount != null) {
                        possibleDecisions.add(buildDecision(order, breakdown, discount));
                    }
                }
            }
        }

        for (PaymentMethod method : wallet.keySet()) {
            if (wallet.get(method).compareTo(orderValue) >= 0) {
                Map<PaymentMethod, BigDecimal> breakdown = Map.of(method, orderValue);
                possibleDecisions.add(buildDecision(order, breakdown, BigDecimal.ZERO));
                break;
            }
        }

        PaymentDecision best = null;
        for (PaymentDecision pd : possibleDecisions) {
            if (best == null) {
                best = pd;
            } else {
                int cmp = pd.getDiscount().compareTo(best.getDiscount());
                if (cmp > 0) {
                    best = pd;
                } else if (cmp == 0) {
                    BigDecimal thisPoints = getAmountPaidWithPoints(pd);
                    BigDecimal bestPoints = getAmountPaidWithPoints(best);
                    if (thisPoints.compareTo(bestPoints) > 0) {
                        best = pd;
                    }
                }
            }
        }
        if (best != null) {
            return best;
        } else {
            return buildDecision(order, Collections.emptyMap(), BigDecimal.ZERO);
        }
    }

    private BigDecimal getAmountPaidWithPoints(PaymentDecision decision) {
        return decision.getUsedMethods().entrySet().stream()
                .filter(e -> "PUNKTY".equals(e.getKey().getId()))
                .map(Map.Entry::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PaymentMethod getPointsMethod(Map<PaymentMethod, BigDecimal> wallet) {
        for (PaymentMethod method : wallet.keySet()) {
            if ("PUNKTY".equals(method.getId())) {
                return method;
            }
        }
        throw new IllegalStateException("No points method found in wallet");
    }

    private PaymentDecision buildDecision(Order order, Map<PaymentMethod, BigDecimal> breakdown, BigDecimal discount) {
        PaymentDecision decision = new PaymentDecision();
        decision.setOrderId(order.getID());
        decision.setUsedMethods(breakdown);
        decision.setDiscount(discount);
        return decision;
    }
}
