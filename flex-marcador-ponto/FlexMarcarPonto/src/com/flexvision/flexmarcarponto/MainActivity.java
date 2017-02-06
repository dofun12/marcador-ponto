package com.flexvision.flexmarcarponto;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.DeflateInputStream;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button clickButton = (Button) findViewById(R.id.submmit);

		EditText userinput = (EditText) findViewById(R.id.usuarioinpt);
		EditText passwdinput = (EditText) findViewById(R.id.senhainpt);

		SharedPreferences settings = getSharedPreferences("UserInfo", 0);
		userinput.setText(settings.getString("Username", "").toString());
		passwdinput.setText(settings.getString("Password", "").toString());

		clickButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myClickHandler(v);
			}
		});
	}

	// When user clicks button, calls AsyncTask.
	// Before attempting to fetch the URL, makes sure that there is a network
	// connection.
	public void myClickHandler(View view) {
		Button button = (Button) findViewById(R.id.submmit);
		button.setEnabled(false);
		new DownloadWebpageTask().execute();

	}

	
	
	private class DownloadWebpageTask extends AsyncTask<Void, Void, String> {
		
		private String print_content(HttpsURLConnection con) {
			if (con != null) {
				InputStream is = null;
				try {
					is = con.getInputStream();
					
					String encodingHeader = con.getHeaderField("Content-Encoding");
					if (encodingHeader != null && encodingHeader.toLowerCase().contains("deflate")){
						is = new DeflateInputStream(is);	
					}else if (encodingHeader != null && encodingHeader.toLowerCase().contains("gzip")){
						is = new GZIPInputStream(is);
					}

					System.out.println("****** Content of the URL ********");
					
					int ch;
					StringBuffer sb = new StringBuffer();
					while ((ch = is.read()) != -1) {
						sb.append((char) ch);
					}
					return sb.toString();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			Resources res = getResources();
			return String.format(res.getString(R.string.error_conection));

		}
		
		
		
		@Override
	    protected void onPostExecute(String result) {
			TextView text = (TextView) findViewById(R.id.responsetv);
			SimpleDateFormat spdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			text.setText("Sucess("+spdf.format(new Date())+"): "+result);
			Button button = (Button) findViewById(R.id.submmit); button.setEnabled(true);
	    }
		
		@Override
		protected String doInBackground(Void... params) {
			Resources res = getResources();
			
			// Gets the URL from the UI's text field.
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				
				
				EditText userinput = (EditText) findViewById(R.id.usuarioinpt);
				EditText passwdinput = (EditText) findViewById(R.id.senhainpt);

				if (userinput.getText() != null && !("".equals(userinput.getText())) && passwdinput.getText() != null
						&& !("".equals(passwdinput.getText()))) {
					SharedPreferences settings = getSharedPreferences("UserInfo", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("Username", userinput.getText().toString());
					editor.putString("Password", passwdinput.getText().toString());
					editor.commit();

					String https_url = "https://apdata.com.br/flexvision/.net/index.ashx/SaveTimmingEvent?"
							+ "deviceID=8001" + "&eventType=1" + "&userName=" + userinput.getText() + "&password="
							+ passwdinput.getText() + "&cracha=&costCenter=" + "&leave=" + "&func=" + "&sessionID=0"
							+ "&selectedEmployee=0" + "&selectedCandidate=0" + "&selectedVacancy=0" + "&dtFmt=d%2Fm%2FY"
							+ "&tmFmt=H%3Ai%3As&shTmFmt=H%3Ai&dtTmFmt=d%2Fm%2FY%20H%3Ai%3As" + "&language=0";
					URL url;
					try {

						url = new URL(https_url);
						HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
						connection = (HttpsURLConnection) url.openConnection();
						connection.addRequestProperty("User-Agent",
								"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
						connection.addRequestProperty("Accept-Encoding", "deflate");
						String cookieOld = "dynSID=%7B%22success%22%3Atrue%2C%22SessionID%22%3A%225nvwnad0elxufpocgi3m2qgl%22%2C%22selectedEmployee%22%3A115%2C%22selectedCandidate%22%3A0%2C%22selectedPosition%22%3A0%2C%22userName%22%3A%2243375020848%22%2C%22userEmployee%22%3A115%2C%22userId%22%3A103%2C%22languages%22%3A%7B%22port%22%3Atrue%2C%22eng%22%3Afalse%2C%22esp%22%3Afalse%7D%2C%22initalURL%22%3A%22https%3A//apdata.com.br/flexvision/%22%2C%22NTLM%22%3Afalse%2C%22cosDiretorioPersWeb2%22%3A%22%22%2C%22selectedVacancy%22%3A0%2C%22selectedEmployeeName%22%3A%22KEVIM%20SUCH%22%2C%22selectedCandidateName%22%3A%22Nenhum%22%2C%22selectedVacancyDescr%22%3A%22Folha%3A%20Nenhuma%20-%20Cargo%3A%20Nenhum%20-%20Centro%20de%20Custo%3A%20Nenhum%22%2C%22serverPort%22%3A%2211842%22%2C%22serverIp%22%3A%22SRVAPS15%22%2C%22upperCase%22%3A%22False%22%2C%22acceptTerm%22%3A%22False%22%2C%22useTerm%22%3A%22False%22%7D; apdataCookieIsEnabled=none; clockDeviceToken8001=nH6C/qecdsJSxp4vxTbzcGMej5eS8nGrKL7+Zjgmf3xHmIA=";
						String cookieNew = "dynSID=apdataCookieIsEnabled=none; clockDeviceToken8001=nH6C/qecdsJSxp4vxTbzcGMej5eS8nGrKL7+Zjgmf3xHmIA=";
						connection.addRequestProperty("Cookie",cookieNew);


						// dumpl all cert info
						// print_https_cert(connection);

						// dump all the content
						return print_content(connection);
					} catch (Exception e) {
						e.printStackTrace();
						return "ERRO: "+e.getCause();
					}
				} else {
					return String.format(res.getString(R.string.error_conection));
				}
			}
			return String.format(res.getString(R.string.error_conection));
		}
	
	}

}
