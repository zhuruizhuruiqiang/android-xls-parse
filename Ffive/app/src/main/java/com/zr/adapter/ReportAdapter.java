package com.zr.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zr.domain.ReportItem;
import com.zr.ffive.ApplicationTrans;
import com.zr.ffive.R;

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
	
	public ReportAdapter(Context mContext2, List<ReportItem> inititemdata) {
		this.mData=inititemdata;
		this.mContext=mContext2;
		this.mInflater = LayoutInflater.from(mContext);
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
		View view;
        ViewHolder holder = null;  
          
        if (map.get(position) == null) 
        {
            view = mInflater.inflate(R.layout.itemrep_item, null);
            holder = new ViewHolder(); 
            holder.tvname = (TextView)view.findViewById(R.id.itemrep_item2_textView_pro);
            holder.tvdes = (TextView)view.findViewById(R.id.itemrep_item2_textView_name);
            final int p = position;  
            map.put(position, view);
            view.setTag(holder);  
        }
        else
        {       
            view = map.get(position);  
            holder = (ViewHolder)view.getTag();  
        }
        ReportItem item=mData.get(position);
        Map map=item.getData();
        Set keys = map.keySet();
        for(Object o:keys)
        {
        	String s=o.toString();
        	holder.tvname.setText(map.get(s).toString());
            holder.tvdes.setText(item.getMessage());
        }
        holder.tvname.setVisibility(View.VISIBLE);
        holder.tvdes.setVisibility(View.VISIBLE);
        return view;  
	}
	
	 class ViewHolder 
	 {  
		    public TextView tvname = null;  
		    public TextView tvdes = null;  
	 }	
}
