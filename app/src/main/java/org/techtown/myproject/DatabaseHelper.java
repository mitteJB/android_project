package org.techtown.myproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ItemData.db"; // 데이터베이스 이름
    private static final int DATABASE_VERSION = 1; // 데이터베이스 버전

    public static final String TABLE_NAME = "ItemTable";

    public static final String ITEM1 = "_id";
    public static final String ITEM2 = "text_main";
    public static final String ITEM3 = "text_edit";
    public static final String ITEM4 = "checkbox";
    public static final String ITEM5 = "cardBackGroundColor";
    public static final String ITEM6 = "time";


    // 생성자
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 데이터베이스 생성될때 호출
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 만들거 들어가야함\
        db.execSQL("create table if not exists " + TABLE_NAME + "("
                + " ITEM1 integer NOT NULL PRIMARY KEY autoincrement, "
                + " ITEM2 text, "
                + " ITEM3 text, "
                + " ITEM4 int, "
                + " ITEM5 int, "
                + " ITEM6 text)");
    }

    // 데이터베이스 열때 호출
    @Override
    public void onOpen(SQLiteDatabase db) {
        showLog("오픈");
        super.onOpen(db);
    }


    // 데이터베이스 업그레이드시 호출
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        showLog("업그레이드");
    }

    public void showLog(String string) {
        Log.d("데이터베이스", string);
    }
}
