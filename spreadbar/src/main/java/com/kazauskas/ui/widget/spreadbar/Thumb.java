package com.kazauskas.ui.widget.spreadbar;

/**
 * Created by vadim on 5/12/15.
 */
public enum Thumb {

    Left {
        public int getSign() {
            return 1;
        }
    },
    Right {
        public int getSign() {
            return -1;
        }
    };

    public Thumb opposite(){
        return this.equals(Left) ? Right : Left;
    }

    public abstract int getSign();

    public static boolean isLeftThumb(Thumb thumb){
        if (thumb == null) throw new IllegalArgumentException("Thumb cannot be null!");
        return (Thumb.Left.equals(thumb));
    }

}
