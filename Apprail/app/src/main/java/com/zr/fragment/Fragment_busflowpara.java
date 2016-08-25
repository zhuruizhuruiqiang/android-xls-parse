package com.zr.fragment;
//8月22日check OK
import java.util.ArrayList;
import java.util.List;

import com.zr.adapter.FlowDetailAdapter;
import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.BusinessList;
import com.zr.apprail.Num;
import com.zr.apprail.R;
import com.zr.domain.ProcessFlow;
import com.zr.domain.ReportItem;

import com.zr.thread.RecordWriteThread;
import com.zr.thread.ReportWriteThread;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment_busflowpara extends Fragment implements OnClickListener{
	private TextView businessflowdetail;
	private TextView businessflowdetailtime;
	private TextView businessflowdetaildesp;
	private Button busiflowdetailproblem;
	private Button busiflowdetailvideo;
	private ImageView img_ret;
	private ListView busiflowdetailparamlist;
	
	ApplicationTrans applicationdata;
	private FlowDetailAdapter adapter;
	
	private ReportItem flow;
	private List<ReportItem> mData;
	private int busposition;
	private int flowposition;
	private static int DEFAULT_MAX_TEXT_SIZE = 40;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
		mData=adapter.getmData();
		saveflowdetail();
		ReportWriteThread t=new ReportWriteThread();
		t.run();
		RecordWriteThread t1=new RecordWriteThread();
		t1.run();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_busflow_param, null);
		applicationdata=(ApplicationTrans) getActivity().getApplication();
		
		busposition=applicationdata.getBusposition();
		flowposition=applicationdata.getFlowposition();
		flow=new ReportItem();
		mData=new ArrayList<ReportItem>();
		flow=applicationdata.getBusinesslist().get(busposition).getBusiness_flowData().get(flowposition);
		initflowdetail();
		adapter=new FlowDetailAdapter(getActivity(), mData);
		
		businessflowdetail=(TextView) view.findViewById(R.id.busflow_param_textView_prolist);
		businessflowdetailtime=(TextView) view.findViewById(R.id.busflow_param_textView_flowtime);
		businessflowdetaildesp=(TextView) view.findViewById(R.id.busflow_param_textView_flowdesp);
		busiflowdetailproblem=(Button) view.findViewById(R.id.busflow_param_button_prob);
		busiflowdetailvideo=(Button) view.findViewById(R.id.busflow_param_button_video);
		img_ret= (ImageView) view.findViewById(R.id.busflow_param_imageView_return);
		busiflowdetailparamlist=(ListView) view.findViewById(R.id.busflow_param_listView);
			
		String s=flow.getMessage();
		if(s.indexOf("(")>0)
		{
			s=s.substring(0,s.indexOf("("));
		}
		if(s.length()>DEFAULT_MAX_TEXT_SIZE)
		{
			businessflowdetaildesp.setLines(2);
			String temop=s;
			s=temop.substring(0, DEFAULT_MAX_TEXT_SIZE)+"\r\n"
					+temop.substring(DEFAULT_MAX_TEXT_SIZE+1,temop.length());
		}	
		
		businessflowdetail.setText("流程"+ Num.getString(flowposition+1));
		businessflowdetailtime.setText(flow.getTime());
		businessflowdetaildesp.setText(s);
		busiflowdetailproblem.setOnClickListener(this);
		busiflowdetailvideo.setOnClickListener(this);
		img_ret.setOnClickListener(this);
		busiflowdetailparamlist.setAdapter(adapter);
		
        return view;
    }
	
	private void initflowdetail() {
		ProcessFlow flow=applicationdata.getBusiness(busposition).getBusiness_flow(flowposition);
		ReportItem item=new ReportItem();
		//HashMap<String, String> data=new HashMap<String, String>();
		//item.setTypenum(ReportItem.ITEM_TYPE_PROPARR);
		//data.put("flownum", "流程编号");
		//item.setData(data);
		//item.setMessage(flow.getFlow_num());
		//mData.add(item);
		
		String s=flow.getFlow_description();
		Log.v("busflowdetail", "flow description:"+s);
		int position=0;
		int index=0;
		while(position<s.length())
		{
			int start=s.indexOf("&", position);
			if(start>0)
			{
				int end=s.indexOf("&",start+1);
				String s_item=s.substring(start+1, end);				
				
				String[] items=s_item.split("#");
				item=new ReportItem();
				//data=new HashMap<String, String>();
				item.setTypenum(ReportItem.ITEM_TYPE_PROPARR);
				//data.put("parr"+index,items[0]);
				item.setParrname(items[0]);
				if(items.length>1)
				{
					item.setMessage(items[1]);					
				}
				else
				{
					item.setMessage("");
				}
				mData.add(item);
				
				position=end+1;
				index=index+1;
			}
			else
			{
				position=s.length();
			}
		}
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.busflow_param_button_prob:
				((BusinessList) getActivity()).gotobusinessflowprob();
				break;
			case R.id.busflow_param_button_video:
				((BusinessList) getActivity()).gotobusinessflowvideo();
				break;
			 case R.id.busflow_param_imageView_return:
				 ((BusinessList) getActivity()).gotobusinessflow();
				 break;
			default:
				break;
		}		
	}

	private void saveflowdetail(){
		String s=applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).getFlow_description();
		String s_change="";		
		
		int position=0;
		int index=0;
		
		while(position<s.length())
		{
			int start=s.indexOf("&", position);
			if(start>0)
			{
				int end=s.indexOf("&",start+1);
				String s_item=s.substring(start+1, end);
				String[] items=s_item.split("#");
				
				String s1=s.substring(position, start);
				String s2="&"+items[0]+"#"+mData.get(index).getMessage()+"&";
				s_change+=s1+s2;
				
				position=end+1;
				index=index+1;
			}
			else
			{
				String s1=s.substring(position, s.length());
				s_change=s_change+s1;
				position=s.length();
			}
		}
		applicationdata.getBusinesslist().get(busposition).getBusiness_flow(flowposition).setFlow_description(s_change);
		applicationdata.getBusinesslist().get(busposition).setBusiness_flowData(flowposition);
		applicationdata.getBusinesslist().get(busposition).setBusiness_mData();
	}
}
