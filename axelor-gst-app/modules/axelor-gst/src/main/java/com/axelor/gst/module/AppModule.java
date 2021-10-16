package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceLineServiceImplementation;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.InvoiceServiceImplementation;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImplementation;

public class AppModule extends AxelorModule{
	
	@Override
	public void configure() {
		
		bind(InvoiceService.class).to(InvoiceServiceImplementation.class);
		
		bind(InvoiceLineService.class).to(InvoiceLineServiceImplementation.class);
		
		bind(SequenceService.class).to(SequenceServiceImplementation.class);
	}

}
