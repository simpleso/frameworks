package com.andnux.core.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class RoundCornerLayout extends ViewGroup {
	
	private static final String EXCEPTION = "RoundLayout can only contain one view !";
	private static final int CORNER = 8;
	
	private View mChildView;
	
	private float mLeftTopCorner;
	private float mRightTopCorner;
	private float mRightBottomCorner;
	private float mLeftBottomCorner;

	public RoundCornerLayout(Context context) {
		super(context);
		initGlobalParams();
	}

	public RoundCornerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGlobalParams();
	}

	public RoundCornerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGlobalParams();
	}
	
	private void initGlobalParams() {
		// setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mLeftTopCorner = mRightTopCorner = mRightBottomCorner = mLeftBottomCorner = dp2px(CORNER);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int childCount = getChildCount();
		if(childCount > 1) {
			Log.e("RoundLayout", EXCEPTION);
			mChildView = getChildAt(0);
		} else if(childCount == 1) {
			mChildView = getChildAt(0);
		} else {
			mChildView = null;
		}
	}
	@Override
	public void addView(View child) {
		if(getChildCount() > 0) {
			throw new RuntimeException(EXCEPTION);
		}
		super.addView(child);
	}
	
	@Override
	public void addView(View child, int index) {
		if(getChildCount() > 0) {
			throw new RuntimeException(EXCEPTION);
		}
		super.addView(child, index);
	}
	
	@Override
	public void addView(View child, int index, LayoutParams params) {
		if(getChildCount() > 0) {
			throw new RuntimeException(EXCEPTION);
		}
		super.addView(child, index, params);
	}
	
	@Override
	public void addView(View child, int width, int height) {
		if(getChildCount() > 0) {
			throw new RuntimeException(EXCEPTION);
		}
		super.addView(child, width, height);
	}
	
	@Override
	public void addView(View child, LayoutParams params) {
		if(getChildCount() > 0) {
			throw new RuntimeException(EXCEPTION);
		}
		super.addView(child, params);
	}
	
	@Override
	protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(null != mChildView) {
			measureChild(mChildView, widthMeasureSpec, heightMeasureSpec);
			int measuredWidth = mChildView.getMeasuredWidth();
			int measuredHeight = mChildView.getMeasuredHeight();
			setMeasuredDimension(measuredWidth, measuredHeight);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed && null != mChildView) {
			mChildView.layout(0, 0, r - l, b - t);
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		
		long start = System.currentTimeMillis();
		
		drawCorner1(canvas);
		// drawCorner3(canvas);
		
		Log.e("", (System.currentTimeMillis() - start) + "");
		
		// 有瑕疵
		// drawCorner2(canvas);
		
	}
	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if(gainFocus) {
			invalidate(getLeft(), getTop(), getRight(), getBottom());
		}
	}

	/**
	 * 继续练习
	 * @param canvas
	 */
	void drawCorner2(Canvas canvas) {
		if(null != mChildView) {
			canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), null, Canvas.ALL_SAVE_FLAG);
			

			Paint paint = new Paint();
			paint.setColor(Color.TRANSPARENT);
			paint.setAntiAlias(true);
			paint.setStyle(Style.FILL);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			// paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			Path path = new Path();
			float[] radii = new float[] {mLeftTopCorner, mLeftTopCorner, mRightTopCorner, mRightTopCorner, mRightBottomCorner, mRightBottomCorner, mLeftBottomCorner, mLeftBottomCorner};
			RectF rect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
			path.addRoundRect(rect , radii , Direction.CW);
			canvas.drawPath(path, paint);
			
			canvas.restore();
			
			super.dispatchDraw(canvas);
			
			
			
		} else {
			super.dispatchDraw(canvas);
		}
	}
	
	/**
	 * 是好使的
	 * @param canvas
	 */
	void drawCorner3(Canvas canvas) {
		
		canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), null, Canvas.ALL_SAVE_FLAG);
		
        super.dispatchDraw(canvas);
        
        drawCanvas(canvas);
	}

	private void drawCanvas(Canvas canvas) {
		
		Paint roundPaint = new Paint();
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Style.FILL);
        roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		
		if (mLeftTopCorner > 0) {
            Path path = new Path();
            path.moveTo(0, mLeftTopCorner);
            path.lineTo(0, 0);
            path.lineTo(mLeftTopCorner, 0);
            path.arcTo(new RectF(0, 0, mLeftTopCorner * 2, mLeftTopCorner * 2),
                    -90, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
		
		if (mRightTopCorner > 0) {
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - mRightTopCorner, 0);
            path.lineTo(width, 0);
            path.lineTo(width, mRightTopCorner);
            path.arcTo(new RectF(width - 2 * mRightTopCorner, 0, width,
            		mRightTopCorner * 2), 0, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
		
		if (mLeftBottomCorner > 0) {
            int height = getHeight();
            Path path = new Path();
            path.moveTo(0, height - mLeftBottomCorner);
            path.lineTo(0, height);
            path.lineTo(mLeftBottomCorner, height);
            path.arcTo(new RectF(0, height - 2 * mLeftBottomCorner,
            		mLeftBottomCorner * 2, height), 90, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
		
		if (mRightBottomCorner > 0) {
            int height = getHeight();
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - mRightBottomCorner, height);
            path.lineTo(width, height);
            path.lineTo(width, height - mRightBottomCorner);
            path.arcTo(new RectF(width - 2 * mRightBottomCorner, height - 2
                    * mRightBottomCorner, width, height), 0, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
		canvas.restore();
	}

	private PaintFlagsDrawFilter mFilter;
	private Path mPath;
	private RectF rect;
	private int right = 0;
	private int bottom = 0;
	private float[] radii;
	
	/**
	 * 有毛边
	 * @param canvas
	 */
	void drawCorner1(Canvas canvas) {
		if(null != mChildView) {
			if(null == mPath) {
				mPath = new Path();
				right = getMeasuredWidth();
				bottom = getMeasuredHeight();
				rect = new RectF(0, 0, right, bottom);
				radii = new float[] {mLeftTopCorner, mLeftTopCorner, mRightTopCorner, mRightTopCorner, mRightBottomCorner, mRightBottomCorner, mLeftBottomCorner, mLeftBottomCorner};
				mPath.addRoundRect(rect , radii , Direction.CW);
				mFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			}
			if(null == canvas.getDrawFilter()) {
				Log.e("", "333333333555555555577777777777");
				canvas.setDrawFilter(mFilter);
			}
			canvas.clipPath(mPath);
			super.dispatchDraw(canvas);
			// canvas.restore();
		} else {
			super.dispatchDraw(canvas);
		}
	}
	
	public void setLeftTopCorner(float leftTopCorner) {
		if(mLeftTopCorner != leftTopCorner) {
			if(leftTopCorner <= 0) {
				mLeftTopCorner = 0;
			} else {
				mLeftTopCorner = dp2px(leftTopCorner);
			}
			invalidate(getLeft(), getTop(), getRight(), getBottom());
		}
	}
	
	public void setRightTopCorner(float rightTopCorner) {
		if(mRightTopCorner != rightTopCorner) {
			if(rightTopCorner <= 0) {
				mRightTopCorner = 0;
			} else {
				mRightTopCorner = dp2px(rightTopCorner);
			}
			invalidate(getLeft(), getTop(), getRight(), getBottom());
		}
	}
	
	public void setRightBottomCorner(float rightBottomCorner) {
		if(mRightBottomCorner != rightBottomCorner) {
			if(rightBottomCorner <= 0) {
				mRightBottomCorner = 0;
			} else {
				mRightBottomCorner = dp2px(rightBottomCorner);
			}
			invalidate(getLeft(), getTop(), getRight(), getBottom());
		}
	}
	
	public void setLeftBottomCorner(float leftBottomCorner) {
		if(mLeftBottomCorner != leftBottomCorner) {
			if(leftBottomCorner <= 0) {
				mLeftBottomCorner = 0;
			} else {
				mLeftBottomCorner = dp2px(leftBottomCorner);
			}
			invalidate(getLeft(), getTop(), getRight(), getBottom());
		}
	}

	int dp2px(float db) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (scale * db + 0.5f);
	}
}