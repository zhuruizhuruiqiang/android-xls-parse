package com.zr.fragment;
//8月22日checkOK
import java.util.ArrayList;
import java.util.List;

import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.BusinessList;
import com.zr.apprail.R;
import com.zr.domain.Business;
import com.zr.domain.Project;
import com.zr.fragment.Fragment_selectproject.MyAdapter.ViewHolder;
import com.zr.thread.RecordWriteThread;
import com.zr.thread.ReportWriteThread;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_selectproject extends Fragment implements OnItemClickListener,OnClickListener{
	private ListView projectlistview;
	private MyAdapter adapter; 
	private Button buttonok;
	ApplicationTrans applicationdata;
	List<Boolean> sel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_selectproject, null);
		
		applicationdata=(ApplicationTrans) getActivity().getApplication();
		sel = new ArrayList<Boolean>();
		for(int i=0;i<applicationdata.getProjectlist().size();i++){
			sel.add(false);
		}

		adapter = new MyAdapter();
		
		projectlistview=(ListView) view.findViewById(R.id.selectpro_listView);
		buttonok=(Button) view.findViewById(R.id.selectpro_button_ok);

		projectlistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		projectlistview.setOnItemClickListener(this);
		buttonok.setOnClickListener(this);
		projectlistview.setAdapter(adapter); 
		return view;
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(sel.get(position))
		{
			sel.set(position, false);
		}
		else
		{
			sel.set(position, true);
		}
		
		ViewHolder holder = (ViewHolder) view.getTag();  
        holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态   		
	}
	
	@Override
	public void onClick(View v) {
		boolean change = false;
		for (int p=0;p<applicationdata.getProjectlist().size();p++)
		{			
			if(sel.get(p))
			{
				change=true;
			}			
		}
		if(change==true)
		{
			Business bus=new Business();			
			for(int i=0;i<sel.size();i++)
			{
				if(sel.get(i))
				{
					Project p=new Project(applicationdata.getProject(i));
					bus.addBusiness_project(p);
				}				
			}
			applicationdata.getBusinesslist().add(bus);
			
			if(applicationdata.isWorkchoose())
			{
				String savename=applicationdata.getWork(applicationdata.getWorkposition()).getWork_name();
				String path=applicationdata.getConfiledir()+"/"+applicationdata.string_worklist;;
				applicationdata.writeworklist(path,savename);
				ReportWriteThread t=new ReportWriteThread();
				t.run();
				RecordWriteThread t1=new RecordWriteThread();
				t1.run();
			}
		}
		((BusinessList) getActivity()).gotobusinesslistactivity(applicationdata.getWorkposition());
	}

	/*
	 * 内部list适配器类
	 */
	class MyAdapter extends BaseAdapter
	{

	    @Override
	    public int getCount() {
	    	return applicationdata.getProjectlist().size();
	    }

	    @Override
	    public Object getItem(int position) {
	    	return applicationdata.getProject(position);
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
		        view.setTag(holder);
	        }else{
	            view = convertView;
	            holder = (ViewHolder)view.getTag();
	        }
	        holder.tv.setText(applicationdata.getProject(position).getProject_name());
	        return view;
	    }

	    class ViewHolder {
		    public TextView tv = null;
		    public CheckBox cb = null;
		}
	}
}
