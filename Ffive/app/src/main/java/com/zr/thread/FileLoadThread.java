//8月19日检查无问题
package com.zr.thread;

import com.zr.domain.Role;
import com.zr.domain.StandardFile;
import com.zr.domain.Project;

import java.io.File;

import com.zr.domain.ProcessFlow;
import com.zr.ffive.ApplicationTrans;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
/*

 * 读取本地存储的xls文档，其中不仅包含了标准库文件，还包含了报告文件中需要的信息
 * stafile：存储每一个xls文档的信息
 * filename：存储当前xls文档的绝对路径
 */
public class FileLoadThread extends Thread{

	private StandardFile stafile;
	private String filename;//绝对路径
	ApplicationTrans applicationdata;
	@Override
	public void run() {
		applicationdata=ApplicationTrans.getInstance();
		xlsUtiljxl();
	}
	
	private void xlsUtiljxl(){
		Workbook book = null;
        try {
        	// 获得第一个工作表对象
        	book = Workbook.getWorkbook(new File(getFilename()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (book != null && book.getNumberOfSheets() > 0) 
        {
			// 获得第一个工作表对象
			//Workbook book = Workbook.getWorkbook(new File(getFilename()));
			Sheet ws = book.getSheet(0);
			// 得到第一列第一行的单元格
			int rownum = ws.getRows();// 得到行数
			//以下三个变量标记xls文件中的当前位置处于哪个位置
			boolean project = false;
			boolean flow = false;
			boolean problem = false;
			Project currentproject = null;
			ProcessFlow currentprocessflow = null;
			String currentProjectProblem = null;
			String editor = null;
			for (int i = 0; i < rownum; i++)// 循环进行读写
			{
			    Cell cell1  = ws.getCell(0, i);
			    String cell1name = cell1.getContents();
			    //判断当前读取位置，是版本？还是
			    if(cell1name.compareTo("版本")==0)
			    {
			    	Cell cell2  = ws.getCell(1, i);
			    	String cell2name = cell2.getContents();
			    	stafile.setFile_version(cell2name);
			    	project=false;
			    	flow=false;
			    	problem=false;
			    }
			    else if(cell1name.compareTo("修订人")==0)
			    {
			    	Cell cell2  = ws.getCell(1, i);
			    	String cell2name = cell2.getContents();
			    	editor=cell2name;
			    	stafile.setFile_editor(cell2name);
			    	project=false;
			    	flow=false;
			    	problem=false;
			    }
			    else if(cell1name.compareTo("项目")==0)
			    {
			    	project=true;
			    	flow=false;
			    	problem=false;
			    	currentproject=new Project(stafile.getFile_version(),editor);
			    	stafile.addFile_project(currentproject);
			    }
			    else if(cell1name.compareTo("处置流程")==0)
			    {
			    	i++;
			    	project=false;
			    	flow=true;
			    	problem=false;
			    }
			    else if(cell1name.compareTo("存在问题")==0)
			    {
			    	i++;
			    	project=false;
			    	flow=false;
			    	problem=true;
			    }
			    //处于项目，流程，或者问题当中
			    else
			    {
			    	if(project==true)
			        {
			    		if(cell1name.compareTo("编号")==0)
			    		{
			        		Cell cell2  = ws.getCell(1, i);
			            	String cell2name = cell2.getContents();
			            	currentproject.setProject_num(cell2name);
			    		}
			    		else if(cell1name.compareTo("名称")==0)
			    		{
			        		Cell cell2  = ws.getCell(1, i);
			            	String cell2name = cell2.getContents();
			            	currentproject.setProject_name(cell2name);
			    		}
			    		else if(cell1name.compareTo("参加部门")==0)
			    		{
			        		Cell cell2  = ws.getCell(1, i);
			            	String cell2name = cell2.getContents();
			            	currentproject.setProject_involved_depart(cell2name);
			    		}			
			        }
			        if(flow==true)
			        {
			        	/*if(cell1name.compareTo(applicationdata.getRole())==0)
			        	{
			        		//角色
				            currentprocessflow=new ProcessFlow();
				            //流程
				            Cell cell2  = ws.getCell(1, i);
				            String cell2name = cell2.getContents();
				            currentprocessflow.setFlow_description(cell2name);
				            //编号
				            Cell cell3  = ws.getCell(2, i);
				            String cell3name = cell3.getContents();
				            currentprocessflow.setFlow_num(cell3name);
				            //存在问题
				            Cell cell4  = ws.getCell(3, i);
				            String cell4name = cell4.getContents();
				            currentprocessflow.setFlowproblem(cell4name);
				            //System.out.println("I am read problem in projects in flow ");
				            //添加到project中
				            currentproject.addProject_processflow(currentprocessflow);
			        	}*/

						//角色
						Role role= Role.getRole(cell1name);
						currentprocessflow=new ProcessFlow(role);
						//流程
						Cell cell2  = ws.getCell(1, i);
						String cell2name = cell2.getContents();
						currentprocessflow.setFlow_description(cell2name);
						//编号
						Cell cell3  = ws.getCell(2, i);
						String cell3name = cell3.getContents();
						currentprocessflow.setFlow_num(cell3name);
						//存在问题
						Cell cell4  = ws.getCell(3, i);
						String cell4name = cell4.getContents();
						currentprocessflow.setFlowproblem(cell4name);
						//System.out.println("I am read problem in projects in flow ");
						//添加到project中
						currentproject.addProject_processflow(currentprocessflow);

			        }
			        if(problem==true)
			        {
			        	if(cell1name.compareTo(applicationdata.getRole())==0)
			        	{
			        		Cell cell2  = ws.getCell(1, i);
				            String cell2name = cell2.getContents();
				            //System.out.println("file load old problem"+currentProjectProblem);
				            currentProjectProblem=cell2name;
				            //System.out.println("file load new problem"+currentProjectProblem);
				            currentproject.setProject_problem(currentProjectProblem);
			        	}
			        }
			    }	            
			}	
		}        
		book.close();
	}
	/*
	 * 线程的构造函数
	 */
	public FileLoadThread(String absolutefilename)
	{
		this.filename=absolutefilename;
		File tmpfile=new File(absolutefilename.trim());
		//System.out.println("FileLoadThread STAFILE TEMPFILE"+tmpfile.getName());
		this.stafile=new StandardFile(tmpfile.getName());
	}
	/*
	 * getters and setters
	 */
	public StandardFile getStafile() {
		return stafile;
	}

	public void setStafile(StandardFile stafile) {
		this.stafile = stafile;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}	
}
