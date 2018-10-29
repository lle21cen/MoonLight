package org.techtown.ideaconcert.SQLiteDir;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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
        Toast.makeText(context, "DB Created", Toast.LENGTH_SHORT).show();
        String sql = "CREATE TABLE recent_view(contents_name VARCHAR(20), view_date VARCHAR(20), contents_num VARCHAR(10), PRIMARY KEY(contents_name, view_date));";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addRecentViewData(String contents_name, String date, String contents_num) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "INSERT INTO recent_view(contents_name, view_date, contents_num) VALUES (?, ?, ?)";
            db.execSQL(sql, new Object[]{
                    contents_name, date, contents_num
            });
            Log.i("SQLite", "Recent View Data Insert Success");
        }catch (SQLException se) {
            Log.e("add_data_error", ""+se.getMessage());
        }
    }

    public void createTable(String table_name) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "CREATE TABLE recent_view(contents_name VARCHAR(20), view_date VARCHAR(20), contents_num VARCHAR(20), PRIMARY KEY(contents_name, view_date));";
        db.execSQL(sql);
        Toast.makeText(context, "DB Created", Toast.LENGTH_SHORT).show();
    }

    public void dropTable(String table_name) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DROP TABLE " + table_name;
        db.execSQL(sql);
        Toast.makeText(context, "drop table success", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<RecentViewPair> getAllRecentViewData() {
        try {
            String sql = "SELECT contents_name, view_date, contents_num FROM recent_view";
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<RecentViewPair> datas = new ArrayList<>();

            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String date = cursor.getString(1);
                String num = cursor.getString(2);
                datas.add(new RecentViewPair(name, date, num));
            }
            return datas;
        } catch (SQLException se) {
            Log.e("sql", se.getMessage());
            return new ArrayList<>();
        }
    }
}
