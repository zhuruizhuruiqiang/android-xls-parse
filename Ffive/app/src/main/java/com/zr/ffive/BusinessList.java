package com.zr.ffive;

import java.io.File;

import com.zr.domain.Work;
import com.zr.fragment.Fragment_busflowpara;
import com.zr.fragment.Fragment_busflowprob;
import com.zr.fragment.Fragment_busflowvideo;
import com.zr.fragment.Fragment_businessflow;
import com.zr.fragment.Fragment_businesslist;
import com.zr.fragment.Fragment_businessmessage;
import com.zr.fragment.Fragment_businessproblem;
import com.zr.fragment.Fragment_loadworklist;
import com.zr.fragment.Fragment_report;
import com.zr.fragment.Fragment_saveload;
import com.zr.fragment.Fragment_selectproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BusinessList extends Activity //OnClickListener,OnItemClickListener
{
	ApplicationTrans applicationdata;
	private String FILE_PATH;
	private String fileName;
	/**
	 * 页面显示跳转
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_businesslist);
		applicationdata = (ApplicationTrans) getApplication();
		Log.d("buslist2", "oncreat");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String nextpage = bundle.getString("nextpage");
		Log.d("buslist2nextpage", nextpage);
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

        Fragment_businesslist fragment=new Fragment_businesslist();
        transaction.add(R.id.fragment_buslist,fragment);

		switch (nextpage) {
			case "LoadWorkList":
				Log.d("buslist2", "LoadWorkList");
				Fragment_loadworklist fragment1 = new Fragment_loadworklist();
				transaction.add(R.id.fragment_detail, fragment1);
				break;
			case "SaveLoad":
				Fragment_saveload fragment2 = new Fragment_saveload();
				transaction.add(R.id.fragment_detail, fragment2);
				break;
			case "Report":
				Fragment_report fragment3 = new Fragment_report();
				transaction.add(R.id.fragment_detail, fragment3);
				break;
			case "SelectProject":
				Fragment_selectproject fragment4 = new Fragment_selectproject();
				transaction.add(R.id.fragment_detail, fragment4);
				break;
			case "BusinessActivity":
				Fragment_businessmessage fragment5 = new Fragment_businessmessage();
				transaction.add(R.id.fragment_detail, fragment5);
				break;
			default:
                Fragment_selectproject fragment6 = new Fragment_selectproject();
                transaction.add(R.id.fragment_detail, fragment6);
                break;
		}

		transaction.addToBackStack(null);
		transaction.commit();
    }

	/*
	 * fragment变换
	 */
	public void gotoloadworklist() {
		Log.d("buslist", "gotoloadworklist");
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment_loadworklist fragment = new Fragment_loadworklist();
		transaction.replace(R.id.fragment_detail, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void gotosaveload() {
		Log.d("buslist", "gotosaveload");
		int i = applicationdata.getBusinesslist().size();
		if (i == 0) {
			Toast.makeText(this,"您没有选择任何项目，请先选择项目", Toast.LENGTH_SHORT).show();
		} else {
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();

			Fragment_saveload fragment = new Fragment_saveload();
			transaction.replace(R.id.fragment_detail, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}

	public void gotoreport() {
		Log.d("buslist", "gotoreport");
		if (applicationdata.getBusinesslist().size() == 0) {
			Toast.makeText(this, "您没有添加任何项目，请先添加项目", Toast.LENGTH_SHORT).show();
		} else {
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();

			Fragment_report fragment = new Fragment_report();
			transaction.replace(R.id.fragment_detail, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}

	public void gotoaddbusi() {
		Log.d("buslist", "gotoaddbusi");
		int i = applicationdata.getProjectlist().size();
		if (i == 0) {
			Toast.makeText(this, "请先加载文件", Toast.LENGTH_SHORT).show();
		} else {
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();

			Fragment_selectproject fragment = new Fragment_selectproject();
			transaction.replace(R.id.fragment_detail, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}

	/*
	 * 
	 */
	public void gotobusinesslist() {
		Log.d("buslist", "gotobusinesslist");
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment_businessmessage fragment = new Fragment_businessmessage();
		transaction.replace(R.id.fragment_detail, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void gotobusinessflow() {
		Log.d("buslist", "gotobusinessflow");
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment_businessflow fragment = new Fragment_businessflow();
		transaction.replace(R.id.fragment_detail, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void gotobusinessproblem() {
		Log.d("buslist", "gotobusinessproblem");
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment_businessproblem fragment = new Fragment_businessproblem();
		transaction.replace(R.id.fragment_detail, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/*
	 * 
	 */
	public void gotobusinessflowpara() {
		Log.d("buslist", "gotobusinessflowpara");
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment_busflowpara fragment = new Fragment_busflowpara();
		transaction.replace(R.id.fragment_detail, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void gotobusinessflowprob() {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment_busflowprob fragment = new Fragment_busflowprob();
		transaction.replace(R.id.fragment_detail, fragment);
		transaction.addToBackStack(null);
		Log.d("buslist", "gotobusinessflowprob");
		transaction.commit();
	}

	public void gotobusinessflowvideo() {
		Log.d("buslist", "gotobusinessflowvideo");
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment_busflowvideo fragment = new Fragment_busflowvideo();
		transaction.replace(R.id.fragment_detail, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/*
	 * 左边fragment刷新
	 */
	public void gotobusinesslistactivity(int workpostion) {
		Log.d("buslist", "gotobusinesslistactivity");FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment_businesslist fragment = new Fragment_businesslist();
        transaction.replace(R.id.fragment_buslist, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
	}

	/*
	 * 方法
	 */
	public void saveload(String filename) {
		String role = applicationdata.getRole();
		String person = applicationdata.getPersonname();

		if (!filename.equals(""))//判断输入是否为空
		{
			final String savename = role + "_" + person + "_" + filename + ".txt";
			final String path = applicationdata.getConfiledir() + "/" + applicationdata.string_worklist;
			;
			final File file = new File(path, savename);
			//判定文件夹下是否存在该role以及editor存储的该filename文件，如果存在，则删除，然后再创建新的
			File fpath = new File(path);
			if (!fpath.exists()) {
				fpath.mkdirs();
			}
			Log.v("saveload", "path exists");
			if (fpath.list().length > 0) {
				for (int i = 0; i < fpath.list().length; i++) {
					final int index = i;
					final String ff = fpath.list()[i];
					String[] s = ff.split("_");
					Log.v("saveload", "worklist  i: " + ff);
					Log.v("saveload", "s2 " + s[2]);
					Log.v("saveload", "filename " + filename);
					if (s[0].equals(role) && s[1].equals(person) && s[2].equals(filename + ".txt")) {//如果保存的重名了
						Log.v("saveload", "重名");
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("提示"); //设置标题  
						builder.setMessage("已有同名列表，是否覆盖同名列表文件?"); //设置内容
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Log.v("saveload", "positive buttton");
								File oldfile = new File(path, ff);
								oldfile.delete();
								//addworktolist_fileWrite(savename, file);													
								applicationdata.writeworklist(path, savename);
								Work work = new Work(savename);
								work.setWork_businesslist(applicationdata.getBusinesslist());
								applicationdata.getWorklist().set(index, work);
								applicationdata.setWorkchoose(true);
								applicationdata.setWorkposition(index);
								dialog.dismiss();
								gotobusinesslistactivity(applicationdata.getWorkposition());
							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Log.v("saveload", "cancel");
							}
						});
						builder.show();//在按键响应事件中显示此对话框  
						i = fpath.list().length;//跳出循环
					} else {//如果到最后一个都没有重复
						if (i == fpath.list().length - 1) {
							applicationdata.writeworklist(path, savename);
							 /*
							 * 增加worklist
							 */
							Work work = new Work(savename);
							work.setWork_businesslist(applicationdata.getBusinesslist());
							applicationdata.addWorklist(work);
							applicationdata.setWorkchoose(true);
							applicationdata.setWorkposition(applicationdata.getWorklist().size() - 1);
							gotobusinesslistactivity(applicationdata.getWorkposition());
						}
					}
				}
			} else {
                applicationdata.writeworklist(path, savename);
                //addworktolist_fileWrite(savename, file);
				Work work = new Work(savename);
				work.setWork_businesslist(applicationdata.getBusinesslist());
				applicationdata.addWorklist(work);
				applicationdata.setWorkchoose(true);
				applicationdata.setWorkposition(applicationdata.getWorklist().size() - 1);
				gotobusinesslistactivity(applicationdata.getWorkposition());
			}
		} else {
			Toast.makeText(this, "请输入名字", Toast.LENGTH_SHORT).show();
		}
	}
/*
	public void addworktolist_fileWrite(final String savename, final File file) {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//获取文件列表
				StringBuilder sb = new StringBuilder();
				for (Business b : applicationdata.getBusinesslist()) {
					String s2 = "";
					for (Project p : b.getBusiness_projects()) {
						s2 += p.getProject_num() + "&" + p.getProject_name() + "+";
					}
					sb.append(s2.substring(0, s2.length() - 1) + "\r\n");
				}
				//写入文件
				try {
					OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
					BufferedWriter writer = new BufferedWriter(write);
					writer.write(sb.toString());
					writer.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.run();
	}
*/
	public String getFILE_PATH() {
		return FILE_PATH;
	}

	public void setFILE_PATH(String fILE_PATH) {
		FILE_PATH = fILE_PATH;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
