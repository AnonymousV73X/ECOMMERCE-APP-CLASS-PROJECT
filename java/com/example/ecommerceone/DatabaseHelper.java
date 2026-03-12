package com.example.ecommerceone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ECommerceDB";
    private static final int DATABASE_VERSION = 2;

    // Product Table
    private static final String TABLE_PRODUCTS = "products";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_DESC = "description";
    private static final String KEY_IMAGE = "image";

    // Cart Table
    private static final String TABLE_CART = "cart";
    private static final String KEY_QUANTITY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating Products Table with Image column
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_DESC + " TEXT,"
                + KEY_IMAGE + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Creating Cart Table
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_ID + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_IMAGE + " INTEGER,"
                + KEY_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_CART_TABLE);

        addInitialProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    private void addInitialProducts(SQLiteDatabase db) {
        ContentValues values;

        // 1. Canon Camera
        values = new ContentValues();
        values.put(KEY_NAME, "Canon EOS 90D DSLR");
        values.put(KEY_PRICE, 157000.00);
        values.put(KEY_DESC, "Latest Canon EOS Model with 18-135mm lens.");
        values.put(KEY_IMAGE, R.drawable.canon_eos_90d_with_18_135mm_usm_ens_bigp70gxst_medium);
        db.insert(TABLE_PRODUCTS, null, values);

        // 2. Sony Headphones
        values = new ContentValues();
        values.put(KEY_NAME, "Sony WH-1000XM4");
        values.put(KEY_PRICE, 24500.00);
        values.put(KEY_DESC, "Industry leading Noise-Cancelling Headphones.");
        values.put(KEY_IMAGE, R.drawable.sony_wh_1000xm4_x9zfjdr0ir_medium);
        db.insert(TABLE_PRODUCTS, null, values);

        // 3. Samsung S24 Ultra
        values = new ContentValues();
        values.put(KEY_NAME, "Samsung Galaxy S24 Ultra");
        values.put(KEY_PRICE, 180000.00);
        values.put(KEY_DESC, "The ultimate Galaxy experience, AI-powered.");
        values.put(KEY_IMAGE, R.drawable.samsung_galaxy_s24_ultra_d_medium);
        db.insert(TABLE_PRODUCTS, null, values);

        // 4. Samsung S25 Ultra
        values = new ContentValues();
        values.put(KEY_NAME, "Samsung Galaxy S25 Ultra");
        values.put(KEY_PRICE, 195000.00);
        values.put(KEY_DESC, "Next generation flagship smartphone.");
        values.put(KEY_IMAGE, R.drawable.samsung_galaxy_s25_ultra_c_medium);
        db.insert(TABLE_PRODUCTS, null, values);

        // 5. Xiaomi 17 Pro Max
        values = new ContentValues();
        values.put(KEY_NAME, "Xiaomi 17 Pro Max");
        values.put(KEY_PRICE, 130000.00);
        values.put(KEY_DESC, "Leica camera system, ultra fast charging.");
        values.put(KEY_IMAGE, R.drawable.xiaomi_17_pro_max_c_medium);
        db.insert(TABLE_PRODUCTS, null, values);

        // 6. Apple MacBook Pro M5
        values = new ContentValues();
        values.put(KEY_NAME, "Apple MacBook Pro M5");
        values.put(KEY_PRICE, 350000.00);
        values.put(KEY_DESC, "Supercharged by the new M5 chip.");
        values.put(KEY_IMAGE, R.drawable.apple_macbook_pro_m5_medium);
        db.insert(TABLE_PRODUCTS, null, values);

        // 7. Sony PS5
        values = new ContentValues();
        values.put(KEY_NAME, "Sony PlayStation 5");
        values.put(KEY_PRICE, 65000.00);
        values.put(KEY_DESC, "Standard Edition Gaming Console.");
        values.put(KEY_IMAGE, R.drawable.sonyps5standardad_medium);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    // --- ''''CART'''' LOGIC ---

    // Checking if item exists in cart
    public boolean isItemInCart(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{KEY_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(productId)}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void addToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, product.getId());
        values.put(KEY_NAME, product.getName());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_IMAGE, product.getImageResource());
        values.put(KEY_QUANTITY, 1);
        db.insert(TABLE_CART, null, values);
        db.close();
    }

    // Getting total items count for FAB
    public int getCartCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }

    public void deleteCartItem(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // Getting products (for listing)
    public List<Product> getAllProducts(String searchQuery) {
        List<Product> productList = new ArrayList<>();
        String selectQuery;

        if (searchQuery == null || searchQuery.isEmpty()) {
            selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        } else {
            selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_NAME + " LIKE '%" + searchQuery + "%'";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getInt(4) // Image index
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public List<Product> getCartItems() {
        List<Product> cartList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Columns in TABLE_CART: ID(0), Name(1), Price(2), Image(3), Quantity(4)
                Product product = new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        "Qty: " + cursor.getInt(4), // Using Quantity for description in Cart
                        cursor.getInt(3) // Image Resource
                );
                cartList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartList;
    }
}