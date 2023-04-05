package com.example.lab06;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {
    //static names for columns, version name self explanatory
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "prodcuts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_SKU = "SKU";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //create a table with the specified table columns of id, prod name and SKU (barcode)
    @Override
    public void onCreate(SQLiteDatabase db) {
        //executes SQL code to create a table with name table products id column with primary key
        //(basically increments up everytime a new row added) product name column that takes string
        //and sky column that takes integer
            String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                    TABLE_PRODUCTS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
                    + " TEXT," + COLUMN_SKU + " INTEGER" + ")";
            db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    public void addProduct  (Product product){
        //get the database in write mode i.e we are writting to the db with this object while open
        SQLiteDatabase db = this.getWritableDatabase();
        //insert into the columns sku and product name id auto gens
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_SKU, product.getSku());
        //finally insert into the table properly
        db.insert(TABLE_PRODUCTS,null,values);
        //stop writting
        db.close();
    }

    public Product findProduct(String productName){
        //get db in read mode
        SQLiteDatabase db = this.getReadableDatabase();
        //perform a query basically select all from the table where the name is == to first param
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = \"" + productName + "\"";
        //make it read
        Cursor cursor = db.rawQuery(query, null);
        Product product = new Product();

        if(cursor.moveToFirst()){
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setSku(Integer.parseInt(cursor.getString(2)));
        }
        else{
            product = null;
        }
        db.close();
        return product;
    }

    public boolean deleteProduct(String productName){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = \"" + productName + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            String idStr = cursor.getString(0);
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = " + idStr, null);
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //executes SQL code to drop the products table on upgrade and make a new one!
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }
}
