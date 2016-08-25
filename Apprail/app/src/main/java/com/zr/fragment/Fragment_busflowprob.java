package com.zr.fragment;
//8月22日check OK
//// TODO: 16/8/22  检查add 问题
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.BusinessList;
import com.zr.apprail.Num;
import com.zr.apprail.R;
import com.zr.domain.Problem;
import com.zr.domain.ReportItem;

import com.zr.thread.RecordWriteThread;
import com.zr.thread.ReportWriteThread;
import com.zr.fragment.Fragment_busflowprob.MyAdapter.ViewHolder;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_busflowprob extends Fragment implements OnClickListener,OnItemClickListener{

	private TextView businessflowprob;
	private TextView businessflowprobtime;
	private TextView businessflowprobdesp;
	private Button busiflowprobmassage;
	private Button busiflowprobvideo;
	private Button busiflowdaddflowprob;
	private ImageView img_ret;
	private View addbusinessflowpro;
	private ListView busiflowprobproblist;
	private MyAdapter adapter;
	ApplicationTrans applicationdata;
	private int busposition;
	private int flowposition;
	private ReportItem flow;
	private static int DEFAULT_MAX_TEXT_SIZE = 40;
	
	
	private List<Problem> thisflowprob;
	private HashMap<Integer,Integer> prob_to_thisprob;	
	
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
		View view = inflater.inflate(R.layout.fragment_busflow_prob, null);
		applicationdata=(ApplicationTrans) getActivity().getApplication();		
		
		busposition=applicationdata.getBusposition();
		flowposition=applicationdata.getFlowposition();
		flow=new ReportItem();
		flow=applicationdata.getBusinesslist().get(busposition).getBusiness_flowData().get(flowposition);
		
		thisflowprob=new ArrayList<Problem>();
		prob_to_thisprob=new HashMap<Integer,Integer>();//第一个存放现在prob的位置作为key
		System.out.println("initflowprob oncreateview before init");
		initAdapter();
		
		businessflowprob=(TextView) view.findViewById(R.id.busflow_prob_textView_prolist);
		businessflowprobtime=(TextView) view.findViewById(R.id.busflow_prob_textView_flowtime);
		businessflowprobdesp=(TextView) view.findViewById(R.id.busflow_prob_textView_flowdesp);
		busiflowprobmassage=(Button) view.findViewById(R.id.busflow_prob_button_param);
		busiflowprobvideo=(Button) view.findViewById(R.id.busflow_prob_button_video);
		img_ret= (ImageView) view.findViewById(R.id.busflow_prob_imageView_return);
		busiflowdaddflowprob=(Button) view.findViewById(R.id.busflow_prob_button_addproblem);
		busiflowprobproblist=(ListView) view.findViewById(R.id.busflow_prob_listView);
		addbusinessflowpro=view.findViewById(R.id.busflow_prob_addlist_view);
				
		String s=flow.getMessage();
		if(s.indexOf("(")>0)
		{
			s=s.substring(0,s.indexOf("("));
		}
		if(s.length()>DEFAULT_MAX_TEXT_SIZE)
		{
			businessflowprobdesp.setLines(2);
			String temop=s;
			s=temop.substring(0, DEFAULT_MAX_TEXT_SIZE)+"\r\n"
					+temop.substring(DEFAULT_MAX_TEXT_SIZE+1,temop.length());
		}
		Log.d("fragment_busflowprob", "onCreateView");
		businessflowprob.setText("流程"+ Num.getString(flowposition+1));
		businessflowprobtime.setText(flow.getTime());
		businessflowprobdesp.setText(s);		
		busiflowprobmassage.setOnClickListener(this);		
		busiflowprobvideo.setOnClickListener(this);
		busiflowdaddflowprob.setOnClickListener(this);
		img_ret.setOnClickListener(this);	
		busiflowprobproblist.setOnItemClickListener(this);
		busiflowprobproblist.setAdapter(adapter);		
        return view;
    }
	
	private void initAdapter() {
		//获取prob中的信息
		int selpronum=0;
		int num=0;
		ArrayList<Problem> flow_probs=applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).getFlowproblem();
		//选择了的在前面
		//System.out.println("busprobsize"+flow_probs.size());
		for(int i=0;i<flow_probs.size();i++)
		{
			if(flow_probs.get(i).isProblem_choose())
			{
				thisflowprob.add(flow_probs.get(i));
				prob_to_thisprob.put(num,i);				
				selpronum+=1;
				num+=1;
			}			
		}
		for(int i=0;i<flow_probs.size();i++)
		{
			if(!flow_probs.get(i).isProblem_choose())
			{
				thisflowprob.add(flow_probs.get(i));
				prob_to_thisprob.put(num,i);
				num+=1;
			}
		}
		//System.out.println("initflowprob");
		adapter = new MyAdapter(thisflowprob);
		
		//判断哪个问题已经被选择了	
		for(int i=0;i<selpronum;i++)
		{
			adapter.mChecked.set(i, true);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//System.out.println("FLOW PROB FLOW POSITION ITEMCLICK"+position);
		//System.out.println("adapter.mChecked.get(position)"+adapter.mChecked.get(position));
		if(adapter.mChecked.get(position))
		{
			adapter.mChecked.set(position, false);
		}
		else
		{
			adapter.mChecked.set(position, true);
		}
		setselectpro();
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.busflow_prob_button_param:
				setselectpro();
				((BusinessList) getActivity()).gotobusinessflowpara();
				break;
			case R.id.busflow_prob_button_video:
				setselectpro();
				((BusinessList) getActivity()).gotobusinessflowvideo();
				break;
			case R.id.busflow_prob_button_addproblem:
				addflowlist();
	        	break;
			case R.id.busflow_prob_imageView_return:
				 setselectpro();
				 ((BusinessList) getActivity()).gotobusinessflow();
				 break;
			default:
				break;
		}		
	}

	private void addflowlist() {
		addbusinessflowpro.setVisibility(View.VISIBLE);		
		addbusinessflowpro.findViewById(R.id.busflow_prob_addlist_button).setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v) {
				EditText et=(EditText) addbusinessflowpro.findViewById(R.id.busflow_prob_addlist_editText);
				String s=et.getText().toString();
				
				if(!TextUtils.isEmpty(s))
				{
					String[] list=s.split(";");
					List<Problem> pros=new ArrayList<Problem>();
					for(String item:list)
					{
						adapter.mChecked.add(false); 
						Problem p=new Problem();
						p.setProblem_description(item);
						p.setProblem_choose(true);
						thisflowprob.add(p);
						pros.add(p);
						prob_to_thisprob.put(thisflowprob.size()-1,thisflowprob.size()-1);
					}
					applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).getFlowproblem().addAll(pros);
					applicationdata.getBusiness(busposition).setBusiness_probData();
					applicationdata.getBusiness(busposition).setBusiness_mData();
					et.setText("");
					addbusinessflowpro.setVisibility(View.GONE);
				}
			}
		});
	}

	private void setselectpro()
	{
		for(int i=0;i<thisflowprob.size();i++)
		{
			int j=prob_to_thisprob.get(i);//对应到原来的prob中的第几个prob
			applicationdata.getBusiness(busposition).getBusiness_flow(flowposition).getFlowproblem().get(j).setProblem_choose(adapter.mChecked.get(i));
		}
		applicationdata.getBusiness(busposition).setBusiness_flowData(flowposition);
		applicationdata.getBusiness(busposition).setBusiness_mData();
	}
	/*
	 * 内部list适配器类
	 */
	class MyAdapter extends BaseAdapter
	{
		List<Boolean> mChecked;  
		List<Problem> pros;
			    
	    public MyAdapter(List<Problem> list){
	    	pros = new ArrayList<Problem>();  
	        pros =list;    	
	        mChecked = new ArrayList<Boolean>();
	        System.out.print("myadapter");
	        for(int i=0;i<list.size();i++)
	        { 
	        	mChecked.add(false);
	        }
	    }    
	    
	    @Override  
	    public int getCount() {  
	    	return pros.size();
	    }  

	    @Override  
	    public Object getItem(int position) {
	    	return pros.get(position);
	    }  

	    @Override  
	    public long getItemId(int position) {  
	        return position;  
	    }  
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        View view;
	        ViewHolder holder = null;  
	        if (convertView == null) 
	        {
	            LayoutInflater mInflater = (LayoutInflater) getActivity()  
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = mInflater.inflate(R.layout.item_projectnamecheck, null);
	            holder = new ViewHolder();
	            holder.cb = (CheckBox)view.findViewById(R.id.projectitem_checkBox);
	            holder.tv = (TextView)view.findViewById(R.id.projectitem_textView_projectname);
	            final int p = position;
	            view.setTag(holder);  
	        }else{       
	            view = convertView;  
	            holder = (ViewHolder)view.getTag();  
	        }  
	          
	        holder.cb.setChecked(mChecked.get(position));  
	        holder.tv.setText(thisflowprob.get(position).getProblem_description());    
	        return view;  
	    }

	    class ViewHolder {  
		    public TextView tv = null;  
		    public CheckBox cb = null;  
		}
	}	
}
