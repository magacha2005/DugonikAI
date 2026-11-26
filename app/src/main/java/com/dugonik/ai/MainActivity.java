package com.dugonik.ai;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.rgb(20, 25, 40));
        layout.setPadding(30, 30, 30, 30);

        TextView face = new TextView(this);
        face.setText("😊");
        face.setTextSize(90);
        face.setGravity(Gravity.CENTER);

        TextView title = new TextView(this);
        title.setText("ДУГОНИК AI");
        title.setTextSize(30);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);

        TextView greeting = new TextView(this);
        greeting.setText("Салом! Ман дугоники ту ҳастам ❤️");
        greeting.setTextSize(18);
        greeting.setTextColor(Color.WHITE);
        greeting.setGravity(Gravity.CENTER);

        layout.addView(face);
        layout.addView(title);
        layout.addView(greeting);

        setContentView(layout);
    }
}
