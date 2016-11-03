package com.practice;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by user1 on 2016/11/3.
 */

public class MyLinearLayout extends LinearLayout {
    private ViewDragHelper viewDragHelper;

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, 10.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;//child.getId()==R.id.normalDragView||child.getId()==R.id.autoBackView||child.getId()==R.id.dragButton;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                final int minLeft = getPaddingLeft();
                final int maxLeft = getWidth() - getPaddingRight() - child.getWidth();
                int realLeft=left<minLeft?minLeft:left;
                realLeft=realLeft>maxLeft?maxLeft:realLeft;
                return realLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int minTop = getPaddingTop();
                final int maxTop = getHeight() - getPaddingBottom() - child.getHeight();
                int realTop=top<minTop?minTop:top;
                realTop=realTop>maxTop?maxTop:realTop;
                return realTop;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getWidth()-child.getWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getHeight()-child.getHeight();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if(releasedChild.getId()==R.id.autoBackView){
                    viewDragHelper.settleCapturedViewAt(100,100);
                    invalidate();
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                viewDragHelper.captureChildView(findViewById(R.id.edgeTrackerView),pointerId);
            }
        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}
