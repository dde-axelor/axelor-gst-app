package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.controller.InvoiceController;
import com.axelor.gst.controller.InvoiceLineController;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceLineServiceImplementation;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.InvoiceServiceImplementation;

public class AppModule extends AxelorModule{
	
	@Override
	public void configure() {
		bind(InvoiceController.class);
		bind(InvoiceLineController.class);
		bind(InvoiceService.class).to(InvoiceServiceImplementation.class);
		bind(InvoiceLineService.class).to(InvoiceLineServiceImplementation.class);
	}

}
