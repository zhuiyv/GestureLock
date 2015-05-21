package com.android.gesturelock;

import com.android.view.GestureLockView;
import com.android.view.GestureLockView.OnGestureFinishListener;
import com.example.gesturelock.R;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ChangePwd extends Activity implements OnClickListener{
	private Button btnConfirm;
	private SharedPreferences sp;
	private GestureLockView changeGv;
	private String result;
	public ChangePwd() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changepwd);
		changeGv = (GestureLockView) findViewById(R.id.changeGv);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);
		sp = this.getSharedPreferences("config", MODE_PRIVATE);
		changeGv.setIsSetting(true);
		changeGv.setOnGestureFinishListener(new OnGestureFinishListener() {
			@Override
			public void OnGestureFinish(boolean result) {
				
			}
			@Override
			public void OnSettingFinish(String str) {
				result = str;
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnConfirm:
			Editor editor = sp.edit();
			editor.putString("pwd", result);
			editor.commit();
			Toast.makeText(ChangePwd.this, "ÐÞ¸ÄÍê³É",
					Toast.LENGTH_SHORT).show();
			this.finish();
			break;

		default:
			break;
		}
		
	}

}
