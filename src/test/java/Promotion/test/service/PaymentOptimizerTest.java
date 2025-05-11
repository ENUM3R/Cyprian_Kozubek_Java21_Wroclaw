package Promotion.test.service;

import Promotion.model.Order;
import Promotion.model.PaymentDecision;
import Promotion.model.PaymentMethod;
import Promotion.service.PaymentOptimizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentOptimizerTest {

    private PaymentOptimizer optimizer;

    @BeforeEach
    public void setUp() {
        optimizer = new PaymentOptimizer();
    }

    private Order createOrder(String id, BigDecimal value, List<String> promotions) {
        Order order = new Order();
        order.setID(id);
        order.setValue(value);
        order.setPromotions(promotions);
        return order;
    }

    private PaymentMethod createMethod(String id, BigDecimal limit, BigDecimal discount) {
        PaymentMethod method = new PaymentMethod();
        method.setId(id);
        method.setLimit(limit);
        method.setDiscount(discount);
        return method;
    }

    @Test
    public void testOptimizeAll_singleFullCardDiscount() {
        Order order = createOrder("ORDER1", new BigDecimal("100.00"), Collections.singletonList("CARD1"));

        PaymentMethod card = createMethod("CARD1", new BigDecimal("200.00"), new BigDecimal("10")); // 10% discount
        Map<PaymentMethod, BigDecimal> methods = new HashMap<>();
        methods.put(card, new BigDecimal("150.00"));

        List<Order> orders = Collections.singletonList(order);
        List<PaymentDecision> decisions = optimizer.optimizeAll(orders, methods);

        assertEquals(1, decisions.size());
        PaymentDecision decision = decisions.get(0);
        assertEquals("ORDER1", decision.getOrderId());
        assertEquals(new BigDecimal("10.00"), decision.getDiscount());
        assertEquals(new BigDecimal("100.00"), decision.getUsedMethods().get(card));
    }

    @Test
    public void testOptimizeAll_fullPointsDiscount() {
        Order order = createOrder("ORDER2", new BigDecimal("80.00"), Collections.emptyList());

        PaymentMethod points = createMethod("PUNKTY", new BigDecimal("100.00"), new BigDecimal("20")); // 20% discount
        Map<PaymentMethod, BigDecimal> methods = new HashMap<>();
        methods.put(points, new BigDecimal("80.00"));

        List<Order> orders = Collections.singletonList(order);
        List<PaymentDecision> decisions = optimizer.optimizeAll(orders, methods);

        assertEquals(1, decisions.size());
        PaymentDecision decision = decisions.get(0);
        assertEquals("ORDER2", decision.getOrderId());
        assertEquals(new BigDecimal("16.00"), decision.getDiscount());
    }

    @Test
    public void testOptimizeAll_partialPointsDiscount() {
        Order order = createOrder("ORDER3", new BigDecimal("100.00"), Collections.emptyList());

        PaymentMethod points = createMethod("PUNKTY", new BigDecimal("200.00"), BigDecimal.ZERO);
        PaymentMethod card = createMethod("CARD2", new BigDecimal("200.00"), BigDecimal.ZERO);

        Map<PaymentMethod, BigDecimal> methods = new HashMap<>();
        methods.put(points, new BigDecimal("100.00"));
        methods.put(card, new BigDecimal("100.00"));

        List<Order> orders = Collections.singletonList(order);
        List<PaymentDecision> decisions = optimizer.optimizeAll(orders, methods);

        assertEquals(1, decisions.size());
        PaymentDecision decision = decisions.get(0);
        assertEquals("ORDER3", decision.getOrderId());
        assertEquals(new BigDecimal("10.00"), decision.getDiscount()); // 10% of 100.00
        assertEquals(2, decision.getUsedMethods().size());
        assertTrue(decision.getUsedMethods().containsKey(points));
        assertTrue(decision.getUsedMethods().containsKey(card));
    }

    @Test
    public void testOptimizeAll_noValidCombination() {
        Order order = createOrder("ORDER4", new BigDecimal("300.00"), Collections.singletonList("CARD3"));

        PaymentMethod card = createMethod("CARD3", new BigDecimal("200.00"), new BigDecimal("10"));

        Map<PaymentMethod, BigDecimal> methods = new HashMap<>();
        methods.put(card, new BigDecimal("150.00")); // not enough

        List<Order> orders = Collections.singletonList(order);
        List<PaymentDecision> decisions = optimizer.optimizeAll(orders, methods);

        assertEquals(0, decisions.size()); // No valid combination
    }

    @Test
    public void testOptimizeAll_prefersHigherPointsUsageWhenDiscountsEqual() {
        Order order = createOrder("ORDER5", new BigDecimal("100.00"), Collections.emptyList());

        PaymentMethod card = createMethod("CARDX", new BigDecimal("200.00"), new BigDecimal("10"));
        PaymentMethod points = createMethod("PUNKTY", new BigDecimal("200.00"), BigDecimal.ZERO);

        Map<PaymentMethod, BigDecimal> methods = new HashMap<>();
        methods.put(card, new BigDecimal("200.00"));
        methods.put(points, new BigDecimal("200.00"));

        List<Order> orders = Collections.singletonList(order);
        List<PaymentDecision> decisions = optimizer.optimizeAll(orders, methods);

        assertEquals(1, decisions.size());
        PaymentDecision decision = decisions.get(0);
        // Should include points since both discounts are 10.00 but points usage is preferred
        assertTrue(decision.getUsedMethods().containsKey(points));
    }
}
