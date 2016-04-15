package com.locateandgetlocated.locategetlocated;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;

/**
 * Created by Kamil on 2016-04-07.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lngl.db";
    private static final String TABLE_NAME = "REQUESTS";

    private static final String REQUEST_ID = "_id";

    private static final String SENDER = "Sender";
    private static final String RECEIVER = "Receiver";

    private static final String REQUEST_SEND_DATE = "RequestSendDate";
    private static final String REQUEST_RECEIVE_DATE = "RequestReceiveDate";
    private static final String REQUEST_LOCALIZATION_DATE = "RequestLocalizationDate";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";


    private static final int DATABASE_VERSION = 1;
    private static final String FILE_DIR = "sdcard";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, boolean external) {
        super(context, Environment.getExternalStorageDirectory() + File.separator + FILE_DIR + File.separator + DATABASE_NAME, factory, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SENDER + " TEXT, "
                + RECEIVER + " TEXT, "
                + REQUEST_SEND_DATE + " INTEGER, "
                + REQUEST_RECEIVE_DATE + " INTEGER, "
                + REQUEST_LOCALIZATION_DATE + " INTEGER, "
                + LATITUDE + " TEXT, "
                + LONGITUDE + " TEXT"
                + ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public void addRequest(Request request) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(REQUEST_SEND_DATE, request.getSendDate().getTime());
        // contentValues.put(REQUEST_RECEIVE_DATE, request.getReceiveDate().getTime());
        //  contentValues.put(REQUEST_LOCALIZATION_DATE, request.getLocalizationDate().getTime());
        contentValues.put(LATITUDE, String.valueOf(request.getLatitude()));
        contentValues.put(LONGITUDE, String.valueOf(request.getLongitude()));

        contentValues.put(RECEIVER, String.valueOf(request.getReceiver()));
        contentValues.put(SENDER, String.valueOf(request.getSender()));

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }


    public void deleteRequest(String requestID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + REQUEST_ID + " =\"" + requestID + "\";");
    }

    public ArrayList<Request> getRequestsArrayList() {
        ArrayList<Request> tmp = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Request request = new Request(
                    cursor.getInt(cursor.getColumnIndex(REQUEST_ID)),
                    cursor.getDouble(cursor.getColumnIndex(LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(LONGITUDE)),
                    new Date(cursor.getLong(cursor.getColumnIndex(REQUEST_SEND_DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(REQUEST_RECEIVE_DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(REQUEST_LOCALIZATION_DATE))),
                    cursor.getString(cursor.getColumnIndex(SENDER)),
                    cursor.getString(cursor.getColumnIndex(RECEIVER))
            );
            tmp.add(request);
            cursor.moveToNext();
        }
        return tmp;
    }
}