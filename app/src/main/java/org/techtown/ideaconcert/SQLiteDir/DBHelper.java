package org.techtown.ideaconcert.SQLiteDir;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        Toast.makeText(context, "DB Created", Toast.LENGTH_SHORT).show();
        String sql = "CREATE TABLE recent_view(contents_pk INTEGER, contents_name VARCHAR(20), view_date DATETIME, date_for_compare DATETIME, contents_num INTEGER, PRIMARY KEY(contents_pk, contents_num));";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addOrUpdateRecentViewData(int contents_pk, String contents_name, String date, String date_for_compare, int contents_num) {
        try {
            String sql = "SELECT contents_pk FROM recent_view WHERE contents_pk = " + contents_pk + " AND contents_num = " + contents_num +";";
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToNext();
            if (cursor.getCount() != 0) {
                sql = "UPDATE recent_view SET date_for_compare = '" + date_for_compare + "' WHERE contents_pk = " + contents_pk + " AND contents_num = " + contents_num + ";";
                db.execSQL(sql);
            } else {
                sql = "INSERT INTO recent_view(contents_pk, contents_name, view_date, date_for_compare, contents_num) VALUES (?, ?, ?, ?, ?)";
                db = getWritableDatabase();
                db.execSQL(sql, new Object[]{
                        contents_pk, contents_name, date, date_for_compare, contents_num
                });
            }
            Log.i("addOrUpdate", "최근작품목록 업데이트 or 삽입 성공");
        } catch (SQLException se) {
            Log.e("addOrUpdate", "" + se.getMessage());
        }
    }

    public void createRecentViewTable() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "CREATE TABLE recent_view(contents_pk INTEGER, contents_name VARCHAR(20), view_date DATETIME, date_for_compare DATETIME, contents_num INTEGER, PRIMARY KEY(contents_pk, contents_num));";
        db.execSQL(sql);
        Toast.makeText(context, "DB Created", Toast.LENGTH_SHORT).show();
    }

    public void dropTable(String table_name) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DROP TABLE " + table_name;
        db.execSQL(sql);
        Toast.makeText(context, "drop table success", Toast.LENGTH_SHORT).show();
    }

    public int getReadContentsCount(int contents_pk) {
        try {
            String sql = "SELECT contents_pk FROM recent_view WHERE contents_pk = " + contents_pk + ";";
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            return cursor.getCount();
        } catch (SQLException se) {
            Log.e("열람목록개수리턴오류", se.getMessage());
            return 0;
        }
    }

    public void dropAllTables() {
        SQLiteDatabase db = getWritableDatabase();
    }

    public ArrayList<RecentViewPair> getAllRecentViewData() {
        try {
            String sql = "SELECT contents_pk, contents_name, view_date, MAX(date_for_compare), contents_num FROM recent_view GROUP BY contents_pk";
//            String sql = "SELECT contents_pk, contents_name, view_date, contents_num FROM recent_view";
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<RecentViewPair> data = new ArrayList<>();

            while (cursor.moveToNext()) {
                int contents_pk = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                int num = cursor.getInt(4);
                data.add(new RecentViewPair(contents_pk, name, date, num));
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
            if (cursor.getCount() != 0)
                return cursor.getInt(0);
            else
                return -1;
        } catch (SQLException se) {
            Log.e("마지막컨텐츠번호오류", se.getMessage());
            return 0;
        }
    }
}
