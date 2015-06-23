package com.kazauskas.ui.widget.spreadbar;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.kazauskas.ui.widget.spreadbar.state.ContentState;
import com.kazauskas.ui.widget.spreadbar.state.ContentViewAdapter;
import com.kazauskas.ui.widget.spreadbar.state.DimensionsState;
import com.kazauskas.ui.widget.spreadbar.state.ViewState;
import com.kazauskas.ui.widget.spreadbar.util.AnimationHelper;
import com.kazauskas.ui.widget.spreadbar.util.ThumbMotionCalculator;

/**
 * Created by vadim on 5/12/15.
 */
public class SpreadBarImpl implements SpreadBar{

    private final static String TAG = "SpreadBar";

    private ContentState contentState;
    private ViewState viewState;
    private DimensionsState dimensionsState;
    private ContentViewAdapter contentViewAdapter;

    private ViewGroup viewGroup;

    private ThumbTouchListener leftThumbListener;
    private ThumbTouchListener rightThumbListener;

    private long defaultStep;

    private OnChangeListener changeListener;

    public SpreadBarImpl(ViewGroup viewGroup, long startValue, long endValue, Integer step) {
        leftThumbListener = new ThumbTouchListener().withThumb(Thumb.Left);
        rightThumbListener = new ThumbTouchListener().withThumb(Thumb.Right);
        initData(viewGroup, startValue, endValue, step);
    }

    private void initData(ViewGroup viewGroup, long startValue, long endValue, long step) {
        this.viewGroup = viewGroup;

        this.defaultStep = step;

        this.contentState = new ContentState(startValue, endValue, step);
        this.dimensionsState = new DimensionsState();
        this.contentViewAdapter = new ContentViewAdapter(contentState, dimensionsState);
        this.viewState = new ViewState(viewGroup, contentViewAdapter);

        this.viewState.setThumbLeftTouchListener(leftThumbListener);
        this.viewState.setThumbRightTouchListener(rightThumbListener);
        this.viewState.subscribeOnNewRange(newRangeListener);
        dimensionsState.initIfNotYet(viewState, contentState);
    }

    @Override
    public View getPreparedView() {
        return viewState.getBarView();
    }

    @Override
    public void resetThumbsToStart() {
        setThumbToInitial();
        contentState.setStepNumber(Thumb.Left, contentState.getLeftBaseStepNumber());
        contentState.setStepNumber(Thumb.Right, contentState.getRightBaseStepNumber());
        viewState.changeBubblesState(true);
    }

    @Override
    public void confirm(long start, long end, long step){
        if (start != contentState.getStartValue() || end != contentState.getEndValue() || step != contentState.getValueByStep()){
            setThumbToInitial();

            this.defaultStep = step;

            this.contentState = new ContentState(start, end, this.defaultStep);
            this.dimensionsState = new DimensionsState();
            this.contentViewAdapter = new ContentViewAdapter(contentState, dimensionsState);

            dimensionsState.initIfNotYet(viewState, contentState);

            viewState.setContentViewAdapter(contentViewAdapter);
            viewState.setRangesTextInfo(start, end);
            viewState.changeBubblesState(true);
        }
    }

    @Override
    public void confirm() {
        confirm(contentState.getLeftValue(), contentState.getRightValue(), this.defaultStep);
    }

    @Override
    public void improve(Thumb thumb) {
        dimensionsState.initIfNotYet(viewState, contentState);
        if (contentState.isImprovable(thumb)){
            contentState.improve(thumb);
            float newTranslation = thumb.getSign() * contentState.getCurrentStepNumberFromStart(thumb) * dimensionsState.pxInStep() ;
            viewState.setAnimatorSet(thumb, AnimationHelper.startTranslateWithThumb(viewState.getThumbView(thumb), newTranslation));

            viewState.setTouchPriorityForThumb(thumb);
        }
        viewState.changeBubbleState(thumb, false);
    }

    @Override
    public void hideHit(Hit hitType) {
        if (hitType.equals(Hit.Min))
            viewState.hideHitMin();
        else
            viewState.hideHitMax();

    }

    @Override
    public void showHit(Hit hitType) {
        if (hitType.equals(Hit.Min))
            viewState.showHitMin();
        else
            viewState.showHitMax();
    }

    @Override
    public void subscribeOnChanges(OnChangeListener listener) {
        changeListener = listener;
    }

    @Override
    public void subscribeOnHit(final OnHitListener listener) {
        viewState.subscribeOnHitListener(new ViewState.OnHitListener() {
            public void onHitMax() {
                listener.onHitMax();
            }
            public void onHitMin() {
                listener.onHitMin();
            }
        });
    }

    private void setThumbToInitial(){
        setThumbToInitial(Thumb.Left);
        setThumbToInitial(Thumb.Right);
    }

    private void setThumbToInitial(Thumb thumb){
        if (contentState.isMoved(thumb))
            viewState.setAnimatorSet(thumb, AnimationHelper.startBounceWithThumb(viewState.getThumbView(thumb)));
    }

    private class ThumbTouchListener implements View.OnTouchListener {
        Thumb thumb;
        private Float baseX;

        public ThumbTouchListener withThumb(Thumb thumb){
            this.thumb = thumb;
            return this;
        }

        public boolean onTouch(View view, MotionEvent event) {
            dimensionsState.initIfNotYet(viewState, contentState);
            Thumb activeThumb = contentState.getActiveThumb();
            if (!contentState.isMoved(thumb.opposite()) && (activeThumb == null || activeThumb.equals(thumb))){

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        contentState.setActiveThumb(thumb);
                        baseX = ThumbMotionCalculator.getBaseX(thumb, view, event);
                        viewState.thumbChangeState(thumb, true);
                        viewState.changeBubbleState(thumb, false);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Float translationX = ThumbMotionCalculator.getMoveTranslationX(
                                dimensionsState,
                                contentState,
                                thumb,
                                baseX,
                                event);

                        viewState.cancelAnimatorSet(thumb);
                        view.setTranslationX(translationX);
                        break;

                    case MotionEvent.ACTION_UP:
                        contentState.setActiveThumb(null);
                        int stepNumber = ThumbMotionCalculator.thumbStepAfterTranslation(
                                thumb,
                                dimensionsState,
                                contentState,
                                view.getTranslationX());

                        contentState.setStepNumber(thumb, stepNumber);
                        int translationXForStep = ThumbMotionCalculator.coordinateForCloser(
                                thumb,
                                dimensionsState,
                                contentState,
                                stepNumber);

                        float delta = view.getTranslationX() - translationXForStep;

                        viewState.cancelAnimatorSet(thumb);
                        viewState.setAnimatorSet(
                                thumb,
                                AnimationHelper.startBounceWithThumb(
                                        viewState.getThumbView(thumb),
                                        translationXForStep, delta));

                        viewState.thumbChangeState(thumb, false);
                        viewState.changeBubbleState(thumb, true);
                        viewState.setTouchPriorityForThumb(thumb);
                        break;
                }
            }
            return true;
        }
    }

    private final ViewState.OnChangeListener newRangeListener = new ViewState.OnChangeListener() {
        @Override
        public void onChange(Long min, Long max) {
            Long startValue = contentState.getStartValue();
            Long endValue = contentState.getEndValue();
            changeListener.onChanged(
                    startValue,
                    startValue.equals(min) ? null : min,
                    endValue,
                    endValue.equals(max) ? null : max);
        }
    };

}
