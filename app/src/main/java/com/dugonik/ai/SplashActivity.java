package com.dugonik.ai;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(5, 8, 35));
        getWindow().setNavigationBarColor(Color.rgb(5, 8, 35));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.rgb(5, 8, 35));

        TextView crown = new TextView(this);
        crown.setText("♛");
        crown.setTextSize(70);
        crown.setTextColor(Color.CYAN);
        crown.setGravity(Gravity.CENTER);
        layout.addView(crown);

        TextView title = new TextView(this);
        title.setText("ДУГОНИК AI");
        title.setTextSize(36);
        title.setTextColor(Color.WHITE);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        TextView slogan = new TextView(this);
        slogan.setText("❤️\nҲамеша дар паҳлӯи ту");
        slogan.setTextSize(22);
        slogan.setTextColor(Color.CYAN);
        slogan.setGravity(Gravity.CENTER);
        layout.addView(slogan);

        setContentView(layout);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(
                SplashActivity.this, MainActivity.class
            ));
            finish();
        }, 5000);
    }
}
