 package com.akigon.pentab_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
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
	private int mAccurcy = 2 ;
	private int mCount = 0;
	private Paint mPaint;
	private Path mPath;
	private float mX, mY, mOldX, mOldY;


	public MainView(Context context) {
		super(context);
		init();
	}

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(12);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStrokeJoin(Join.ROUND);
		mPath = new Path();

	}

	@Override
	public void onDraw(Canvas canvas) {
		mPath.moveTo(mOldX, mOldY);
		mPath.lineTo(mX, mY);
		canvas.drawPath(mPath, mPaint);

	}


	@Override
	public boolean onTouchEvent(MotionEvent e) {

		switch(e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mX = e.getX();
			mY = e.getY();
			mOldX = mX;
			mOldY = mY;
			mListner.onDrawEvent("1", (int)mX, (int)mY);
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			mCount++;
			if(mCount >= mAccurcy) {
				mCount = 0;
				mOldX = mX;
				mOldY = mY;
				mX = e.getX();
				mY = e.getY();
				mListner.onDrawEvent("0", (int)mX, (int)mY);
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
		public void onDrawEvent(String  downflag, int x, int y);
	}

}
