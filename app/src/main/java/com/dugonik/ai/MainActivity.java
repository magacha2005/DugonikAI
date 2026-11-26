
package com.dugonik.ai;

import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import java.util.ArrayList;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.InputType;
import android.content.res.ColorStateList;
import org.json.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class MainActivity extends Activity {

    LinearLayout messages;
    EditText input, apiKey;
    ScrollView scroll;
    JSONArray history = new JSONArray();
    SharedPreferences prefs;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    SpeechRecognizer speechRecognizer;
    boolean isListening = false;

    int bg = Color.rgb(5, 10, 32);
    int panel = Color.rgb(14, 24, 52);
    int blue = Color.rgb(25, 110, 210);
    int cyan = Color.rgb(65, 220, 255);

    GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);

        prefs = getSharedPreferences("dugonik_memory", MODE_PRIVATE);
        history = loadHistory();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(bg);
        root.setPadding(12, 8, 12, 8);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(10, 10, 10, 10);
        header.setBackground(shape(panel, 24));

        ImageView avatar = new ImageView(this);
        avatar.setImageResource(R.drawable.dugonik_profile);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(65, 65);
        ap.rightMargin = 12;
        header.addView(avatar, ap);

        LinearLayout names = new LinearLayout(this);
        names.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(this);
        title.setText("ДУГОНИК AI ❤️");
        title.setTextSize(21);
        title.setTextColor(Color.WHITE);
        title.setTypeface(null, Typeface.BOLD);
        names.addView(title);

        TextView online = new TextView(this);
        online.setText("● Ҳамеша дар паҳлӯи ту");
        online.setTextSize(13);
        online.setTextColor(cyan);
        names.addView(online);

        header.addView(names, new LinearLayout.LayoutParams(0, -2, 1));
        root.addView(header);

        apiKey = new EditText(this);
        apiKey.setSingleLine(true);
        apiKey.setHint("Калиди Groq-ро ин ҷо гузор");
        apiKey.setTextColor(Color.WHITE);
        apiKey.setHintTextColor(Color.LTGRAY);
        apiKey.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        apiKey.setPadding(16, 8, 16, 8);
        apiKey.setBackground(shape(panel, 18));

        LinearLayout.LayoutParams kp = new LinearLayout.LayoutParams(-1, -2);
        kp.topMargin = 10;
        root.addView(apiKey, kp);

        scroll = new ScrollView(this);
        messages = new LinearLayout(this);
        messages.setOrientation(LinearLayout.VERTICAL);
        messages.setPadding(4, 12, 4, 12);
        scroll.addView(messages);

        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, 0, 1);
        mp.topMargin = 8;
        root.addView(scroll, mp);

        if (history.length() == 0) {
            addMessage("Салом! Ман ДУГОНИК AI ҳастам. ❤️\nҲамеша дар паҳлӯи ту!", false);
        } else {
            for (int i = 0; i < history.length(); i++) {
                JSONObject item = history.optJSONObject(i);
                if (item == null) continue;
                String role = item.optString("role");
                if (role.equals("user") || role.equals("assistant")) {
                    addMessage(item.optString("content"), role.equals("user"));
                }
            }
        }

        LinearLayout bottom = new LinearLayout(this);
        bottom.setGravity(Gravity.CENTER_VERTICAL);
        bottom.setPadding(8, 5, 8, 5);
        bottom.setBackground(shape(panel, 25));

        input = new EditText(this);
        input.setHint("Ба ман навис...");
        input.setTextColor(Color.WHITE);
        input.setHintTextColor(Color.LTGRAY);
        input.setTextSize(15);
        input.setSingleLine(false);
        input.setMaxLines(3);
        input.setBackgroundColor(Color.TRANSPARENT);
        bottom.addView(input, new LinearLayout.LayoutParams(0, -2, 1));

        Button mic = new Button(this);
        mic.setText("🎤");
        mic.setTextSize(18);
        mic.setAllCaps(false);
        mic.setTextColor(Color.WHITE);
        mic.setMinWidth(0);
        mic.setMinHeight(0);
        mic.setPadding(0, 0, 0, 0);
        mic.setBackground(shape(bg, 50));
        LinearLayout.LayoutParams micParams =
                new LinearLayout.LayoutParams(46, 46);
        micParams.leftMargin = 3;
        bottom.addView(mic, micParams);
        mic.setOnClickListener(v -> startVoiceInput());

        Button voice = new Button(this);
        voice.setText("🎧");
        voice.setTextSize(18);
        voice.setAllCaps(false);
        voice.setTextColor(Color.WHITE);
        voice.setMinWidth(0);
        voice.setMinHeight(0);
        voice.setPadding(0, 0, 0, 0);
        voice.setBackground(shape(bg, 50));
        LinearLayout.LayoutParams voiceParams =
                new LinearLayout.LayoutParams(46, 46);
        voiceParams.leftMargin = 3;
        bottom.addView(voice, voiceParams);
        voice.setOnClickListener(v ->
                Toast.makeText(this,
                        "Овози зинда дар қадами оянда фаъол мешавад.",
                        Toast.LENGTH_SHORT).show());

        Button send = new Button(this);
        send.setText("➤");
        send.setTextSize(22);
        send.setAllCaps(false);
        send.setTextColor(Color.WHITE);
        send.setMinWidth(0);
        send.setMinHeight(0);
        send.setPadding(0, 0, 0, 0);
        send.setBackground(shape(blue, 50));
        LinearLayout.LayoutParams sendParams =
                new LinearLayout.LayoutParams(46, 46);
        sendParams.leftMargin = 3;
        bottom.addView(send, sendParams);

        LinearLayout.LayoutParams bp =
                new LinearLayout.LayoutParams(-1, -2);
        bp.topMargin = 8;
        root.addView(bottom, bp);

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
                saveHistory();

                JSONObject body = new JSONObject();
                body.put("model", "openai/gpt-oss-120b");
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
                            saveHistory();
                        } catch (Exception ignored) {}

                        send.setEnabled(true);
                    });
                });

            } catch (Exception e) {
                addMessage("Хато: " + e.getMessage(), false);
                send.setEnabled(true);
            }
        });

        setContentView(root);
        scroll.post(() -> scroll.fullScroll(ScrollView.FOCUS_DOWN));
    }


    void startVoiceInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            addMessage("Хидмати шинохтани овоз дар ин телефон дастрас нест.", false);
            return;
        }

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.RECORD_AUDIO}, 101);
            return;
        }

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            public void onReadyForSpeech(Bundle params) {
                addMessage("Мешунавам... гап зан 🎤", false);
            }
            public void onBeginningOfSpeech() {}
            public void onRmsChanged(float rmsdB) {}
            public void onBufferReceived(byte[] buffer) {}
            public void onEndOfSpeech() {}
            public void onError(int error) {
                addMessage("Овоз шинохта нашуд. Боз кӯшиш кун.", false);
            }
            public void onResults(Bundle results) {
                ArrayList<String> words =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (words != null && !words.isEmpty()) {
                    input.setText(words.get(0));
                    input.setSelection(input.getText().length());
                }
            }
            public void onPartialResults(Bundle results) {}
            public void onEvent(int eventType, Bundle params) {}
        });

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tg-TJ");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "tg-TJ");
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);

        speechRecognizer.startListening(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startVoiceInput();
        } else if (requestCode == 101) {
            addMessage("Барои истифодаи микрофон иҷозат лозим аст.", false);
        }
    }

    JSONArray loadHistory() {
        try {
            return new JSONArray(prefs.getString("history", "[]"));
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    void saveHistory() {
        prefs.edit().putString("history", history.toString()).apply();
    }

    String askGroq(String key, String body) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL("https://api.groq.com/openai/v1/chat/completions");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + key);

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
                JSONObject error = json.optJSONObject("error");
                String message = error != null
                        ? error.optString("message", "Номаълум")
                        : "Номаълум";
                return "Хатои Groq (" + code + "): " + message;
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
        message.setPadding(16, 12, 16, 12);
        message.setBackground(shape(
                mine ? blue : panel, 22));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -2, -2);
        params.setMargins(5, 7, 5, 7);
        params.gravity = mine ? Gravity.RIGHT : Gravity.LEFT;
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.78);

        messages.addView(message, params);

        if (scroll != null) {
            scroll.post(() -> scroll.fullScroll(ScrollView.FOCUS_DOWN));
        }
    }

    @Override
    protected void onDestroy() {
        executor.shutdownNow();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
        super.onDestroy();
    }
}
