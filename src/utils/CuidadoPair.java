/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.math.BigDecimal;
import jdk.dynalink.Operation;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author 38164
 */
public class CuidadoPair implements PackageOperations.Pair<Integer, BigDecimal>{
    private int offerId;
    private BigDecimal offerDiscount;
		
    public CuidadoPair(int offerId, BigDecimal offerDiscount) {
        this.offerId = offerId;
        this.offerDiscount = offerDiscount;
    }
		
    @Override
    public Integer getFirstParam() {
        return offerId;
    }

    @Override
    public BigDecimal getSecondParam() {
        return offerDiscount;
    }
}
