package com.zr.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zr.Thread.RecordWriteThread;
import com.zr.Thread.ReportWriteThread;
import com.zr.domain.Problem;
import com.zr.fourth.ApplicationTrans;
import com.zr.fourth.BusinessList;
import com.zr.fourth.Num;
import com.zr.fourth.R;
import com.zr.fragment.Fragment_businessproblem.MyAdapter.ViewHolder;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Fragment_businessproblem extends Fragment implements OnClickListener,OnItemClickListener{
	private Button businessmassage;
	private Button businessflow;
	private Button addbusinesspro;
	private ImageView img_ret;
	private ListView businessproblemlist;
	private View addbusinessproview;
	private TextView businessnum;
	private MyAdapter adapter;
	ApplicationTrans applicationdata;
	private int busposition;
	private List<Problem> thisprob;
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
		View view = inflater.inflate(R.layout.fragment_businessproblem, null);
		applicationdata=(ApplicationTrans) getActivity().getApplication();		
		busposition=applicationdata.getBusposition();
		
		thisprob=new ArrayList<Problem>();
		prob_to_thisprob=new HashMap<Integer,Integer>();//第一个存放现在prob的位置作为key
		initAdapter();
		
		businessnum=(TextView) view.findViewById(R.id.business_problem_textView_prolist);
		businessmassage=(Button) view.findViewById(R.id.business_problem_button_message);
		businessflow=(Button) view.findViewById(R.id.business_problem_button_flow);
		addbusinesspro=(Button) view.findViewById(R.id.business_problem_button_addproblem);
		img_ret= (ImageView) view.findViewById(R.id.business_problem_imageView_return);
		businessproblemlist=(ListView) view.findViewById(R.id.business_problem_listView);
		addbusinessproview=view.findViewById(R.id.business_problem_addlist_view);
		
		businessnum.setText("项目"+Num.getString(busposition+1));
		businessmassage.setOnClickListener(this);
		businessflow.setOnClickListener(this);
		addbusinesspro.setOnClickListener(this);
		img_ret.setOnClickListener(this);
		businessproblemlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		businessproblemlist.setOnItemClickListener(this);
		businessproblemlist.setAdapter(adapter);		
        return view;
    }
	
	private void initAdapter() {
		//获取prob中的信息
		int selpronum=0;
		int num=0;
		ArrayList<Problem> business_probs=applicationdata.getBusiness(busposition).getBusiness_probs();
		//选择了的在前面
		for(int i=0;i<business_probs.size();i++)
		{
			if(business_probs.get(i).isProblem_choose())
			{
				thisprob.add(business_probs.get(i));
				prob_to_thisprob.put(num,i);
				//sel.add(false);				
				selpronum+=1;
				num+=1;
			}			
		}
		for(int i=0;i<business_probs.size();i++)
		{
			if(!business_probs.get(i).isProblem_choose())
			{
				thisprob.add(business_probs.get(i));
				prob_to_thisprob.put(num,i);
				//sel.add(true);
				num+=1;
			}
		}
		adapter = new MyAdapter(thisprob);
		
		//判断哪个问题已经被选择了	
		for(int i=0;i<selpronum;i++)
		{
			adapter.mChecked.set(i, true);
		}
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{  
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
	
	private void setselectpro()
	{
		for(int i=0;i<thisprob.size();i++)
		{
			int j=prob_to_thisprob.get(i);//对应到原来的prob中的第几个prob
			applicationdata.getBusiness(busposition).getBusiness_prob(j).setProblem_choose(adapter.mChecked.get(i));
		}
		applicationdata.getBusiness(busposition).setBusiness_probData();
		applicationdata.getBusiness(busposition).setBusiness_mData();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {  
        case R.id.business_problem_button_message: 
        	((BusinessList) getActivity()).gotobusinesslist();
            break;  
        case R.id.business_problem_button_flow:
        	((BusinessList) getActivity()).gotobusinessflow();
        	break;
        case R.id.business_problem_button_addproblem:
        	addprolist();
        	break;
        case R.id.business_problem_imageView_return:
        	((BusinessList) getActivity()).gotobusinesslistactivity(applicationdata.getWorkposition());
        	break;
        default:  
            break;  
        }
	}
	
	private void addprolist() {
		addbusinessproview.setVisibility(View.VISIBLE);		
		addbusinessproview.findViewById(R.id.business_problem_addlist_button).setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v) {
				EditText et=(EditText) addbusinessproview.findViewById(R.id.business_problem_addlist_editText);
				String s=et.getText().toString();
				
				if(!TextUtils.isEmpty(s))
				{
					String[] list=s.split(";");
					List<Problem> pros=new ArrayList<Problem>();
					for(String item:list)
					{
						adapter.mChecked.add(true); 
						Problem p=new Problem();
						p.setProblem_description(item);	
						p.setProblem_choose(true);
						thisprob.add(p);
						pros.add(p);
						prob_to_thisprob.put(thisprob.size()-1,thisprob.size()-1);
					}
					applicationdata.getBusiness(busposition).getBusiness_probs().addAll(pros);
					applicationdata.getBusiness(busposition).setBusiness_probData();
					applicationdata.getBusiness(busposition).setBusiness_mData();
					et.setText("");
					addbusinessproview.setVisibility(View.GONE);
				}
			}
		});
		
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
	            view = mInflater.inflate(R.layout.item_loadstafile, null);
	            holder = new ViewHolder();
	            holder.cb = (CheckBox)view.findViewById(R.id.stafileloaditem_checkBox);  
	            holder.tv = (TextView)view.findViewById(R.id.stafileloaditem_textView_stafilename);
	            final int p = position; 
	            view.setTag(holder);  
	        }else{       
	            view = convertView;  
	            holder = (ViewHolder)view.getTag();  
	        }  
	          
	        holder.cb.setChecked(mChecked.get(position));  
	        holder.tv.setText(pros.get(position).getProblem_description());    
	        return view;  
	    }

	    class ViewHolder {  
		    public TextView tv = null;  
		    public CheckBox cb = null;  
		}
	}	
	
}
