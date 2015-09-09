package com.abara.stoptherain.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.abara.stoptherain.MainActivity;
import com.abara.stoptherain.R;
import com.abara.stoptherain.utils.PreferenceIds;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Abb on 8/29/2015.
 */
public class MyIntro extends AppIntro2 {

    private View decorView;

    @Override
    public void init(Bundle bundle) {

        addSlide(AppIntroFragment.newInstance("Welcome!",
                "Now stopping the rain is a button click away.",
                R.drawable.ic_intro_one,
                Color.parseColor("#2196F3")));
        addSlide(AppIntroFragment.newInstance("How it works?",
                "Well you can give it a try once.",
                R.drawable.ic_intro_two, Color.parseColor("#4CAF50")));
        addSlide(AppIntroFragment.newInstance("Disclaimer", "The laws of nature cannot be changed. " +
                "This app may or may not stop the rain. " +
                "This is a fun app, developer doesn't take any responsibility if stopping rain from this app causes damage to your life :P"
                , R.drawable.ic_intro_three, Color.parseColor("#FF9800")));
        addSlide(new NameIntroFragment());

        showDoneButton(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        decorView = getWindow().getDecorView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onDonePressed() {

        AppCompatEditText name = (AppCompatEditText) findViewById(R.id.user_txt_input);
        String myname = name.getText().toString();


        if (!TextUtils.isEmpty(myname)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putString(PreferenceIds.USER_NAME_KEY, myname).commit();
            prefs.edit().putBoolean(PreferenceIds.IS_FIRST__RUN, false).commit();

            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            name.setError("cannot be empty");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(1);
    }
}
