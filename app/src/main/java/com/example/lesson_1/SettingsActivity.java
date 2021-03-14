package com.example.lesson_1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial mSwitchDarkTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        Boolean isNightTheme = intent.getBooleanExtra(MainActivity.NAME_KEY_NIGTH_THEME,false);

        if (isNightTheme) {
            setTheme(R.style.MyThemeDark);
        } else {
            setTheme(R.style.MyTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSwitchDarkTheme = findViewById(R.id.switchDarkTheme);
        mSwitchDarkTheme.setChecked(isNightTheme);
        MaterialButton buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener((view) -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        MaterialButton buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener((view) -> {
            Intent result = new Intent();
            result.putExtra(MainActivity.NAME_KEY_NIGTH_THEME, mSwitchDarkTheme.isChecked());
            setResult(RESULT_OK, result);
            finish();
        });

    }
}