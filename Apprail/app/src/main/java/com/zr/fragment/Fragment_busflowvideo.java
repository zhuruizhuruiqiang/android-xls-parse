package com.zr.fragment;
///8月22check OK
import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.BusinessList;
import com.zr.apprail.Num;
import com.zr.apprail.R;
import com.zr.domain.ReportItem;
import com.zr.thread.RecordWriteThread;
import com.zr.thread.ReportWriteThread;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Fragment_busflowvideo extends Fragment implements OnClickListener{

	private TextView businessflowvideo;
	private TextView businessflowvideotime;
	private TextView businessflowvideodesp;
	private Button busiflowvideomassage;
	private Button busiflowvideoproblem;
	private ImageView img_ret;
	private Button busiflowvideo_chakan;
	private Button busiflowvideo_paizhao;
	private Button busiflowvideo_shexiang;
	private Button busiflowvideo_start;
	private ImageView img_pic;
	private VideoView video_pic;
	ApplicationTrans applicationdata;
	private int busposition;
	private int flowposition;
	private ReportItem flow;
	private static int DEFAULT_MAX_TEXT_SIZE = 40;
	private static int CASE_VIDEO = 1;
	private static int CASE_CAMERA = 2;
	private String[] pictures=null;
	private int thisposition=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
		ReportWriteThread t=new ReportWriteThread();
		t.run();
		RecordWriteThread t1=new RecordWriteThread();
		t1.run();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_busflow_video, null);
		applicationdata=(ApplicationTrans) getActivity().getApplication();		
		
		busposition=applicationdata.getBusposition();
		flowposition=applicationdata.getFlowposition();
		flow=new ReportItem();
		flow=applicationdata.getBusinesslist().get(busposition).getBusiness_flowData().get(flowposition);
		
		//mData=new ReportItem();
		//mData=applicationdata.getBusinesslist().get(busposition).getBusiness_flowData().get(flowposition);
		
		businessflowvideo=(TextView) view.findViewById(R.id.busflow_video_textView_prolist);
		businessflowvideotime=(TextView) view.findViewById(R.id.busflow_video_textView_flowtime);
		businessflowvideodesp=(TextView) view.findViewById(R.id.busflow_video_textView_flowdesp);
		busiflowvideomassage=(Button) view.findViewById(R.id.busflow_video_button_param);
		busiflowvideoproblem=(Button) view.findViewById(R.id.busflow_video_button_prob);
		img_ret= (ImageView) view.findViewById(R.id.busflow_video_imageView_return);
		busiflowvideo_chakan=(Button) view.findViewById(R.id.busflow_video_button_chakan);
		busiflowvideo_paizhao=(Button) view.findViewById(R.id.busflow_video_button_paizhao);
		busiflowvideo_shexiang=(Button) view.findViewById(R.id.busflow_video_button_shexiang);
		busiflowvideo_start=(Button) view.findViewById(R.id.busflow_video_button_start);
		img_pic=(ImageView) view.findViewById(R.id.busflow_video_imageView);
		video_pic=(VideoView) view.findViewById(R.id.busflow_video_videoView);
			
		img_pic.setVisibility(View.GONE);
		video_pic.setVisibility(View.GONE);
		
		businessflowvideo.setText("流程"+ Num.getString(flowposition+1));
		businessflowvideotime.setText(flow.getTime());
		
		String s=flow.getMessage();
		if(s.indexOf("(")>0)
		{
			s=s.substring(0,s.indexOf("("));
		}
		if(s.length()>DEFAULT_MAX_TEXT_SIZE)
		{
			businessflowvideodesp.setLines(2);
			String temop=s;
			s=temop.substring(0, DEFAULT_MAX_TEXT_SIZE)+"\r\n"
					+temop.substring(DEFAULT_MAX_TEXT_SIZE+1,temop.length());
		}
		
		businessflowvideodesp.setText(s);
		
		busiflowvideomassage.setOnClickListener(this);
		busiflowvideoproblem.setOnClickListener(this);
		img_ret.setOnClickListener(this);
		busiflowvideo_chakan.setOnClickListener(this);
		busiflowvideo_paizhao.setOnClickListener(this);
		busiflowvideo_shexiang.setOnClickListener(this);
		
		
		String workname=applicationdata.getWork(applicationdata.getWorkposition()).getWork_name();
		workname=workname.substring(workname.indexOf("_")+1,workname.length());//去掉前面的司机_朱瑞_
		workname=workname.substring(workname.indexOf("_")+1,workname.length());
		workname=workname.substring(0,workname.indexOf("."));//去掉后面的文件类型
	
		
		String FILE_PATH=applicationdata.getConfiledir()
				+"/"+applicationdata.string_videofile
				+"/"+applicationdata.getPersonname()
				+"/"+applicationdata.getRole()
				+"/"+workname;
		applicationdata.initvideofiledir();
		File file = new File(FILE_PATH);
		if(!file.exists())
		{
			file.mkdirs();
		}
		
		((BusinessList) getActivity()).setFILE_PATH(FILE_PATH);
		
		String ss=applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).getFlow_video();
		
		if(ss.trim()!=null)
		{
			pictures=ss.trim().split(";");
		}
		
		for(int i=0;i<pictures.length;i++)
		{
			System.out.println("picture "+i+" "+pictures[i]);
		}		
        return view;
    }

	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.busflow_video_button_param:
				((BusinessList) getActivity()).gotobusinessflowpara();
				break;
			case R.id.busflow_video_button_prob:
				((BusinessList) getActivity()).gotobusinessflowprob();
				break;
			case R.id.busflow_video_imageView_return:
				((BusinessList) getActivity()).gotobusinessflow();
				 break;
			case R.id.busflow_video_button_chakan:
				gotochakanvideo();
				break;
			case R.id.busflow_video_button_paizhao:
				gotopaizhao();
				break;
			case R.id.busflow_video_button_shexiang:
				gotoshexiang();
				break;
			case R.id.busflow_video_button_start:
				gotostart();
				break;
			default:
				break;
		}		
	}	
	
	private void gotostart() {
		img_pic.setVisibility(View.GONE);
    	video_pic.setVisibility(View.GONE);
    	String ss=applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).getFlow_video();

		if(!ss.trim().equals(""))
		{
			pictures=ss.trim().split(";");
			thisposition+=1;
			Uri uri=null;
			if(thisposition<pictures.length)
			{
				System.out.println("picture "+" "+pictures[thisposition]);
				uri = Uri.fromFile(new File(pictures[thisposition]));
			}
			else
			{
				thisposition=0;
				uri = Uri.fromFile(new File(pictures[thisposition]));
			}
			
			if(pictures[thisposition].endsWith("jpg"))
			{
				img_pic.setVisibility(View.VISIBLE);
            	video_pic.setVisibility(View.GONE);
            	img_pic.setImageURI(uri);
			}
			else
			{
				img_pic.setVisibility(View.GONE);
            	video_pic.setVisibility(View.VISIBLE);
            	video_pic.setMediaController(new MediaController(getActivity()));  
            	video_pic.setVideoURI(uri);  
            	video_pic.start();  
            	video_pic.requestFocus();
			}
			busiflowvideo_start.setOnClickListener(this);
		}
		else
		{
			((BusinessList) getActivity()).maketoast("没有照片", 5000);
		}
	}

	private void gotochakanvideo() {
		img_pic.setVisibility(View.GONE);
    	video_pic.setVisibility(View.GONE);
		thisposition=0;
		String ss=applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).getFlow_video();

		if(!ss.trim().equals(""))
		{
			pictures=ss.trim().split(";");
			System.out.println("picture "+" "+pictures[thisposition]);
			Uri uri = Uri.fromFile(new File(pictures[thisposition]));
			if(pictures[thisposition].endsWith("jpg"))
			{
				img_pic.setVisibility(View.VISIBLE);
            	video_pic.setVisibility(View.GONE);
            	img_pic.setImageURI(uri);
			}
			else
			{
				File file=new File(pictures[thisposition]); 
				img_pic.setVisibility(View.GONE);
            	video_pic.setVisibility(View.VISIBLE);
            	video_pic.setMediaController(new MediaController(getActivity()));    
            	video_pic.setVideoPath(file.getAbsolutePath());
            	video_pic.start();  
            	video_pic.requestFocus();
			}
			busiflowvideo_start.setOnClickListener(this);
		}
		else
		{
			((BusinessList) getActivity()).maketoast("没有照片", 5000);
		}
	}
	
	private void gotoshexiang() {
		//applicationdata.initvideofiledir();
		Intent intent = new Intent();
		intent.setAction("android.media.action.VIDEO_CAPTURE");
		intent.addCategory("android.intent.category.DEFAULT");

		String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".mp4";     
		String fileName = ((BusinessList) getActivity()).getFILE_PATH()+"/"+name; 
		((BusinessList) getActivity()).setFileName(fileName);
		// 把文件地址转换成Uri格式
		Uri uri = Uri.fromFile(new File(fileName));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, CASE_VIDEO);
	}

	private void gotopaizhao() {
		//applicationdata.initvideofiledir();
		Intent intent = new Intent();
		// 指定开启系统相机的Action
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		
		String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
		String fileName = ((BusinessList) getActivity()).getFILE_PATH()+"/"+name; 
		((BusinessList) getActivity()).setFileName(fileName);
		// 把文件地址转换成Uri格式
		Uri uri = Uri.fromFile(new File(fileName));
		// 设置系统相机拍摄照片完成后图片文件的存放地址
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, CASE_CAMERA);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);
        String fileName=((BusinessList) getActivity()).getFileName();
        //System.out.println("filrname in onresult  "+fileName);
        if (resultCode == Activity.RESULT_OK) 
        {   
        		
        	String s=applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).getFlow_video();
        	s+=fileName+";";
        	applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).setFlow_video(s);        		
        	String ss=applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).getFlow_video();
        	if(ss.trim()!=null)
        	{
        		pictures=ss.split(";");
        	}
        	
        	if(fileName.endsWith("jpg")||fileName.endsWith("png"))
            {
        		System.out.println("i am in CASE_CAMERA");
        		Uri uri = Uri.fromFile(new File(fileName));
            	img_pic.setVisibility(View.VISIBLE);
            	video_pic.setVisibility(View.GONE);
            	img_pic.setImageURI(uri);
            }
            else
            {
            	System.out.println("i am in CASE_video");
            	Uri uri = Uri.fromFile(new File(fileName));    		
            	img_pic.setVisibility(View.GONE);
            	video_pic.setVisibility(View.VISIBLE);
            	video_pic.setMediaController(new MediaController(getActivity()));  
            	video_pic.setVideoURI(uri);  
            	video_pic.start();  
            	video_pic.requestFocus();  
            }
        } 
	}
}
