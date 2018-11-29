package org.techtown.ideaconcert.SQLiteDir;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createRecentViewTable();
        createContentsUrlTable();
        Log.e("onCreate", "테이블 생성 완료");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertOrUpdateRecentViewData(int contents_pk, String contents_name, String date, String date_for_compare, int contents_num) {
        try {
            String sql = "SELECT contents_pk FROM recent_view WHERE contents_pk = " + contents_pk + " AND contents_num = " + contents_num +";";
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToNext();
            db = getWritableDatabase();
            if (cursor.getCount() != 0) {
                sql = "UPDATE recent_view SET date_for_compare = '" + date_for_compare + "' WHERE contents_pk = " + contents_pk + " AND contents_num = " + contents_num + ";";
                db.execSQL(sql);
                Log.e("addOrUpdate", "최근작품목록 업데이트 성공");
            } else {
                sql = "INSERT INTO recent_view(contents_pk, contents_name, view_date, date_for_compare, contents_num) VALUES (?, ?, ?, ?, ?)";
                db.execSQL(sql, new Object[]{
                        contents_pk, contents_name, date, date_for_compare, contents_num
                });
                Log.e("addOrUpdate", "최근작품목록 삽입 성공 " + contents_name + " " + contents_num);
            }
        } catch (SQLException se) {
            Log.e("addOrUpdate", "" + se.getMessage());
        }
    }

    public void insertContentsUrl (int contents_pk, String url) {
        try {

            String sql = "SELECT contents_pk FROM contents_url WHERE contents_pk = " + contents_pk + " AND url = '" + url +"';";
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() != 0) {
                db = getWritableDatabase();
                sql = "UPDATE contents_url SET url = '" + url + "' WHERE contents_pk = " + contents_pk + ";";
                db.execSQL(sql);
                Log.e("insertContentUrl", "url 업데이트 성공");
            } else {
                db = getWritableDatabase();
                sql = "INSERT INTO contents_url(contents_pk, url) VALUES(?, ?)";
                db.execSQL(sql, new Object[] {
                        contents_pk, url
                } );
                Log.e("insertContentUrl", "url 삽입 성공 " + contents_pk + " " + url);
                db.close();
            }
        } catch (SQLException se) {
            Log.e("insertUrlException", se.getMessage());
        }
    }
    public void createRecentViewTable() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "CREATE TABLE recent_view(contents_pk INTEGER, contents_name VARCHAR(20), view_date DATETIME, date_for_compare DATETIME, contents_num INTEGER, contents_url VARCHAR(100), PRIMARY KEY(contents_pk, contents_num));";
        db.execSQL(sql);
        Log.e("createRecentViewTable", "테이블 생성 완료");
    }

    public void createContentsUrlTable() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "CREATE TABLE contents_url(contents_pk INTEGER, url VARCHAR(100), PRIMARY KEY(contents_pk, url));";
        db.execSQL(sql);
        Log.e("createContentsUrlTable", "테이블 생성 완료");
    }

    public void dropTable(String table_name) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DROP TABLE " + table_name;
        db.execSQL(sql);
        Log.e("dropTable", "테이블 드랍 완료 " + table_name);
    }

    public void dropAllTables() {
        dropTable("recent_view");
        dropTable("contents_url");
    }

    public ArrayList<Integer> getReadContentsNum(int contents_pk) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            ArrayList<Integer> data = new ArrayList<>();
            String sql = "SELECT contents_num FROM recent_view WHERE contents_pk = " + contents_pk + ";";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                int contents_num = cursor.getInt(0);
                data.add(contents_num);
            }
            return data;
        } catch (SQLException se) {
            Log.e("열람목록번호리턴오류", se.getMessage());
            return new ArrayList<>();
        }
    }

    public ArrayList<RecentViewData> getAllRecentViewData() {
        try {
            String sql = "SELECT rv.contents_pk, contents_name, view_date, MAX(date_for_compare), contents_num, url FROM recent_view rv, contents_url cu WHERE rv.contents_pk = cu.contents_pk GROUP BY rv.contents_pk";
//            String sql = "SELECT contents_pk, contents_name, view_date, contents_num FROM recent_view";
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<RecentViewData> data = new ArrayList<>();
            Log.e("getAllRecentViewData", ""+cursor.getCount());
            while (cursor.moveToNext()) {
                int contents_pk = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                int num = cursor.getInt(4);
                String url = cursor.getString(5);
                Log.e("name = " + name + " num = " + num + "url= ", url);
                data.add(new RecentViewData(contents_pk, name, date, num, url));
            }
            return data;
        } catch (SQLException se) {
            Log.e("sql", se.getMessage());
            return new ArrayList<>();
        }
    }

    public int getLastViewContentsNum(int contents_pk) {
        try {
            String sql = "SELECT contents_num FROM recent_view WHERE contents_pk = " + contents_pk + " ORDER BY date_for_compare DESC LIMIT 1;";
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            int ret;
            if (cursor.getCount() != 0)
                ret = cursor.getInt(0);
            else
                ret = -1;
            return ret;
        } catch (SQLException se) {
            Log.e("마지막컨텐츠번호오류", se.getMessage());
            return 0;
        }
    }
}
