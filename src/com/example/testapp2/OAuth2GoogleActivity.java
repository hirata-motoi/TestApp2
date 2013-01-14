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
	    // お約束
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.oauth2google);
Log.v("onClickBtn", "create oauth2googleactivity2");

	    // Intentからパラメータ取得
	    Intent intent = getIntent();
	    clientId = intent.getStringExtra(CLIENT_ID);
	    clientSecret = intent.getStringExtra(CLIENT_SECRET);
	    scope = intent.getStringExtra(SCOPE);
Log.v("onClickBtn", "create oauth2googleactivity3");
	    // 各種Viewを取得
	    vs = (ViewSwitcher) findViewById(R.id.vs);
	    tvProg = (TextView) findViewById(R.id.tvProg);
	    wv = (WebView) findViewById(R.id.wv);
Log.v("onClickBtn", "create oauth2googleactivity4");
	    // WebView設定
		wv.getSettings().setJavaScriptEnabled(true);
	    wv.setWebViewClient(new WebViewClient() { // これをしないとアドレスバーなどが出る
	    


	      @Override
	      public void onPageFinished(WebView view, String url) { // ページ読み込み完了時

	        // ページタイトルからコードを取得
	        String title = view.getTitle();
	        if (title == null) {
	        		title = "prepared_title";
	        }
	        String code = getCode(title);

	        // コード取得成功ページ以外
	        if (code == null) {
	          Log.v("onPageFinished", "コード取得成功ページ以外 url=" + url);
	          if (!(vs.getCurrentView() instanceof WebView)) { // WebViewが表示されてなかったら
	            vs.showNext(); // Web認証画面表示
	          }
	        }

	        // コード取得成功
	        else {
	          Log.v("onPageFinished", "コード取得成功 code=" + code);
	          vs.showPrevious(); // プログレス画面に戻る
	          new TaskGetAccessToken().execute(code); // アクセストークン取得開始
	        }

	      }
	    });

	    // 認証ページURL
	    String url = "https://accounts.google.com/o/oauth2/auth" // ここに投げることになってる
	        + "?client_id=" + clientId // アプリケーション登録してもらった
	        + "&response_type=code" // InstalledAppだとこの値で固定
	        + "&redirect_uri=urn:ietf:wg:oauth:2.0:oob" // タイトルにcodeを表示する場合は固定
	        + "&scope=" + URLEncoder.encode(scope); // 許可を得たいサービス

	    Log.v("onCreate", "clientId=" + clientId + " clientSecret=" + clientSecret + " scope=" + scope + " url=" + url);

	    // 認証ページロード開始
	    wv.loadUrl(url);

	  }


	  /**
	   * 認証成功ページのタイトルは「Success code=XXXXXXX」という風になっているので、
	   * このタイトルから「code=」以下の部分を切り出してOAuth2アクセスコードとして返す
	   * 
	   * @param title
	   *          ページタイトル
	   * @return OAuth2アクセスコード
	   */

	  protected String getCode(String title) {
	    String code = null;
	    String codeKey = "code=";
Log.v("onClickBtn", "getCode1");
	    int idx = title.indexOf(codeKey);
Log.v("onClickBtn", "getCode2");
	    if (idx != -1) { // 認証成功ページだった
Log.v("onClickBtn", "getCode3");
	      code = title.substring(idx + codeKey.length()); // 「code」を切り出し
Log.v("onClickBtn", "getCode4");
	    }
	    return code;
	  }


	  // アクセストークン取得タスク
	  protected class TaskGetAccessToken extends AsyncTask<String, Void, String> {


	    @Override
	    protected void onPreExecute() {
	      Log.v("onPostExecute", "アクセストークン取得開始");
	      tvProg.setText("アクセストークンを取得中...");
	    }


	    @Override
	    protected String doInBackground(String... codes) {
	      String token = null;
	      DefaultHttpClient client = new DefaultHttpClient();
	      try {

	        // パラメータ構築
	        ArrayList<NameValuePair> formParams = new ArrayList<NameValuePair>();
	        formParams.add(new BasicNameValuePair("code", codes[0]));
	        formParams.add(new BasicNameValuePair("client_id", clientId));
	        formParams.add(new BasicNameValuePair("client_secret", clientSecret));
	        formParams.add(new BasicNameValuePair("redirect_uri", "urn:ietf:wg:oauth:2.0:oob"));
	        formParams.add(new BasicNameValuePair("grant_type", "authorization_code"));

	        // トークンの取得はPOSTで行うことになっている
	        HttpPost httpPost = new HttpPost("https://accounts.google.com/o/oauth2/token");
	        httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8")); // パラメータセット
	        HttpResponse res = client.execute(httpPost);
	        HttpEntity entity = res.getEntity();
	        String result = EntityUtils.toString(entity);

	        // JSONObject取得
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
	        Log.v("onPostExecute", "アクセストークン取得失敗");
	      } else {
	        Log.v("onPostExecute", "アクセストークン取得成功 token=" + token);
	        Intent intent = new Intent();
	        intent.putExtra(ACCESS_TOKEN, token);
	        setResult(Activity.RESULT_OK, intent);
	      }
	      finish();
	    }

	  } // END class TaskGetAccessToken
	} // END class OAuth2GoogleActivity
