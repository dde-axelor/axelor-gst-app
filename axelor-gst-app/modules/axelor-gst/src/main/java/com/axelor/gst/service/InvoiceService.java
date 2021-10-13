package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.app.Address;
import com.axelor.gst.app.Invoice;
import com.axelor.gst.app.InvoiceLine;

public interface InvoiceService {
	public BigDecimal totalAmount(List<BigDecimal> amount);
	public boolean checkString(String s1,String s2);
	public boolean compareState(Address s1,Address s2);
	public List<InvoiceLine> invoiceItemAmounts(Invoice invoice);
	public List<InvoiceLine> invoiceItemOnCreate(Invoice invoice,List<Integer> ids);
}
