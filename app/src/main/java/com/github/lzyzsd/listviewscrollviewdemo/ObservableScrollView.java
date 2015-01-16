package com.github.lzyzsd.listviewscrollviewdemo;

/**
 * Created by Bruce on 1/12/15.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * A custom ScrollView that can accept a scroll listener.
 */
public class ObservableScrollView extends ScrollView {
    private Callbacks mCallbacks;
    private boolean isListViewScrolledToTop = true;
    public static enum State {
        OPENED, MIDDLE, CLOSED
    }
    private State state = State.OPENED;

    public void setState(State state) {
        this.state = state;
    }

    public void setListViewScrolledToTop(boolean isListViewScrolledToTop) {
        this.isListViewScrolledToTop = isListViewScrolledToTop;
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    float lastY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                boolean ret = false;
                float y = ev.getY();
                float diff = Math.abs(y - lastY);
                if (state == State.CLOSED) {
                    if (isListViewScrolledToTop && y > lastY && diff > 4) {
                        ret = true;
                    }
                } else if (state == State.OPENED) {
                    if (y < lastY && diff > 4) {
                        ret = true;
                    }
                }
                System.out.println("---------------scrollview intercept: " +
                        isListViewScrolledToTop +
                        + y +"" +lastY + ret + "state" + state);
                lastY = y;
                return ret;
            default:
                lastY = ev.getY();
                return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(t);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mCallbacks.onDownMotionEvent();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mCallbacks.onUpOrCancelMotionEvent();
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    public static interface Callbacks {
        public void onScrollChanged(int scrollY);
        public void onDownMotionEvent();
        public void onUpOrCancelMotionEvent();
    }
}
