//8月19日检查无问题
package com.zr.thread;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zr.apprail.ApplicationTrans;
import com.zr.domain.Problem;
import com.zr.domain.ProcessFlow;
import com.zr.domain.Role;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ReportReadThread extends Thread {
	
	private String fullname;
	private int index;
	ApplicationTrans applicationdata;

	@Override
	public void run() {
		applicationdata=ApplicationTrans.getInstance();
		readrecordxls();
	}
	
	private void readrecordxls() {
		Workbook book = null;
        try {
        	// 获得第一个工作表对象
        	book = Workbook.getWorkbook(new File(getFullname()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (book != null && book.getNumberOfSheets() > 0) 
        {
			// 获得第一个工作表对象
			Sheet ws = book.getSheet(0);
			// 得到第一列第一行的单元格
			int rownum = ws.getRows();// 得到行数
			//以下三个变量标记xls文件中的当前位置处于哪个位置
			boolean business = false;
			boolean flow = false;
			ProcessFlow pflow;
			boolean problem = false;
			String sdate="";
			int currentbusiness = 0;//处于第几个business
			int currentflow = 0;//处于第几个flow
			for (int i = 0; i < rownum; i++)// 循环进行读写
			{
			    Cell cell1  = ws.getCell(0, i);
			    String cell1name = cell1.getContents();
			    //判断当前读取位置，是版本？还是
			    if(cell1name.compareTo("记录人员")==0)
			    {
			    	business=false;
			    	flow=false;
			    	problem=false;
			    }
			    else if(cell1name.compareTo("评估角色")==0)
			    {
			    	business=false;
			    	flow=false;
			    	problem=false;
			    }
			    else if(cell1name.compareTo("项目")==0)
			    {
			    	business=true;
			    	flow=false;
			    	problem=false;
			    	currentbusiness+=1;
			    }
			    else if(cell1name.compareTo("处置流程")==0)
			    {
			    	i++;
			    	business=false;
			    	flow=true;
			    	problem=false;
			    	currentflow=0;
			    }
			    else if(cell1name.compareTo("存在问题")==0)
			    {
			    	business=false;
			    	flow=false;
			    	problem=true;
			    }
			    //处于项目，流程，或者问题当中
			    else
			    {
			    	if(business==true)
			        {
			    		if(cell1name.compareTo("编号")==0)
			    		{
			        		Cell cell2  = ws.getCell(1, i);
			            	String cell2name = cell2.getContents();
			            	applicationdata.getWork(index).getWork_business(currentbusiness-1).setBusiness_num(cell2name);
			    		}
			    		else if(cell1name.compareTo("名称")==0)
			    		{
			        		Cell cell2  = ws.getCell(1, i);
			            	String cell2name = cell2.getContents();
			            	applicationdata.getWork(index).getWork_business(currentbusiness-1).setBusiness_name(cell2name);
			    		}
			    		else if(cell1name.compareTo("参加部门")==0)
			    		{
			        		Cell cell2  = ws.getCell(1, i);
			            	String cell2name = cell2.getContents();
			            	applicationdata.getWork(index).getWork_business(currentbusiness-1).setBusiness_involeveddepart(cell2name);
			    		}
			    		else if(cell1name.compareTo("时间")==0)
			    		{
			        		Cell cell2  = ws.getCell(1, i);
			            	String cell2name = cell2.getContents();
			            	sdate=cell2name;
			            	//string转换为时间
			            	SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
			            	Date date = null;
							try {
								if(cell2name!=null&&!cell2name.equals(""))
								{
									date = df.parse(cell2name);
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
			            	applicationdata.getWork(index).getWork_business(currentbusiness-1).setBusiness_date(date);
			    		}
			    		else if(cell1name.compareTo("地点")==0)
			    		{
			        		Cell cell2  = ws.getCell(1, i);
			            	String cell2name = cell2.getContents();
			            	applicationdata.getWork(index).getWork_business(currentbusiness-1).setBusiness_place(cell2name);
			    		}
			    		else if(cell1name.compareTo("车次")==0)
			    		{
			        		Cell cell2  = ws.getCell(1, i);
			            	String cell2name = cell2.getContents();
			            	applicationdata.getWork(index).getWork_business(currentbusiness-1).setBusiness_carnum(cell2name);
			    		}
			        }
			        if(flow==true)
			        {
						Role role= Role.getRole(cell1name);
						pflow=new ProcessFlow(role);
		        		//流程
			            Cell cell2  = ws.getCell(1, i);
			            String cell2name = cell2.getContents();
			            pflow.setFlow_description(cell2name);
			            //编号
			            Cell cell3  = ws.getCell(2, i);
			            String cell3name = cell3.getContents();
			            pflow.setFlow_num(cell3name);
			            //时间
			            Cell cell4  = ws.getCell(3, i);
			            String cell4name = cell4.getContents();
			            //string转换为时间
		           		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");  
					    Date date=null;
						try {
							if(cell4name!=null &&!cell4name.equals(""))
							{
								date = df.parse(sdate+cell4name);
							}							
							pflow.setFlow_time(date);
						} catch (ParseException e) {
							e.printStackTrace();
						}
			            //影像
			            Cell cell5  = ws.getCell(4, i);
			            String cell5name = cell5.getContents();
			            pflow.setFlow_video(cell5name);
			            //存在问题
			            Cell cell6  = ws.getCell(5, i);
			            String cell6name = cell6.getContents();
			            String[] probs=cell6name.split("；");
			            ArrayList<Problem> flowprobs=new ArrayList<Problem>();
			            for(int ii=0;ii<(probs.length);ii++)
			            {
			            	String[] s1=probs[ii].split("###");
			            	if(s1.length>1)
			            	{
			            		Problem pp=new Problem();
				            	pp.setProblem_description(s1[0]);
				            	if(s1[1].equals("true"))
				            	{
				            		pp.setProblem_choose(true);
				            	}
				            	else
				            	{
				            		pp.setProblem_choose(false);
				            	}
				            	flowprobs.add(pp);
			            	}		            	
			            }
			            pflow.setFlowproblem(flowprobs);

			            if(applicationdata.getWork(index).getWork_business(currentbusiness-1).getBusiness_flows().size()<currentflow+1)
		        		{//判断是否为自定义的流程
			        		applicationdata.getWork(index).getWork_business(currentbusiness-1).getBusiness_flows().add(pflow);
		        		}
			            else
			            {
			            	applicationdata.getWork(index).getWork_business(currentbusiness-1).getBusiness_flows().set(currentflow, pflow); 
			            }
			            
			            currentflow+=1;
			            applicationdata.getWork(index).getWork_business(currentbusiness-1).setReportitemdata(); 
			        }
			        if(problem==true)
			        {			        	
		        		Cell cell2  = ws.getCell(1, i);
			            String cell2name = cell2.getContents();
			            String[] probs=cell2name.split("；");
			            ArrayList<Problem> busprobs=new ArrayList<Problem>();
			            for(int ii=0;ii<(probs.length);ii++)
			            {
			            	String[] s1=probs[ii].split("###");
			            	if(s1.length>1)
			            	{
			            		Problem pp=new Problem();
				            	pp.setProblem_description(s1[0]);
				            	if(s1[1].equals("true"))
				            	{
				            		pp.setProblem_choose(true);
				            	}
				            	else
				            	{
				            		pp.setProblem_choose(false);
				            	}
				            	busprobs.add(pp);
			            	}		            	
			            }
			            applicationdata.getWork(index).getWork_business(currentbusiness-1).setBusiness_probs(busprobs);
			        }
			    }	            
			}	
		}        
		book.close();		
	}
	
	public ReportReadThread(File file,int index) {
		this.fullname=file.toString();
		this.index=index;
	}
	
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
}
