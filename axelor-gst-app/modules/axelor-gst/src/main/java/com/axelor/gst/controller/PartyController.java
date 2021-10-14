package com.axelor.gst.controller;

import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class PartyController {
	
	@Inject
	private SequenceService sequence;
	
	public void setPartyReference(ActionRequest req,ActionResponse resp) {
		
		try {
			
			resp.setValue("reference", sequence.getNextSequence("Party"));
			
		}catch(Exception e)
		{
			
			resp.addError("reference", "No sequence is specified for this model");
		}
	}
}
