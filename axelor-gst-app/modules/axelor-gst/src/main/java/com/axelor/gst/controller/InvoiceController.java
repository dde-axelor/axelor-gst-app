package com.axelor.gst.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.app.Address;
import com.axelor.gst.app.Contact;
import com.axelor.gst.app.Invoice;
import com.axelor.gst.app.InvoiceLine;
import com.axelor.gst.app.Party;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController {

	@Inject
	private InvoiceService service;
	
	public void setContact(ActionRequest req,ActionResponse resp) {
		Invoice invoice=req.getContext().asType(Invoice.class);
		Party party=invoice.getParty();
		List<Contact> contact=party.getContact();
		for(Contact c:contact) {
			String type=c.getType();
			if(type.equalsIgnoreCase("primary")) {
				resp.setValue("partyContact", c);
			}
		}
		
		List<Address> address=party.getAddress();
		
		for(Address a:address) {
			if(service.checkString("invoice", a.getType())) {
				resp.setValue("invoiceAddress", a);
				break;
			}else if(service.checkString("default", a.getType())) {
				resp.setValue("invoiceAddress", a);
			}
		}
		
		for(Address a:address) {
			if(service.checkString("shipping", a.getType())) {
				resp.setValue("shippingAddress", a);
				break;
			}else if(service.checkString(a.getType(), "default")) {
				resp.setValue("shippingAddress", a);
			}
		}
	}
	
	
	public void setAmounts(ActionRequest req,ActionResponse resp) {
		Invoice invoice=req.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceLine= invoice.getInvoiceItem();
		
		List<BigDecimal> netAmount=new ArrayList <BigDecimal>();
		for(InvoiceLine singleInvoice:invoiceLine) {
			netAmount.add(singleInvoice.getNetAmount());
		}
		resp.setValue("netAmount", service.totalAmount(netAmount));
		
		List<BigDecimal> netIgst=new ArrayList <BigDecimal>();
		for(InvoiceLine singleInvoice:invoiceLine) {
			netIgst.add(singleInvoice.getIgst());
		}
		resp.setValue("netIgst", service.totalAmount(netIgst));
		
		List<BigDecimal> netCgst=new ArrayList <BigDecimal>();
		for(InvoiceLine singleInvoice:invoiceLine) {
			netCgst.add(singleInvoice.getCgst());
		}
		resp.setValue("netCgst", service.totalAmount(netCgst));
		
		List<BigDecimal> netSgst=new ArrayList <BigDecimal>();
		for(InvoiceLine singleInvoice:invoiceLine) {
			netSgst.add(singleInvoice.getSgst());
		}
		resp.setValue("netSgst", service.totalAmount(netSgst));
		
		List<BigDecimal> grossAmount=new ArrayList <BigDecimal>();
		for(InvoiceLine singleInvoice:invoiceLine) {
			grossAmount.add(singleInvoice.getGrossAmount());
		}
		resp.setValue("grossAmount", service.totalAmount(grossAmount));
	}

}
