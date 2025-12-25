package com.example.memoria;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemoryGameActivity extends AppCompatActivity {

    private ImageButton[] cards = new ImageButton[8];

    // По две ссылки на каждую из четырёх картинок
    private Integer[] cardImageIds = {
            R.drawable.img0, R.drawable.img0,
            R.drawable.img1, R.drawable.img1,
            R.drawable.img2, R.drawable.img2,
            R.drawable.img3, R.drawable.img3
    };

    private int firstCardIndex = -1;
    private boolean isBusy = false;
    private int matchesFound = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        // Перемешиваем карточки
        shuffleCards();

        // Инициализируем ImageButton и вешаем обработчик
        for (int i = 0; i < cards.length; i++) {
            int resID = getResources().getIdentifier("card" + i, "id", getPackageName());
            cards[i] = findViewById(resID);
            final int index = i;
            cards[i].setOnClickListener(v -> onCardClicked(index));
        }
    }

    private void shuffleCards() {
        List<Integer> list = Arrays.asList(cardImageIds);
        Collections.shuffle(list);
        list.toArray(cardImageIds);
    }

    private void onCardClicked(int index) {
        if (isBusy) return;                      // ждём окончания анимации
        if (cards[index].getTag() != null) return; // эта карта уже угадана

        // Показываем «лицевая» сторона карты
        cards[index].setImageResource(cardImageIds[index]);

        if (firstCardIndex == -1) {
            // первый клик
            firstCardIndex = index;
        } else {
            // второй клик
            isBusy = true;
            int secondIndex = index;
            if (cardImageIds[firstCardIndex].equals(cardImageIds[secondIndex])) {
                // пара найдена
                cards[firstCardIndex].setTag("matched");
                cards[secondIndex].setTag("matched");
                matchesFound++;
                resetSelection();

                // если найдены все пары — победа!
                if (matchesFound == cardImageIds.length / 2) {
                    Toast.makeText(this, "Вы выиграли!", Toast.LENGTH_SHORT).show();
                    // через 2 сек возвращаемся в главное меню
                    new Handler().postDelayed(this::finish, 2000);
                }
            } else {
                // не пара — через секунду скрываем обратно
                new Handler().postDelayed(() -> {
                    cards[firstCardIndex].setImageResource(R.drawable.card_back);
                    cards[secondIndex].setImageResource(R.drawable.card_back);
                    resetSelection();
                }, 1000);
            }
        }
    }

    private void resetSelection() {
        firstCardIndex = -1;
        isBusy = false;
    }

    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_center_from_left, R.anim.slide_in_right_from_center);
    }

    public void goToProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_center_from_right, R.anim.slide_in_left_from_center);
    }

    public void goToSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_center_from_right, R.anim.slide_in_right_from_center);
    }
}
