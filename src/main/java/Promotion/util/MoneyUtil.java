package Promotion.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyUtil {
    public BigDecimal roundToTwoDecimalPlaces(BigDecimal money) {
        return new BigDecimal(String.valueOf(money)).setScale(2, RoundingMode.UP);
    }
}
