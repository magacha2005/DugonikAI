package com.dugonik.ai;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.animation.ValueAnimator;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(5, 8, 35));
        getWindow().setNavigationBarColor(Color.rgb(5, 8, 35));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(30, 30, 30, 30);

        GradientDrawable bg = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{
                Color.rgb(5, 8, 35),
                Color.rgb(12, 25, 75),
                Color.rgb(30, 5, 65)
            }
        );
        root.setBackground(bg);

        TextView crown = new TextView(this);
        crown.setText("♛");
        crown.setTextSize(70);
        crown.setTextColor(Color.CYAN);
        crown.setGravity(Gravity.CENTER);
        root.addView(crown);

        TextView logo = new TextView(this);
        logo.setText("ДУГОНИК AI");
        logo.setTextSize(36);
        logo.setTextColor(Color.WHITE);
        logo.setTypeface(Typeface.DEFAULT_BOLD);
        logo.setGravity(Gravity.CENTER);
        root.addView(logo);

        TextView heart = new TextView(this);
        heart.setText("❤️");
        heart.setTextSize(55);
        heart.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams hp =
            new LinearLayout.LayoutParams(-1, -2);
        hp.topMargin = 30;
        root.addView(heart, hp);

        TextView slogan = new TextView(this);
        slogan.setText("Ҳамеша дар паҳлӯи ту");
        slogan.setTextSize(20);
        slogan.setTextColor(Color.CYAN);
        slogan.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams sp =
            new LinearLayout.LayoutParams(-1, -2);
        sp.topMargin = 25;
        root.addView(slogan, sp);

        TextView loading = new TextView(this);
        loading.setText("Дугоник омода мешавад...");
        loading.setTextSize(16);
        loading.setTextColor(Color.WHITE);
        loading.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = 55;
        root.addView(loading, lp);

        ProgressBar progress = new ProgressBar(
            this, null, android.R.attr.progressBarStyleHorizontal
        );
        progress.setMax(100);
        progress.setProgress(0);
        progress.setProgressTintList(
            android.content.res.ColorStateList.valueOf(Color.CYAN)
        );

        LinearLayout.LayoutParams pp =
            new LinearLayout.LayoutParams(-1, 18);
        pp.topMargin = 20;
        root.addView(progress, pp);

        TextView percent = new TextView(this);
        percent.setText("0%");
        percent.setTextSize(22);
        percent.setTextColor(Color.WHITE);
        percent.setTypeface(Typeface.DEFAULT_BOLD);
        percent.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams per =
            new LinearLayout.LayoutParams(-1, -2);
        per.topMargin = 12;
        root.addView(percent, per);

        TextView footer = new TextView(this);
        footer.setText("❤️  Мо ҳамеша бо ту ҳастем  ❤️");
        footer.setTextSize(15);
        footer.setTextColor(Color.LTGRAY);
        footer.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams fp =
            new LinearLayout.LayoutParams(-1, -2);
        fp.topMargin = 45;
        root.addView(footer, fp);

        setContentView(root);

        logo.setAlpha(0f);
        logo.setScaleX(0.7f);
        logo.setScaleY(0.7f);
        logo.animate().alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(1200).start();

        ValueAnimator pulse = ValueAnimator.ofFloat(1f, 1.2f);
        pulse.setDuration(650);
        pulse.setRepeatMode(ValueAnimator.REVERSE);
        pulse.setRepeatCount(ValueAnimator.INFINITE);
        pulse.addUpdateListener(a -> {
            float v = (float) a.getAnimatedValue();
            heart.setScaleX(v);
            heart.setScaleY(v);
        });
        pulse.start();

        ValueAnimator bar = ValueAnimator.ofInt(0, 100);
        bar.setDuration(4000);
        bar.setInterpolator(new AccelerateDecelerateInterpolator());

        bar.addUpdateListener(a -> {
            int value = (int) a.getAnimatedValue();
            progress.setProgress(value);
            percent.setText(value + "%");
        });

        bar.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                pulse.cancel();
                startActivity(new Intent(
                    SplashActivity.this, MainActivity.class
                ));
                finish();
            }
        });

        bar.start();
    }
}
