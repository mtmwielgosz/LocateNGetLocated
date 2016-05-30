package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kamil on 2016-04-07.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lngl.db";

    //Requests table
    private static final String TABLE_NAME_REQUESTS = "REQUESTS";
    private static final String REQUEST_ID = "_id";
    private static final String REQUEST_RECEIVER = "Receiver";
    private static final String REQUEST_SEND_DATE = "RequestSendDate";
    private static final String REQUEST_RECEIVE_DATE = "RequestReceiveDate";
    private static final String REQUEST_LOCALIZATION_DATE = "RequestLocalizationDate";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    //Requests table -- end

    //Devices table
    private static final String TABLE_NAME_DEVICES = "DEVICES";
    private static final String DEVICE_ID = "_id";
    private static final String DEVICE_NUMBER = "Number";
    private static final String DEVICE_NAME = "Name";
    private static final String DEVICE_TYPE = "Type";
    //Devices table -- end

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
        createRequestsTable(sqLiteDatabase);
        createDevicesTables(sqLiteDatabase);
    }

    private void createDevicesTables(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME_DEVICES + " ("
                + DEVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DEVICE_NUMBER + " TEXT, "
                + DEVICE_NAME + " TEXT, "
                + DEVICE_TYPE + " INTEGER"
                + ");";
        sqLiteDatabase.execSQL(query);
    }

    private void createRequestsTable(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME_REQUESTS + " ("
                + REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + REQUEST_RECEIVER + " TEXT, "
                + REQUEST_SEND_DATE + " INTEGER, "
                + REQUEST_RECEIVE_DATE + " INTEGER, "
                + REQUEST_LOCALIZATION_DATE + " INTEGER, "
                + LATITUDE + " TEXT, "
                + LONGITUDE + " TEXT, "
                + "FOREIGN KEY(" + REQUEST_RECEIVER + ") REFERENCES " + TABLE_NAME_DEVICES + "(" + DEVICE_NUMBER + ")"
                + ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_REQUESTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DEVICES);
        onCreate(sqLiteDatabase);
    }

    // obsluga request

    public void addRequest(Request request) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(REQUEST_SEND_DATE, request.sendDate.getTime());

        if (request.receiveDate != null) {
            contentValues.put(REQUEST_RECEIVE_DATE, request.receiveDate.getTime());
        }
        if (request.localizationDate != null) {
            contentValues.put(REQUEST_LOCALIZATION_DATE, request.localizationDate.getTime());
        }
        contentValues.put(LATITUDE, String.valueOf(request.latitude));
        contentValues.put(LONGITUDE, String.valueOf(request.longitude));
        contentValues.put(REQUEST_RECEIVER, String.valueOf(request.receiver));

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME_REQUESTS, null, contentValues);
        db.close();
    }


    public void updateRequest(Request request) {
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format(
                "UPDATE %s " +
                        "SET %s = '%d', " +
                        "%s = '%f', " +
                        "%s = '%f' " +
                        "WHERE %s=%d",
                TABLE_NAME_REQUESTS, REQUEST_LOCALIZATION_DATE, request.localizationDate.getTime(), LATITUDE, request.latitude, LONGITUDE, request.longitude, REQUEST_SEND_DATE, request.sendDate.getTime());
        Log.d("SELEEECT", query);

        db.execSQL(query);
    }

    public void deleteRequest(String requestId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_REQUESTS + " WHERE " + REQUEST_ID + " =\"" + requestId + "\";");
    }

    public Request[] getRequestsArray() {
        Request[] tmp = new Request[getRequestsArrayList().size()];

        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = getRequestsArrayList().get(i);
        }
        return tmp;
    }

    public Request[] getRequestsArrayByDevice(String deviceName) {
        Request[] tmp = new Request[getRequestsByDevice(deviceName).size()];

        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = getRequestsByDevice(deviceName).get(i);
        }
        return tmp;
    }

    public ArrayList<Request> getRequestsArrayList() {
        ArrayList<Request> tmp = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_REQUESTS + " WHERE 1";

        Log.d("SELEEECT", query);

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
                    cursor.getString(cursor.getColumnIndex(REQUEST_RECEIVER))
            );
            if(request.longitude != 0)
                tmp.add(request);
            cursor.moveToNext();
        }
        return tmp;
    }

    public ArrayList<Request> getRequestsByDevice(String deviceName) {
        ArrayList<Request> tmp = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_REQUESTS + " JOIN " + TABLE_NAME_DEVICES + " ON " + TABLE_NAME_REQUESTS + "." + REQUEST_RECEIVER + " = " + TABLE_NAME_DEVICES + "." + DEVICE_NUMBER
                + " WHERE " + DEVICE_NAME + " = \'" + deviceName + "\'";

        Log.d("SELEEECT", query);

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
                    cursor.getString(cursor.getColumnIndex(REQUEST_RECEIVER))
            );
            tmp.add(request);
            cursor.moveToNext();
        }
        return tmp;
    }


    // end - obsluga request


    // obsluga device
    public void addDevice(Device device) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEVICE_NAME, device.deviceName);
        contentValues.put(DEVICE_NUMBER, device.phoneNumber);
        contentValues.put(DEVICE_TYPE, device.deviceType);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME_DEVICES, null, contentValues);
        db.close();
    }

    public void updateDevice(Device device) {
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("UPDATE %s SET %s = '%s', %s = '%s' WHERE %s=%d", TABLE_NAME_DEVICES, DEVICE_NAME, device.deviceName, DEVICE_NUMBER, device.phoneNumber, DEVICE_ID, device.id);
        Log.d("SELEEECT", query);
        db.execSQL(query);
    }


    public void deleteDevice(int deviceId) {
        deleteDevice(Integer.toString(deviceId));
    }

    public void deleteDevice(String deviceId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_DEVICES + " WHERE " + DEVICE_ID + " = " + deviceId);
    }

    public Device getDeviceById(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase == null) {
            System.out.println("gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg");
        }
        String query = "SELECT * FROM " + TABLE_NAME_DEVICES + " WHERE " + DEVICE_ID + " = \'" + id + "\'";

        Log.d("SELEEECT", query);

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        Device device = new Device(
                cursor.getInt(cursor.getColumnIndex(DEVICE_ID)),
                cursor.getString(cursor.getColumnIndex(DEVICE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(DEVICE_NAME)),
                cursor.getInt(cursor.getColumnIndex(DEVICE_TYPE))
        );
        return device;
    }

    public Device getDeviceByName(String name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase == null) {
            System.out.println("gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg");
        }
        String query = "SELECT * FROM " + TABLE_NAME_DEVICES + " WHERE " + DEVICE_NAME + " = \'" + name + "\'";
        Log.d("SELEEECT", query);
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        Device device = new Device(
                cursor.getInt(cursor.getColumnIndex(DEVICE_ID)),
                cursor.getString(cursor.getColumnIndex(DEVICE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(DEVICE_NAME)),
                cursor.getInt(cursor.getColumnIndex(DEVICE_TYPE))
        );
        return device;
    }

    public Device[] getDevicesArray() {
        Device[] tmp = new Device[getAllDevicesArrayList().size()];

        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = getAllDevicesArrayList().get(i);
        }
        return tmp;
    }

    public ArrayList<Device> getAllDevicesArrayList() {
        ArrayList<Device> tmp = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEVICES + " WHERE 1";
        Log.d("SELEEECT", query);

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Device device = new Device(
                    cursor.getInt(cursor.getColumnIndex(DEVICE_ID)),
                    cursor.getString(cursor.getColumnIndex(DEVICE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(DEVICE_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DEVICE_TYPE))
            );

            tmp.add(device);
            cursor.moveToNext();
        }
        return tmp;
    }


    public Device[] getDevicesArrayByType(int type) {
        Device[] tmp = new Device[getDevicesArrayListByType(type).size()];

        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = getDevicesArrayListByType(type).get(i);
        }
        return tmp;
    }

    public ArrayList<Device> getDevicesArrayListByType(int type) {
        ArrayList<Device> tmp = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEVICES + " WHERE " + DEVICE_TYPE + "=\'" + type + "\'";
        Log.d("SELEEECT", query);

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Device device = new Device(
                    cursor.getInt(cursor.getColumnIndex(DEVICE_ID)),
                    cursor.getString(cursor.getColumnIndex(DEVICE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(DEVICE_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DEVICE_TYPE))
            );

            tmp.add(device);
            cursor.moveToNext();
        }
        return tmp;
    }


    // end - obsluga device

    public String[] getRequestsWithDevicesArray(String deviceName) {
        String[] tmp = new String[getRequestsWithDevices(deviceName).size()];

        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = getRequestsWithDevices(deviceName).get(i);
        }
        return tmp;
    }

    public ArrayList<String> getRequestsWithDevices(String deviceName) {
        ArrayList<String> tmp = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_REQUESTS + " JOIN " + TABLE_NAME_DEVICES + " WHERE " + DEVICE_NAME + " = \'" + deviceName + "\'";
        Log.d("SELEEECT", query);

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String line = "Zapytanie: " + cursor.getInt(cursor.getColumnIndex(REQUEST_ID)) + "\n" +
                    "  Urządzenie: " + cursor.getString(cursor.getColumnIndex(DEVICE_NAME)) + "\n" +
                    "    Zlokalizowano: " + (new Date(cursor.getLong(cursor.getColumnIndex(REQUEST_LOCALIZATION_DATE)))).toString();

            tmp.add(line);
            cursor.moveToNext();
        }
        return tmp;
    }

    public Device getDeviceByDeviceNumber(String phoneNumber) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format(
                "Select * " +
                        "FROM %s " +
                        "WHERE %s = %s",
                TABLE_NAME_DEVICES, DEVICE_NUMBER, phoneNumber);

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        Device device = new Device(
                cursor.getInt(cursor.getColumnIndex(DEVICE_ID)),
                cursor.getString(cursor.getColumnIndex(DEVICE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(DEVICE_NAME)),
                cursor.getInt(cursor.getColumnIndex(DEVICE_TYPE))
        );

        return device;
    }
}