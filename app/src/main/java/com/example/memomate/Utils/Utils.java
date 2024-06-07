package com.example.memomate.Utils;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;

public class Utils {
    public interface DelayCallBack{
        void afterDelay();
    }

    public static void delay(int secs, final DelayCallBack delayCallback)
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs);
    }

    public static void shake(View view)
    {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 2f, 0f, -2f, 0f);
        rotate.setRepeatCount(3);
        rotate.setDuration(80);
        rotate.start();
    }

}
