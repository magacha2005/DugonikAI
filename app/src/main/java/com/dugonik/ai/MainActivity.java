package com.dugonik.ai;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.text.InputType;
import android.widget.*;
import org.json.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class MainActivity extends Activity {

    LinearLayout messages;
    EditText input, apiKey;
    JSONArray history = new JSONArray();
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(16, 16, 16, 16);
        root.setBackgroundColor(Color.rgb(20, 25, 40));

        TextView title = new TextView(this);
        title.setText("ДУГОНИК AI ❤️");
        title.setTextSize(26);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        apiKey = new EditText(this);
        apiKey.setSingleLine(true);
        apiKey.setHint("Калиди Groq-ро ин ҷо гузор");
        apiKey.setTextColor(Color.WHITE);
        apiKey.setHintTextColor(Color.LTGRAY);
        apiKey.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        root.addView(apiKey);

        ScrollView scroll = new ScrollView(this);
        messages = new LinearLayout(this);
        messages.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(messages);

        root.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1));

        addMessage("Салом! Ман ДУГОНИК AI ҳастам. ❤️", false);

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
            String question = input.getText().toString().trim();
            String key = apiKey.getText().toString().trim();

            if (key.isEmpty()) {
                addMessage("Аввал калиди Groq-ро ворид кун.", false);
                return;
            }

            if (question.isEmpty()) return;

            input.setText("");
            addMessage(question, true);
            send.setEnabled(false);

            try {
                if (history.length() == 0) {
                    JSONObject system = new JSONObject();
                    system.put("role", "system");
                    system.put("content",
                        "Ту ДУГОНИК AI ҳастӣ, дугоники меҳрубон ва самимӣ. " +
                        "Ҳамеша бо забони тоҷикӣ ҷавоб деҳ. " +
                        "Ҷавобҳоят равшан ва дӯстона бошанд.");
                    history.put(system);
                }

                JSONObject user = new JSONObject();
                user.put("role", "user");
                user.put("content", question);
                history.put(user);

                JSONObject body = new JSONObject();
                body.put("model", "llama-3.3-70b-versatile");
                body.put("messages", history);
                body.put("temperature", 0.7);
                body.put("max_tokens", 700);

                String requestBody = body.toString();

                executor.execute(() -> {
                    String answer = askGroq(key, requestBody);

                    runOnUiThread(() -> {
                        addMessage(answer, false);

                        try {
                            JSONObject reply = new JSONObject();
                            reply.put("role", "assistant");
                            reply.put("content", answer);
                            history.put(reply);
                        } catch (Exception ignored) {}

                        send.setEnabled(true);
                    });
                });

            } catch (Exception e) {
                addMessage("Хато: " + e.getMessage(), false);
                send.setEnabled(true);
            }
        });

        root.addView(bottom);
        setContentView(root);
    }

    String askGroq(String key, String body) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(
                "https://api.groq.com/openai/v1/chat/completions"
            );

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true);
            connection.setRequestProperty(
                "Content-Type", "application/json"
            );
            connection.setRequestProperty(
                "Authorization", "Bearer " + key
            );

            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            int code = connection.getResponseCode();

            InputStream stream = code >= 200 && code < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int count;

            while ((count = stream.read(buffer)) != -1) {
                result.write(buffer, 0, count);
            }
            stream.close();

            String response = result.toString("UTF-8");
            JSONObject json = new JSONObject(response);

            if (code < 200 || code >= 300) {
                return "Хатои Groq (" + code + "): " +
                    json.optJSONObject("error")
                        .optString("message", "Номаълум");
            }

            return json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

        } catch (Exception e) {
            return "Пайвастшавӣ нашуд: " + e.getMessage();
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    void addMessage(String text, boolean mine) {
        TextView message = new TextView(this);
        message.setText(text);
        message.setTextSize(16);
        message.setTextColor(Color.WHITE);
        message.setPadding(14, 12, 14, 12);
        message.setBackgroundColor(
            mine ? Color.rgb(35, 100, 180)
                 : Color.rgb(55, 65, 85)
        );

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(-2, -2);
        params.setMargins(5, 8, 5, 8);
        params.gravity = mine ? Gravity.RIGHT : Gravity.LEFT;

        messages.addView(message, params);
    }

    @Override
    protected void onDestroy() {
        executor.shutdownNow();
        super.onDestroy();
    }
}
