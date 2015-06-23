package com.kazauskas.ui.widget.spreadbar.util;

import android.view.MotionEvent;
import android.view.View;

import com.kazauskas.ui.widget.spreadbar.Thumb;
import com.kazauskas.ui.widget.spreadbar.state.ContentState;
import com.kazauskas.ui.widget.spreadbar.state.DimensionsState;

import static com.kazauskas.ui.widget.spreadbar.Thumb.isLeftThumb;

/**
 * Created by vadim on 5/12/15.
 */
public class ThumbMotionCalculator {

    private static final String TAG = "ThumbMotionHandler";

    /**
     * @return
     * baseX for Left or Right thumbs
     * */
    public static float getBaseX(Thumb thumb, View view, MotionEvent event){
        return isLeftThumb(thumb) ? event.getRawX() - view.getTranslationX() : event.getRawX() - view.getTranslationX();
    }

    /**
     * @return
     * offset for translation by X axis
     * */
    public static Float getMoveTranslationX(DimensionsState dimensionsState, ContentState contentState, Thumb thumb, float baseX, MotionEvent event){
        float translationX = event.getRawX() - baseX;
        int leftBorderPx;
        int rightBorderPx;
        int contentBorderValue;
        if (isLeftThumb(thumb)) {
            leftBorderPx = 0;
            if (translationX < leftBorderPx) return 0f;

            contentBorderValue = contentState.getRightCurrentStepNumber() - 1;
            rightBorderPx = contentBorderValue * dimensionsState.pxInStep();
            if (translationX > rightBorderPx) return (float)rightBorderPx;

            return translationX;
        }
        else{
            leftBorderPx = (contentState.getStepsCount() - contentState.getLeftCurrentStepNumber() - 1) * dimensionsState.pxInStep();
            if (Math.abs(translationX) > leftBorderPx) return (float)-1*leftBorderPx;

            rightBorderPx = 0;
            if ( translationX > rightBorderPx ) return (float)rightBorderPx;

            return translationX;
        }
    }

    /**
     * @return
     * x-coordinate for nearest state by current translation
     * */
    public static int thumbStepAfterTranslation(Thumb thumb, DimensionsState dimensionsState, ContentState contentState,  float viewTranslationX){
        if (isLeftThumb(thumb)) {
            return Math.round(viewTranslationX / dimensionsState.pxInStep());
        }
        else{
            return contentState.getStepsCount() - Math.abs(Math.round(viewTranslationX / dimensionsState.pxInStep()));
        }
    }

    /**
     * @return
     * coordinate for closer
     * */
    public static int coordinateForCloser(Thumb thumb, DimensionsState dimensionsState, ContentState contentState, int stepNumber){
        if (isLeftThumb(thumb)) return dimensionsState.pxInStep() * stepNumber;
        else {
            return -1*(contentState.getStepsCount() - contentState.getRightCurrentStepNumber()) * dimensionsState.pxInStep();
        }
    }
 }
