package com.kazauskas.ui.widget.spreadbar.state;

import com.kazauskas.ui.widget.spreadbar.Thumb;

/**
 * Created by vadim on 5/12/15.
 */
public class DimensionsState {

    private final static String TAG = "DimensionsState";

    private Integer pxInStep;

    public void initIfNotYet(ViewState viewState, ContentState contentState){
        pxInStep = ( viewState.getPositionOfThumb(Thumb.Right) - viewState.getPositionOfThumb(Thumb.Left) )
                        / contentState.getStepsCount();
    }

    public int pxInStep(){
        return pxInStep;
    }
}
