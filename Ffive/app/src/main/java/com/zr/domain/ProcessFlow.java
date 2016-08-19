package com.zr.domain;

import java.util.ArrayList;
import java.util.Date;

public class ProcessFlow {

	private Role flow_role;//流程对应的角色
	private String flow_description;//流程描述
	private String flow_num;//流程编号
	private Date flow_time;//流程时间
	private String flow_video;//流程影像
	private ArrayList<Problem>  flowproblem;//流程问题
	
	public ProcessFlow(Role role)
	{
		this.flow_role=role;
		this.flow_description=new String("");
		this.flow_num=new String("");
		this.flow_time=null;
		this.flow_video=new  String("");
		this.flowproblem=new ArrayList<Problem> ();
	}
	
	public ProcessFlow(ProcessFlow f)
	{
		this.flow_role=f.getFlow_role();
		this.flow_description=new String("");
		this.flow_num=new String("");
		this.flow_time=null;
		this.flow_video=new  String("");
		this.flowproblem=new ArrayList<Problem> ();
		
		setFlow_description(f.getFlow_description());
		setFlow_num(f.getFlow_num());
		this.flow_time=null;
		if(f.getFlow_time()!=null)
		{
			setFlow_time(f.getFlow_time());
		}
		setFlow_video(f.getFlow_video());
		if(f.getFlowproblem()!=null)
		{
			setFlowproblem(f.getFlowproblem());
		}
	}

	public Role getFlow_role() {
		return flow_role;
	}

	/*public void setFlow_role(Role flow_role) {
		this.flow_role = flow_role;
	}*/

	public String getFlow_description() {
		return flow_description;
	}
	
	public void setFlow_description(String flow_description) {
		this.flow_description = flow_description;
	}
	
	public String getFlow_num() {
		return flow_num;
	}
	
	public void setFlow_num(String flow_num) {
		this.flow_num = flow_num;
	}
	
	public Date getFlow_time() {
		return flow_time;
	}
	
	public void setFlow_time() {
		this.flow_time =new Date();
	}
	
	public void setFlow_time(Date date) {
		this.flow_time=date;
	}
	
	public String getFlow_video() {
		return flow_video;
	}
	
	public void setFlow_video(String flow_video) {
		this.flow_video = flow_video;
	}
	
	public ArrayList<Problem> getFlowproblem() {
		return flowproblem;
	}
	
	public void setFlowproblem(ArrayList<Problem> flowproblem) {
		this.flowproblem=new ArrayList<Problem>();
		for(int i=0;i<flowproblem.size();i++)
		{	
			Problem p=new Problem(flowproblem.get(i));
			this.flowproblem.add(p);
		}
	}

	public void setFlowproblem(String flowproblem) {
		this.flowproblem=new ArrayList<Problem> ();
		addFlowproblem(flowproblem);
		/*String[] s=flowproblem.split(";");
		for(String ss:s)
		{
			String[] sss=ss.split("；");
			for(String a:sss)
			{
				if(!a.equals(""))
				{
					Problem p=new Problem();
					p.setProblem_description(a);
					this.flowproblem.add(p);
				}				
			}			
		}*/
	}

	public void addFlowproblem(String flowproblem) {
		String[] s=flowproblem.split(";");
		for(String ss:s)
		{
			String[] sss=ss.split("；");
			for(String a:sss)
			{
				if(!a.equals(""))
				{
					Problem p=new Problem();
					p.setProblem_description(a);
					this.flowproblem.add(p);
				}
			}			
		}
	}
}
