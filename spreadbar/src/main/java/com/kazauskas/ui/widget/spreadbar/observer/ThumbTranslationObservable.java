package com.kazauskas.ui.widget.spreadbar.observer;

/**
 * Created by vadim on 5/13/15.
 */
public interface ThumbTranslationObservable {

    void subscribe(ThumbTranslationObserver observer);

    void unSubscribe(ThumbTranslationObserver observer);

}
