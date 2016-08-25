package com.zr.fragment;
//8月22check OK
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zr.adapter.BusMessAdapter;
import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.BusinessList;
import com.zr.apprail.Num;
import com.zr.apprail.R;
import com.zr.domain.Business;
import com.zr.domain.ReportItem;

import com.zr.thread.RecordWriteThread;
import com.zr.thread.ReportWriteThread;

import java.util.ArrayList;
import java.util.List;

public class Fragment_businessmessage extends Fragment implements OnClickListener{

	private Button businessflow;
	private Button businessproblem;
	private ImageView img_ret;
	private ListView businessmessagelist;
	private TextView businessnum;
	private BusMessAdapter adapter;
	ApplicationTrans applicationdata;
	private Business bus;
	private List<ReportItem> mData;
	private int busposition;
	
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
		View view = inflater.inflate(R.layout.fragment_businessmessage, null);
		applicationdata=(ApplicationTrans) getActivity().getApplication();		
		busposition= applicationdata.getBusposition();
		bus=new Business();
		mData=new ArrayList<ReportItem>();
		bus=applicationdata.getBusiness(busposition);
		Log.d("fra_busmess", "busposition"+busposition);
		applicationdata.getBusiness(busposition).setBusiness_nameData();
		applicationdata.getBusiness(busposition).setBusiness_messData();
		mData.addAll(bus.getBusiness_nameData());
		mData.addAll(bus.getBusiness_messData());
		
		adapter = new BusMessAdapter(getActivity(),mData,busposition);		
		
		businessnum=(TextView) view.findViewById(R.id.business_message_textView_prolist);
		businessflow=(Button) view.findViewById(R.id.business_message_button_flow);
		businessproblem=(Button) view.findViewById(R.id.business_message_button_problem);
		img_ret= (ImageView) view.findViewById(R.id.business_message_imageView_return);
		businessmessagelist=(ListView) view.findViewById(R.id.business_message_listView);
	
		businessnum.setText("项目"+ Num.getString(busposition+1));
		businessflow.setOnClickListener(this);
		businessproblem.setOnClickListener(this);
		img_ret.setOnClickListener(this);
		businessmessagelist.setAdapter(adapter);		
        return view;
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {  
        case R.id.business_message_button_flow:  
        	((BusinessList) getActivity()).gotobusinessflow();
            break;
        case R.id.business_message_button_problem:
        	((BusinessList) getActivity()).gotobusinessproblem();
        	break;
        case R.id.business_message_imageView_return:
        	((BusinessList) getActivity()).gotobusinesslistactivity(applicationdata.getWorkposition());
        	break;
        default:  
            break;  
        }  
		
	}
}
