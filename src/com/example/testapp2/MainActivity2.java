package com.example.testapp2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

public class MainActivity2 extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("MainActivity2", "onCreate started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		Button btn = (Button)findViewById(R.id.button2);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// インテントのインスタンス生成
				Intent intent = new Intent(MainActivity2.this, TextToSpeechActivity.class);
				// 次画面のアクティビティ起動
				startActivity(intent);
			}
		});
	}
}
