package com.pintumagang.android_app._sliders;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;
/**
 * Created by aribambang on 28/02/18.
 */

public class SliderView extends ViewPager {
    public static final int DEFAULT_SCROLL_DURATION = 200;
//    public static final int SLIDE_MODE_SCROLL_DURATION = 1000;

    public SliderView(Context context) {
        super(context);
        init();
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setDurationScroll(DEFAULT_SCROLL_DURATION);
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        // Here u can write code which is executed after the user touch on the screen

                        Log.v("Hoam", "kllik me down");
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        // Here u can write code which is executed after the user release the touch on the screen

                        Log.v("Hoam", "kllik me up");
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                    {
                        // Here u can write code which is executed when user move the finger on the screen

                        //Toast.makeText(getActivity(), "kllik me move", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });
    }
    public void setDurationScroll(int millis) {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new OwnScroller(getContext(), millis));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class OwnScroller extends Scroller {

        private int durationScrollMillis = 1;

        public OwnScroller(Context context, int durationScroll) {
            super(context, new DecelerateInterpolator());
            this.durationScrollMillis = durationScroll;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, durationScrollMillis);
        }
    }
}
