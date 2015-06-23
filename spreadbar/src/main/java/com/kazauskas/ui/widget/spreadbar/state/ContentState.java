package com.kazauskas.ui.widget.spreadbar.state;

import android.util.Log;

import com.kazauskas.ui.widget.spreadbar.Thumb;

import static com.kazauskas.ui.widget.spreadbar.Thumb.isLeftThumb;

/**
 * Created by vadim on 5/12/15.
 */
public class ContentState {

    private final static String TAG = "ContentState";

    private Thumb activeThumb;

    private long startValue;
    private long endValue;
    private long step;
    private long delta;

    private int baseStepLeft;
    private int baseStepRight;
    private int currentStepLeft;
    private int currentStepRight;
    private int stepsCount;

    public ContentState(long startValue, long endValue, long step) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.delta = this.endValue - this.startValue;
        this.step = step;
        initSteps();
    }

    public void initSteps(){
        stepsCount = (int) (delta / step);
        currentStepLeft = 0;
        currentStepRight = stepsCount;
        baseStepLeft = currentStepLeft;
        baseStepRight = currentStepRight;
    }

    public long getStartValue() {
        return startValue;
    }

    public long getEndValue() {
        return endValue;
    }

    public int getStepsCount(){
        return stepsCount;
    }

    public int getCurrentStepNumberFromStart(Thumb thumb){
        return (isLeftThumb(thumb)) ? currentStepLeft : stepsCount - currentStepRight;
    }

    public int getRightCurrentStepNumber(){
        return currentStepRight;
    }

    public int getRightBaseStepNumber(){
        return baseStepRight;
    }

    public int getLeftCurrentStepNumber(){
        return currentStepLeft;
    }

    public int getLeftBaseStepNumber(){
        return baseStepLeft;
    }

    public void setStepNumber(Thumb thumb, int number){
        if (isLeftThumb(thumb))
            currentStepLeft = number;
        else
            currentStepRight = number;
    }

    public long getValueByStep(){
        return delta / stepsCount;
    }

    public boolean isMoved(Thumb thumb){
        return isLeftThumb(thumb) ? getLeftStepDistance() != 0 : getRightStepDistance() != 0;
    }

    public int getLeftStepDistance(){
        return baseStepLeft - currentStepLeft;
    }

    public int getRightStepDistance(){
        return baseStepRight - currentStepRight;
    }

    public long getLeftValue(){
        return getLeftCurrentStepNumber() * getValueByStep() + getStartValue();
    }

    public long getRightValue(){
        return getRightCurrentStepNumber() * getValueByStep() + getStartValue();
    }

    public boolean isImprovable(Thumb thumb){
        boolean minimumDistance = currentStepLeft - currentStepRight < -1;
        if (isLeftThumb(thumb)){
            return minimumDistance && currentStepRight == baseStepRight;
        }
        else
            return minimumDistance && currentStepLeft == baseStepLeft;
    }

    public void improve(Thumb thumb){
        if (isLeftThumb(thumb)) currentStepLeft++;
        else currentStepRight--;
    }

    public Thumb getActiveThumb() {
        return activeThumb;
    }

    public void setActiveThumb(Thumb activeThumb) {
        this.activeThumb = activeThumb;
    }

}
