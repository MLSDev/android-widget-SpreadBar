package com.kazauskas.ui.widget.spreadbar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kazauskas.ui.widget.spreadbar.observer.ThumbTranslationObservable;
import com.kazauskas.ui.widget.spreadbar.observer.ThumbTranslationObserver;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by vadim on 5/13/15.
 */
public class ThumbView extends ImageView implements ThumbTranslationObservable {

    Set<ThumbTranslationObserver> observers = new HashSet<>();

    public ThumbView(Context context) {
        this(context, null);
    }

    public ThumbView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTranslationX(float translationX){
        super.setTranslationX(translationX);
        informObservers(translationX);
    }

    private void informObservers(float translationX){
        for(ThumbTranslationObserver observer : observers) observer.inform(translationX);
    }

    @Override
    public void subscribe(ThumbTranslationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unSubscribe(ThumbTranslationObserver observer) {
        observers.remove(observer);
    }

}
