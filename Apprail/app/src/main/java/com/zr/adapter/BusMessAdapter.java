package com.zr.adapter;
//8月24日check OK
import java.util.List;

import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.R;
import com.zr.domain.ReportItem;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ZHURUI on 16/8/16.
 */
public class BusMessAdapter extends BaseAdapter 
{ 
	ApplicationTrans applicationdata;
	private Context mContext = null;//上下文
	private LayoutInflater mInflater = null;
	private List<ReportItem> mData = null;//要显示的数据
	int projectnum;
	int busposition;
	
	public BusMessAdapter(Context mContext2, List<ReportItem> inititemdata,int busposition) {
		applicationdata=ApplicationTrans.getInstance();
		this.mData=inititemdata;
		this.mContext=mContext2;
		this.busposition=busposition;
		this.mInflater = LayoutInflater.from(mContext);
		for(ReportItem item:inititemdata)
		{
			if(item.getTypenum()==ReportItem.ITEM_TYPE_PRONAME)
			{
				projectnum++;
			}
		}
	}

	@Override
	public int getItemViewType(int position) {
		return mData.get(position).getTypenum();
	}


	@Override
	public int getViewTypeCount() {
		return 2;
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
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		//由系统调用，获取一个view对象，作为listview对象的条目
		//position对象的条目对应于那个条目
		//converview缓存
		//parent
		View view=null;
		ViewHolder1 holder1 = null; 
		ViewHolder2 holder2 = null; 
		int type = getItemViewType(position); 
		mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
	    switch(type)
    	{
    		case ReportItem.ITEM_TYPE_PRONAME:
    			 view = mInflater.inflate(
						 R.layout.item_projectnamecheck, null);
	             holder1 = new ViewHolder1();
	             holder1.cb = (CheckBox)view.findViewById(R.id.projectitem_checkBox);
	             holder1.tv = (TextView)view.findViewById(R.id.projectitem_textView_projectname);
	             view.setBackgroundResource(R.color.ivory);
				 holder1.cb.setVisibility(View.GONE);
	             view.setTag(holder1);
    			 break; 
    		case ReportItem.ITEM_TYPE_PROPARR: 
    			 view = mInflater.inflate(R.layout.item_busimess,null); 
    			 holder2 = new ViewHolder2(); 
    			 holder2.tvname = (TextView)view.findViewById(R.id.item_busmess_textView);   
    			 holder2.tvpara = (EditText)view.findViewById(R.id.item_busmess_edit); 
    			 view.setTag(holder2); 
    			 break;
    	}
	    switch(type)
    	{
    		case ReportItem.ITEM_TYPE_PRONAME:
    			String s=mData.get(position).getMessage();
    			 holder1.tv.setText(s);
    			 break;
    		case ReportItem.ITEM_TYPE_PROPARR:
    			if(position==projectnum)
    			{
    				//我们要拿到里面的值根据position拿值
    				holder2.tvpara.addTextChangedListener(new TextWatcher() {     
    	                @Override  
    	                public void afterTextChanged(Editable s) {  
    	                    //将editText中改变的值设置的HashMap中  
    	                	mData.get(position).setMessage(s.toString());  
    	                	applicationdata.getBusiness(busposition).setBusiness_place(s.toString());
    	                }

						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {	
						}
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
						}  
    	            });  
    			}
    			else if(position==projectnum+1)
    			{
    				holder2.tvpara.addTextChangedListener(new TextWatcher() {
    	                @Override  
    	                public void afterTextChanged(Editable s) {  
    	                    //将editText中改变的值设置的HashMap中  
    	                	mData.get(position).setMessage(s.toString());  
    	                	applicationdata.getBusiness(busposition).setBusiness_carnum(s.toString());
    	                }

						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {	
						}
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
						}  
    	            });
    			}
    			else
    			{
    				holder2.tvpara.addTextChangedListener(new TextWatcher() {
    	                @Override  
    	                public void afterTextChanged(Editable s) {  
    	                    //将editText中改变的值设置的HashMap中  
    	                	mData.get(position).setMessage(s.toString());    
    	                	applicationdata.getBusiness(busposition).setBusiness_involeveddepart(s.toString());
    	                }

						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {	
						}
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
						}  
    	            });
    			}
    			holder2.tvname.setText(mData.get(position).getParrname());
    			holder2.tvpara.setText(mData.get(position).getMessage());
    			break;
    	}
	    return view;  
	}
	
	class ViewHolder1
    {  
	    public TextView tv = null;  
	    public CheckBox cb = null;  
	} 
	
	class ViewHolder2
    {  
	    public TextView tvname = null;  
	    public EditText tvpara = null;  
	} 	
}