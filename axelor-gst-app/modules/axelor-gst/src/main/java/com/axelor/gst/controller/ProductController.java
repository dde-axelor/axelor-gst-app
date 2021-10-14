package com.axelor.gst.controller;

import java.util.List;

import com.axelor.gst.app.Product;
import com.axelor.gst.app.repo.ProductRepository;
import com.axelor.meta.CallMethod;
import com.google.inject.Inject;

public class ProductController {
	
	@Inject
	private ProductRepository product;
	
	@CallMethod
	public String getProductIds(List<Integer> ids) {
		
		String str="";
		
		if(ids==null) {
			
			List<Product> list=product.all().fetch();
			
			for(Product p:list) {
				
				String s=Long.toString(p.getId());
				
				str+=s;
				
				if(list.indexOf(p)!=list.size()-1) 
					str+=",";
			}
		}
		else {
			
			for(Integer id:ids) {
				
				String s=Integer.toString(id);
				
				str+=s;
				
				if(ids.indexOf(id)!=ids.size()-1) 
					str+=",";
			}
		}
		
		return str;
	}
}
