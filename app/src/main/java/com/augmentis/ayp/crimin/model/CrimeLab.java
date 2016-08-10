package com.augmentis.ayp.crimin.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.augmentis.ayp.crimin.model.CrimeDbSchema.CrimeTable;

/**
 * Created by Nutdanai on 7/18/2016.
 */
public class CrimeLab {
    private Context context;
    private SQLiteDatabase database;

    private static CrimeLab instance;  //Bind with class

    public static CrimeLab getInstance(Context context) {
        if (instance == null) {
            instance = new CrimeLab(context);
        }
        return instance;
    }

    private CrimeLab(Context context) {
        this.context = context.getApplicationContext();
        CrimeBaseHelper crimeBaseHelper = new CrimeBaseHelper(context);
        database = crimeBaseHelper.getWritableDatabase();


    }

    public static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getCrimeDate().getTime()); // type long
        contentValues.put(CrimeTable.Cols.SOLVED, (crime.isSolved())?1:0);
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return contentValues;
    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        database.insert(CrimeTable.NAME,null,contentValues);
    }

    public Crime getCrimeById(UUID uuid) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",new String[]{uuid.toString()});

        try{
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }

    }
////public Crime getCrimeForDelete(List<Crime> crimes,Crime c){
//    for (Crime temp:crimes){
//        if(second.getId().equals(c.getId())) {
//            first = second = temp;
//        }
//        crimes.
//
//    }
//
//}

    public List<Crime> getCrime() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null,null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return crimes;
    }

    public void deleteCrime(UUID uuid) {
        database.delete(CrimeTable.NAME,CrimeTable.Cols.UUID + " = ?",new String[]{uuid.toString()});

    }

    public CrimeCursorWrapper queryCrimes(String whereCause, String[] whereArgs){
        Cursor cursor = database.query(CrimeTable.NAME,null,whereCause,whereArgs,null,null,null);
        return new CrimeCursorWrapper(cursor);
    }

    public void updateCrime(Crime crime){
        String uuidStr = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);
        database.update(CrimeTable.NAME,contentValues,CrimeTable.Cols.UUID + "= ?",new String[]{uuidStr});
    }

    public File getPhotoFile(Crime crime){
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalFilesDir == null){
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }


}
