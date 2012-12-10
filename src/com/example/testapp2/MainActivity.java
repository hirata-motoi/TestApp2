package com.example.testapp2;

import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn = (Button)findViewById(R.id.button01_id);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("MainActivity", "button1 clicked");
				// TODO Auto-generated method stub
				// インテントのインスタンス生成
				Intent intent = new Intent(MainActivity.this, TextToSpeechActivity.class);
				// 次画面のアクティビティ起動
				startActivity(intent);
			}
		});
		Button btn2 = (Button)findViewById(R.id.button1);
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// インテントのインスタンス生成
				Intent intent = new Intent(MainActivity.this, MainActivity2.class);
				// 次画面のアクティビティ起動
				startActivity(intent);
			}
		});
		Button btn3 = (Button)findViewById(R.id.button3);
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("button3", "button3 clicked");
			}
			
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
