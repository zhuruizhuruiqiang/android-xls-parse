package com.zr.fragment;
//todo flow修改
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zr.apprail.ApplicationTrans;
import com.zr.apprail.BusinessList;
import com.zr.apprail.Num;
import com.zr.apprail.R;
import com.zr.domain.Business;
import com.zr.domain.ProcessFlow;
import com.zr.domain.ReportItem;

import com.zr.thread.RecordWriteThread;
import com.zr.thread.ReportWriteThread;

import android.annotation.SuppressLint;
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
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ShowToast")
public class Fragment_businessflow extends Fragment implements OnClickListener,AdapterView.OnItemClickListener
{
	private Button businessmassage;
	private Button businessproblem;
	//private Button businessaddflow;
	private ImageView img_ret;
	private ListView businessflowlist;
	private TextView businessnum;
	//private View addflow;
	private MyAdapter adapter;
	ApplicationTrans applicationdata;
	private Business bus;
	private List<ReportItem> mData;
	private int busposition;
	private List<Integer> prob_to_thisprob;//排序用
	private List<ReportItem> mData_c;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
		ReportWriteThread t=new ReportWriteThread();
		t.run();
		RecordWriteThread t1=new RecordWriteThread();
		t1.run();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_businessflow, null);
		applicationdata=(ApplicationTrans) getActivity().getApplication();		
		
		busposition=applicationdata.getBusposition();
		applicationdata.getBusiness(busposition).setBusiness_flowData();

		mData=applicationdata.getBusiness(busposition).getBusiness_flowData();
		mData_c=new ArrayList<ReportItem>();
		prob_to_thisprob=new ArrayList<Integer>();
		getflowchange();
		adapter = new MyAdapter();
		
		businessnum=(TextView) view.findViewById(R.id.business_flow_textView_prolist);
		businessmassage=(Button) view.findViewById(R.id.business_flow_button_message);
		businessproblem=(Button) view.findViewById(R.id.business_flow_button_problem);
		//businessaddflow=(Button) view.findViewById(R.id.business_flow_button_addflow);
		img_ret= (ImageView) view.findViewById(R.id.business_flow_imageView_return);
		businessflowlist=(ListView) view.findViewById(R.id.business_flow_listView);
		//addflow=view.findViewById(R.id.business_flow_addlist_view);
		
		businessnum.setText("项目"+ Num.getString(busposition+1));
		businessmassage.setOnClickListener(this);
		businessproblem.setOnClickListener(this);
		//businessaddflow.setOnClickListener(this);
		img_ret.setOnClickListener(this);
		businessflowlist.setAdapter(adapter);
		businessflowlist.setOnItemClickListener(this);
        return view;
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {  
        case R.id.business_flow_button_message:  
        	((BusinessList) getActivity()).gotobusinesslist();
            break;  
        case R.id.business_flow_button_problem:
        	((BusinessList) getActivity()).gotobusinessproblem();
        	break;
        /*case R.id.business_flow_button_addflow:
        	dotoaddflow();
        	break;*/
        case R.id.business_flow_imageView_return:
        	((BusinessList) getActivity()).gotobusinesslistactivity(applicationdata.getWorkposition());
        	break;
        default:  
            break;  
        }
	}


	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		applicationdata.setFlowchoose(true);
		applicationdata.setFlowposition(prob_to_thisprob.get(i));
		((BusinessList) getActivity()).gotobusinessflowpara();

	}

	/*private void dotoaddflow() {
		addflow.setVisibility(View.VISIBLE);
		
		addflow.findViewById(R.id.business_flow_addlist_button).setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v) {
				EditText et=(EditText) addflow.findViewById(R.id.business_flow_addlist_editText);
				String s=et.getText().toString();
				
				if(!TextUtils.isEmpty(s))
				{
					Role role= Role.getRole(applicationdata.getRole());
					ProcessFlow flow=new ProcessFlow(role);
					flow.setFlow_description(s);
					flow.setFlow_num(applicationdata.generateflownum(busposition));
					applicationdata.getBusiness(busposition).addBusiness_flow(flow);
					//mData=applicationdata.getBusiness(busposition).getBusiness_flowData();
					//getflowchange();
					//adapter.notifyDataSetChanged();
					et.setText("");
					addflow.setVisibility(View.GONE);
					((BusinessList) getActivity()).gotobusinessflow();
				}
			}
		});		
	}*/
	
	public void getflowchange()
	{
		Thread t=new Thread()
				{
					@Override
					public void run() {
						int length=mData.size();
						for(int i=0;i<length;i++)
						{
							addflowtochange(i);//判断插入到哪个位置
						}						
					}			
				};
		t.run();
		
	}
	
	public void addflowtochange(int index)
	{
		
		if(mData.get(index).getTime().equals("------")||prob_to_thisprob.size()==0)//business没有流程或者要加入的流程时间没有，直接加到后面
		{
			prob_to_thisprob.add(index);
		}
		
		else//business有流程并且要加入的流程有流程时间
		{
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			try {
				Date d1=df.parse(mData.get(index).getTime());//要加入的流程时间
				
				int length=prob_to_thisprob.size();//判断处于当前business流程的什么位置
				int j=0;
				for(;j<length;j++)
				{
					int index_1=prob_to_thisprob.get(j);
					
					if(mData.get(index_1).getTime().equals("------"))//如果碰到了第一个没有时间的位置，就放在这个位置上
					{
						prob_to_thisprob.add(j, index);
						j=length+2;
					}
					else
					{
						Date d2=df.parse(mData.get(index_1).getTime());//要比较的流程时间
						if(d2.compareTo(d1)>0)
						{
							prob_to_thisprob.add(j, index);
							j=length+2;
						}
					}
				}
				if(j==length)
				{
					prob_to_thisprob.add(j, index);
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		
		
	}

	@SuppressLint("ShowToast")
	/*
	 * 内部list适配器类
	 */
	class MyAdapter extends BaseAdapter
	{
		//HashMap<Integer,View> map = new HashMap<Integer,View>();
		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(prob_to_thisprob.get(position));
			//return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
		    ViewHolder holder = null; 
		    MyListener myListener=null; 
		    LayoutInflater mInflater = (LayoutInflater) getActivity()  
		                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    view = mInflater.inflate(R.layout.item_busiflow, null);
			if(convertView==null)
			{
				holder = new ViewHolder();

				holder.tv = (TextView)view.findViewById(R.id.item_busflow_textView_flow);
				holder.time =(Button) view.findViewById(R.id.item_busflow_button_time);
				holder.im=(ImageView) view.findViewById(R.id.item_busflow_imageView);
				holder.time.setText(mData.get(prob_to_thisprob.get(position)).getTime());
				myListener=new MyListener(position);
				holder.im.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						applicationdata.setFlowchoose(true);
						applicationdata.setFlowposition(prob_to_thisprob.get(position));
						((BusinessList) getActivity()).gotobusinessflowpara();
					}

				});
				holder.time.setOnClickListener(myListener);

				view.setTag(holder);
			}
			else
			{
				view = convertView;
				holder = (ViewHolder)view.getTag();
			}

			if(!mData.get(prob_to_thisprob.get(position)).getParrname().equals(applicationdata.getRole()))
			{
				view.setBackgroundResource(R.color.darkgray);
			}
			String s=mData.get(prob_to_thisprob.get(position)).getMessage();
			if(s.indexOf("(")>0)
			{
				s=s.substring(0,s.indexOf("("));
			}
		    holder.tv.setText(s);
		    return view;  
		}
		    
		public final class ViewHolder
	    {  
		    public TextView tv = null;  
		    public Button time = null; 
		    public ImageView im = null;
		}
		
		 //@SuppressLint("ShowToast")
		private class MyListener implements OnClickListener{  
			 
			 private int mPosition;
			 private View v;
			 private boolean waitDouble = true;  
			 private static final int DOUBLE_CLICK_TIME = 350; //两次单击的时间间隔  
			 private long mlastclicktime=System.currentTimeMillis();
			 
			 public MyListener(int inPosition){ 
	                mPosition= inPosition;  
	            }
			 
			 @SuppressLint("ShowToast")
			@Override  
		      public void onClick(View v)   
		      {  
				  this.v=v;
				  singleClick();
				 /*if ( waitDouble == true )
		         {  
		            waitDouble = false;  
		            Thread thread = new Thread() {
		            	
		               @Override  
		               public void run() {  
		                  try {  
		                     sleep(DOUBLE_CLICK_TIME);  
		                     if ( waitDouble == false ) {  
		                        waitDouble = true;  
		                        singleClick();  
		                     }  
		                  } catch (InterruptedException e) {  
		                     e.printStackTrace();  
		                  }  
		               }  
		            };  
		            thread.run();  
		         }
		         else {  
		            waitDouble = true;  
		            doubleClick();  
		         }//*/
		      }
			 
		   // 单击响应事件  
		   private void singleClick(){  
			   // Log.d("businessflow", "singleClick="+mPosition);
           		final Button b=(Button) v;
    			String s=b.getText().toString();
    			if(s.equals("------"))
    			{
    				//时间变换
    				Date date=new Date();
    				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    				b.setText("------");
    				applicationdata.getBusiness(busposition).getBusiness_flow(prob_to_thisprob.get(mPosition)).setFlow_time();
    				//applicationdata.getBusiness(busposition).setBusiness_flowData();
    				//applicationdata.getBusiness(busposition).setBusiness_mData();
    				mData.get(prob_to_thisprob.get(mPosition)).setTime(df.format(date));
    			}
    			else
    			{
    				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
					builder.setTitle("提示"); //设置标题  
			        builder.setMessage("是否删除时间?"); //设置内容  
			        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
			        { //设置确定按钮  
			  
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							b.setText("------");
		     				applicationdata.getBusiness(busposition).getBusiness_flow(prob_to_thisprob.get(mPosition)).setFlow_time(null);
		     				//applicationdata.getBusiness(busposition).setBusiness_flowData();
		     				//applicationdata.getBusiness(busposition).setBusiness_mData();
		     				mData.get(prob_to_thisprob.get(mPosition)).setTime("------");
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
    			Log.d("FRAG_BUSFLOW", "time change");
    			b.setText(mData.get(prob_to_thisprob.get(mPosition)).getTime());
		   }  
		     
		   // 双击响应事件  
		   private void doubleClick(){  
			   	//Log.d("businessflow", "doubleClick="+mPosition);
          	   	final Button b=(Button) v;
          	   	Date date=new Date();
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
				applicationdata.getBusiness(busposition).getBusiness_flow(prob_to_thisprob.get(mPosition)).setFlow_time();
				//applicationdata.getBusiness(busposition).setBusiness_flowData();
				//applicationdata.getBusiness(busposition).setBusiness_mData();
				mData.get(prob_to_thisprob.get(mPosition)).setTime(df.format(date));
				b.setText(mData.get(prob_to_thisprob.get(mPosition)).getTime());
		   }
		 }; 
	}
}

