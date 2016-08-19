package com.zr.fragment;

import com.zr.adapter.ReportAdapter;
import com.zr.ffive.ApplicationTrans;
import com.zr.ffive.R;
import com.zr.listview.ChildListView;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment_report extends Fragment {

	private ListView businesslistview;
	private  MyListAdapter adapter;
	private TextView rolename;
	private TextView personname;
	ApplicationTrans applicationdata;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report, null);
		applicationdata=(ApplicationTrans) getActivity().getApplication();			
		
		adapter = new MyListAdapter();
		
		businesslistview=(ListView) view.findViewById(R.id.report_listView);
		rolename=(TextView) view.findViewById(R.id.report_textView_recordrolenaeme);	
		personname=(TextView) view.findViewById(R.id.report_textView_recordpersonnaeme);	
		
		rolename.setText(applicationdata.getRole());
		personname.setText(applicationdata.getPersonname());	
		businesslistview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
        return view;
    }

	class MyListAdapter extends BaseAdapter 
	{ 
		@Override
		public int getCount() {
			return applicationdata.getBusinesslist().size();
		}

		@Override
		public Object getItem(int position) {
			return applicationdata.getBusinesslist().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			//由系统调用，获取一个view对象，作为listview对象的条目
			//position对象的条目对应于那个条目
			//converview缓存
			//parent
			View view;
		    ViewHolder holder = null;  
		    //System.out.println("select project adapter View getView");
		    if (convertView == null) 
		    {
		        LayoutInflater mInflater = (LayoutInflater) getActivity()  
		                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        view = mInflater.inflate(R.layout.item_reportlist, null);
		        holder = new ViewHolder();
		        
		        holder.tv_num = (TextView)view.findViewById(R.id.itemreport_textView_prolist);
		        holder.im_prolist =(ChildListView) view.findViewById(R.id.itemreport_listView);
		        
		        ReportAdapter tempAdapt = new ReportAdapter(getActivity(), applicationdata.getBusinesslist().get(position).getBusiness_mData());
		        holder.im_prolist.setAdapter(tempAdapt);
		        holder.im_prolist.setVisibility(View.VISIBLE);
		        tempAdapt.notifyDataSetChanged();
		        
		        final int p = position; 
		        view.setTag(holder);  
		    }
		    else
		    {       
		        view = convertView;  
		        holder = (ViewHolder)view.getTag();  
		    }
		    holder.tv_num.setText("项目"+Num.getString(position+1));    
		    return view;  
		}
		
		class ViewHolder
	    {  
		    public TextView tv_num   = null;  
		    public ChildListView im_prolist   = null;
		} 	
	}
}
