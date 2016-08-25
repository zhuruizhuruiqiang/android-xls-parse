package com.zr.fragment;
//8月22日checkOK
import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.BusinessList;
import com.zr.apprail.R;
import com.zr.domain.Business;
import com.zr.dslv.DragSortListView;

import com.zr.thread.RecordWriteThread;
import com.zr.thread.ReportWriteThread;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_businesslist extends Fragment implements OnClickListener,OnItemClickListener{

	private DragSortListView businesslistview;		
	private Button loadstafile;
	private Button saveload;
	private Button report;
	private Button addbusi;
	private  MyListAdapter adapter;
	ApplicationTrans applicationdata;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("fragment_businesslist", "onCreate");  
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("fragment_businesslist", "onPause");
	}

	private DragSortListView.DropListener onDrop =
	        new DragSortListView.DropListener() {
	            @Override
	            public void drop(int from, int to) {
	            	Log.d("fragment_businesslist", "from="+from);
	            	Log.d("fragment_businesslist", "to="+to);
	            	int oldbusposition=applicationdata.getBusposition();
	            	int newbusposition=oldbusposition;
	            	Log.d("fragment_businesslist", "oldbusposition="+oldbusposition);
	            	if(from!=to)
	            	{
	            		businesslistview.getChildAt(oldbusposition).setBackgroundResource(R.color.white);
		            	Business item = applicationdata.getBusiness(from);
	            		applicationdata.getBusinesslist().remove(from);
	            		applicationdata.getBusinesslist().add(to,item);
	            		//判断新的busposition去了哪里
	            		if(from==oldbusposition)
	            		{//说明当前进行的位置发生了改变
	            			newbusposition=to;
	            			Log.d("fragment_businesslist","from==oldbusposition");
	            		}
	            		else//当前挪动的是当前进行的项目之前的项目
	            		if(from<oldbusposition)
	            		{
	            			if(to<oldbusposition)
	            			{
	            				//newbusposition=oldbusposition;
	            				Log.d("fragment_businesslist","from<oldbusposition to<oldbusposition");
	            			}
	            			else
	            			{
	            				Log.d("fragment_businesslist","from<oldbusposition to>=oldbusposition");
	            				newbusposition=oldbusposition-1;
	            			}
	            		}
	            		else//当前挪动的是当前进行的项目之后的项目
	            		{
	            			if(to>oldbusposition)
	            			{
	            				//newbusposition=to;
	            				Log.d("fragment_businesslist","from>oldbusposition to>oldbusposition");
	            			}
	            			else
	            			{
	            				Log.d("fragment_businesslist","from>oldbusposition to<=oldbusposition");
		            			newbusposition=oldbusposition+1;
	            			}
	            		}
	            		Log.d("fragment_businesslist", "newbusposition="+newbusposition);
		            	applicationdata.setBusposition(newbusposition);
		            	businesslistview.getChildAt(newbusposition).setBackgroundResource(R.color.blanchedalmond);
		            	//写入文件记录
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
		            	adapter.notifyDataSetChanged();		            	
	            	}
	            }
	        };
	        
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_businesslist, null);
		Log.d("fragment_businesslist", "onCreateView");
		loadstafile=(Button) view.findViewById(R.id.businesslist_button_load);
		saveload=(Button) view.findViewById(R.id.businesslist_button_save);
		report=(Button) view.findViewById(R.id.businesslist_button_report);
		addbusi=(Button) view.findViewById(R.id.businesslist_button_addbusines);
		businesslistview=(DragSortListView) view.findViewById(R.id.businesslist_listView);
		applicationdata=(ApplicationTrans) getActivity().getApplication();	
		loadstafile.setOnClickListener(this);		
		saveload.setOnClickListener(this);
		report.setOnClickListener(this);
		addbusi.setOnClickListener(this);
		
		adapter = new MyListAdapter();
		businesslistview.setDropListener(onDrop);
        businesslistview.setAdapter(adapter); 
        Log.d("fragment_businesslist", "setadapter");
		businesslistview.setOnItemClickListener(this);
			
        return view;
    }
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{ 
		int oldbusposition=applicationdata.getBusposition();
		if(applicationdata.isWorkchoose())
		{
			/*
			 * 保存当前work的businesslist
			 */
			int workposition=applicationdata.getWorkposition();
			String savename=applicationdata.getWork(applicationdata.getWorkposition()).getWork_name();
			String path=applicationdata.getConfiledir()+"/"+applicationdata.string_worklist;;
			applicationdata.writeworklist(path,savename);
			applicationdata.setBuschoose(true);
			applicationdata.setBusposition(position);
			Log.d("fra_buslist", "busposition set"+applicationdata.getBusposition());
			((BusinessList) getActivity()).gotobusinesslist();
			}
		else
		{
			((BusinessList) getActivity()).gotosaveload();
		}
		adapter.notifyDataSetChanged();
		//businesslistview.getChildAt(oldbusposition).setBackgroundResource(color.white);
		//businesslistview.getChildAt(position).setBackgroundResource(R.color.blanchedalmond);	
	}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              

	@Override
	public void onClick(View v) {
		switch (v.getId()) {  
        case R.id.businesslist_button_load:  
        	((BusinessList) getActivity()).gotoloadworklist();
            break;  
        case R.id.businesslist_button_save:  
        	((BusinessList) getActivity()).gotosaveload();
            break;
        case R.id.businesslist_button_report:
        	((BusinessList) getActivity()).gotoreport();
        	break;
        case R.id.businesslist_button_addbusines:
        	((BusinessList) getActivity()).gotoaddbusi();
        	break;
        default:  
            break;  
        }  
	}
	/*
	 * 内部list适配器类
	 */
	class MyListAdapter extends BaseAdapter 
	{ 		
		@Override
		public int getCount() {
			//获取元素数量，系统调用，用来获知集合中有多少条元素
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
		    Log.d("fragment_businesslist","getview"+position);
		    //System.out.println("select project adapter View getView");
		    if (convertView == null) 
		    {
		        LayoutInflater mInflater = (LayoutInflater) getActivity()  
		                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        view = mInflater.inflate(R.layout.item_businesslist, null);
		        holder = new ViewHolder();
		        holder.tv = (TextView)view.findViewById(R.id.businesslistitem_textView_businessname);
		        holder.im =(ImageView)view.findViewById(R.id.businessitem_button_delete);
		        final int p = position;  
		        holder.im.setOnClickListener(new OnClickListener() {
	                  
	                @Override  
	                public void onClick(final View v) {
	                	AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
						builder.setTitle("提示"); //设置标题  
				        builder.setMessage("是否删除项目?"); //设置内容  
				        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
				        { //设置确定按钮  
				  
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{	                	
			                	applicationdata.getBusinesslist().remove(p);
			                	Log.d("fragment_businesslist","delete p="+p);
			                	
			                	//改变当前进行的项目位置
			                	int oldbusposition=applicationdata.getBusposition();
			                	Log.d("fragment_businesslist","old position="+oldbusposition);
			                	if(p<oldbusposition)
			                	{
			                		applicationdata.setBusposition(oldbusposition-1);
			                	}
			                	if(p==oldbusposition&&p==applicationdata.getBusinesslist().size())
			                	{
			                		if(applicationdata.getBusinesslist().size()>0)
			                		{
			                			applicationdata.setBusposition(applicationdata.getBusinesslist().size()-1);
			                		}
			                		else
			                		{
			                			applicationdata.setBusposition(0);
			                		}
			                	}
			                	Log.d("fragment_businesslist","new position="+applicationdata.getBusposition());
			                	
			                	//写入文件记录
			                	if(applicationdata.isWorkchoose())
			        			{
			        				String savename=applicationdata.getWork(applicationdata.getWorkposition()).getWork_name();
			        				String path=applicationdata.getConfiledir()+"/"+applicationdata.string_worklist;;
			        				applicationdata.writeworklist(path,savename);
			        			}
			                	//adapter.notifyDataSetChanged();
			                	
			                	//判断右边页面是否要变化
			                	((BusinessList) getActivity()).gotobusinesslistactivity(applicationdata.getWorkposition());
							}
				        });
				        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() 
						{
					     @Override
					         public void onClick(DialogInterface dialog, int which)
					     	 {
					    	    
					         }		  
					     });
						builder.show();//在按键响应事件中显示此对话框  
	                }  
	            }); 
		        view.setTag(holder); 
		        Log.d("fragment_businesslist", "mapput position");
		    }
		    else
		    {       
		        view = convertView;  
		        holder = (ViewHolder)view.getTag();  
		        Log.d("fragment_businesslist", "mapget position");
		    }
		    int line=applicationdata.getBusinesslist().get(position).getBusiness_projects().size();
			holder.tv.setLines(line);
		    String s=applicationdata.getBusinesslist().get(position).getBusiness_name();
		    Log.d("fragment_businesslist", "busname"+s);
		    String []name=s.split("&&");
		    String namea="";
			for(String ss:name)
			{
				namea+=ss+"\r\n";					
			}			
		    holder.tv.setText(namea);
		    if(applicationdata.getBusposition()==position)
	    	{
	    		view.setBackgroundResource(R.color.blanchedalmond);
	    	}
		    else
		    {
		    	view.setBackgroundResource(R.color.white);
		    }
		    return view;  
		}
		    
		class ViewHolder
	    {  
		    public TextView tv = null;  
		    public ImageView im = null;  
		}
	}
}
