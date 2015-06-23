package com.kazauskas.ui.widget.spreadbar;

import android.view.View;

/**
 * Created by vadim on 5/18/15.
 */
public enum Hit {

    Min {
        @Override
        protected void setListener() {
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onHitMin();
                }
            });
        }
    },
    Max {
        @Override
        protected void setListener() {
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onHitMax();
                }
            });
        }
    };

    public interface OnClickListener{
        void onHitMin();
        void onHitMax();
    }

    protected View view;
    protected OnClickListener listener;

    public Hit initWith(View view, OnClickListener listener){
        this.view = view;
        this.listener = listener;
        setListener();
        return this;
    }

    protected abstract void setListener();

}
