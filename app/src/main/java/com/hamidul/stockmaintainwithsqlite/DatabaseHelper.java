package com.hamidul.stockmaintainwithsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "my_database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table stock (id INTEGER primary key autoincrement, sku TEXT, unit INTEGER, time DOUBLE)");
        db.execSQL("create table purchase (id INTEGER primary key autoincrement, sku TEXT, unit INTEGER, time DOUBLE)");
        db.execSQL("create table sell (id INTEGER primary key autoincrement, sku TEXT, unit INTEGER, time DOUBLE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists stock");
        db.execSQL("drop table if exists purchase");
        db.execSQL("drop table if exists sell");

    }




    public Cursor getAllAddData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from addData",null);
        return cursor;
    }


    public void updateStock(String sku, int unit){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update stock set unit = "+unit+" where sku = '"+sku+"' ");

    }


    public int getStockOldUnit (String key){

        int oldUnit = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from stock where sku like '"+key+"' ",null);

        while (cursor.moveToNext()){
            oldUnit = cursor.getInt(2);
        }

        return oldUnit;
    }



    public void insertStock (String sku, int unit){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sku",sku);
        contentValues.put("unit",unit);
        contentValues.put("time",System.currentTimeMillis());

        db.insert("stock",null,contentValues );

    }



    public void insertSell (String sku, int unit){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sku",sku);
        contentValues.put("unit",unit);
        contentValues.put("time",System.currentTimeMillis());

        db.insert("sell",null,contentValues );

    }

    public void insertPurchase (String sku, int unit){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sku",sku);
        contentValues.put("unit",unit);
        contentValues.put("time",System.currentTimeMillis());

        db.insert("purchase",null,contentValues );

    }

    public Cursor getAllStockData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from stock",null);
        return cursor;
    }

    public Cursor getAllPurchaseData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from purchase",null);
        return cursor;
    }

    public Cursor getAllSellData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from sell",null);
        return cursor;
    }

    public void deleteById (String name){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from my_table where name like '"+name+"' ");

    }


}
