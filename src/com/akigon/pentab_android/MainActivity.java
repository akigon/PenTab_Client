package com.akigon.pentab_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.akigon.pentab_android.MainView.DrawEventListener;

public class MainActivity extends ActionBarActivity {

	private SocketInterface sock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainView v = (MainView)findViewById(R.id.mainView1);

		connectToServer();

		if(v==null) System.out.println("Warning v=null");
		v.setListener(new DrawEventListener() {
			// 描画イベントでソケットにデータを送る
			@Override
			public void onDrawEvent(String downflag, int x, int y, int p) {
				sock.Send(downflag + "," + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(p));
			}
		});

	}

	// 設定を利用して接続
	private void connectToServer() {
		final Handler h = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// プリファレンスの取得
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				//プリファレンスからの読み出し
				String setting_ip = sharedPreferences.getString("setting_ip", "127.0.0.1");
				int setting_port = Integer.parseInt(sharedPreferences.getString("setting_port", "12345"));
				boolean setting_pleasure = sharedPreferences.getBoolean("setting_pleasure", true);
				System.out.println("setting_ip = " + setting_ip);
				System.out.println("setting_port = " + setting_port);
				System.out.println("setting_pleasure = " + setting_pleasure);

				if(setting_port > 65535) setting_port = 65535;
				if(setting_port < 0) setting_port = 0;
				sock = new SocketInterface();
				final boolean result = sock.Connect(setting_ip, setting_port);
				h.post(new Runnable() {
					@Override
					public void run() {
						if(result) {
							Toast.makeText(getApplicationContext(), "サーバーに接続しました", Toast.LENGTH_LONG ).show();
						} else {
							Toast.makeText(getApplicationContext(), "サーバーに接続できません", Toast.LENGTH_LONG ).show();
						}
					}
				});
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		sock.Disconnect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			// 設定画面の呼び出し
			Intent nextActivity = new Intent(this, SettingsActivity.class);
			startActivity(nextActivity);
			return true;
		case R.id.action_connect:
			// 接続
			connectToServer();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

}
