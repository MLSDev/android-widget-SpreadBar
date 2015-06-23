package com.kazauskas.ui.widget.spreadbar;

/**
 * Created by vadim on 5/13/15.
 */
public class GameControls {

    public interface OnChangeListener{
        void onChanged(Long prevMin, Long min, Long prevMax, Long max);
    }

    public interface OnHitListener{
        void onHitMin();
        void onHitMax();
    }

    private SpreadBar spreadBar;

    public GameControls(SpreadBar spreadBar, final OnChangeListener listener, final OnHitListener hitListener) {
        this.spreadBar = spreadBar;

        spreadBar.subscribeOnChanges(new SpreadBar.OnChangeListener() {
            public void onChanged(Long prevMin, Long min, Long prevMax, Long max) {
                listener.onChanged(prevMin, min, prevMax, max);
            }
        });

        subscribeOnHit(hitListener);
    }

    /**
     * Needs for reset all non-commited moves
     * */
    public void resetThumbs(){
        spreadBar.resetThumbsToStart();
    }
    /**
     * Needs for commit all moves and recalculate spread
     * */
    public void confirmThumbs(){
        spreadBar.confirm();
    }
    /**
     * Needs for increase left thumb
     * */
    public void improveMin(){
        spreadBar.improve(Thumb.Left);
    }

    /**
     * Needs for increase right thumb
     * */
    public void improveMax(){
        spreadBar.improve(Thumb.Right);
    }

    /**
     * Needs for hiding left "hit" button when opponent made move
     * */
    public void hideHitMin(){
        spreadBar.hideHit(Hit.Min);
    }

    /**
     * Needs for hiding right "hit" button when opponent made move
     * */
    public void hideHitMax(){
        spreadBar.hideHit(Hit.Max);
    }

    /**
     * Needs for showing left "hit" button when opponent made move
     * */
    public void showHitMin(){
        spreadBar.showHit(Hit.Min);
    }

    /**
     * Needs for showing right "hit" button when opponent made move
     * */
    public void showHitMax(){
        spreadBar.showHit(Hit.Max);
    }

    /**
     * Needs for recalculating spread when opponent made move
     * */
    public void spreadBarWasChanged(long startValue, long endValue, long stepValue){
        spreadBar.confirm(startValue, endValue, stepValue);
    }

    /**
     * Subscribe on "hit" moves
     * */
    private void subscribeOnHit(final OnHitListener listener){
        spreadBar.subscribeOnHit(new SpreadBar.OnHitListener() {
            public void onHitMin() {
                listener.onHitMin();
            }
            public void onHitMax() {
                listener.onHitMax();
            }
        });
    }

}
