package com.zr.fragment;
//8月22日checkOK


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.BusinessList;
import com.zr.apprail.R;

public class Fragment_saveload extends Fragment implements OnClickListener{

	private EditText saveload;
	private Button buttonok;
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
		View view = inflater.inflate(R.layout.fragment_saveload, null);
		applicationdata=(ApplicationTrans) getActivity().getApplication();		
		buttonok=(Button) view.findViewById(R.id.saveload_button_ok);
		saveload=(EditText) view.findViewById(R.id.saveload_editText_savename);
		
		buttonok.setOnClickListener(this);
		String workname="";
		if(applicationdata.isWorkchoose())
		{
			String name=applicationdata.getWork(applicationdata.getWorkposition()).getWork_name();
			name=name.substring(name.indexOf("_")+1,name.length());//去掉前面的司机_朱瑞_
	        name=name.substring(name.indexOf("_")+1,name.length());
	        workname=name.substring(0,name.indexOf("."));//去掉后面的文件类型
		}
		saveload.setText(workname);	
        return view;
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {  
        case R.id.saveload_button_ok:
        	String filename=saveload.getText().toString();
        	((BusinessList) getActivity()).saveload(filename);
        	break;
        default:  
            break;  
        } 	
	}
}
