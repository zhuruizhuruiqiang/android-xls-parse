package com.zr.apprail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.zr.domain.Role;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView versionname;
    private EditText editname;
    private Spinner role_sel;
    private Button button;
    ApplicationTrans applicationdata;
    private Role role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        versionname=(TextView) findViewById(R.id.textView_appversion);
        editname=(EditText) findViewById(R.id.editText_recordpersonname);
        role_sel=(Spinner) findViewById(R.id.spinner1);
        button=(Button)this.findViewById(R.id.button_enterintomain);
        button.setOnClickListener(this);
        applicationdata=(ApplicationTrans) getApplication();
        versionname.setText(applicationdata.getStandardFileVersion());
        editname.setText(applicationdata.getPersonname());
        role_sel.setSelection(0);
    }
    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        //进入到新的activity,一般由主页面进入到项目列表页面
        intent=new Intent(this,BusinessList.class);
        Bundle bundle=new Bundle();
        bundle.putString("nextpage", "SelectProject");
        intent.putExtras(bundle);
        //传递参数
        String name=editname.getText().toString();
        setPersonname(name.trim());
        String role=role_sel.getSelectedItem().toString();
        applicationdata.setRole(role);
        applicationdata.loadstaxls();
        //使用意图打开新的activity
        startActivityForResult(intent,1);
    }

    /*
     * getters and setters
     */
    public void setPersonname(String personname) {
        String info="personname##"+personname;
        applicationdata.addinfo("personname",info);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
