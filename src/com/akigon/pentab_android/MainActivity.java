package com.akigon.pentab_android;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.akigon.pentab_android.MainView.DrawEventListener;

public class MainActivity extends ActionBarActivity {
	private static final String ADDRESS = "192.168.0.50";
	private static final int PORT = 12345;
	private Socket socket;
	private PrintWriter out;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainView v = (MainView)findViewById(R.id.mainView1);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// ソケット通信を開始
					socket = new Socket(ADDRESS, PORT);
					if(socket.isConnected()) {
						String addr = String.valueOf(socket.getRemoteSocketAddress());
    					System.out.println("connect success " + addr);
					}else{
						System.out.println("connect fail ");
						return;
					}
					// ソケットの送信インターフェイスを作成
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

				} catch (IOException e) {
					e.printStackTrace();

				}

			}
		}).start();


		v.setListener(new DrawEventListener() {
			// 描画イベントでソケットにデータを送る
			@Override
			public void onDrawEvent(String downflag, int x, int y, int p) {
				out.println(downflag + "," + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(p));
				out.flush();

			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		case R.id.action_settings_check:
			// 設定チェック
			// プリファレンスの取得
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			//プリファレンスからの読み出し
			String setting_ip = sharedPreferences.getString("setting_ip", "127.0.0.1");
			String setting_port = sharedPreferences.getString("setting_port", "12345");
			boolean setting_pleasure = sharedPreferences.getBoolean("setting_pleasure", true);
			System.out.println("setting_ip = " + setting_ip);
			System.out.println("setting_port = " + setting_port);
			System.out.println("setting_pleasure = " + setting_pleasure);
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

}
