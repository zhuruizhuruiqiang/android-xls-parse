package com.zr.apprail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.zr.domain.Business;
import com.zr.domain.Project;
import com.zr.domain.Role;
import com.zr.domain.StandardFile;
import com.zr.domain.Work;
import com.zr.thread.FileLoadThread;
import com.zr.thread.RecordWriteThread;
import com.zr.thread.ReportReadThread;
import com.zr.thread.ReportWriteThread;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
/*
 * instance              app实例
 * projectlist           标准库文件中所有工程列表
 * worklist              计划列表,每一个计划列表中包含多个business
 * business              项目计划,每一个项目计划由多个project构成
 * 
 * standardFileDir       标准库文件存放地址
 * fileDir               APP文件目录所在地址
 * confiledir            针对当前标准库文件和评估人以及评估角色对应的当前文件目录
 *
 * standardFileVersion   当前标准库文件版本
 * personname            评估人
 * role                  评估角色
 */
public class ApplicationTrans extends Application implements Thread.UncaughtExceptionHandler{

	/*
	以下为文件系统结构
	 */
	public final static String string_appfilename="PingGu";//app文件名
	//PingGu目录下面文件
	public final static String string_infofile="info.txt";//存储app根目录,标准库文件目录,评估人,评估角色信息
	public final static String string_stafile="StandardFile";//存放标准库文件
	public final static String string_recordfile="Record";//报告文件，记录项目的生成报告
	public final static String string_configfile="Config";//配置文件，存放工作计划等信息
	//Config目录下面为以标准库文件名列出的文件夹,configfilepath就是这个目录
	// configfilepath这个文件夹下的文件为
	public final static String string_reportfile="ReportFile";//存放这个标准库文件对应的记录文件
	public final static String string_videofile="Video";//存放这个标准库文件对应的video信息
	public final static String string_worklist="Worklist";//存放这个标准库文件对应的工作计划


    /*
    以下为app程序中用到的变量
     */
	private	static ApplicationTrans instance;
	public  List<Project> projectlist;
	public  List<Work> worklist;
	public  List<Business> businesslist;
	
	public  String standardFileDir;
	public  String fileDir;
	public  String confiledir;

	public  String standardFileVersion;
	public  String personname;
	public  String role;

	public boolean workchoose;//是否当前有选择的work列表
	public int workposition;//当前work

	public boolean buschoose;//是否当前有选择的bus
	public int busposition;//当前bus
	
	public boolean flowchoose;//是否当前有选择的flow
	public int flowposition;//当前flow
	
	public	static ApplicationTrans getInstance() {
	    return	instance;
	}

	public void onCreate() {
		super.onCreate();
		instance=this;
		projectlist=new ArrayList<Project>();
		worklist=new ArrayList<Work>();
		businesslist=new ArrayList<Business>();
		
		standardFileDir="";
		fileDir="";
		confiledir="";

		standardFileVersion="";
		personname="";
		role="";
		initfiledirs();//初始化文档以及基础文件夹
		readversionfromxls();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}	
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
	@Override

	public void uncaughtException(Thread thread, Throwable ex)
	{
		initfiledirs();//初始化文档以及基础文件夹
		if(getPersonname().equals("")||getRole().equals(""))
		{//没有记录人员和评估人员，则进入到首页重新开始
			readversionfromxls();
			Intent intent = new Intent(this, MainActivity.class);  
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  
            startActivity(intent);
		}
		else {
			loadstaxls();

			if (!isWorkchoose() || getWorkposition() < 0) {//没有选择计划列表或者计划列表出现错误，则进入到businessListactivity开始重新选择计划列表

				Intent intent = new Intent(this, BusinessList.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle = new Bundle();
				bundle.putString("nextpage", "LoadWorkList");
				intent.putExtras(bundle);
				startActivity(intent);
			} else if (!isBuschoose() || getBusposition() < 0) {//没有选择项目或者项目出现错误，则进入到businessListactivity开始重新选择项目
				Intent intent = new Intent(this, BusinessList.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle = new Bundle();
				bundle.putString("nextpage", "SelectProject");
				intent.putExtras(bundle);
				startActivity(intent);
			} else
			//有计划列表有项目则重新开始，到选择的项目也开始做
			{
				Intent intent = new Intent(this, BusinessList.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle = new Bundle();
				bundle.putString("nextpage", "BusinessActivity");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
		// 16/8/16 这里还需要修改页面跳转的问题
		// 16/8/19 页面跳转的问题修改完成
	}
	
	public void initfiledirs()
	{
		File sdcardDir =Environment.getExternalStorageDirectory();
        String pathdir=sdcardDir.getPath()+"/"+string_appfilename;
        
        //文件根目录
        File file = new File(pathdir);
        if (!file.exists()) 
        {
	         file.mkdirs();				         
        }        
        //配置文件
        File infofile=new File(pathdir,string_infofile);
        if(infofile.exists()&&infofile.isFile())
		{
			readparam(infofile);
		}
		else
		{
			try 
			{
				infofile.createNewFile();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
        setFileDir(pathdir);
		//存放标准库文件
		String path1=pathdir+"/"+string_stafile;
		File file1 = new File(path1);
		if (!file1.exists()&& !file1.isDirectory())
		{
			 file1.mkdirs();				         
		}
		setStandardFileDir(path1);
		//存放记录的数据
		path1=pathdir+"/"+string_recordfile;
		file1 = new File(path1);
		if (!file1.exists()) 
		{
			 file1.mkdirs();
		}
		//存放配置文件信息
		path1=pathdir+"/"+string_configfile;
		file1 = new File(path1);
		if (!file1.exists()) 
		{
			 file1.mkdirs();
		}
	}
	
	public void initrecordfiledir() {
		String filepath=getFileDir()+"/"+string_recordfile;
		File file = new File(filepath);
		if(!file.exists())
		{
			file.mkdirs();
		}
		String path=filepath+"/"+getPersonname();
		file = new File(path);
		if(!file.exists())
		{
			file.mkdirs();
		}
		String rolepath=path+"/"+getRole();
		file = new File(rolepath);
		if(!file.exists())
		{
			file.mkdirs();
		}
	}
	
	public void initvideofiledir() {
		String filepath=getConfiledir()+"/"+string_videofile;
		File file = new File(filepath);
		if(!file.exists())
		{
			file.mkdirs();
		}
		String path=filepath+"/"+getPersonname();
		file = new File(path);
		if(!file.exists())
		{
			file.mkdirs();
		}
		String rolepath=path+"/"+getRole();
		file = new File(rolepath);
		if(!file.exists())
		{
			file.mkdirs();
		}
	}
	
	public void initreportfiledir() {
		String filepath=getConfiledir()+"/"+string_reportfile;
		File file = new File(filepath);
		if(!file.exists())
		{
			file.mkdirs();
		}
		String path=filepath+"/"+getPersonname();
		file = new File(path);
		if(!file.exists())
		{
			file.mkdirs();
		}
		String rolepath=path+"/"+getRole();
		file = new File(rolepath);
		if(!file.exists())
		{
			file.mkdirs();
		}
	}
	
	private void readversionfromxls() {
		File file=new File(getStandardFileDir());
		String[] sflist=file.list();
		for(int i=0;i<sflist.length;i++)
		{
			String filename=new String(sflist[i]);
			String fullpath=file.toString()+"/"+filename;
				
			Workbook book = null;
	        try {
	        	// 获得第一个工作表对象
	        	book = Workbook.getWorkbook(new File(fullpath));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        if (book != null && book.getNumberOfSheets() > 0) 
	        {
				// 获得第一个工作表对象
				//Workbook book = Workbook.getWorkbook(new File(getFilename()));
				Sheet ws = book.getSheet(0);
				Cell cell1  = ws.getCell(1, 0);
			    String version = cell1.getContents();//读取version信息
			    if(getStandardFileVersion().equals(""))
			    {
			    	float sourcenew = Float.valueOf(version);
			    	setStandardFileVersion(String.valueOf(sourcenew));
			    }
			    else
			    {
			    	float sourcenew = Float.valueOf(version);
					float sourceold = Float.valueOf(getStandardFileVersion());
					if(sourcenew>sourceold)
					{
						setStandardFileVersion(String.valueOf(sourcenew));
					}
			    }			   
	        }
		}		
	}
	/*
	 * 读取stafile文件以及project
	 * 根据role和personname选择对应的project中的内容进行读取
	 */
	public void loadstaxls() {
		Thread t=new Thread()
		{
			@Override
			public void run() {
				/*
				 * 读取所有的记录文件
				 */
				File file=new File(getStandardFileDir());
				//List<StandardFile> stafilelist=new ArrayList<StandardFile>();
				String[] sflist=file.list();
				//读取第一个xls文件，一般默认读取第一个
				String filename=new String(sflist[0]);
				FileLoadThread t=new FileLoadThread(file.toString()+"/"+filename);
				t.run();


				StandardFile standardfile=new StandardFile(filename);
				standardfile=t.getStafile();
				/*
				 * 获得所有的project
				 */
				projectlist=new ArrayList<Project>();
				for(Project p:standardfile.getFile_projects())
				{
					projectlist.add(p);
				}
				String version=standardfile.getFile_version();
				float sourcenew = Float.valueOf(version);
				float sourceold = Float.valueOf(getStandardFileVersion());
				if(sourcenew>sourceold)
				{
					setStandardFileVersion(String.valueOf(sourcenew));
				}
				loadworklist();
			}		
		};
		t.start();
	}

	/*
	 * 读取以前保存好的工作计划文件
	 * 工作计划文件存储在/config/planlist/xxx.txt中
	 * 工作计划文件命名格式为：司机_朱瑞_名称.txt
	 * 根据工作计划中存储的business名字重新将business添加到对应的工作计划中
	 * 根据business名字将project重新添加到business中，还原工作计划中的business
	 */
	public void loadworklist()
	{
		//读取xls文件对应的各种文件，并且处理过期的文件
		File file=new File(getStandardFileDir());
		String[] sflist=file.list();		
		String filename=new String(sflist[0]);
		filename=filename.substring(0, filename.indexOf("."));
		
		//首先删除过期文件
		File confile=new File(getFileDir()+"/"+string_configfile);
		String[] conflist=confile.list();
		for(int i=0;i<conflist.length;i++)
		{
			if(!conflist[i].equals(filename))
			{
				File fileconfile=new File(
						getFileDir()+"/"+string_configfile+"/"+filename);
				file.delete();
			}
		}
		//接着初始化标准库文件对应的目录,判断是否存在
		String filecondir=
				getFileDir()+"/"+string_configfile+"/"+filename;
		setConfiledir(filecondir);//设置configuration目录
		File filecon = new File(filecondir);
        if (!file.exists()) 
        {
	         file.mkdirs();				         
        }
        
        File worklistfile=new File(filecondir,string_worklist);
        if(!worklistfile.exists())
		{
        	worklistfile.mkdirs();
		}
        
        File reportfile=new File(filecondir,string_reportfile);
        if(!reportfile.exists())
		{
        	reportfile.mkdirs();
		}
        
        File videofile=new File(filecondir,string_videofile);
        if(!videofile.exists())
		{
        	videofile.mkdirs();
		}
        //读取worklist
		worklist=new ArrayList<Work>();
		for(String ff:worklistfile.list())
		{
			String[] s=ff.split("_");
			if(s[0].equals(getRole())&&s[1].equals(getPersonname()))
			{
				Work work=new Work(ff);
				File filew=new File(worklistfile,ff);
				try
				{					
					 FileInputStream fis=new FileInputStream(filew);
					 InputStreamReader read = new InputStreamReader(fis,"UTF-8");
					 BufferedReader bufferedReader = new BufferedReader(read);
					 String lineTxt=null;
				     while((lineTxt = bufferedReader.readLine()) != null)
				     {
				    	 Business b=new Business();
				    	 String[] project_numname=lineTxt.split("&&");		    	 
				    	 for(String numandname:project_numname)
			    		 {
			    			 String[] numname=numandname.split("&");
			    			 for(int i=0;i<getProjectlist().size();i++)
					    	 {
					    		 String project_num=getProject(i).getProject_num();
					    		 String project_name=getProject(i).getProject_name();
					    		 if(numname[0].equals(project_num)&&numname[1].equals(project_name))
				    			 {
					    			 Project p=new Project(getProject(i));					    			 
					    			//Log.d("appdata", p.toString());
				    				 b.addBusiness_project(p);
				    			 }
				    			 else
				    			 {
				    				 Log.d("app", "find no project"+project_name);
				    			 }
					    	 }			    			 
			    		 }			    	 
				    	 work.addWork_businesslist(b);
				     }
				     read.close();				     
				}				
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				worklist.add(work);
			} 
		}
		loadreportfile(filecondir);
	}
	/*
	 * 读取记录文件
	 * 记录文件存储在/recordfile/朱瑞/司机/xxx.xls中
	 * 记录文件命名格式为：司机_朱瑞_名称.txt
	 * 读取工作计划中的business中记录的详细信息，还原business上次记录的所有状态
	 */

	public void loadreportfile(String pathcondir)
	{
		String path=pathcondir
				+"/"+string_reportfile
				+"/"+getPersonname()
				+"/"+getRole();
		File fpath=new File(path);
		if(!fpath.exists())
		{
			fpath.mkdirs();
		}
		for(String ff:fpath.list())
		{
			for(int i=0;i<getWorklist().size();i++)
			{
				String wname=getWork(i).getWork_name();
				wname=wname.substring(0,ff.indexOf("."))+".xls";
				if(wname.equals(ff))
				{
					ReportReadThread t=new ReportReadThread(new File(path,ff),i);
					t.run();
				}			
			}	
		}	
	}

	/*
	 * 读取配置文件参数信息
	 */
	public void readparam(File infofile) {
		try 
		{            
			FileInputStream fis=new FileInputStream(infofile);
			InputStreamReader read = new InputStreamReader(fis,"UTF-8");
			 BufferedReader bufferedReader = new BufferedReader(read);
			 String lineTxt = null;
		     while((lineTxt = bufferedReader.readLine()) != null)
		     {
		         if(lineTxt.startsWith("standardFileDir"))
		         {
		        	 String[] s=lineTxt.split("##");
		        	 standardFileDir=s[1];
		         }
		         if(lineTxt.startsWith("fileDir"))
		         {
		        	 String[] s=lineTxt.split("##");
		        	 fileDir=s[1];
		         }
		         if(lineTxt.startsWith("version"))
		         {
		        	 String[] s=lineTxt.split("##");
		        	 standardFileVersion=s[1];
		         }
		         if(lineTxt.startsWith("personname"))
		         {
		        	 String[] s=lineTxt.split("##");
		        	 personname=s[1];
		         }
				 if(lineTxt.startsWith("role"))
				 {
					 String[] s=lineTxt.split("##");
					 role=s[1];
				 }
		     }
		     read.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addinfo(String param,String info)
	//info包含了param的完整信息
	{
		File infofile=new File(getFileDir(),"info.txt");//获取当前内部存储路径 data/com.zr.first/files
		String temp = "";
        try 
        {
            FileInputStream fis = new FileInputStream(infofile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();            
	        //定位到特定位置处
	        for (int j = 1; (temp = br.readLine()) != null
	                    && !temp.startsWith(param); j++) 
	        {
	            buf = buf.append(temp+"\n");
	        }
	        // 将内容插入
            buf = buf.append(info+"\n");
            // 保存该行后面的内容
            while ((temp = br.readLine()) != null) 
            {
                buf = buf.append(temp+"\n");
            }
            br.close();
            
            FileOutputStream fos = new FileOutputStream(infofile);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            pw.close();
            readparam(infofile);
        }
        catch (Exception e)
        {   
        	e.printStackTrace();   
        }      
	}

	/*
	 * worklist文件写入
	 */
	public void writeworklist(String path,String savename){
		File file=new File(path,savename);
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//获取文件列表
		StringBuilder sb=new StringBuilder();
		for(Business b:getBusinesslist())
		{
			String s="";
			for(Project p:b.getBusiness_projects())
			{
				s+=p.getProject_num()+"&"+p.getProject_name()+"&&";
			}
			sb.append(s.substring(0, s.length()-2)+"\r\n");
		}			
		//写入文件
		try {
			OutputStreamWriter write=new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
	        BufferedWriter writer=new BufferedWriter(write);
	        writer.write(sb.toString());
	        writer.close();
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
		ReportWriteThread t=new ReportWriteThread();
		t.run();
		RecordWriteThread t1=new RecordWriteThread();
		t1.run();
	}

	/*
	 * projectlist set and get and add
	 */
	public ArrayList<Project> getProjectlist() {
		return (ArrayList<Project>) this.projectlist;
	}
	
	public void setProjectlist(ArrayList<Project> projectlist) {
		this.projectlist = projectlist;
	}

	public void addProject(Project project) {
		this.projectlist.add(project);
	}
	
	public Project getProject(int i) {
		return this.projectlist.get(i);
	}
	/*
	 * worklist set and get and add
	 */
	public List<Work> getWorklist() {
		return worklist;
	}

	public void setWorklist(List<Work> worklist) {
		this.worklist = worklist;
	}
	
	public void addWorklist(Work work) {
		this.worklist.add(work);
	}
	
	public Work getWork(int index) {
		return this.worklist.get(index);
	}
	/*
	 * Businesslist
	 */
	public List<Business> getBusinesslist() {
		return this.businesslist;
	}
	
	public Business getBusiness(int index) {
		return this.businesslist.get(index);
	}
	
	public void setBusinesslist(List<Business> businesslist)
	{
		this.businesslist=businesslist;
	}

	public void setBusinesslist(int workposition)
	{
		setBusinesslist(getWork(workposition).getWork_businesslist());
	}
	/*
	 * getters and setters
	 */
	public String getStandardFileDir() {
		return this.standardFileDir;
	}

	public void setStandardFileDir(String standardFileDir) {
		this.standardFileDir = standardFileDir;
		addinfo("standardFileDir","standardFileDir##"+standardFileDir);
	}

	public String getFileDir() {
		return this.fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
		addinfo("fileDir","fileDir##"+fileDir);
	}

	public String getStandardFileVersion() {
		return this.standardFileVersion;
	}

	public void setStandardFileVersion(String standardFileVersion) {
		this.standardFileVersion = standardFileVersion;
		addinfo("version","version##"+standardFileVersion);
	}
	
	public String getConfiledir() {
		return this.confiledir;
	}

	public void setConfiledir(String confiledir) {
		this.confiledir = confiledir;
	}
	
	public String getPersonname() {
		return this.personname;
	}

	public void setPersonname(String personname) {
		this.personname = personname;
		addinfo("personname","personname##"+personname);
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
		addinfo("role","role##"+role);
	}
	
	public boolean isWorkchoose() {
		return this.workchoose;
	}

	public void setWorkchoose(boolean workchoose) {
		this.workchoose = workchoose;
	}

	public int getWorkposition() {
		return this.workposition;
	}

	public void setWorkposition(int workposition) {
		this.workposition = workposition;
		this.setBusinesslist(workposition);
	}

	public boolean isBuschoose() {
		return this.buschoose;
	}

	public void setBuschoose(boolean buschoose) {
		this.buschoose = buschoose;
	}

	public int getBusposition() {
		return this.busposition;
	}

	public void setBusposition(int busposition) {
		this.busposition = busposition;
	}
	
	public boolean isFlowchoose() {
		return flowchoose;
	}

	public void setFlowchoose(boolean flowchoose) {
		this.flowchoose = flowchoose;
	}

	public int getFlowposition() {
		return flowposition;
	}

	public void setFlowposition(int flowposition) {
		this.flowposition = flowposition;
	}

	public String generateflownum(int busposition) {
		String busnum=getBusiness(busposition).getBusiness_num();
		int roleindex=Role.getRole(getRole()).getOrdinal();
		int flownum=0;
		for(Project p:getBusiness(busposition).getBusiness_projects())
		{
			flownum+=p.getProject_processflows().size();
		}
		int addflownums=getBusiness(busposition).getBusiness_flows().size()-flownum;
		String s= ""+busnum+roleindex+roleindex+"+"+addflownums;
		return s;
	}

}
