package com.kazauskas.ui.widget.spreadbar;

import android.view.View;

/**
 * Created by vadim on 5/12/15.
 */
public interface SpreadBar {

    interface OnHitListener{
        void onHitMin();
        void onHitMax();
    }

    interface OnChangeListener{
        void onChanged(Long prevMin, Long min, Long prevMax, Long max);
    }

    /**
     * @return
     * View with all spreadBar components
     * */
    View getPreparedView();

    /**
     * Command for set both thumbs to the start position
     * */
    void resetThumbsToStart();

    /**
     * Called for approve current range and reinitialization
     * */
    void confirm();

    /**
     * Called when we should change spread bar
     * */
    void confirm(long startValue, long endValue, long step);

    /**
     * Called for improve left or right thumb in one step
     * @param thumb
     * Thumb which we want improve
     * */
    void improve(Thumb thumb);

    /**
     * Side hit action-panel
     *
     * @param hitType
     * hit type {@link Hit}
     * */
    void hideHit(Hit hitType);

    /**
     * Show hit-panel
     *
     * @param hitType
     * hit type {@link Hit}
     * */
    void showHit(Hit hitType);
    /**
     * Subscribe to spreadBar min or max changes
     * */
    void subscribeOnChanges(OnChangeListener listener);

    /**
     * Subscribe to hit panel actions
     * @param listener
     * Listener for hit actions {@link com.kazauskas.ui.widget.spreadbar.SpreadBar.OnHitListener}
     * */
    void subscribeOnHit(OnHitListener listener);

 }
