package com.dugonik.ai;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.rgb(5, 8, 35));

        TextView text = new TextView(this);
        text.setText("ДУГОНИК AI\nСАНҶИШИ ЭКРАН");
        text.setTextSize(32);
        text.setTextColor(Color.WHITE);
        text.setGravity(Gravity.CENTER);

        layout.addView(text);
        setContentView(layout);
    }
}
