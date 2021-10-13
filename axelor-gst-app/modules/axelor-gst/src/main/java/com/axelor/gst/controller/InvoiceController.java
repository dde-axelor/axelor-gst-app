package com.axelor.gst.controller;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.axelor.app.AppSettings;
import com.axelor.gst.app.Address;
import com.axelor.gst.app.Contact;
import com.axelor.gst.app.Invoice;
import com.axelor.gst.app.InvoiceLine;
import com.axelor.gst.app.Party;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.SequenceService;
import com.axelor.meta.CallMethod;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController {

	@Inject
	private InvoiceService service;
	
	@Inject
	private SequenceService sequence;
	
	public void setContact(ActionRequest req,ActionResponse resp) {
		Invoice invoice=req.getContext().asType(Invoice.class);
		try {
			Party party=invoice.getParty();
			List<Contact> contact=party.getContact();
			for(Contact c:contact) {
				if(service.checkString("primary", c.getType())) {
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
		}catch(Exception e) {
			
		}
	}
	
	
	public void setAmounts(ActionRequest req,ActionResponse resp) {
		Invoice invoice=req.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceLine= invoice.getInvoiceItem();
		try {

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
		}catch(Exception e) {
			
		}
	}
	
	public void checkInvoiceLine(ActionRequest req,ActionResponse resp) {
		Invoice invoice=req.getContext().asType(Invoice.class);
		List<InvoiceLine> list=invoice.getInvoiceItem();
		if(!list.isEmpty()) {
			resp.setAttr("company", "readonly", "true");
			resp.setAttr("party", "readonly", "true");
		}
		else {
			resp.setAttr("company", "readonly", "false");
			resp.setAttr("party", "readonly", "false");
		}
	}
	
	public void setValuesOnChange(ActionRequest req,ActionResponse resp) {
		Invoice invoice=req.getContext().asType(Invoice.class);
		resp.setValue("invoiceItem", service.invoiceItemAmounts(invoice));		
		
	}
	
	
	public void setInvoiceItem(ActionRequest req,ActionResponse resp) {
		Invoice invoice=req.getContext().asType(Invoice.class);
		List<Integer> invoiceItems= (List<Integer>) req.getContext().get("productId");
		resp.setValue("invoiceItem", service.invoiceItemOnCreate(invoice, invoiceItems));
		
	}
	
	public void setReference(ActionRequest req,ActionResponse resp) {
		Invoice invoice=req.getContext().asType(Invoice.class);
		resp.setValue("reference", sequence.getNextSequence("Invoice"));
	}
	
	
	@CallMethod
	public String getLogoUrl() {
		AppSettings app=AppSettings.get();
		String uploadUrl=app.get("file.upload.dir");
		uploadUrl+=File.separator;
		return uploadUrl;
	}
	
	public void setGst(ActionRequest req,ActionResponse resp) {
		Invoice invoice=req.getContext().asType(Invoice.class);
		resp.setValue("invoiceItem", service.invoiceItemAmounts(invoice));
		
	}
	
	@CallMethod
	public LocalDateTime setFromDate() {
		LocalDateTime current=LocalDateTime.now();
		long today=current.getDayOfMonth();
		LocalDateTime fromDate=current.minusDays(today-1);
		return fromDate;
	}
	

}
