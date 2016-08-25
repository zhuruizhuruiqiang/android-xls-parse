package com.zr.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ReportItem {

	public static final int ITEM_TYPE_PRONAME=0;
	public static final int ITEM_TYPE_PROPARR=1;
	public static final int ITEM_TYPE_PROFLOW=2;
	public static final int ITEM_TYPE_PROPROB=3;
	private int typenum;	
	private String parrname;
	private String message;
	private String time;

	//PRONAME使用parrname和message表示项目编号和项目名称
	//PROPARR使用parrname和message
	//PROFLOW使用parrname表示角色,time 表示时间,message表示描述和问题。
    //PROPROB使用parrname和message
	
	
	public ReportItem()
	{
		this.typenum=0;
		parrname="";
		message="";
		time="";
	}
	

	public int getTypenum() {
		return typenum;
	}

	public void setTypenum(int typenum) {
		this.typenum = typenum;
	}

	public String getParrname() {
		return parrname;
	}

	public void setParrname(String parrname) {
		this.parrname = parrname;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}	
}
