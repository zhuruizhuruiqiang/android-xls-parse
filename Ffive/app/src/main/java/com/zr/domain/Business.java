package com.zr.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zr.domain.ReportItem;

import android.util.Log;
/*
 * business用来存储项目,一个business可能由多个Project组成
 * 
 * business_projects;//business中的project
 * business_flows;//事项的所有流程
 * business_probs;//事项的所有问题
 * 
 * business_mData,business_nameData,business_messData,business_flowData,business_probData存放方便显示的一些信息
 * 
 * business_name;//事项的名称
 * business_place;//事项的地点
 * business_carnum;//事项的车次
 * business_involeveddepart;//事项的相关部门
 * business_date;//事项的时间
 */
public class Business {
	private ArrayList<Project> business_projects;//business列表中的每个project
	
	
	private ArrayList<ProcessFlow> business_flows;//事项所有流程
	private ArrayList<Problem> business_probs;//事项的所有问题
	
	private List<ReportItem> business_mData = null;
	private List<ReportItem> business_nameData = null;//存放名称
	private List<ReportItem> business_messData = null;//存放地点、车次、部门
	private List<ReportItem> business_flowData = null;//存放流程
	private ReportItem business_probData = null;//存放问题
	
	private String business_num;//事项的编号
	private String business_name;//事项的名称
	private String business_place;//事项的地点
	private String business_carnum;//事项的车次
	private String business_involeveddepart;//事项的相关部门
	private Date business_date;//事项的时间

	public Business()
	{
		this.business_projects= new ArrayList<Project>();
		this.business_flows=new ArrayList<ProcessFlow>();
		this.business_probs=new ArrayList<Problem>();
				
		this.business_mData=new ArrayList<ReportItem>();
		this.business_nameData=new ArrayList<ReportItem>();
		this.business_messData=new ArrayList<ReportItem>();
		this.business_flowData=new ArrayList<ReportItem>();
		this.business_probData=new ReportItem();
		
		this.business_num="";
		this.business_name="";
		this.business_place="";
		this.business_carnum="";
		this.business_involeveddepart="";
		this.business_date=new Date();
	}	
	
	/*
	 * business projects的get set add
	 * 当projects变化时，name,depart,flow,prob都发生变化
	 */
	public ArrayList<Project> getBusiness_projects() {
		return business_projects;
	}

	public Project getBusiness_project(int index) {
		return business_projects.get(index);
	}
	
	public void setBusiness_projects(ArrayList<Project> business_projects) {
		this.business_projects = business_projects;
		setBusinessparam();
		setBusinessprobs();
		setBusiness_flows();
	}
	
	public void addBusiness_project(Project business_project) {
		Project p=new Project(business_project);
		this.business_projects.add(p);
		setBusinessparam();
		setBusinessprobs();
		setBusiness_flows();
	}

	/*
	 * business flows的get set add
	 * 当flow变化时，仅仅reportitem发生变化
	 */
	public ArrayList<ProcessFlow> getBusiness_flows() {
		return business_flows;
	}

	public ProcessFlow getBusiness_flow(int index) {
		return business_flows.get(index);
	}
	
	public void setBusiness_flows(ArrayList<ProcessFlow> business_flows) {
		this.business_flows = business_flows;
		setBusiness_flowData();
		setBusiness_mData();	
	}

	public void addBusiness_flow(ProcessFlow business_flow) {
		this.business_flows.add(business_flow);
		setBusiness_flowData();
		setBusiness_mData();	
	}
	
	public void setBusiness_flows()
	{
		this.business_flows=new ArrayList<ProcessFlow>();
		for(Project p:getBusiness_projects())
		{
			business_flows.addAll(p.getProject_processflows());
		}
		setBusiness_flowData();
		setBusiness_mData();
	}

	/*
	 * business problems的get set add
	 * 当prob变化时，仅仅reportitem发生变化
	 */
	public ArrayList<Problem> getBusiness_probs() {
		return business_probs;
	}

	public Problem getBusiness_prob(int index) {
		return business_probs.get(index);
	}
	
	public void setBusiness_probs(ArrayList<Problem> business_probs) {
		this.business_probs = business_probs;
		setBusiness_probData();
		setBusiness_mData();
	}

	public void addBusiness_prob(Problem business_prob) {
		this.business_probs.add(business_prob);
		setBusiness_probData();
		setBusiness_mData();
	}
	
	public void setBusinessprobs() {
		this.business_probs=new ArrayList<Problem>();
		String s="";
		if(business_projects.size()>0)
		{
			for(Project p:business_projects)
			{
				String[] s1=p.getProject_problem().split(";");
				for(String s11:s1)
				{
					String[] s2=s11.split("；");
					for(String s22:s2)
					{
						if(!s.contains(s22))
						{
							s+=s22+"；";
							if(!s22.equals(""))								
							{
								Problem prob=new Problem();
								prob.setProblem_description(s22);
								this.business_probs.add(prob);
							}							
						}
					}
				}
			}			
		}
		setBusiness_probData();
		setBusiness_mData();
	}
	/*
	 * ReportItem 类型参数设置
	 */	
	public void setReportitemdata() 
	{
		setBusiness_nameData();		
		setBusiness_messData();
		setBusiness_flowData();
		setBusiness_probData();
		setBusiness_mData();		
	}

	public List<ReportItem> getBusiness_mData() {
		return business_mData;
	}

	public void setBusiness_mData(List<ReportItem> business_mData) {
		this.business_mData = business_mData;
	}

	public void setBusiness_mData() {
		this.business_mData=new ArrayList<ReportItem>();
		//时间，流程
		ReportItem item=new ReportItem();
		Map<String, String> data=new HashMap<String, String>();
		data.put("flowdescription", "时间");
		item.setMessage("流程（流程问题）");
		item.setTypenum(ReportItem.ITEM_TYPE_PROPARR);	
		item.setData(data);
		
		business_mData.addAll(business_nameData);
		business_mData.addAll(business_messData);		
		business_mData.add(item);
		business_mData.addAll(business_flowData);
		business_mData.add(business_probData);
	}
	/*
	 * 存储name信息
	 * data以pronum+i为key,存储project_num
	 * messeage存贮project_num+projectname
	 */
	public List<ReportItem> getBusiness_nameData() {
		return business_nameData;
	}

	public void setBusiness_nameData(List<ReportItem> business_nameData) {
		this.business_nameData = business_nameData;
	}

	public void setBusiness_nameData()
	{
		this.business_nameData=new ArrayList<ReportItem>();
		for(int i=0;i<getBusiness_projects().size();i++)
		{	
			ReportItem item=new ReportItem();
			Map<String, String> data=new HashMap<String, String>();	
			Project p=getBusiness_projects().get(i);
			data.put("pronum"+i,p.getProject_num());
			item.setTypenum(ReportItem.ITEM_TYPE_PRONAME);				
			item.setData(data);
			item.setMessage(p.getProject_name());
			business_nameData.add(item);
		}
	}
	/*
	 * 存储地点/车次/参与部门信息
	 * data以place,carnum,depart为key,存储"地点","车次","参与部门"
	 * messeage存贮business_place,business_carnum,business_depart
	 */
	public List<ReportItem> getBusiness_messData() {
		return business_messData;
	}

	public void setBusiness_messData(List<ReportItem> business_messData) {
		this.business_messData = business_messData;
	}

	public void setBusiness_messData() {
		this.business_messData=new ArrayList<ReportItem>();
		//地点
		ReportItem item=new ReportItem();			
		Map<String, String> data=new HashMap<String, String>();
		data.put("place", "地点");
		item.setData(data);
		item.setMessage(getBusiness_place());
		item.setTypenum(ReportItem.ITEM_TYPE_PROPARR);
		business_messData.add(item);
		//车次
		item=new ReportItem();			
		data=new HashMap<String, String>();
		data.put("carnum", "车次");
		item.setMessage(getBusiness_carnum());
		item.setData(data);
		item.setTypenum(ReportItem.ITEM_TYPE_PROPARR);
		business_messData.add(item);	
		//部门
		item=new ReportItem();			
		data=new HashMap<String, String>();
		data.put("depart", "参与部门");
		item.setMessage(getBusiness_involeveddepart());
		item.setData(data);
		item.setTypenum(ReportItem.ITEM_TYPE_PROPARR);
		business_messData.add(item);	
	}
	/*
	 * 存储流程信息
	 * data以flow+i为key,存储流程时间
	 * messeage存贮流程信息
	 */
	public List<ReportItem> getBusiness_flowData() {
		return business_flowData;
	}

	public void setBusiness_flowData(List<ReportItem> business_flowData) {
		this.business_flowData = business_flowData;
	}

	public void setBusiness_flowData()
	{
		this.business_flowData=new ArrayList<ReportItem>();
		for(int i=0;i<getBusiness_flows().size();i++)
		{
			ProcessFlow f=getBusiness_flow(i);
			ReportItem item=new ReportItem();
			Map<String, String> data=new HashMap<String, String>();
			if(f.getFlow_time()!=null)
			{
				Date flowdate=f.getFlow_time();
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
				data.put("flow"+i,df.format(flowdate));
			}
			else
			{
				data.put("flow"+i,"------");
			}
			item.setTypenum(ReportItem.ITEM_TYPE_PROFLOW);
			item.setData(data);
			
			String s="";
			for(int j=0;j<f.getFlowproblem().size();j++)
			{
				if(f.getFlowproblem().get(j).isProblem_choose())
				{
					s+=f.getFlowproblem().get(j).getProblem_description()+";";
				}
			}
			if(!s.equals(""))
			{
				item.setMessage(GetFlowDesc(f.getFlow_description())+"("+s+")");
			}
			else
			{
				item.setMessage(GetFlowDesc(f.getFlow_description()));
			}
			business_flowData.add(item);
		}
	}
	
	public void setBusiness_flowData(int flowposition)
	{
		ProcessFlow f=getBusiness_flow(flowposition);
		ReportItem item=new ReportItem();
		Map<String, String> data=new HashMap<String, String>();
		if(f.getFlow_time()!=null)
		{
			Date flowdate=f.getFlow_time();
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			data.put("flow"+flowposition,df.format(flowdate));
		}
		else
		{
			data.put("flow"+flowposition,"------");
		}
		item.setTypenum(ReportItem.ITEM_TYPE_PROFLOW);
		item.setData(data);
			
		String s="";
		for(int j=0;j<f.getFlowproblem().size();j++)
		{
			if(f.getFlowproblem().get(j).isProblem_choose())
			{
				s+=f.getFlowproblem().get(j).getProblem_description()+";";
			}
		}
		if(!s.equals(""))
		{
			item.setMessage(GetFlowDesc(f.getFlow_description())+"("+s+")");
		}
		else
		{
			item.setMessage(GetFlowDesc(f.getFlow_description()));
		}
		this.business_flowData.set(flowposition, item);
	}


	public static String GetFlowDesc(String s) {
		String s_item="";
		int position=0;
		int start=0;
		int end=0;
		while(position<s.length())
		{
			start=s.indexOf("&", position);
			if(start>0)
			{
				end=s.indexOf("&",start+1);
				s_item+=s.substring(position, start-1);	
				String saa=s.substring(start+1, end);
				String[] aa=saa.split("#");
				if(aa.length>1)
				{
					s_item+=aa[1];
					Log.v("business", "desc(flow) aa"+aa[1]);
				}
				position=end+2;
			}
			else
			{
				s_item+=s.substring(position, s.length());
				position=s.length();
			}
		}
		//Log.v("business", "desc(flow)"+s_item);
		return s_item;
	}
	/*
	 * 存储流程信息
	 * data以flow+i为key,存储流程时间
	 * messeage存贮流程信息
	 */
	public ReportItem getBusiness_probData() {
		return business_probData;
	}

	public void setBusiness_probData(ReportItem business_probData) {
		this.business_probData = business_probData;
	}

	public void setBusiness_probData()
	{
		this.business_probData=new ReportItem();
		ReportItem item=new ReportItem();
		Map<String, String> data=new HashMap<String, String>();
		String prob="";
		for(Problem p:getBusiness_probs())
		{
			if(p.isProblem_choose())
			{
				prob+=p.getProblem_description()+"；";
			}
		}
		data.put("problem","问题");
		item.setTypenum(ReportItem.ITEM_TYPE_PROPARR);
		item.setData(data);
		item.setMessage(prob);
		setBusiness_probData(item);
	}
	/*
	 * 其他基本参数设置getter and setter
	 */
	public void setBusinessparam() {
		setBusiness_num();
		setBusiness_name();
		setBusiness_involeveddepart();
	}

	public String getBusiness_num() {
		return business_num;
	}

	public void setBusiness_num(String business_num) {
		this.business_num = business_num;
		setBusiness_nameData();
		setBusiness_mData();
	}

	public void setBusiness_num() {
		String s="";
		if(business_projects.size()>0)
		{
			for(Project p:business_projects)
			{
				s+=p.getProject_num()+"，";
			}			
		}
		if(s.endsWith("，"))
		{
			s=s.substring(0, s.length()-1);
		}
		this.business_num =s;
		setBusiness_nameData();
		setBusiness_mData();
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
		setBusiness_nameData();
		setBusiness_mData();
	}

	public void setBusiness_name() {
		String s="";
		if(business_projects.size()>0)
		{
			for(Project p:business_projects)
			{
				s+=p.getProject_name()+"&&";
			}			
		}
		if(s.endsWith("+"))
		{
			s=s.substring(0, s.length()-2);
		}
		this.business_name =s;
		setBusiness_nameData();
		setBusiness_mData();
	}
	
	public String getBusiness_involeveddepart() {
		return business_involeveddepart;
	}

	public void setBusiness_involeveddepart(String business_involeveddepart) {
		this.business_involeveddepart = business_involeveddepart;
		setBusiness_messData();
		setBusiness_mData();
	}

	private void setBusiness_involeveddepart() {
		String s_depart="";
		//System.out.println("bussize"+businessprojects.size());
		if(business_projects.size()>0)
		{			
			for(Project p:business_projects)
			{
				
				String[] s1=p.getProject_involved_depart().split(",");
				for(String s11:s1)
				{
					String[] s2=s11.split("，");
					for(String s22:s2)
					{
						if(!s_depart.contains(s22))
						{
							s_depart+=s22+"，";
						}
					}
				}
			}
		}
		if(s_depart.endsWith("，"))
		{
			s_depart=s_depart.substring(0, s_depart.length()-1);
		}
		this.business_involeveddepart=s_depart;
		setBusiness_messData();
		setBusiness_mData();
	}

	public String getBusiness_place() {
		return business_place;
	}

	public void setBusiness_place(String business_place) {
		this.business_place = business_place;
		setBusiness_messData();
		setBusiness_mData();
	}

	public String getBusiness_carnum() {
		return business_carnum;
	}

	public void setBusiness_carnum(String business_carnum) {
		this.business_carnum = business_carnum;
		setBusiness_messData();
		setBusiness_mData();
	}

	public Date getBusiness_date() {
		return business_date;
	}

	public void setBusiness_date(Date business_date) {
		this.business_date = business_date;
	}

}
