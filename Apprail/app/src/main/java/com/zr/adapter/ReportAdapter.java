package com.zr.adapter;
//8月24check OK
import java.util.HashMap;
import java.util.List;

import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.R;
import com.zr.domain.ReportItem;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReportAdapter extends BaseAdapter{

	ApplicationTrans applicationdata;
	private Context mContext = null;//上下文
	private LayoutInflater mInflater = null;
	private List<ReportItem> mData = null;//要显示的数据
	HashMap<Integer,View> map = new HashMap<Integer,View>(); 
	
	public ReportAdapter(Context mContext2,int position) {
		applicationdata.getBusiness(position).setReportitemdata();
		this.mData=applicationdata.getBusiness(position).getBusiness_mData();
		this.mContext=mContext2;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getItemViewType(int position) {
		return mData.get(position).getTypenum();
	}


	@Override
	public int getViewTypeCount() {
		return 4;
	}


	@Override
	public int getCount() {
		return mData.size();
	}
	
	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=null;
		ViewHolder holder = null;
		ViewHolder1 holder1 = null;
		int type = getItemViewType(position);
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		switch(type) {
			case ReportItem.ITEM_TYPE_PROFLOW:
				if(convertView==null)
				{
					view = mInflater.inflate(R.layout.item_reportflow, null);
					holder1 = new ViewHolder1();
					holder1.rolename = (TextView)view.findViewById(R.id.item_reportflow_role);
					holder1.time = (TextView)view.findViewById(R.id.item_reportflow_time);
					holder1.des= (TextView)view.findViewById(R.id.item_reportflow_desp);
					map.put(position, view);
					view.setTag(holder1);
				}
				else{
					view = convertView;
					holder1 = (ViewHolder1)view.getTag();
				}
				holder1.rolename.setText(mData.get(position).getParrname());
				holder1.time.setText(mData.get(position).getTime());
				holder1.des.setText(mData.get(position).getMessage());
				break;
			default:
				if(convertView==null) {
					view = mInflater.inflate(R.layout.itemrep_item, null);
					holder = new ViewHolder();
					holder.tvname = (TextView) view.findViewById(R.id.itemrep_item2_textView_pro);
					holder.tvdes = (TextView) view.findViewById(R.id.itemrep_item2_textView_name);
					map.put(position, view);
					view.setTag(holder);
				}
				else{
					view = convertView;
					holder = (ViewHolder)view.getTag();
				}
				holder.tvname.setText(mData.get(position).getParrname());
				holder.tvdes.setText(mData.get(position).getMessage());
				break;
		}
		return view;
	}
	
	 class ViewHolder 
	 {  
		    public TextView tvname = null;  
		    public TextView tvdes = null;  
	 }

	 class ViewHolder1
	 {
		 public TextView rolename = null;
		 public TextView time = null;
		 public TextView des=null;
	 }
}
