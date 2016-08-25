package com.zr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.R;

/**
 * Created by jimubox on 16/8/25.
 */
public class SelectProjectAdapter extends BaseAdapter
{

    ApplicationTrans applicationdata;
    private Context mContext = null;//上下文
    private LayoutInflater mInflater = null;

    public void SelectProjectAdapter(Context mContext)
    {
        applicationdata=ApplicationTrans.getInstance();
        this.mContext=mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }
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
            mInflater = (LayoutInflater) mContext
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
