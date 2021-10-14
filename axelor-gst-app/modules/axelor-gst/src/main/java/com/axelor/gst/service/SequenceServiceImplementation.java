package com.axelor.gst.service;


import com.axelor.gst.app.Sequence;
import com.axelor.gst.app.repo.SequenceRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class SequenceServiceImplementation implements SequenceService {

	@Inject
	private SequenceRepository seqRepo;
	
	
	@Override
	public String getNextSequence(String modelName) {
		
		Sequence seq =seqRepo.all().filter("self.model.name=?",modelName).fetchOne();
		
		return incrementedSequence(seq);
	}

	
	
	@Override
	public String getNextNumber(Sequence seq) {
		
		String prefix=seq.getPrefix();
		
		String suffix=seq.getSuffix();
		
		int padding=seq.getPadding();
		
		String nextNumber="";
		
		if(seq.getNextNumber()==null)
		{	
			if(prefix!=null)
				nextNumber=prefix;
			
			for(int i=0;i<padding;i++) 
				nextNumber=nextNumber.concat("0");	
			
			
			if(suffix!=null)
				nextNumber=nextNumber.concat(suffix);
		}else{
			
			if(prefix!=null && suffix!=null)
				nextNumber=prefix+getPaddingString(seq.getNextNumber(),seq.getPadding())+suffix;
			
			else if(prefix==null && suffix!=null)
				nextNumber=getPaddingString(seq.getNextNumber(),seq.getPadding())+suffix;
			
			else if(suffix==null && prefix!=null)
				nextNumber=prefix+getPaddingString(seq.getNextNumber(),seq.getPadding());
			
			else
				nextNumber=getPaddingString(seq.getNextNumber(),seq.getPadding());
		}
		return nextNumber;
	}

	
	
	@Override
	public String getPaddingString(String seq,int padding) {
		
		String pad="";
		for(int i=0;i<seq.length();i++) {
			
			if((int) seq.charAt(i)>=48 && (int) seq.charAt(i)<=57) 
				pad=pad+seq.charAt(i);
		}
		
		if(pad.length()>padding) {
			
			for(int j=0;j<pad.length();j++) {
				
				if(pad.length()-padding<=j)
					pad=pad.replace(pad.charAt(j), pad.charAt(j+1));
			}
		}
		else if(pad.length()<padding){
			
			for(int j=0;j<padding-pad.length();j++) 
				pad="0"+pad;
		}
		return pad;
	}

	
	
	@Transactional
	@Override
	public String incrementedSequence(Sequence sequence) {
		
		String pad=getPaddingString(sequence.getNextNumber(),sequence.getPadding());
		
		String incremented="";
		
		String s=Integer.toString(Integer.parseInt(pad)+1);
		
		for(int i=0;i<pad.length()-s.length();i++)
			incremented+="0";
		
		incremented+=s;
		
		if(sequence.getPrefix()!=null && sequence.getSuffix()!=null) 
			incremented=sequence.getPrefix()+incremented+sequence.getSuffix();
		
		else if(sequence.getPrefix()==null && sequence.getSuffix()!=null)
			incremented=incremented+sequence.getSuffix();
		
		else if(sequence.getSuffix()==null && sequence.getPrefix()!=null)
			incremented=sequence.getPrefix()+incremented;
		
		sequence.setNextNumber(incremented);
		
		seqRepo.save(sequence);
		
		return incremented;
	}

}
