//8月19日检查无问题
package com.zr.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import com.zr.domain.Business;
import com.zr.domain.Problem;
import com.zr.domain.ProcessFlow;
import com.zr.ffive.ApplicationTrans;

import android.util.Log;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class RecordWriteThread extends Thread {
	private File recordfile;
	ApplicationTrans applicationdata;

	public void run() {
		recorddata();
	}

	private void recorddata() {
		if(recordfile.exists())
		{
			recordfile.delete();			
		}
		try {
			newexcel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WritableWorkbook wwb = null;  
		try 
		{   
			String fileName = recordfile.toString();  
			Log.v("recordwrite", "recordfile "+recordfile.toString());			//构建Workbook对象, 只读Workbook对象
			Workbook wb = Workbook.getWorkbook(new File(fileName)); 
			wwb=Workbook.createWorkbook(new File(fileName), wb); 
			//创建Excel工作表   
            WritableSheet ws = wwb.getSheet(0);

			ws.addCell(new Label(0,0,"记录人员"));
			ws.addCell(new Label(1,0,applicationdata.getPersonname()));
			ws.addCell(new Label(0,1,"评估角色"));
			ws.addCell(new Label(1,1,applicationdata.getRole()));

			int position=2;
			for(Business b:applicationdata.getBusinesslist())
			{
				Log.v("recordwrite", "business name"+b.getBusiness_name());
				ws.addCell(new Label(0, position, "项目"));
				ws.addCell(new Label(1, position, ""));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
				ws.addCell(new Label(0,position,"编号"));
				ws.addCell(new Label(1,position,b.getBusiness_num()));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
				ws.addCell(new Label(0,position,"名称"));
				ws.addCell(new Label(1,position,b.getBusiness_name()));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
				ws.addCell(new Label(0,position,"时间"));
				SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
 				String ddate=df.format(b.getBusiness_date());
				ws.addCell(new Label(1,position,ddate));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
				ws.addCell(new Label(0,position,"地点"));
				ws.addCell(new Label(1,position,b.getBusiness_place()));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
				ws.addCell(new Label(0,position,"车次"));
				ws.addCell(new Label(1,position,b.getBusiness_carnum()));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
				ws.addCell(new Label(0,position,"参加部门"));
				ws.addCell(new Label(1,position,b.getBusiness_involeveddepart()));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
				ws.addCell(new Label(0,position,"处置流程"));
				ws.addCell(new Label(1,position,""));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
				ws.addCell(new Label(0,position,"角色"));
				ws.addCell(new Label(1,position,"流程"));
				ws.addCell(new Label(2,position,"编号"));
				ws.addCell(new Label(3,position,"时间"));
				ws.addCell(new Label(4,position,"影像"));
				ws.addCell(new Label(5,position,"存在问题"));
				for(ProcessFlow flow:b.getBusiness_flows())
				{
					position=position+1;
					ws.addCell(new Label(0,position,flow.getFlow_role().getName()));
					ws.addCell(new Label(1,position,GetFlowDesc(flow.getFlow_description())));
					ws.addCell(new Label(2,position,flow.getFlow_num()));
					df = new SimpleDateFormat("HH:mm:ss");
					if(flow.getFlow_time()!=null)
					{
						ddate=df.format(flow.getFlow_time());
						ws.addCell(new Label(3,position,ddate));
					}
					else
					{
						ws.addCell(new Label(3,position,""));
					}
					ws.addCell(new Label(4,position,flow.getFlow_video()));
					String s="";
					for(Problem p:flow.getFlowproblem())
					{
						if(p.isProblem_choose())
						{
							s+=p.getProblem_description()+"；";
						}
					}
					ws.addCell(new Label(5,position,s));
				}
				position=position+1;
				ws.addCell(new Label(0,position,"存在问题"));
				ws.addCell(new Label(1,position,""));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
				ws.addCell(new Label(0,position,applicationdata.getRole()));
				String s="";
				for(Problem p:b.getBusiness_probs())
				{
					if(p.isProblem_choose())
					{
						s+=p.getProblem_description()+"；";
					}
				}
				ws.addCell(new Label(1,position,s));
				ws.addCell(new Label(2, position, ""));
				ws.addCell(new Label(3, position, ""));
				ws.addCell(new Label(4, position, ""));
				ws.addCell(new Label(5, position, ""));
				position=position+1;
			}				
			//关闭Excel工作薄对象 
			wwb.write(); 
			wwb.close(); 
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace(); 
		}
	}
	
	private String GetFlowDesc(String s) {
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
				String saa=s.substring(start+1, end-1);
				String[] aa=saa.split("#");
				if(aa.length>1)
				{
					s_item+=aa[1];
				}
				position=end+2;
			}
			else
			{
				s_item+=s.substring(position, s.length());
				position=s.length();
			}
		}
		return s_item;
	}

	public RecordWriteThread()
	{
		applicationdata=ApplicationTrans.getInstance();
		applicationdata.initrecordfiledir();
		String filepath=applicationdata.getFileDir()
				+"/"+applicationdata.string_recordfile
				+"/"+applicationdata.getPersonname()
				+"/"+applicationdata.getRole();
		String name=applicationdata.getWork(applicationdata.getWorkposition()).getWork_name();
		name=name.substring(0,name.indexOf("."));
		recordfile=new File(filepath,name+".xls");
	}
	
	private void newexcel() {
		try{
			WritableWorkbook wwb = Workbook.createWorkbook(new File(recordfile.toString())); 
			//创建Excel工作表 
			WritableSheet ws = wwb.createSheet("Sheet1", 0);
            //将值添加到单元格中  
            ws.addCell(new Label(0,0,"记录人员")); 
            ws.addCell(new Label(1,0,applicationdata.getPersonname()));
            ws.addCell(new Label(0,1,"评估角色")); 
            ws.addCell(new Label(1,1,applicationdata.getRole()));
            wwb.write();
			wwb.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
