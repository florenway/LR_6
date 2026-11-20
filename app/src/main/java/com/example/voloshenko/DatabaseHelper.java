package com.example.voloshenko;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Волошенко Кирилл Андреевич, группа Т-433901-ИСТ
// База shop.db: категории и товары. Заполняется один раз, при создании файла.
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shop.db";
    private static final int SCHEMA = 1;

    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CATEGORY_ID = "category_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_PRICE + " INTEGER, "
                + COLUMN_CATEGORY_ID + " INTEGER);");

        // id категорий пойдут 1, 2, 3, 4 — на них ссылается category_id у товаров
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_NAME + ") VALUES ('Еда');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_NAME + ") VALUES ('Одежда');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_NAME + ") VALUES ('Игрушки');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_NAME + ") VALUES ('Канцтовары');");

        addProduct(db, "Хлеб бородинский", "Черный хлеб с кориандром, 400 г", 54, 1);
        addProduct(db, "Молоко 3.2%", "Пастеризованное, 1 литр", 89, 1);
        addProduct(db, "Яблоки Гала", "Сладкие, 1 кг", 129, 1);
        addProduct(db, "Гречка", "Крупа гречневая, 800 г", 97, 1);

        addProduct(db, "Футболка", "Белая, хлопок", 799, 2);
        addProduct(db, "Джинсы", "Синие, прямой крой", 2490, 2);
        addProduct(db, "Куртка", "Демисезонная, с капюшоном", 4590, 2);

        addProduct(db, "Конструктор", "Набор, 120 деталей", 990, 3);
        addProduct(db, "Мяч футбольный", "Размер 5", 650, 3);
        addProduct(db, "Кукла", "В летнем платье", 540, 3);

        addProduct(db, "Тетрадь", "48 листов, клетка", 35, 4);
        addProduct(db, "Ручка шариковая", "Синяя", 25, 4);
        addProduct(db, "Карандаши цветные", "Набор, 12 штук", 120, 4);
    }

    private void addProduct(SQLiteDatabase db, String name, String description, int price, int categoryId) {
        db.execSQL("INSERT INTO " + TABLE_PRODUCTS + " ("
                + COLUMN_NAME + ", " + COLUMN_DESCRIPTION + ", "
                + COLUMN_PRICE + ", " + COLUMN_CATEGORY_ID + ") VALUES ('"
                + name + "', '" + description + "', " + price + ", " + categoryId + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }
}
