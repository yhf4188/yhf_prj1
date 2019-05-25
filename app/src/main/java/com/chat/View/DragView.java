package com.chat.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class DragView extends ViewGroup {
    //是否需要拖动目标
    private boolean mIsTarget;
    private int mLastScrollX;
    private int mLastScrollY;


    public DragView(Context context) {
        this(context, null);
    }


    public DragView(Context context, AttributeSet set) {
        this(context, set, 0);
    }


    public DragView(Context context, AttributeSet set, int style) {
        super(context, set, style);


    }


    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("DragView can host only one direct child");
        }


        super.addView(child);
    }


    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("DragView can host only one direct child");
        }


        super.addView(child, index);
    }


    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("DragView can host only one direct child");
        }


        super.addView(child, params);
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("DragView can host only one direct child");
        }


        super.addView(child, index, params);
    }


    //这个方法主要控制视图拖动的位置
    private void childLayout(int dx, int dy) {


        View child = getChildAt(0);
        int left = child.getLeft();
        int top = child.getTop();


        int layoutLeft = left + dx;
        int layoutTop = top + dy;


        //判断边界
        if (layoutLeft < getPaddingLeft() || layoutLeft + child.getWidth() + getPaddingRight() >= getRight()) {
            layoutLeft = left;
        }


        //判断边界
        if (layoutTop < getPaddingTop() || layoutTop + child.getHeight() + getPaddingBottom() >= getBottom()) {
            layoutTop = top;
        }


        child.layout(layoutLeft, layoutTop, layoutLeft + child.getWidth(), layoutTop + child.getHeight());
    }


    public boolean onInterceptTouchEvent(MotionEvent ev) {


        int action = ev.getAction();


        //当前是否在拖动，如果在拖动，则拦截当前事件
        if (action == MotionEvent.ACTION_MOVE && mIsTarget) {
            return true;
        }


        //如果是按下操作，判断当前坐标是否在拖动视图范围之内
        if (action == MotionEvent.ACTION_DOWN) {
            View child = getChildAt(0);
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            if (x >= child.getLeft() && x <= child.getRight() && y >= child.getTop() && y <= child.getBottom()) {
                mIsTarget = true;
            } else {
                mIsTarget = false;
            }
            mLastScrollX = x;
            mLastScrollY = y;
        }


        return super.onInterceptTouchEvent(ev);
    }


    public boolean onTouchEvent(MotionEvent event) {


        if (!mIsTarget) {
            return false;
        }


        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN :
                mLastScrollX = (int) event.getX();
                mLastScrollY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE :
                int x = (int) event.getX();
                int y = (int) event.getY();
                int dx = x - mLastScrollX;
                int dy = y - mLastScrollY;
                childLayout(dx, dy);
                mLastScrollX = (int) event.getX();
                mLastScrollY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP :
            case MotionEvent.ACTION_CANCEL :
                mIsTarget = false;
                break;
        }
        return true;
    }


    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {


        if (getChildCount() > 1) {
            throw new IllegalStateException("DragView can host only one direct child");
        }


        if (getChildCount() > 0) {


            View view = getChildAt(0);


            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();


            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            int childLeft = lp.leftMargin;
            int childTop = lp.topMargin;


            view.layout(childLeft + getPaddingLeft(), childTop + getPaddingTop(), childLeft + width + getPaddingLeft(), childTop + height + getPaddingTop());
        }
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        if (getChildCount() > 0) {
            View child = getChildAt(0);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);


            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);


            int width = 0;
            int height = 0;


            if (widthMode == MeasureSpec.EXACTLY) {
                width = MeasureSpec.getSize(widthMeasureSpec);
            } else {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                width = lp.leftMargin + lp.rightMargin + child.getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
            }


            if (heightMode == MeasureSpec.EXACTLY) {
                height = MeasureSpec.getSize(heightMeasureSpec);
            } else {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                height = lp.topMargin + lp.bottomMargin + child.getMeasuredHeight() + getPaddingTop() + getPaddingBottom();
            }


            setMeasuredDimension(width, height);
        }
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }


    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }


    public static class LayoutParams extends MarginLayoutParams {




        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }


        public LayoutParams(int width, int height) {
            super(width, height);
        }


        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }


        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }
}
