package com.ig.soap;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ig.queue.QueueOperations;

@WebService
public class GCD {

	@WebMethod
	public String getGcd() {
		int gcdVal = 0;
		try {
			QueueOperations queue = new QueueOperations();
			gcdVal = queue.gcd();
		} catch (Exception ex) {
			ex.printStackTrace();
			return String.valueOf(-1);

		}

		return String.valueOf(gcdVal);
	}
	
	@WebMethod
	public String gcdList() {
		List<Integer> list = new ArrayList<>();
				
		try{
			QueueOperations queue = new QueueOperations();
			list = queue.getGCDList();
			}catch(Exception ex){
				ex.printStackTrace();
				return "Error in getting list from queue.";
				
			}

	return list.toString();
	}
	
	@WebMethod
	public String gcdSum() {
		List<Integer> list = new ArrayList<>();
		long sum = 0;
		try{
			QueueOperations queue = new QueueOperations();
			list = queue.getGCDList();
			for(Integer val : list){
				sum += val;
			}
			
			}catch(Exception ex){
				ex.printStackTrace();
				return String.valueOf(-1);
				
			}

	return String.valueOf(sum);
	}

}
