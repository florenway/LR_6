package com.example.voloshenko;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Товары одной категории: название, описание, цена.
public class ProductsActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID = "category_id";
    public static final String EXTRA_CATEGORY_NAME = "category_name";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor productCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        long categoryId = getIntent().getLongExtra(EXTRA_CATEGORY_ID, -1);
        String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        if (categoryName == null) {
            categoryName = "Товары";
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(categoryName);
        }

        TextView title = findViewById(R.id.categoryTitle);
        title.setText(categoryName);

        ListView productList = findViewById(R.id.productList);
        productList.setEmptyView(findViewById(R.id.emptyText));

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // к числу из столбца price дописываем «руб.»
        productCursor = db.rawQuery(
                "select " + DatabaseHelper.COLUMN_ID + ", "
                        + DatabaseHelper.COLUMN_NAME + ", "
                        + DatabaseHelper.COLUMN_DESCRIPTION + ", "
                        + DatabaseHelper.COLUMN_PRICE + " || ' руб.' as price_text "
                        + "from " + DatabaseHelper.TABLE_PRODUCTS + " "
                        + "where " + DatabaseHelper.COLUMN_CATEGORY_ID + " = ? "
                        + "order by " + DatabaseHelper.COLUMN_ID,
                new String[]{String.valueOf(categoryId)});

        String[] from = new String[]{
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_DESCRIPTION,
                "price_text"
        };
        int[] to = new int[]{
                R.id.productName,
                R.id.productDescription,
                R.id.productPrice
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item_product,
                productCursor,
                from,
                to,
                0);
        productList.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (productCursor != null) {
            productCursor.close();
        }
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
