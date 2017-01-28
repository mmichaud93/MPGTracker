package com.mpgtracker.tallmatt.mpgtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mpgtracker.tallmatt.mpgtracker.models.Car;

/**
 * Created by TallMatt on 1/24/2017.
 */

public class CarDatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "CarDB.db";
    public static final String CARDB_TABLE_NAME = "cars";
    public static final String CARDB_COLUMN_ID = "id";
    public static final String CARDB_COLUMN_MAKE = "make";
    public static final String CARDB_COLUMN_MODEL = "model";
    public static final String CARDB_COLUMN_YEAR = "year";
    public static final String CARDB_COLUMN_LICENSE = "license";
    public static final String CARDB_COLUMN_NAME = "name";

    public CarDatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table " + CARDB_TABLE_NAME + "(" +
                        CARDB_COLUMN_ID + " integer primary key, " +
                        CARDB_COLUMN_MAKE + " text," +
                        CARDB_COLUMN_MODEL + " text," +
                        CARDB_COLUMN_YEAR + " text," +
                        CARDB_COLUMN_LICENSE + " text," +
                        CARDB_COLUMN_NAME + " text)"
        );
    }

    public void resetTable() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + CARDB_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetTable();
        onCreate(db);
    }

    public long insertCar(Car car) {

        if (car == null) {
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesFromCarModel(car);

        long i = db.insert(CARDB_TABLE_NAME, null, contentValues);
        db.close();

        car.setId(i);

        return i;
    }

    public boolean updateCar(Car car) {

        if (car == null) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesFromCarModel(car);

        int r = db.update(CARDB_TABLE_NAME, contentValues, CARDB_COLUMN_ID + " = " + Long.toString(car.getId()), null);
        db.close();

        return r > 0;
    }

    public boolean deleteCar(Car car) {

        if (car == null) {
            return false;
        }

        return deleteCar(car.getId());
    }

    public boolean deleteCar(long carId) {

        SQLiteDatabase db = this.getWritableDatabase();

        int d = db.delete(CARDB_TABLE_NAME, CARDB_COLUMN_ID + " = " + Long.toString(carId), null);
        return d > 0;
    }

    public int getNumberOfRows() {

        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, CARDB_TABLE_NAME);
    }

    public Car[] getAllCars() {

        Car[] cars = new Car[getNumberOfRows()];

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CARDB_TABLE_NAME, null);
        cursor.moveToFirst();

        int i = 0;
        while (!cursor.isAfterLast()) {
            cars[i++] = new Car(
                    cursor.getLong(cursor.getColumnIndex(CARDB_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(CARDB_COLUMN_MAKE)),
                    cursor.getString(cursor.getColumnIndex(CARDB_COLUMN_MODEL)),
                    cursor.getString(cursor.getColumnIndex(CARDB_COLUMN_YEAR)),
                    cursor.getString(cursor.getColumnIndex(CARDB_COLUMN_LICENSE)),
                    cursor.getString(cursor.getColumnIndex(CARDB_COLUMN_NAME))
            );
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return cars;
    }

    private ContentValues getContentValuesFromCarModel(Car car) {
        if (car == null) {
            return null;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(CARDB_COLUMN_MAKE, car.getMake());
        contentValues.put(CARDB_COLUMN_MODEL, car.getModel());
        contentValues.put(CARDB_COLUMN_YEAR, car.getYear());
        contentValues.put(CARDB_COLUMN_LICENSE, car.getLicense());
        contentValues.put(CARDB_COLUMN_NAME, car.getName());

        return contentValues;
    }
}
