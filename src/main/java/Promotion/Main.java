    package Promotion;

    import Promotion.model.Order;
    import Promotion.model.PaymentDecision;
    import Promotion.model.PaymentMethod;
    import Promotion.model.Results;
    import Promotion.service.InputParser;
    import Promotion.service.PaymentOptimizer;
    import Promotion.util.MoneyUtil;

    import java.math.BigDecimal;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class Main {
        public static void main(String[] args) {
            if (args.length < 2) {
                System.out.println("Usage: java Main <orders.json> <paymentmethods.json>");
                return;
            }
            String order_file = args[0];
            String payment_file = args[1];
            InputParser inputParser = new InputParser();
            List<Order> orders = inputParser.parseOrder(order_file);
            Map<PaymentMethod,BigDecimal> paymentMethods = inputParser.parsePaymentMethods(payment_file);

            PaymentOptimizer optimizer = new PaymentOptimizer();
            Results results = new Results();

            Map<String, BigDecimal> aggregatedResults = new HashMap<>();
            for(Order order : orders) {
                PaymentDecision decision = optimizer.optimize(order, paymentMethods);
                Map<PaymentMethod, BigDecimal> bestBreakdown = decision.getUsedMethods();
                if(bestBreakdown == null) {
                    System.err.println("Cannot choose payment for this order: " + order.getID());
                    continue;
                }
                for(Map.Entry<PaymentMethod, BigDecimal> entry : bestBreakdown.entrySet()) {
                    String methodId = entry.getKey().getId();
                    BigDecimal amount = entry.getValue();

                    aggregatedResults.merge(methodId, amount, BigDecimal::add);
                }
            }
            MoneyUtil moneyUtil = new MoneyUtil();
            for (Map.Entry<String, BigDecimal> entry : aggregatedResults.entrySet()) {
                System.out.println(entry.getKey() + " " + moneyUtil.roundToTwoDecimalPlaces(entry.getValue()));
            }
        }
    }
