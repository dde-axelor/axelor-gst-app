package com.axelor.gst.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import com.axelor.app.AppSettings;
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
			
			resp.setValue("partyContact", service.getAddress(party, "primary"));
			
			resp.setValue("invoiceAddress", service.getAddress(party, "invoice"));
			
			resp.setValue("shippingAddress", service.getAddress(party, "shipping"));
			
		}catch(Exception e) { }
	}
	
	
	public void setAmounts(ActionRequest req,ActionResponse resp) {
		
		Invoice invoice=req.getContext().asType(Invoice.class);
		
		List<InvoiceLine> invoiceLine= invoice.getInvoiceItem();
		
		try {

			resp.setValue("netAmount", service.getAmounts(invoiceLine, "netAmount"));
			
			resp.setValue("netIgst", service.getAmounts(invoiceLine, "igst"));
			
			resp.setValue("netCgst", service.getAmounts(invoiceLine, "cgst"));
			
			resp.setValue("netSgst", service.getAmounts(invoiceLine, "sgst"));
			
			resp.setValue("grossAmount", service.getAmounts(invoiceLine, "grossAmount"));
			
		}catch(Exception e) { }
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
	
	
	public void setInvoiceReference(ActionRequest req,ActionResponse resp) {
		
		try {
			resp.setValue("reference", sequence.getNextSequence("Invoice"));
			
		}catch(Exception e) {
			
			resp.addError("reference", "No sequence is specified for this model");
		}
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
