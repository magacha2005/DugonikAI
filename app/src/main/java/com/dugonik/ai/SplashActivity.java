
package com.dugonik.ai;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.animation.ValueAnimator;
import android.animation.ObjectAnimator;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;

public class SplashActivity extends Activity {

    TextView logo, heart, percent, slogan;
    ProgressBar progress;
    int blue = Color.rgb(0, 180, 255);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(5, 8, 28));
        getWindow().setNavigationBarColor(Color.rgb(5, 8, 28));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(25, 30, 25, 30);

        GradientDrawable bg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.rgb(5, 8, 35),
                        Color.rgb(12, 25, 75),
                        Color.rgb(30, 5, 65)
                });
        root.setBackground(bg);

        TextView crown = new TextView(this);
        crown.setText("♛");
        crown.setTextSize(65);
        crown.setTextColor(Color.CYAN);
        crown.setGravity(Gravity.CENTER);
        root.addView(crown);

        logo = new TextView(this);
        logo.setText("ДУГОНИК AI");
        logo.setTextSize(38);
        logo.setTypeface(Typeface.DEFAULT_BOLD);
        logo.setTextColor(Color.WHITE);
        logo.setGravity(Gravity.CENTER);
        root.addView(logo);

        heart = new TextView(this);
        heart.setText("❤️");
        heart.setTextSize(55);
        heart.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams hp =
                new LinearLayout.LayoutParams(-1, -2);
        hp.topMargin = 35;
        root.addView(heart, hp);

        slogan = new TextView(this);
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
        lp.topMargin = 65;
        root.addView(loading, lp);

        progress = new ProgressBar(
                this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(100);
        progress.setProgress(0);
        progress.setProgressTintList(
                android.content.res.ColorStateList.valueOf(blue));
        progress.setProgressBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        Color.rgb(65, 75, 110)));
        LinearLayout.LayoutParams pp =
                new LinearLayout.LayoutParams(-1, 18);
        pp.topMargin = 20;
        root.addView(progress, pp);

        percent = new TextView(this);
        percent.setText("0%");
        percent.setTextSize(22);
        percent.setTypeface(Typeface.DEFAULT_BOLD);
        percent.setTextColor(Color.WHITE);
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
        fp.topMargin = 55;
        root.addView(footer, fp);

        setContentView(root);

        logo.setAlpha(0f);
        logo.setScaleX(0.5f);
        logo.setScaleY(0.5f);

        logo.animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(1200).start();

        ObjectAnimator pulseX =
                ObjectAnimator.ofFloat(heart, "scaleX", 1f, 1.25f, 1f);
        ObjectAnimator pulseY =
                ObjectAnimator.ofFloat(heart, "scaleY", 1f, 1.25f, 1f);

        pulseX.setDuration(850);
        pulseY.setDuration(850);
        pulseX.setRepeatCount(ValueAnimator.INFINITE);
        pulseY.setRepeatCount(ValueAnimator.INFINITE);
        pulseX.start();
        pulseY.start();

        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(4000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            progress.setProgress(value);
            percent.setText(value + "%");
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                startActivity(new Intent(
                        SplashActivity.this, MainActivity.class));
                finish();
            }
        });

        animator.start();
    }
}
