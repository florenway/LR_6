package com.example.voloshenko;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

// Главный экран. Кнопки категорий собираются из таблицы categories,
// поэтому если в базе 4 категории — на экране тоже 4 кнопки.
public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        showCategoryButtons();
    }

    private void showCategoryButtons() {
        LinearLayout container = findViewById(R.id.categoryButtons);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + DatabaseHelper.COLUMN_ID + ", "
                + DatabaseHelper.COLUMN_NAME
                + " from " + DatabaseHelper.TABLE_CATEGORIES
                + " order by " + DatabaseHelper.COLUMN_ID, null);

        int margin = (int) (8 * getResources().getDisplayMetrics().density);

        while (cursor.moveToNext()) {
            final long categoryId = cursor.getLong(0);
            final String categoryName = cursor.getString(1);

            Button button = new Button(this);
            button.setText(categoryName);
            // без этого AppCompat пишет «ЕДА» вместо «Еда»
            button.setAllCaps(false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = margin;
            button.setLayoutParams(params);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                    intent.putExtra(ProductsActivity.EXTRA_CATEGORY_ID, categoryId);
                    intent.putExtra(ProductsActivity.EXTRA_CATEGORY_NAME, categoryName);
                    startActivity(intent);
                }
            });

            container.addView(button);
        }

        cursor.close();
        db.close();
    }
}
