package Promotion;

import Promotion.model.Order;
import Promotion.model.PaymentDecision;
import Promotion.model.PaymentMethod;
import Promotion.service.InputParser;
import Promotion.service.PaymentOptimizer;
import Promotion.util.MoneyUtil;

import java.math.BigDecimal;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Main <orders.json> <paymentmethods.json>");
            return;
        }

        String orderFile = args[0];
        String paymentFile = args[1];
        //Parsing JSON files, using InputParser.java
        InputParser inputParser = new InputParser();
        List<Order> orders = inputParser.parseOrder(orderFile);
        Map<PaymentMethod, BigDecimal> available = inputParser.parsePaymentMethods(paymentFile);

        System.out.println("Available payment methods: ");
        for (Map.Entry<PaymentMethod, BigDecimal> entry : available.entrySet()) {
            System.out.println("Method: " + entry.getKey().getId() + ", Amount: " + entry.getValue());
        }
        //Finding best discounts for each method, using PaymentOptimizer.java
        PaymentOptimizer optimizer = new PaymentOptimizer();
        List<PaymentDecision> decisions = optimizer.optimizeAll(orders, new HashMap<>(available));

        Map<String, BigDecimal> summary = new LinkedHashMap<>();
        MoneyUtil util = new MoneyUtil();
        for (PaymentDecision d : decisions) {
            System.out.println("Order " + d.getOrderId() + " - discount: " + d.getDiscount());
            BigDecimal orderValue = BigDecimal.ZERO;

            if (d.getOrderId() != null) {
                for (Order order : orders) {
                    if (order.getID().equals(d.getOrderId())) {
                        orderValue = order.getValue();
                        break;
                    }
                }
            }
            BigDecimal amountAfterDiscount = orderValue.subtract(d.getDiscount());
            System.out.println("Amount to pay for " + d.getOrderId() + " after discount: " + util.roundToTwoDecimalPlaces(amountAfterDiscount));

            for (Map.Entry<PaymentMethod, BigDecimal> use : d.getUsedMethods().entrySet()) {
                String id = use.getKey().getId();
                BigDecimal amount = use.getValue();
                summary.merge(id, amount, BigDecimal::add);
                BigDecimal oldAmount = available.get(use.getKey());
                available.put(use.getKey(), oldAmount.subtract(amount));
            }
        }
    }
}
