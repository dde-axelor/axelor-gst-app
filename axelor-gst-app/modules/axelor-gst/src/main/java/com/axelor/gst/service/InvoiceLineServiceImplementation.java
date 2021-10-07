package com.axelor.gst.service;

import com.axelor.gst.app.State;

public class InvoiceLineServiceImplementation implements InvoiceLineService {

	@Override
	public boolean checkState(State s1, State s2) {
		if(s1.getName().equalsIgnoreCase(s2.getName()))
			return true;
		return false;
	}

}
