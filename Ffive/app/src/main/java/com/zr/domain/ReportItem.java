package com.zr.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ReportItem {

	public static final int ITEM_TYPE_PRONAME=0;
	public static final int ITEM_TYPE_PROPARR=1;
	public static final int ITEM_TYPE_PROFLOW=2;
	private int typenum;	
	private Map<String, String> data;
	private String message;
	
	
	public ReportItem()
	{
		this.typenum=0;
		data=new HashMap<String, String>();
		message="";
	}
	
	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
	public int getTypenum() {
		return typenum;
	}

	public void setTypenum(int typenum) {
		this.typenum = typenum;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}	
}
