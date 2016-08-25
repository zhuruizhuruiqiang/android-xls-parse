package com.zr.domain;

import java.util.ArrayList;
import java.util.List;

public class Work {
	public String work_name;//工作计划的名称
	public List<Business> work_businesslist;//工作计划中包含的工作项目列表

	public Work(String work_name)
	{
		this.work_name=work_name;
		this.work_businesslist=new ArrayList<Business>();
	}
	
	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String work_name) {
		this.work_name = work_name;
	}

	public List<Business> getWork_businesslist() {
		return work_businesslist;
	}

	public Business getWork_business(int index) {
		return work_businesslist.get(index);
	}
	
	public void setWork_businesslist(List<Business> work_businesslist) {
		this.work_businesslist = work_businesslist;
	}
	
	public void addWork_businesslist(Business work_business) {
		this.work_businesslist.add(work_business);
	}
	
}
