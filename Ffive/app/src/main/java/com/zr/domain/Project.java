package com.zr.domain;

import java.util.ArrayList;
/*
 * 存储有关项目的信息
 * 
 * project_version;//项目文件对应的版本号，如1.0，可从标准项目库以及流程文件中获取
 * project_editor;//项目文件的修订者，如刘敏，可从标准项目库以及流程文件中获取
 * project_num;//项目对应的编号，如A1，可从标准项目库以及流程文件中获取
 * project_name;//项目对应的名称，如线路短路返回，可从标准项目库以及流程文件中获取
 * project_involved_depart;//项目参与的部门
 * project_problems;//项目对应的角色的问题
 * project_processflows;//项目对应的处理流程
 */
public class Project {	
	private String project_version;//项目文件对应的版本号，如1.0，可从标准项目库以及流程文件中获取
	private String project_editor;//项目文件的修订者，如刘敏，可从标准项目库以及流程文件中获取	
	private String project_num;//项目对应的编号，如A1，可从标准项目库以及流程文件中获取
	private String project_name;//项目对应的名称，如线路短路返回，可从标准项目库以及流程文件中获取
	private String project_involved_depart;//项目参与的部门
	private String project_problem;//项目存在的问题
	private ArrayList<ProcessFlow> project_processflows;//项目对应的处理流程

	/*
	 * 构造方法完成初始化
	 */
	public Project(String project_version,String editor)
	{
		this.project_version=project_version;
		this.project_editor=editor;		
		this.project_num=new String("");
		this.project_name=new String("");
		this.project_involved_depart=new String("");
		this.project_problem=new String("");				
		this.project_processflows=new ArrayList<ProcessFlow>();
	}
	public Project(Project p)
	{
		this.project_version=new String("");
		this.project_editor=new String("");		
		this.project_num=new String("");
		this.project_name=new String("");
		this.project_involved_depart=new String("");
		this.project_problem=new String("");				
		this.project_processflows=new ArrayList<ProcessFlow>();
		
		setProject_version(p.getProject_version());
		setProject_editor(p.getProject_editor());
		setProject_num(p.getProject_num());
		setProject_name(p.project_name);
		setProject_involved_depart(p.getProject_involved_depart());
		setProject_problem(p.getProject_problem());
		if(p.getProject_processflows()!=null)
		{
			setProject_processflows(p.getProject_processflows());
		}
	}

	/*
	 * 项目流程，项目问题添加以及获取方法
	 */
	public ArrayList<ProcessFlow> getProject_processflows() {
		return project_processflows;
	}
	
	public void setProject_processflows(ArrayList<ProcessFlow> project_processflows) {
		for(int i=0;i<project_processflows.size();i++)
		{
			ProcessFlow f=new ProcessFlow(project_processflows.get(i));
			addProject_processflow(f);
			//this.project_processflows.add(f);
		}
	}
	
	public void addProject_processflow(ProcessFlow project_processflow) {
		//当添加一个流程的时候，也要添加相关联的角色，添加相关联的角色问题
		this.project_processflows.add(project_processflow);
	}
	/*
	 * 项目基本信息获取以及写入方法
	 */	
	public String getProject_version() {
		return project_version;
	}
	
	public void setProject_version(String project_version) {
		this.project_version = project_version;
	}
	
	public String getProject_editor() {
		return project_editor;
	}
	
	public void setProject_editor(String project_editor) {
		this.project_editor = project_editor;
	}
	
	public String getProject_num() {
		return project_num;
	}
	
	public void setProject_num(String project_num) {
		this.project_num = project_num;
	}
	
	public String getProject_name() {
		return project_name;
	}
	
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	
	public String getProject_involved_depart() {
		return project_involved_depart;
	}
	
	public void setProject_involved_depart(String project_involved_depart) {
		this.project_involved_depart = project_involved_depart;
	}	

	public String getProject_problem() {
		return project_problem;
	}

	public void setProject_problem(String project_sproblem) {
		this.project_problem = project_sproblem;
	}
}
