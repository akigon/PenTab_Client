package com.akigon.pentab_android;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketInterface {

	private Socket socket;
	private String address = "127.0.0.1";
	private int port = 12345;
	private PrintWriter out;

	public SocketInterface() {
		System.out.println("SocketInterface new");
	}

	public boolean Connect(String newaddress, int newport) {
		if(newaddress==address && newport==port && socket!=null && socket.isConnected()) return true;
		address = newaddress;
		port = newport;
		if(socket!=null && socket.isConnected()) Disconnect();
		try {
			// ソケット通信を開始
			socket = new Socket(address, port);
			//System.out.println("SocketInterface connect socket: " + socket);
			if(socket.isConnected()) {
				String addr = String.valueOf(socket.getRemoteSocketAddress());
				System.out.println("connect success " + addr);
			}else{
				System.out.println("connect fail ");
				return false;
			}
			// ソケットの送信インターフェイスを作成
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void Disconnect() {
		if(out != null) {
			out.flush();
			out.close();
		}
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void Send(String data) {
		if(socket == null) return;
		if(!socket.isConnected()) return;
		if(out == null) return;
		out.println(data);
		out.flush();
	}

}
