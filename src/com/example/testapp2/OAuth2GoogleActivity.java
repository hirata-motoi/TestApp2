package com.example.testapp2;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.ViewSwitcher;


public class OAuth2GoogleActivity extends Activity {

	  public static final int REQCODE_OAUTH = 0;
	  public static final String CLIENT_ID = "422323033304.apps.googleusercontent.com";
	  public static final String CLIENT_SECRET = "";
	  public static final String SCOPE = "https://www.google.com/reader/api";
	  public static final String ACCESS_TOKEN = "access_token";

	  protected String clientId;
	  protected String clientSecret;
	  protected String scope;

	  protected ViewSwitcher vs;
	  protected TextView tvProg;
	  protected WebView wv;


	  @Override
	  public void onCreate(Bundle savedInstanceState) {
Log.v("onClickBtn", "create oauth2googleactivity1");
	    // ����
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.oauth2google);
Log.v("onClickBtn", "create oauth2googleactivity2");

	    // Intent����p�����[�^�擾
	    Intent intent = getIntent();
	    clientId = intent.getStringExtra(CLIENT_ID);
	    clientSecret = intent.getStringExtra(CLIENT_SECRET);
	    scope = intent.getStringExtra(SCOPE);
Log.v("onClickBtn", "create oauth2googleactivity3");
	    // �e��View���擾
	    vs = (ViewSwitcher) findViewById(R.id.vs);
	    tvProg = (TextView) findViewById(R.id.tvProg);
	    wv = (WebView) findViewById(R.id.wv);
Log.v("onClickBtn", "create oauth2googleactivity4");
	    // WebView�ݒ�
		wv.getSettings().setJavaScriptEnabled(true);
	    wv.setWebViewClient(new WebViewClient() { // ��������Ȃ��ƃA�h���X�o�[�Ȃǂ��o��
	    


	      @Override
	      public void onPageFinished(WebView view, String url) { // �y�[�W�ǂݍ��݊�����

	        // �y�[�W�^�C�g������R�[�h���擾
	        String title = view.getTitle();
	        if (title == null) {
	        		title = "prepared_title";
	        }
	        String code = getCode(title);

	        // �R�[�h�擾�����y�[�W�ȊO
	        if (code == null) {
	          Log.v("onPageFinished", "�R�[�h�擾�����y�[�W�ȊO url=" + url);
	          if (!(vs.getCurrentView() instanceof WebView)) { // WebView���\������ĂȂ�������
	            vs.showNext(); // Web�F�؉�ʕ\��
	          }
	        }

	        // �R�[�h�擾����
	        else {
	          Log.v("onPageFinished", "�R�[�h�擾���� code=" + code);
	          vs.showPrevious(); // �v���O���X��ʂɖ߂�
	          new TaskGetAccessToken().execute(code); // �A�N�Z�X�g�[�N���擾�J�n
	        }

	      }
	    });

	    // �F�؃y�[�WURL
	    String url = "https://accounts.google.com/o/oauth2/auth" // �����ɓ����邱�ƂɂȂ��Ă�
	        + "?client_id=" + clientId // �A�v���P�[�V�����o�^���Ă������
	        + "&response_type=code" // InstalledApp���Ƃ��̒l�ŌŒ�
	        + "&redirect_uri=urn:ietf:wg:oauth:2.0:oob" // �^�C�g����code��\������ꍇ�͌Œ�
	        + "&scope=" + URLEncoder.encode(scope); // ���𓾂����T�[�r�X

	    Log.v("onCreate", "clientId=" + clientId + " clientSecret=" + clientSecret + " scope=" + scope + " url=" + url);

	    // �F�؃y�[�W���[�h�J�n
	    wv.loadUrl(url);

	  }


	  /**
	   * �F�ؐ����y�[�W�̃^�C�g���́uSuccess code=XXXXXXX�v�Ƃ������ɂȂ��Ă���̂ŁA
	   * ���̃^�C�g������ucode=�v�ȉ��̕�����؂�o����OAuth2�A�N�Z�X�R�[�h�Ƃ��ĕԂ�
	   * 
	   * @param title
	   *          �y�[�W�^�C�g��
	   * @return OAuth2�A�N�Z�X�R�[�h
	   */

	  protected String getCode(String title) {
	    String code = null;
	    String codeKey = "code=";
Log.v("onClickBtn", "getCode1");
	    int idx = title.indexOf(codeKey);
Log.v("onClickBtn", "getCode2");
	    if (idx != -1) { // �F�ؐ����y�[�W������
Log.v("onClickBtn", "getCode3");
	      code = title.substring(idx + codeKey.length()); // �ucode�v��؂�o��
Log.v("onClickBtn", "getCode4");
	    }
	    return code;
	  }


	  // �A�N�Z�X�g�[�N���擾�^�X�N
	  protected class TaskGetAccessToken extends AsyncTask<String, Void, String> {


	    @Override
	    protected void onPreExecute() {
	      Log.v("onPostExecute", "�A�N�Z�X�g�[�N���擾�J�n");
	      tvProg.setText("�A�N�Z�X�g�[�N�����擾��...");
	    }


	    @Override
	    protected String doInBackground(String... codes) {
	      String token = null;
	      DefaultHttpClient client = new DefaultHttpClient();
	      try {

	        // �p�����[�^�\�z
	        ArrayList<NameValuePair> formParams = new ArrayList<NameValuePair>();
	        formParams.add(new BasicNameValuePair("code", codes[0]));
	        formParams.add(new BasicNameValuePair("client_id", clientId));
	        formParams.add(new BasicNameValuePair("client_secret", clientSecret));
	        formParams.add(new BasicNameValuePair("redirect_uri", "urn:ietf:wg:oauth:2.0:oob"));
	        formParams.add(new BasicNameValuePair("grant_type", "authorization_code"));

	        // �g�[�N���̎擾��POST�ōs�����ƂɂȂ��Ă���
	        HttpPost httpPost = new HttpPost("https://accounts.google.com/o/oauth2/token");
	        httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8")); // �p�����[�^�Z�b�g
	        HttpResponse res = client.execute(httpPost);
	        HttpEntity entity = res.getEntity();
	        String result = EntityUtils.toString(entity);

	        // JSONObject�擾
	        JSONObject json = new JSONObject(result);
	        if (json.has("access_token")) {
	          token = json.getString("access_token");
	        } else {
	          if (json.has("error")) {
	            String error = json.getString("error");
	            Log.d("getAccessToken", error);
	          }
	        }

	      } catch (ClientProtocolException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      } catch (JSONException e) {
	        e.printStackTrace();
	      } finally {
	        client.getConnectionManager().shutdown();
	      }
	      return token;
	    }


	    @Override
	    protected void onPostExecute(String token) {
	      if (token == null) {
	        Log.v("onPostExecute", "�A�N�Z�X�g�[�N���擾���s");
	      } else {
	        Log.v("onPostExecute", "�A�N�Z�X�g�[�N���擾���� token=" + token);
	        Intent intent = new Intent();
	        intent.putExtra(ACCESS_TOKEN, token);
	        setResult(Activity.RESULT_OK, intent);
	      }
	      finish();
	    }

	  } // END class TaskGetAccessToken
	} // END class OAuth2GoogleActivity
