package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.app.Address;
import com.axelor.gst.app.Contact;
import com.axelor.gst.app.Invoice;
import com.axelor.gst.app.InvoiceLine;
import com.axelor.gst.app.Party;
import com.axelor.gst.app.Product;
import com.axelor.gst.app.repo.ProductRepository;
import com.axelor.inject.Beans;

public class InvoiceServiceImplementation implements InvoiceService {

	
	@Override
	public boolean checkString(String s1, String s2) {
		if(s1.equalsIgnoreCase(s2))
			return true;
		return false;
	}

	@Override
	public boolean compareState(Address s1, Address s2) {
		if(s1.getState().equals(s2.getState()))
			return true;
		return false;
	}

	@Override
	public List<InvoiceLine> invoiceItemAmounts(Invoice invoice) {
		List<InvoiceLine> invoiceItems=invoice.getInvoiceItem();
		BigDecimal igst=BigDecimal.ZERO;
		BigDecimal sgst=BigDecimal.ZERO;
		BigDecimal cgst=BigDecimal.ZERO;
		try {
			if(invoice.getCompany().equals(null) || invoice.getInvoiceAddress().equals(null) || invoice.getParty().equals(null)) {
				invoiceItems=invoice.getInvoiceItem();
				for(InvoiceLine item:invoiceItems) {
					item.setCgst(cgst);
					item.setSgst(sgst);
					item.setIgst(igst);
					item.setGrossAmount(item.getNetAmount());
				}
			}
			else {
				
				if(compareState(invoice.getCompany().getAddress(), invoice.getInvoiceAddress())) {
					for(InvoiceLine item:invoiceItems) {	
						BigDecimal netAmount=item.getNetAmount();
						BigDecimal gstRate=item.getGstRate().divide(BigDecimal.valueOf(100));
						sgst=netAmount.multiply(gstRate).divide(BigDecimal.valueOf(2));
						cgst=netAmount.multiply(gstRate).divide(BigDecimal.valueOf(2));
						BigDecimal grossAmount=netAmount;
						grossAmount=grossAmount.add(sgst);
						grossAmount=grossAmount.add(cgst);
						item.setIgst(igst);
						item.setSgst(sgst);
						item.setCgst(cgst);
						item.setGrossAmount(grossAmount);	
					}
				}
				else {
					for(InvoiceLine item:invoiceItems) {
						BigDecimal netAmount=item.getNetAmount();
						BigDecimal gstRate=item.getGstRate().divide(BigDecimal.valueOf(100));
						igst=netAmount.multiply(gstRate);
						BigDecimal grossAmount=netAmount;
						grossAmount=grossAmount.add(igst);
						item.setIgst(igst);
						item.setCgst(cgst);
						item.setSgst(sgst);
						item.setGrossAmount(grossAmount);
					}
				}
				}
		}catch(Exception e) {
			
		}
		return invoiceItems;
	}

	@Override
	public List<InvoiceLine> invoiceItemOnCreate(Invoice invoice, List<Integer> ids) {
		List<InvoiceLine> items=new ArrayList<InvoiceLine>();
		for(Integer id:ids) {
			Long il_id=(long) id;
			Product i=Beans.get(ProductRepository.class).find(il_id);
			InvoiceLine invoiceLine=new InvoiceLine();
			invoiceLine.setProduct(i);
			invoiceLine.setInvoice(invoice);
			invoiceLine.setQty(1);
			invoiceLine.setHsbn(i.getHsbn());
			String code="["+i.getCode()+"] "+i.getName();
			invoiceLine.setItem(code);
			invoiceLine.setNetAmount(i.getSalePrice());
			invoiceLine.setGstRate(i.getGstRate());
			invoiceLine.setPrice(i.getSalePrice());
			items.add(invoiceLine);
		}
		return items;
	}

	@Override
	public Object getAddress(Party p, String str) {
		if(str.equalsIgnoreCase("primary")) {
			List<Contact> contactList=p.getContact();
			Contact contact=null;
			for(Contact c:contactList) {
				if(checkString("primary", c.getType())) {
					contact= c;
				}
			}
			return contact;
		}else {
			List<Address> addressList=p.getAddress();
			Address address=null;
			for(Address a:addressList) {
				if(checkString(str, a.getType())) {
					address=a;
					break;
				}else if(checkString("default", a.getType())) {
					address=a;
				}
			}
			return address;
		}
	}

	@Override
	public BigDecimal getAmounts(List<InvoiceLine> il, String str) {
		List<BigDecimal> list=new ArrayList <BigDecimal>();
		if(str.equalsIgnoreCase("netAmount")) {
			for(InvoiceLine item:il)
				list.add(item.getNetAmount());
		}
		else if(str.equalsIgnoreCase("igst"))
		{
			for(InvoiceLine item:il)
				list.add(item.getIgst());
		}
		else if(str.equalsIgnoreCase("sgst"))
		{
			for(InvoiceLine item:il)
				list.add(item.getSgst());
		}
		else if(str.equalsIgnoreCase("cgst"))
		{
			for(InvoiceLine item:il)
				list.add(item.getCgst());
		}else if(str.equalsIgnoreCase("grossAmount"))
		{
			for(InvoiceLine item:il)
				list.add(item.getGrossAmount());
		}
		
		BigDecimal total=BigDecimal.valueOf(0);
		for(BigDecimal value:list) {
			total=total.add(value);
		}
		return total;
	}

	

	

	
	

}
