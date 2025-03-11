package com.polkasol.mobileplanner;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TodayPlansActivity extends AppCompatActivity {


    private static final String PREFS_NAME = "Prefs"; // Имя файла настроек
    private SharedPreferences sharedPreferences;

    private boolean isChecked = false;
    private ImageView checkCircle;

    private EditText[] taskFields = new EditText[7]; // Массив полей

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_plans_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        RelativeLayout layout = findViewById(R.id.relativeLayout);

        int previousId = R.id.dateArea;
        int startHour = 8;
        int endHour = 23;

        for (int i = startHour; i <= endHour; i++) {
            // Создаем TextView (часы)
            TextView textView = new TextView(this);
            textView.setId(1000 + i);
            textView.setText(i + ":00");
            textView.setTextSize(14);
            textView.setTextColor(getResources().getColor(R.color.textColor, getTheme()));
            textView.setGravity(android.view.Gravity.CENTER);

            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                    dpToPx(40), dpToPx(30)
            );
            textParams.setMargins(dpToPx(20), 0, 0, 0);

            if (previousId == R.id.dateArea) {
                textParams.addRule(RelativeLayout.BELOW, previousId);
                textParams.setMargins(dpToPx(20), dpToPx(50), 0, 0);
            } else {
                textParams.addRule(RelativeLayout.BELOW, previousId);
                textParams.setMargins(dpToPx(20), dpToPx(12), 0, 0);
            }

            textView.setLayoutParams(textParams);
            layout.addView(textView);

            // Создаем CardView
            CardView cardView = new CardView(this);
            cardView.setId(2000 + i);
            cardView.setCardElevation(dpToPx(8));

            RelativeLayout.LayoutParams cardParams = new RelativeLayout.LayoutParams(
                    dpToPx(140), dpToPx(35)
            );
            cardParams.addRule(RelativeLayout.ALIGN_TOP, textView.getId());
            cardParams.setMargins(dpToPx(60), 0, 0, 0);
            cardView.setLayoutParams(cardParams);

            // Создаем EditText
            EditText editText = new EditText(this);
            editText.setId(3000 + i);
            editText.setTextSize(12);
            editText.setBackgroundColor(Color.TRANSPARENT);

            RelativeLayout.LayoutParams editParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            editParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            editText.setLayoutParams(editParams);
            cardView.addView(editText);
            layout.addView(cardView);

            // Загружаем сохранённые данные
            String savedText = sharedPreferences.getString("text_" + i, "");
            editText.setText(savedText);

            previousId = textView.getId();
        }


        previousId = R.id.dateArea;
        for (int i = 0; i < 7; i++) {
            // Создаем `ImageView` (чекбокс)
            ImageView checkCircle = new ImageView(this);
            checkCircle.setId(3000 + i);
            RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(
                    dpToPx(30), dpToPx(30));
            imgParams.setMargins(dpToPx(220), (i == 0) ? dpToPx(87) : dpToPx(12), 0, 0);
            imgParams.addRule(RelativeLayout.BELOW, previousId);
            checkCircle.setLayoutParams(imgParams);
            checkCircle.setImageResource(R.drawable.circle_gray);

            // Обработчик клика (смена цвета)
            checkCircle.setOnClickListener(new View.OnClickListener() {
                boolean isChecked = false;

                @Override
                public void onClick(View v) {
                    isChecked = !isChecked;
                    checkCircle.setImageResource(isChecked ? R.drawable.circle_green : R.drawable.circle_gray);
                }
            });

            layout.addView(checkCircle);

            // Создаем `CardView` (поле ввода)
            CardView cardView = new CardView(this);
            cardView.setId(2000 + i);
            cardView.setCardElevation(dpToPx(8));

            RelativeLayout.LayoutParams cardParams = new RelativeLayout.LayoutParams(
                    dpToPx(140), dpToPx(35));
            cardParams.addRule(RelativeLayout.ALIGN_TOP, checkCircle.getId());
            cardParams.setMargins(dpToPx(255), 0, 0, 0);
            cardView.setLayoutParams(cardParams);

            // Создаем `EditText`
            EditText editText = new EditText(this);
            editText.setId(3000 + i);
            editText.setTextSize(12);
            editText.setBackgroundColor(Color.TRANSPARENT);
            editText.setGravity(android.view.Gravity.CENTER);

            RelativeLayout.LayoutParams editParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            editParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            editText.setLayoutParams(editParams);
            cardView.addView(editText);
            layout.addView(cardView);

            // Загружаем сохранённые данные
            String savedText = sharedPreferences.getString("text_" + i, "");
            editText.setText(savedText);

            previousId = checkCircle.getId(); // Обновляем предыдущий ID
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData(); // Вызываем только один метод
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Сохраняем текст в задачах (7 элементов)
        for (int i = 0; i < 7; i++) {
            EditText editText = findViewById(3000 + i);
            if (editText != null) {
                editor.putString("task_" + i, editText.getText().toString()); // Используем уникальные ключи
            }
        }

        // Сохраняем текст в часовом диапазоне (8:00 - 23:00)
        for (int i = 8; i <= 23; i++) {
            EditText editText = findViewById(4000 + i); // Убедись, что ID соответствует тем, что в XML
            if (editText != null) {
                editor.putString("hour_" + i, editText.getText().toString()); // Уникальные ключи
            }
        }

        editor.apply(); // Сохранение в SharedPreferences
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}