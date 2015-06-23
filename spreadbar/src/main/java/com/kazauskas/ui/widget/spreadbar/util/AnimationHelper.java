package com.kazauskas.ui.widget.spreadbar.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by vadim on 5/12/15.
 */
public class AnimationHelper {

    private final static int DEFAULT_TRANSLATION_POSITION = 0;
    private final static int DEFAULT_TRANSLATION_DELTA = 30;

    public static AnimatorSet startTranslateWithThumb(View thumbView, float endPositionX){
        Integer duration = thumbView.getResources().getInteger(android.R.integer.config_shortAnimTime) / 8;
        AnimatorSet as = new AnimatorSet();
        as.playSequentially(ObjectAnimator.ofFloat(thumbView, "translationX", endPositionX));
        as.setDuration(duration);
        as.start();
        return as;
    }

    public static AnimatorSet startBounceWithThumb(View thumbView){
        return startBounceWithThumb(thumbView, DEFAULT_TRANSLATION_POSITION, DEFAULT_TRANSLATION_DELTA, null);
    }

    public static AnimatorSet startBounceWithThumb(View thumbView, float endPositionX, float deltaX){
        return startBounceWithThumb(thumbView, endPositionX, deltaX, null);
    }

    public static AnimatorSet startBounceWithThumb(View thumbView, float endPositionX, float deltaX, Integer duration){
        AnimatorSet as = new AnimatorSet();
        as.playSequentially(
                ObjectAnimator.ofFloat(thumbView, "translationX", endPositionX - deltaX / 4),
                ObjectAnimator.ofFloat(thumbView, "translationX", endPositionX + deltaX / 8),
                ObjectAnimator.ofFloat(thumbView, "translationX", endPositionX));
        if (duration == null) duration = thumbView.getResources().getInteger(android.R.integer.config_shortAnimTime) / 2;
        as.setDuration(duration);
        as.start();
        return as;
    }

    public static void startScaleWithThumb(View thumbView, boolean isActive){
        float coefficient = isActive ? 0.8f : 1.0f;
        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(thumbView,
                PropertyValuesHolder.ofFloat("scaleX", coefficient),
                PropertyValuesHolder.ofFloat("scaleY", coefficient));
        scale.setDuration(thumbView.getResources().getInteger(android.R.integer.config_shortAnimTime));
        scale.setInterpolator(new LinearInterpolator());
        scale.start();
    }

    public static void startScaleWithBubble(View bubbleView, boolean isActive){
        float coefficient = isActive ? 0.0f : 1.0f;
        bubbleView.setPivotY(bubbleView.getHeight());
        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(bubbleView,
                PropertyValuesHolder.ofFloat("scaleX", coefficient),
                PropertyValuesHolder.ofFloat("scaleY", coefficient));
        scale.setDuration(bubbleView.getResources().getInteger(android.R.integer.config_longAnimTime));
        scale.setInterpolator(new BounceInterpolator());
        scale.start();
    }
}
