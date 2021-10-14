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
			for(int i=0;i<padding;i++) {
				if(i==padding-1) {
					nextNumber=nextNumber.concat("1");
				}else {
					nextNumber=nextNumber.concat("0");
				}
			}
			if(suffix!=null)
				nextNumber=nextNumber.concat(suffix);
		}else{
			if(prefix!=null && suffix!=null)
				nextNumber=prefix+getPadding(seq.getNextNumber(),seq.getPadding())+suffix;
			else if(prefix==null && suffix!=null)
				nextNumber=getPadding(seq.getNextNumber(),seq.getPadding())+suffix;
			else if(suffix==null && prefix!=null)
				nextNumber=prefix+getPadding(seq.getNextNumber(),seq.getPadding());
			else
			{
				nextNumber=getPadding(seq.getNextNumber(),seq.getPadding());
			}
		}
		return nextNumber;
	}

	@Override
	public String getPadding(String seq,int padding) {
		String pad="";
		for(int i=0;i<seq.length();i++) {
			if((int) seq.charAt(i)>=48 && (int) seq.charAt(i)<=57) {
				pad=pad+seq.charAt(i);
			}
		}
		if(pad.length()==padding)
			return pad;
		else if(pad.length()>padding) {
			String s="";
			for(int j=0;j<pad.length();j++) {
				if(pad.length()-padding<=j)
					s+=pad.charAt(j);
			}
			return s;
		}
		else {
			for(int j=0;j<padding-pad.length();j++) {
				pad="0"+pad;
			}
			return pad;
		}
	}

	@Transactional
	@Override
	public String incrementedSequence(Sequence sequence) {
		String pad=getPadding(sequence.getNextNumber(),sequence.getPadding());
		String incremented="";
		int val=Integer.parseInt(pad);
		val+=1;
		String s=Integer.toString(val);
		int sl=pad.length()-s.length();
		for(int i=0;i<sl;i++)
		{
			incremented+="0";
		}
		incremented+=s;
		String mainSequence="";
		if(sequence.getPrefix()!=null && sequence.getSuffix()!=null) {
			mainSequence=sequence.getPrefix()+incremented+sequence.getSuffix();
		}
		else if(sequence.getPrefix()==null && sequence.getSuffix()!=null)
		{
			mainSequence=incremented+sequence.getSuffix();
		}
		else if(sequence.getSuffix()==null && sequence.getPrefix()!=null)
		{
			mainSequence=sequence.getPrefix()+incremented;
		}
		else
		{
			mainSequence=incremented;
		}
		System.out.println(mainSequence);
		sequence.setNextNumber(mainSequence);
		seqRepo.save(sequence);
		return mainSequence;
	}

}
