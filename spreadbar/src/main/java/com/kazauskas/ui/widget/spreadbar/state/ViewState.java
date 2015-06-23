package com.kazauskas.ui.widget.spreadbar.state;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kazauskas.ui.widget.spreadbar.Hit;
import com.kazauskas.ui.widget.spreadbar.util.AnimationHelper;
import com.kazauskas.ui.widget.spreadbar.view.GradientImageView;
import com.kazauskas.ui.widget.spreadbar.R;
import com.kazauskas.ui.widget.spreadbar.Thumb;
import com.kazauskas.ui.widget.spreadbar.observer.ThumbTranslationObserver;
import com.kazauskas.ui.widget.spreadbar.view.ThumbView;

import static com.kazauskas.ui.widget.spreadbar.Thumb.isLeftThumb;

/**
 * Created by vadim on 5/12/15.
 */
public class ViewState {

    public interface OnChangeListener{
        void onChange(Long min, Long max);
    }

    public interface OnHitListener{
        void onHitMax();
        void onHitMin();
    }

    private final static String TAG = "ViewState";

    private ContentViewAdapter contentViewAdapter;

    private Context context;
    private LinearLayout llContainer;
    private FrameLayout flSpreadBar;
    private ThumbView thumbLeft;
    private ThumbView thumbRight;
    private GradientImageView gradientLine;

    private View llLeftBubble;
    private View llRightBubble;
    private TextView tvLeftBubble;
    private TextView tvRightBubble;

    private TextView tvMinRange;
    private TextView tvMaxRange;

    private View flHitMin;
    private View flHitMax;

    private AnimatorSet thumbLeftSet;
    private AnimatorSet thumbRightSet;

    private long tempUpdatedMaxRange;
    private long tempUpdatedMinRange;

    private OnChangeListener changeListener;
    private ViewState.OnHitListener onHitListener;

    public ViewState(ViewGroup viewGroup, ContentViewAdapter contentViewAdapter) {
        this.context = viewGroup.getContext().getApplicationContext();
        this.contentViewAdapter = contentViewAdapter;
        initViews(viewGroup);
    }

    private void initViews(ViewGroup viewGroup){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        llContainer = (LinearLayout)inflater.inflate(R.layout.layout_spread_bar, viewGroup, false);

        flSpreadBar = (FrameLayout)llContainer.findViewById(R.id.fl_bar);

        thumbLeft = (ThumbView) flSpreadBar.findViewById(R.id.iv_left_thumb);
        thumbRight = (ThumbView) flSpreadBar.findViewById(R.id.iv_right_thumb);
        gradientLine = (GradientImageView) flSpreadBar.findViewById(R.id.gradient_line);

        thumbRight.subscribe(rightObserver);
        thumbLeft.subscribe(leftObserver);

        llLeftBubble = flSpreadBar.findViewById(R.id.bubble_left);
        llRightBubble = flSpreadBar.findViewById(R.id.bubble_right);
        tvLeftBubble = (TextView)llLeftBubble.findViewById(R.id.tv_range);
        tvRightBubble = (TextView)llRightBubble.findViewById(R.id.tv_range);

        tvMinRange = (TextView) flSpreadBar.findViewById(R.id.tv_range_min);
        tvMaxRange = (TextView) flSpreadBar.findViewById(R.id.tv_range_max);

        flHitMax = flSpreadBar.findViewById(R.id.fl_hit_max);
        flHitMin = flSpreadBar.findViewById(R.id.fl_hit_min);

        Hit.Min.initWith(flHitMin, onHitClickListener);
        Hit.Max.initWith(flHitMax, onHitClickListener);

        setRangesTextInfo(contentViewAdapter.getStartValue(), contentViewAdapter.getEndValue());

        flSpreadBar.invalidate();

    }

    public void setContentViewAdapter(ContentViewAdapter contentViewAdapter) {
        this.contentViewAdapter = contentViewAdapter;
    }

    public View getBarView(){
        return llContainer;
    }

    public void setThumbLeftTouchListener(View.OnTouchListener listener){
        thumbLeft.setOnTouchListener(listener);
    }

    public void setThumbRightTouchListener(View.OnTouchListener listener){
        thumbRight.setOnTouchListener(listener);
    }

    public int getThumbWidth(){
        return thumbLeft.getWidth();
    }

    public int getPositionOfThumb(Thumb thumb){
        return (isLeftThumb(thumb)) ? thumbLeft.getLeft() : thumbRight.getLeft();
    }

    public View getThumbView(Thumb thumb){
        return (isLeftThumb(thumb)) ? thumbLeft : thumbRight;
    }

    public void setAnimatorSet(Thumb thumb, AnimatorSet set){
        if (isLeftThumb(thumb)) thumbLeftSet = set;
        else thumbRightSet = set;
    }

    public void cancelAnimatorSet(Thumb thumb){
        if (isLeftThumb(thumb)){
            if (thumbLeftSet != null) thumbLeftSet.cancel();
        }
        else if (thumbRightSet != null) thumbRightSet.cancel();
    }

    public void thumbChangeState(Thumb thumb, boolean isActive) {
        View view = isLeftThumb(thumb) ? thumbLeft : thumbRight;
        AnimationHelper.startScaleWithThumb(view, isActive);
    }

    public void setTouchPriorityForThumb(Thumb thumb){
        View view = isLeftThumb(thumb) ? thumbLeft : thumbRight;
        View viewForDown = !isLeftThumb(thumb) ? thumbLeft : thumbRight;
        ViewCompat.setTranslationZ(view, 10f);
        ViewCompat.setTranslationZ(viewForDown, 1f);
    }

    public void changeBubblesState(boolean isActive){
        changeBubbleState(Thumb.Left, isActive);
        changeBubbleState(Thumb.Right, isActive);
    }

    public void changeBubbleState(Thumb thumb, boolean isActive){
        View bubbleView;
        TextView bubbleTextView;
        if (isLeftThumb(thumb)){
            bubbleView = llLeftBubble;
            bubbleTextView = tvLeftBubble;
        }
        else{
            bubbleView = llRightBubble;
            bubbleTextView = tvRightBubble;
        }
        bubbleTextView.setText(contentViewAdapter.getContentValueByTranslation(thumb, bubbleView.getTranslationX()) + "");

        if (!contentViewAdapter.isThumbMoved(thumb) && isActive || !isActive)
            AnimationHelper.startScaleWithBubble(bubbleView, isActive);
    }

    public void setRangesTextInfo(long min, long max){
        boolean allIsEmpty = TextUtils.isEmpty(tvMinRange.getText()) && TextUtils.isEmpty(tvMaxRange.getText());
        if (allIsEmpty){
            tvMinRange.setText(min + "");
            tvMaxRange.setText(max + "");
            return;
        }
        long prevMin = TextUtils.isEmpty(tvMinRange.getText()) ? min : Integer.valueOf(tvMinRange.getText().toString());
        long prevMax = TextUtils.isEmpty(tvMaxRange.getText()) ? max : Integer.valueOf(tvMaxRange.getText().toString());

        if (prevMin != min)
            setMinRangeTextInfo(prevMin, min);
        else
            setMaxRangeTextInfo(prevMax, max);
    }

    private void setMaxRangeTextInfo(long fromValue, final long toValue){
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN){
            tvMaxRange.setText(toValue + "");
        }
        else{
            final float decreaseCoefficient = (fromValue - toValue) / 10;
            tempUpdatedMaxRange = fromValue;
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tvMaxRange, "x", tvMaxRange.getX());
            objectAnimator.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (tempUpdatedMaxRange >= toValue) {
                        tempUpdatedMaxRange = (tempUpdatedMaxRange - (int) decreaseCoefficient);
                        if (tempUpdatedMaxRange <= toValue) {
                            tempUpdatedMaxRange = toValue;
                            tvMaxRange.setText(tempUpdatedMaxRange + "");
                            return;
                        }
                        tvMaxRange.setText(tempUpdatedMaxRange + "");
                    }
                    tvMaxRange.invalidate();
                }
            });
            objectAnimator.start();
        }
    }

    private void setMinRangeTextInfo(long fromValue, final long toValue){
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN){
            tvMinRange.setText(toValue + "");
        }
        else{
            long midCoefficient = (toValue - fromValue) / 10;
            final long growthCoefficient = midCoefficient != 0 ? midCoefficient : 1;

            tempUpdatedMinRange = fromValue;
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tvMinRange, "x", tvMinRange.getX());
            objectAnimator.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (tempUpdatedMinRange <= toValue) {
                        tempUpdatedMinRange = (tempUpdatedMinRange + growthCoefficient);
                        if (tempUpdatedMinRange > toValue) {
                            tempUpdatedMinRange = toValue;
                            tvMinRange.setText(tempUpdatedMinRange + "");
                            return;
                        }
                        tvMinRange.setText(tempUpdatedMinRange + "");
                    }
                }
            });
            objectAnimator.start();
        }
    }

    public void hideHitMin(){
        flHitMin.setVisibility(View.INVISIBLE);
    }

    public void hideHitMax(){
        flHitMax.setVisibility(View.INVISIBLE);
    }

    public void showHitMin(){
        flHitMin.setVisibility(View.VISIBLE);
    }

    public void showHitMax(){
        flHitMax.setVisibility(View.VISIBLE);
    }

    public void subscribeOnNewRange(OnChangeListener listener){
        changeListener = listener;
    }

    public void subscribeOnHitListener(ViewState.OnHitListener listener){
        this.onHitListener = listener;
    }

    private final Hit.OnClickListener onHitClickListener = new Hit.OnClickListener() {
        public void onHitMin() {
            onHitListener.onHitMin();
        }
        public void onHitMax() {
            onHitListener.onHitMax();
        }
    };

    private final ThumbTranslationObserver leftObserver = new ThumbTranslationObserver() {
        public void inform(float translationX) {
            llLeftBubble.setTranslationX(translationX);
            long value = contentViewAdapter.getContentValueByTranslation(Thumb.Left, translationX);
            tvLeftBubble.setText(value + "");
            if (changeListener != null) changeListener.onChange(value, null);
            float newLeft = translationX + getThumbWidth() / 2;
            gradientLine.setLeftX(newLeft);
            gradientLine.invalidate();
        }
    };

    private final ThumbTranslationObserver rightObserver = new ThumbTranslationObserver() {
        public void inform(float translationX) {
            llRightBubble.setTranslationX(translationX);
            long value = contentViewAdapter.getContentValueByTranslation(Thumb.Right, translationX);
            tvRightBubble.setText(value + "");
            if (changeListener != null) changeListener.onChange(null, value);
            float newRight = thumbRight.getLeft() + translationX + getThumbWidth() / 2;
            gradientLine.setRightX(newRight);
            gradientLine.invalidate();
        }
    };

}
