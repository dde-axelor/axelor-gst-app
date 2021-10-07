package com.axelor.gst.controller;

import java.math.BigDecimal;

import com.axelor.gst.app.Address;
import com.axelor.gst.app.Invoice;
import com.axelor.gst.app.InvoiceLine;
import com.axelor.gst.app.Product;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceLineController {
	
	@Inject
	private InvoiceLineService service;
	
	
	public void setItem(ActionRequest req,ActionResponse resp) {
		InvoiceLine invoiceLine=req.getContext().asType(InvoiceLine.class);
		Product product=invoiceLine.getProduct();
		String item="["+product.getCode()+"] "+product.getName();
		resp.setValue("item", item);
	}
	
	
	
	public void setNetAmount(ActionRequest req,ActionResponse resp) {
		InvoiceLine invoiceLine=req.getContext().asType(InvoiceLine.class);
		Integer qty=invoiceLine.getQty();
		BigDecimal price=invoiceLine.getPrice();
		BigDecimal netAmount=price.multiply(BigDecimal.valueOf(qty));
		resp.setValue("netAmount", netAmount);
	}
	
	
	
	public boolean checkState(ActionRequest req,ActionResponse resp) {
		InvoiceLine invoiceLine=req.getContext().asType(InvoiceLine.class);
		Invoice invoice=invoiceLine.getInvoice();
		Address companyAddress=invoice.getCompany().getAddress();
		Address invoiceAddress=invoice.getInvoiceAddress();
		if(service.checkState(companyAddress.getState(),invoiceAddress.getState())) {
			return true;
		}
		return false;
	}
	
	
	
	public void setGst(ActionRequest req,ActionResponse resp) {
		InvoiceLine invoiceLine=req.getContext().asType(InvoiceLine.class);
		Invoice invoice=invoiceLine.getInvoice();
		try {
			Address companyAddress=invoice.getCompany().getAddress();
			Address invoiceAddress=invoice.getInvoiceAddress();
			BigDecimal net_amount=invoiceLine.getNetAmount();
			BigDecimal gstRate=invoiceLine.getGstRate();
			BigDecimal igst=net_amount.multiply(gstRate);
			BigDecimal gst=igst.divide(BigDecimal.valueOf(2));
			BigDecimal grossAmount=net_amount;
			if(service.checkState(companyAddress.getState(), invoiceAddress.getState())) {
				resp.setValue("sgst", gst);
				grossAmount=grossAmount.add(gst);
			}
			else {
				resp.setValue("igst", igst);
				grossAmount=grossAmount.add(igst);
			}
			resp.setValue("cgst", gst);
			grossAmount=grossAmount.add(gst);
			resp.setValue("grossAmount", grossAmount);
		}catch(Exception e) {
			
		}	
	}

}
