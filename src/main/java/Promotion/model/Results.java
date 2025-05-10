package Promotion.model;

import java.math.BigDecimal;
import java.util.HashMap;

public class Results {
    private HashMap<BigDecimal, BigDecimal> results;


    public void setResults(HashMap<BigDecimal,BigDecimal> results) {
        this.results = results;
    }
    public HashMap<BigDecimal,BigDecimal> getResults() {
        return results;
    }
}
