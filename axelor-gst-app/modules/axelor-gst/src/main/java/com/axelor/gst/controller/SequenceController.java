package com.axelor.gst.controller;

import com.axelor.gst.app.Sequence;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class SequenceController {

	@Inject
	private SequenceService service;
	
	public void getSequence(ActionRequest req,ActionResponse resp) {
		
		Sequence sequence = req.getContext().asType(Sequence.class);
		
		resp.setValue("nextNumber", (service.getNextNumber(sequence)));
	}
}
