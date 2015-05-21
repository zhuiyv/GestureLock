package com.android.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GestureLockView extends View
{
	private static final String SETTING = "setting";
	private Paint paintNormal;
	private Paint paintOnTouch;
	private Paint paintInnerCycle;
	private Paint paintLines;
	private Paint paintKeyError;
	private MyCycle[] cycles;
	private Path linePath = new Path();
	private List<Integer> linedCycles = new ArrayList<Integer>();
	private OnGestureFinishListener onGestureFinishListener;
	private String key;
	private int eventX, eventY;
	private boolean canContinue = true;
	private boolean result;
	private Timer timer;

	private int OUT_CYCLE_NORMAL = Color.rgb(108, 119, 138); // 正常外圆颜色
	private int OUT_CYCLE_ONTOUCH = Color.rgb(025, 066, 103); // 选中外圆颜色
	private int INNER_CYCLE_ONTOUCH = Color.rgb(002, 210, 255); // 选择内圆颜色
	private int LINE_COLOR = Color.argb(127, 002, 210, 255); // 连接线颜色
	private int ERROR_COLOR = Color.argb(127, 255, 000, 000); // 连接错误醒目提示颜色

	public void setOnGestureFinishListener(OnGestureFinishListener onGestureFinishListener)
	{
		this.onGestureFinishListener = onGestureFinishListener;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public interface OnGestureFinishListener
	{
		public void OnGestureFinish(boolean result);
		public void OnSettingFinish(String result);
	}
	
	public GestureLockView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public GestureLockView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public GestureLockView(Context context)
	{
		super(context);
		init();
	}

	/**
	 * 设定为设置模式
	 * @param b 为true则为设置模式
	 */
	public void setIsSetting(boolean b){
		if(b){
			this.key = SETTING;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		int perSize = 0;
		if (cycles == null && (perSize = getWidth() / 6) > 0)
		{
			cycles = new MyCycle[9];
			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 3; j++)
				{
					MyCycle cycle = new MyCycle();
					cycle.setPerSize(perSize);
					cycle.setX(j);
					cycle.setY(i);
					cycle.setR(perSize * 0.5f);
					cycles[i * 3 + j] = cycle;
				}
			}
		}
	}

	private void init()
	{
		paintNormal = new Paint();
		paintNormal.setAntiAlias(true);
		paintNormal.setStrokeWidth(3);
		paintNormal.setStyle(Paint.Style.STROKE);

		paintOnTouch = new Paint();
		paintOnTouch.setAntiAlias(true);
		paintOnTouch.setStrokeWidth(3);
		paintOnTouch.setStyle(Paint.Style.STROKE);

		paintInnerCycle = new Paint();
		paintInnerCycle.setAntiAlias(true);
		paintInnerCycle.setStyle(Paint.Style.FILL);

		paintLines = new Paint();
		paintLines.setAntiAlias(true);
		paintLines.setStyle(Paint.Style.STROKE);
		paintLines.setStrokeWidth(6);

		paintKeyError = new Paint();
		paintKeyError.setAntiAlias(true);
		paintKeyError.setStyle(Paint.Style.STROKE);
		paintKeyError.setStrokeWidth(3);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		for (int i = 0; i < cycles.length; i++)
		{
			if (!canContinue && !result)
			{
				paintOnTouch.setColor(ERROR_COLOR);
				paintInnerCycle.setColor(ERROR_COLOR);
				paintLines.setColor(ERROR_COLOR);
			} else if (cycles[i].isOnTouch())
			{
				paintOnTouch.setColor(OUT_CYCLE_ONTOUCH);
				paintInnerCycle.setColor(INNER_CYCLE_ONTOUCH);
				paintLines.setColor(LINE_COLOR);
			} else
			{
				paintNormal.setColor(OUT_CYCLE_NORMAL);
				paintInnerCycle.setColor(INNER_CYCLE_ONTOUCH);
				paintLines.setColor(LINE_COLOR);
			}
			if (cycles[i].isOnTouch())
			{
				canvas.drawCircle(cycles[i].getOx(), cycles[i].getOy(), cycles[i].getR(), paintOnTouch);
				drawInnerBlueCycle(cycles[i], canvas);
			} else
			{
				canvas.drawCircle(cycles[i].getOx(), cycles[i].getOy(), cycles[i].getR(), paintNormal);
			}
		}
		drawLine(canvas);
	}

	private void drawLine(Canvas canvas)
	{
		linePath.reset();
		if (linedCycles.size() > 0)
		{
			for (int i = 0; i < linedCycles.size(); i++)
			{
				int index = linedCycles.get(i);
				float x = cycles[index].getOx();
				float y = cycles[index].getOy();
				if (i == 0)
				{
					linePath.moveTo(x, y);
				} else
				{
					linePath.lineTo(x, y);
				}
			}
			if (canContinue)
			{
				linePath.lineTo(eventX, eventY);
			} else
			{
				linePath.lineTo(cycles[linedCycles.get(linedCycles.size() - 1)].getOx(),
						cycles[linedCycles.get(linedCycles.size() - 1)].getOy());
			}
			canvas.drawPath(linePath, paintLines);
		}
	}

	private void drawInnerBlueCycle(MyCycle myCycle, Canvas canvas)
	{
		canvas.drawCircle(myCycle.getOx(), myCycle.getOy(), myCycle.getR() / 3, paintInnerCycle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (!canContinue)
		{
			return true;
		}
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
		{
			eventX = (int) event.getX();
			eventY = (int) event.getY();
			for (int i = 0; i < cycles.length; i++)
			{
				if (!cycles[i].isPointIn(eventX, eventY))
				{
					continue;
				}
				cycles[i].setOnTouch(true);
				if (linedCycles.contains(cycles[i].getNum()))
				{	
					continue;
				}
				//防止滑动是跳过中间点，路径上的加入已连接的列表并设置为已选择
				if(linedCycles.size() != 0){
					MyCycle lastCycle = cycles[linedCycles.get(linedCycles.size() -1)];
					int num = (cycles[i].getX() - lastCycle.getX()) * (cycles[i].getX() - lastCycle.getX()) 
							+ (cycles[i].getY() - lastCycle.getY()) * (cycles[i].getY() - lastCycle.getY());
					if(num == 8 || num == 4){
						int midNum = (lastCycle.getNum() + cycles[i].getNum()) / 2;
						if (linedCycles.contains(cycles[midNum].getNum()))
						cycles[midNum].setOnTouch(true);
						linedCycles.add(cycles[midNum].getNum());
					}
				}
				linedCycles.add(cycles[i].getNum());
			}
			break;
		}
		case MotionEvent.ACTION_UP:
		{
			// 暂停触碰
			canContinue = false;
			// 检查结果
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < linedCycles.size(); i++)
			{
				sb.append(linedCycles.get(i));
			}
			result = sb.toString().equals(key);
			if (onGestureFinishListener != null)
			{
				if(!SETTING.equals(key)){
				onGestureFinishListener.OnGestureFinish(result);
				timer = new Timer();
				timer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						// 还原
						eventX = eventY = 0;
						for (int i = 0; i < cycles.length; i++)
						{
							cycles[i].setOnTouch(false);
						}
						linedCycles.clear();
						linePath.reset();
						canContinue = true;
						postInvalidate();
					}
				}, 1000);
				}else{
					onGestureFinishListener.OnSettingFinish(sb.toString());
					result = true;
				}
			}
			}
			break;	
		}
		invalidate();
		return true;
	}
}
