package com.kazauskas.ui.widget.spreadbar.state;

import com.kazauskas.ui.widget.spreadbar.Thumb;

import static com.kazauskas.ui.widget.spreadbar.Thumb.isLeftThumb;

/**
 * Created by vadim on 5/13/15.
 */
public class ContentViewAdapter {

    private ContentState contentState;
    private DimensionsState dimensionsState;

    public ContentViewAdapter(ContentState contentState, DimensionsState dimensionsState) {
        this.contentState = contentState;
        this.dimensionsState = dimensionsState;
    }

    public long getContentValueByTranslation(Thumb thumb, float translation){
        if (isLeftThumb(thumb)){
            return Math.round(translation / dimensionsState.pxInStep()) * contentState.getValueByStep() + contentState.getStartValue();
        }
        else{
            return (contentState.getStepsCount() - Math.round(Math.abs(translation) / dimensionsState.pxInStep()) ) * contentState.getValueByStep() + contentState.getStartValue();
        }
    }

    public long getStartValue(){
        return contentState.getStartValue();
    }

    public long getEndValue(){
        return contentState.getEndValue();
    }

    public boolean isThumbMoved(Thumb thumb){
        return contentState.isMoved(thumb);
    }

}
