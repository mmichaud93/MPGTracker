package com.mpgtracker.tallmatt.mpgtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mpgtracker.tallmatt.mpgtracker.models.DataPointModel;
import com.mpgtracker.tallmatt.mpgtracker.utils.Utils;

import java.util.ArrayList;

/**
 * Created by TallMatt on 1/24/2017.
 */

public class DataPointDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DataPointDB.db";
    public static final String DPDB_TABLE_NAME = "cars";
    public static final String DPDB_COLUMN_ID = "id";
    public static final String DPDB_COLUMN_CAR_ID = "car_id";
    public static final String DPDB_COLUMN_GALLONS = "gallons";
    public static final String DPDB_COLUMN_MILES = "miles";
    public static final String DPDB_COLUMN_MONEY = "money";
    public static final String DPDB_COLUMN_DATE = "date";
    public static final String DPDB_COLUMN_LOCATION = "location";

    public DataPointDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void resetTable() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + DPDB_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table " + DPDB_TABLE_NAME + "(" +
                        DPDB_COLUMN_ID + " integer primary key, " +
                        DPDB_COLUMN_CAR_ID + " integer," +
                        DPDB_COLUMN_GALLONS + " float," +
                        DPDB_COLUMN_MILES + " float," +
                        DPDB_COLUMN_MONEY + " float," +
                        DPDB_COLUMN_DATE + " integer," +
                        DPDB_COLUMN_LOCATION + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetTable();
        onCreate(db);
    }

    public long insertDataPoint(DataPointModel dataPoint) {

        if (dataPoint == null) {
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesFromDataPointModel(dataPoint);

        long i = db.insert(DPDB_TABLE_NAME, null, contentValues);
        db.close();

        dataPoint.id = i;

        return i;
    }

    public boolean updateDataPoint(DataPointModel dataPoint) {
        if (dataPoint == null) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesFromDataPointModel(dataPoint);

        int r = db.update(DPDB_TABLE_NAME, contentValues, DPDB_COLUMN_ID + " = " + Long.toString(dataPoint.id), null);
        db.close();

        return r > 0;
    }

    public boolean deleteDataPoint(DataPointModel dataPoint) {

        if (dataPoint == null) {
            return false;
        }

        return deleteDataPoint(dataPoint.id);
    }

    public boolean deleteDataPoint(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int d = db.delete(DPDB_TABLE_NAME, DPDB_COLUMN_ID + " = " + Long.toString(id), null);
        return d > 0;
    }

    public int getNumberOfRows() {

        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, DPDB_TABLE_NAME);
    }

    public ArrayList<DataPointModel> getAllDataPoints(long carId) {

        ArrayList<DataPointModel> dataPoints = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DPDB_TABLE_NAME + " where " + DPDB_COLUMN_CAR_ID + " = " + Long.toString(carId), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            dataPoints.add(new DataPointModel(
                    cursor.getLong(cursor.getColumnIndex(DPDB_COLUMN_ID)),
                    cursor.getLong(cursor.getColumnIndex(DPDB_COLUMN_CAR_ID)),
                    cursor.getFloat(cursor.getColumnIndex(DPDB_COLUMN_GALLONS)),
                    cursor.getFloat(cursor.getColumnIndex(DPDB_COLUMN_MILES)),
                    cursor.getFloat(cursor.getColumnIndex(DPDB_COLUMN_MONEY)),
                    cursor.getLong(cursor.getColumnIndex(DPDB_COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndex(DPDB_COLUMN_LOCATION))
            ));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return dataPoints;
    }

    private ContentValues getContentValuesFromDataPointModel(DataPointModel dataPoint) {

        if (dataPoint == null) {
            return null;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(DPDB_COLUMN_CAR_ID, dataPoint.carId);
        contentValues.put(DPDB_COLUMN_GALLONS, dataPoint.gallonsPutIn);
        contentValues.put(DPDB_COLUMN_MILES, dataPoint.milesTravelled);
        contentValues.put(DPDB_COLUMN_MONEY, dataPoint.moneySpent);
        contentValues.put(DPDB_COLUMN_DATE, dataPoint.dateAdded);
        if (dataPoint.location == null) {
            contentValues.put(DPDB_COLUMN_LOCATION, "");
        } else {
            contentValues.put(DPDB_COLUMN_LOCATION, dataPoint.location.toString());
        }


        return contentValues;
    }
}
