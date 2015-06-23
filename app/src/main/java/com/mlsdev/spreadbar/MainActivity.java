package com.mlsdev.spreadbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.kazauskas.ui.widget.spreadbar.GameControls;
import com.kazauskas.ui.widget.spreadbar.SpreadBar;
import com.kazauskas.ui.widget.spreadbar.SpreadBarImpl;

public class MainActivity extends AppCompatActivity {

    private final static int MIN_SPREAD = 0;
    private final static int MAX_SPREAD = 10000;
    private final static int DELTA = 200;

    GameControls gameControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout spreadBarLayout = (LinearLayout) findViewById(R.id.ll_spread_bar_container);
        SpreadBar spreadBar = new SpreadBarImpl(spreadBarLayout, MIN_SPREAD, MAX_SPREAD, DELTA);
        View spreadBarView = spreadBar.getPreparedView();
        spreadBarLayout.addView(spreadBarView, 0);

        gameControls = new GameControls(spreadBar, changesListener, hitListener);

        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameControls.resetThumbs();
            }
        });

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameControls.confirmThumbs();
            }
        });
    }

    private final GameControls.OnHitListener hitListener = new GameControls.OnHitListener() {
        public void onHitMin() {
        }
        public void onHitMax() {
        }
    };

    private final GameControls.OnChangeListener changesListener = new GameControls.OnChangeListener() {
        public void onChanged(Long prevMin, Long min, Long prevMax, Long max) {
        }
    };
}
