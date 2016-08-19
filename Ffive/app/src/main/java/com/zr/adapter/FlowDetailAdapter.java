package com.zr.adapter;

import java.util.List;

import com.zr.domain.ReportItem;
import com.zr.ffive.ApplicationTrans;
import com.zr.ffive.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
//这里加上对flow每次的处理
public class FlowDetailAdapter extends BaseAdapter {

	ApplicationTrans applicationdata;
	private Context mContext = null;//上下文
	private LayoutInflater mInflater = null;
	private List<ReportItem> mData = null;//要显示的数据
	
	public FlowDetailAdapter(Context mContext2, List<ReportItem> inititemdata) {
		applicationdata=ApplicationTrans.getInstance();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		//由系统调用，获取一个view对象，作为listview对象的条目
		//position对象的条目对应于那个条目
		//converview缓存
		//parent
		View view=null;
		ViewHolder holder = null;  
		mInflater = (LayoutInflater) mContext  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        

		view = mInflater.inflate(R.layout.item_busimess,null); 
		holder = new ViewHolder(); 
		holder.tvname = (TextView)view.findViewById(R.id.item_busmess_textView);
		holder.tvpara = (EditText)view.findViewById(R.id.item_busmess_edit); 
		view.setTag(holder); 

		holder.tvpara.addTextChangedListener(new TextWatcher() {     
            @Override  
            public void afterTextChanged(Editable s) {  
                //将editText中改变的值设置的HashMap中  
            	mData.get(position).setMessage(s.toString());
            	Log.v("flowdetailadapter", "mdata change to "+s);
            }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {	
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}  
        });
		String s1="";
		//if(position==0)
		//{
			//s1=mData.get(position).getData().get("flownum");
			//holder.tvpara.setFocusable(false);
			//holder.tvpara.setEnabled(false);
		//}
		//else
		//{
			s1=mData.get(position).getData().get("parr"+position);
		//}
		holder.tvname.setText(s1);    			
		holder.tvpara.setText(mData.get(position).getMessage());
	    return view;  
	}
	
	public List<ReportItem> getmData() {
		return this.mData;
	}
	public void setmData(List<ReportItem> mData) {
		this.mData = mData;
	}

	class ViewHolder
	{  
	    public TextView tvname = null;  
	    public EditText tvpara = null;  
	} 
}
