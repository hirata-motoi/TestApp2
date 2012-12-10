package com.example.testapp2;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TextToSpeechActivity extends Activity implements TextToSpeech.OnInitListener {
		TextToSpeech tts;
		EditText et;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	tts = new TextToSpeech(this,this);
	 
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.texttospeechactivity);
	        Log.d("TextToSpeechActivity", "speech create started");

			Button btn = (Button)findViewById(R.id.button4);
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("TextToSpeechActivity", "button4 clicked");
					et = (EditText)findViewById(R.id.editText1);
					
					
					tts.speak(et.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
					
					//Intent intent = new Intent(TextToSpeechActivity.this, TextToSpeechActivity.class);
					// 次画面のアクティビティ起動
					//startActivity(intent);
				}
			});

	        //tts = new TextToSpeech(this,this);
	    }
	    public void onInit(int status) {
	    }
/*
	    public void onInit(int status){

	    	Log.d("TextToSpeechActivity", "speech init started");
	        Locale loc = new Locale("en",",");
	        if(tts.isLanguageAvailable(loc)>= TextToSpeech.LANG_AVAILABLE){
	            tts.setLanguage(loc);
	        }
	        tts.speak("Hello world", TextToSpeech.QUEUE_FLUSH, null);
	    }
	    @Override
	    protected void onDestroy(){
	    	super.onDestroy();
	    	tts.shutdown();
	    }
*/
}
