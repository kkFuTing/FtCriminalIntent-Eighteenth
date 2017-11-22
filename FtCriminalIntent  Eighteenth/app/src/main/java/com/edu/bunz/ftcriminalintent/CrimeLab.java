package com.edu.bunz.ftcriminalintent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.bunz.ftcriminalintent.database.CrimeBaseHelper;
import com.edu.bunz.ftcriminalintent.database.CrimeCursorWrapper;
import com.edu.bunz.ftcriminalintent.database.CrimeDbSchema;
import com.edu.bunz.ftcriminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by asuss on 2017/9/27.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

//        private List<Crime> mCrimes;
//    private Map<UUID,Crime> mCrimes;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){

        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context context){

        mContext =context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();
//        mCrimes = new ArrayList<>();
//        mCrimes = new LinkedHashMap<>();
//        for (int i=0;i<100;i++){
//            Crime crime = new Crime();
//            crime.setTtitle("Crime #"+ i);
//            crime.setSolved(i%2 == 0);
//
//            crime.setRequiresPolice(i % 3 == 0);
//
////             mCrimes.add(crime);
//            mCrimes.put(crime.getId(), crime);
//        }
    }

    public void addCrime(Crime c){
//        mCrimes.put(c.getId(),c);
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME,null,values);
    }

    public void deleteCrime(Crime c) {
//        for(int i = 0; i < mCrimes.size(); i++) {
//            if(mCrimes.get(i).getId() == c.getId()) {
//                mCrimes.remove(i);
//                return;
//            }
//        }
//        mCrimes.remove(c.getId());
        String uuidString = c.getId().toString();
        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString}
        );

    }

    public  List<Crime> getCrimes(){
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

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID  + " = ?",
                new String[] {id.toString()}
        );
        try {
            if (cursor.getCount() == 0){
                return  null;
            }
            cursor.moveToFirst();
            return  cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime){
        File filesDir = mContext.getFilesDir();
        if (filesDir  == null) {
            return null;
        }
        return new File(filesDir,crime.getPhotoFilename());
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME,values,CrimeTable.Cols.UUID +  "= ?",new String[] {uuidString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
//                null,
                null,
                null
        );
        return  new CrimeCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTtitle());
        values.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
//        values.put(CrimeTable.Cols.TIME,crime.getTime().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());
        values.put(CrimeTable.Cols.SUSPECT_PN,crime.getSuspectPN());

        return  values;
    }

}
