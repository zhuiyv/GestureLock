package com.android.gesturelock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.view.GestureLockView;
import com.android.view.GestureLockView.OnGestureFinishListener;
import com.example.gesturelock.R;

public class MainActivity extends Activity implements OnClickListener{
	GestureLockView gv;
	Button btnChange;
	String pwd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnChange = (Button) findViewById(R.id.btnChangePwd);
		btnChange.setOnClickListener(this);
		SharedPreferences sp = this.getSharedPreferences("config", MODE_PRIVATE);
		gv = (GestureLockView) findViewById(R.id.gv);
		if(sp != null){
			pwd = sp.getString("pwd", null);
		}
		gv.setOnGestureFinishListener(new OnGestureFinishListener() {

			@Override
			public void OnGestureFinish(boolean result) {
				if(pwd == null){
					Toast.makeText(MainActivity.this, "未检测到密码，请先设置",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(result){
					Toast.makeText(MainActivity.this, "验证成功！",
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(MainActivity.this, "验证失败！",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void OnSettingFinish(String result) {
				// TODO Auto-generated method stub
				
			}
		});

	}
	@Override
	protected void onResume() {
		SharedPreferences sp = this.getSharedPreferences("config", MODE_PRIVATE);
		gv = (GestureLockView) findViewById(R.id.gv);
		if(sp != null){
			pwd = sp.getString("pwd", null);
		}
		gv.setKey(pwd); 
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnChangePwd:
			Intent intent = new Intent(this, ChangePwd.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
