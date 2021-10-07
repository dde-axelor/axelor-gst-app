package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceServiceImplementation implements InvoiceService {

	@Override
	public BigDecimal totalAmount(List<BigDecimal> amount) {
		BigDecimal total=BigDecimal.valueOf(0);
		for(BigDecimal value:amount) {
			total=total.add(value);
		}
		return total;
	}
	
	@Override
	public boolean checkString(String s1, String s2) {
		if(s1.equalsIgnoreCase(s2))
			return true;
		return false;
	}
	

}
