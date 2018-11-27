package com.niklas.cp.citypopulation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FinalHandler extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelpers";

    private static final String TABLE_NAME = "driver_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "location";
    private static final String COL4 = "pictureid";
    private static final String COL5 = "highscore";

    public FinalHandler(Context context){
        super(context, TABLE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){

        String create_Table = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT ,"+ COL3 + " TEXT ," +
                COL4 + " INTEGER ," + COL5 + " INTEGER)";
        db.execSQL(create_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean addData(String name,String location,int picID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,name);
        contentValues.put(COL3,location);
        contentValues.put(COL4,picID);
        Log.d(TAG,"addData: Adding" + name + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor showData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME + " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query,null);
        return data;
    }
    public void updateName(String newName, String newLocation, int newPicID, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + newName + "' WHERE " + COL1 + " ='" + id +"'";
        String query2 = "UPDATE " + TABLE_NAME + " SET " + COL3 + " = '" + newLocation + "' WHERE " + COL1 + " ='" + id +"'";
        String query3 = "UPDATE " + TABLE_NAME + " SET " + COL4 + " = '" + newPicID + "' WHERE " + COL1 + " ='" + id +"'";
        Log.d(TAG, "updateName: query:" + query);
        Log.d(TAG,"updateName: Setting Name to " + newName);
        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
    }
    public void updateHighscore(int newHighscore){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL5 + " = '" + newHighscore + "' WHERE " + COL1 + " ='" + 1 +"'";
        Log.d(TAG,"updatingHighscore: query: " + query);
        Log.d(TAG,"updatingHighscore: Setting Highscore to " + newHighscore);
        db.execSQL(query);
    }
    public void deleteName(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " ='" + id + "'";
        Log.d(TAG,"deleteName: deleting name from ID" + id);
        db.execSQL(query);
    }



}