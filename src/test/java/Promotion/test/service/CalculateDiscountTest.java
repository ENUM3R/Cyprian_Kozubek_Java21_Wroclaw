package Promotion.test.service;

import Promotion.model.Order;
import Promotion.model.PaymentMethod;
import Promotion.model.PaymentDecision;
import Promotion.service.CalculateDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CalculateDiscountTest {
    private CalculateDiscount calculateDiscount;

    @BeforeEach
    public void setup() {
        calculateDiscount = new CalculateDiscount();
    }

    @Test
    public void testCalculateDiscountFullCard_valid() {
        Order order = new Order();
        order.setID("ORDER1");
        order.setValue(new BigDecimal("100.00"));
        order.setPromotions(Collections.singletonList("CARD"));

        PaymentMethod method = new PaymentMethod();
        method.setId("CARD");
        method.setLimit(new BigDecimal("200.00"));
        method.setDiscount(new BigDecimal("10"));

        Map<PaymentMethod, BigDecimal> map = new HashMap<>();
        map.put(method, new BigDecimal("100.00"));

        BigDecimal result = calculateDiscount.calculateDiscountFullCard(order, map);
        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    public void testCalculateDiscountFullCard_exceedsLimit() {
        Order order = new Order();
        order.setID("ORDER1");
        order.setValue(new BigDecimal("300.00"));
        order.setPromotions(Collections.singletonList("CARD"));

        PaymentMethod method = new PaymentMethod();
        method.setId("CARD");
        method.setLimit(new BigDecimal("200.00"));
        method.setDiscount(new BigDecimal("10"));

        Map<PaymentMethod, BigDecimal> map = new HashMap<>();
        map.put(method, new BigDecimal("300.00"));

        BigDecimal result = calculateDiscount.calculateDiscountFullCard(order, map);
        assertNull(result);
    }

    @Test
    public void testCalculateDiscountPartPoints_valid() {
        Order order = new Order();
        order.setID("ORDER1");
        order.setValue(new BigDecimal("100.00"));

        PaymentMethod points = new PaymentMethod();
        points.setId("PUNKTY");

        Map<PaymentMethod, BigDecimal> map = new HashMap<>();
        map.put(points, new BigDecimal("15.00"));

        BigDecimal result = calculateDiscount.calculateDiscountPartPoints(order, map);
        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    public void testCalculateDiscountPartPoints_notEnoughPoints() {
        Order order = new Order();
        order.setID("ORDER1");
        order.setValue(new BigDecimal("100.00"));

        PaymentMethod points = new PaymentMethod();
        points.setId("PUNKTY");

        Map<PaymentMethod, BigDecimal> map = new HashMap<>();
        map.put(points, new BigDecimal("5.00")); // Less than 10%

        BigDecimal result = calculateDiscount.calculateDiscountPartPoints(order, map);
        assertNull(result);
    }

    @Test
    public void testCalculateDiscountFullPoints_valid() {
        Order order = new Order();
        order.setID("ORDER1");
        order.setValue(new BigDecimal("80.00"));

        PaymentMethod points = new PaymentMethod();
        points.setId("PUNKTY");
        points.setLimit(new BigDecimal("100.00"));
        points.setDiscount(new BigDecimal("20"));

        Map<PaymentMethod, BigDecimal> map = new HashMap<>();
        map.put(points, new BigDecimal("80.00"));

        BigDecimal result = calculateDiscount.calculateDiscountFullPoints(order, map);
        assertEquals(new BigDecimal("16.00"), result); // 20% of 80.00
    }

    @Test
    public void testCalculateDiscounts_mixedScenario() {
        Order order = new Order();
        order.setID("ORDER1");
        order.setValue(new BigDecimal("100.00"));
        order.setPromotions(Collections.singletonList("CARD"));

        PaymentMethod method = new PaymentMethod();
        method.setId("CARD");
        method.setLimit(new BigDecimal("200.00"));
        method.setDiscount(new BigDecimal("10"));

        Map<PaymentMethod, BigDecimal> map = new HashMap<>();
        map.put(method, new BigDecimal("100.00"));

        PaymentDecision decision = calculateDiscount.calculateDiscounts(order, map);
        assertEquals("ORDER1", decision.getOrderId());
        assertEquals(new BigDecimal("10.00"), decision.getDiscount());
        assertEquals(1, decision.getUsedMethods().size());
        assertEquals(new BigDecimal("100.00"), decision.getUsedMethods().get(method));
    }
}
