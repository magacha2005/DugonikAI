package com.dugonik.ai;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    LinearLayout messages;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 20, 20, 20);
        root.setBackgroundColor(Color.rgb(20, 25, 40));

        TextView title = new TextView(this);
        title.setText("ДУГОНИК AI ❤️");
        title.setTextSize(27);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        ScrollView scroll = new ScrollView(this);
        messages = new LinearLayout(this);
        messages.setOrientation(LinearLayout.VERTICAL);
        messages.setPadding(5, 20, 5, 20);
        scroll.addView(messages);

        root.addView(scroll, new LinearLayout.LayoutParams(
            -1, 0, 1
        ));

        addMessage("Салом! Ман дугоники ту ҳастам ❤️", false);

        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.HORIZONTAL);

        input = new EditText(this);
        input.setHint("Ба ман навис...");
        input.setTextColor(Color.WHITE);
        input.setHintTextColor(Color.LTGRAY);
        bottom.addView(input, new LinearLayout.LayoutParams(0, -2, 1));

        Button send = new Button(this);
        send.setText("Фирист");
        bottom.addView(send);

        send.setOnClickListener(v -> {
            String message = input.getText().toString().trim();

            if (!message.isEmpty()) {
                addMessage(message, true);
                input.setText("");
                addMessage("Паёматро гирифтам ❤️ Ҳоло пайвасти зеҳни сунъӣ танзим нашудааст.", false);
            }
        });

        root.addView(bottom);
        setContentView(root);
    }

    void addMessage(String text, boolean mine) {
        TextView message = new TextView(this);
        message.setText(text);
        message.setTextSize(17);
        message.setTextColor(Color.WHITE);
        message.setPadding(15, 12, 15, 12);
        message.setBackgroundColor(
            mine ? Color.rgb(35, 100, 180) : Color.rgb(55, 65, 85)
        );

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(-2, -2);
        params.setMargins(5, 8, 5, 8);
        params.gravity = mine ? Gravity.RIGHT : Gravity.LEFT;

        messages.addView(message, params);
    }
}
