package com.zr.fragment;
//8月22日checkOK

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.BusinessList;
import com.zr.apprail.R;


public class Fragment_loadworklist extends Fragment implements OnClickListener{

	private ListView stafilelistview;
	private Button buttonok;
	private ArrayAdapter<String> adapter;
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
		View view = inflater.inflate(R.layout.fragment_loadworklist, null);
		applicationdata=(ApplicationTrans) getActivity().getApplication();		
		
		stafilelistview=(ListView) view.findViewById(R.id.loadstafile_listView);
		buttonok=(Button) view.findViewById(R.id.loadstafile_button_ok);
		
		String[] worklist=new String[applicationdata.getWorklist().size()];
		for(int i=0;i<worklist.length;i++)
		{
			String name=applicationdata.getWork(i).getWork_name();
			name=name.substring(name.indexOf("_")+1,name.length());//去掉前面的司机_朱瑞_
	        name=name.substring(name.indexOf("_")+1,name.length());
	        worklist[i]=name.substring(0,name.indexOf("."));//去掉后面的文件类型
		}
		
		if(worklist!=null&&worklist.length>0)
		{
			adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,worklist);
			stafilelistview.setAdapter(adapter); 
			stafilelistview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			if(applicationdata.isWorkchoose())
			{
				stafilelistview.setItemChecked(applicationdata.getWorkposition(), true);
			}
		}
		
		buttonok.setOnClickListener(this);
		return view;
    }
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {  
        case R.id.loadstafile_button_ok:
        	int workposition=stafilelistview.getCheckedItemPosition();
        	if(workposition>=0)
        	{
        		applicationdata.setWorkchoose(true);
            	applicationdata.setWorkposition(workposition);
            }
        	Log.d("loadworklist","workposition="+workposition);
        	((BusinessList) getActivity()).gotobusinesslistactivity(workposition);
        	break;
        default:  
            break;  
        } 		
	}
}
