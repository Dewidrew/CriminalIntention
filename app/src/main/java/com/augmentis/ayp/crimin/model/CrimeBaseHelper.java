package com.augmentis.ayp.crimin.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.augmentis.ayp.crimin.model.CrimeDbSchema.CrimeTable;

/**
 * Created by Hattapong on 8/1/2016.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME
                + "("
                + "_id integer primary key autoincrement, "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED + ","
                + CrimeTable.Cols.SUSPECT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //1. rename table to _(oldversion)
        db.execSQL("alter table " + CrimeTable.NAME + " rename to " + CrimeTable.NAME + "_" + oldVersion);

        //3. create new table
        db.execSQL("create table " + CrimeTable.NAME
                + "("
                + "_id integer primary key autoincrement, "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED + ","
                + CrimeTable.Cols.SUSPECT + ")"
        );

        //4. insert data from temp data
        db.execSQL("insert into " + CrimeTable.NAME
                + " ("
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED
                + ") "
                + " select "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED
                + " from " + CrimeTable.NAME + "_" + oldVersion);

        db.execSQL("drop table if exists " + CrimeTable.NAME + "_" + oldVersion);

    }
}
