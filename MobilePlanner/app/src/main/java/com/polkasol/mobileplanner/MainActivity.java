package com.polkasol.mobileplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySavedLanguage();
        setContentView(R.layout.main_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Button btnChangeLanguage = findViewById(R.id.changeLanguageButton);
        btnChangeLanguage.setOnClickListener(v -> showLanguageDialog());

        Button todayPlansButton = findViewById(R.id.todayPlansButton);
        todayPlansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodayPlansActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showLanguageDialog() {
        final String[] languages = {"English", "Русский"};
        final String[] locales = {"en", "ru"};

        new AlertDialog.Builder(this)
                .setTitle("Select Language")
                .setItems(languages, (dialog, which) -> {
                    if (!getCurrentLanguage().equals(locales[which])) {
                        setLocale(locales[which]); // Изменяем язык только если он отличается
                    }
                })
                .show();
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Сохраняем выбор
        SharedPreferences.Editor editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", languageCode);
        editor.apply();

        // Перезапускаем активность
        recreate();
    }

    private void applySavedLanguage() {
        SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String savedLanguage = prefs.getString("My_Lang", "en"); // По умолчанию английский
        Locale currentLocale = Locale.getDefault();

        if (!currentLocale.getLanguage().equals(savedLanguage)) {
            Locale locale = new Locale(savedLanguage);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
    }

    private String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }
}