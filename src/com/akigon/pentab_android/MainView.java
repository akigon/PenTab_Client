 package com.akigon.pentab_android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MainView extends View {
	private DrawEventListener mListner = null;
	private int mAccurcy = 0 ;
	private int mCount = 0;
	private Paint mPaint;
	private Path mPath;
	private float mX, mY, mP, mOldX, mOldY, mOldP;

	// 手動生成用
	public MainView(Context context) {
		super(context);
		init();
	}
	// XML用
	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	// 初期化
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(12); // 線幅
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND); // 端処理
		mPaint.setStrokeJoin(Join.ROUND);
		mPath = new Path();

	}

	// ビューの描画イベント
	@Override
	public void onDraw(Canvas canvas) {
		// 線を描画
		mPath.moveTo(mOldX, mOldY);
		mPath.lineTo(mX, mY);
		canvas.drawPath(mPath, mPaint);
	}

	// ビューのタッチイベント
	@Override
	public boolean onTouchEvent(MotionEvent e) {

		switch(e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mX = e.getX();
			mY = e.getY();
			//mP = e.getPressure() * 100;
			mP = 0.0f; //テスト用(速度を筆圧とする)
			mOldX = mX;
			mOldY = mY;
			mOldP = mP;
			// サーバーへデータを送るためのイベントを発行
			mListner.onDrawEvent("1", (int)mX, (int)mY, (int)mP);
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			mCount++;
			if(mCount >= mAccurcy) {
				mCount = 0;
				mP = (float)Math.sqrt((mOldX-mX)*(mOldX-mX)+(mOldY-mY)*(mOldY-mY))*0.1f; //テスト用(速度を筆圧とする)
				mOldX = mX;
				mOldY = mY;
				mOldP = mP;
				mX = e.getX();
				mY = e.getY();
				//mP = e.getPressure() * 100;
				// サーバーへデータを送るためのイベントを発行
				mListner.onDrawEvent("0", (int)mX, (int)mY, (int)mP);
				// Androidビューの描画
				invalidate();
			}
			break;

		}
		return true;
	}

	public void setListener(DrawEventListener listener) {
		mListner = listener;
	}

	interface DrawEventListener {
		public void onDrawEvent(String  downflag, int x, int y, int p);
	}

}
