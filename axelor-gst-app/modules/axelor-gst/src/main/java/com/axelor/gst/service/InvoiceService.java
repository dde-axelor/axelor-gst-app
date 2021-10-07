package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {
	public BigDecimal totalAmount(List<BigDecimal> amount);
	public boolean checkString(String s1,String s2);
}
