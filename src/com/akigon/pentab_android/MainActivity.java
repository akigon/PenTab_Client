package com.akigon.pentab_android;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.akigon.pentab_android.MainView.DrawEventListener;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
	private static final String ADDRESS = "192.168.3.2";
	private static final int PORT = 12345;
	private Socket socket;
	private PrintWriter out;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainView v = new MainView(this);
		setContentView(v);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					socket = new Socket(ADDRESS, PORT);
					if(socket.isConnected()) {
						String addr = String.valueOf(socket.getRemoteSocketAddress());
    						System.out.println("connect success " + addr);

					}else{
						System.out.println("connect fail ");
						return;

					}
					out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())));

				} catch (IOException e) {
					e.printStackTrace();

				}

			}
		}).start();


		v.setListener(new DrawEventListener() {

			@Override
			public void onDrawEvent(String downflag, int x, int y) {
//				System.out.println(String.valueOf(x) + "," + String.valueOf(y));
				out.println(downflag + "," + String.valueOf(x) + "," + String.valueOf(y));
				out.flush();

			}
		});

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
		if (id == R.id.action_settings) {
			return true;

		}
		return super.onOptionsItemSelected(item);

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
}
