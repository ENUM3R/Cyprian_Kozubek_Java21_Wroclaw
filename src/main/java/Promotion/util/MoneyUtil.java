package Promotion.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

//The MoneyUtil class provides utility methods for rounding monetary values to two decimal places.
public class MoneyUtil {
    //this method takes a BigDecimal value representing money and rounds it to two decimal places.
    //The rounding is done using the RoundingMode.UP, meaning the value will always be
    //rounded up to the next closest value with two decimal places.
    //The method returns the rounded BigDecimal.
    public BigDecimal roundToTwoDecimalPlaces(BigDecimal money) {
        return new BigDecimal(String.valueOf(money)).setScale(2, RoundingMode.UP);
    }
}
