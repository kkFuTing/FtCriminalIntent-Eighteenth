package com.edu.bunz.ftcriminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.edu.bunz.ftcriminalintent.Crime;
import com.edu.bunz.ftcriminalintent.database.CrimeDbSchema.CrimeTable;


import java.util.Date;
import java.util.UUID;

/**
 * Created by asuss on 2017/10/24.
 */

public class CrimeCursorWrapper extends CursorWrapper{
    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }
    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
//        long time = getLong(getColumnIndex(CrimeTable.Cols.TIME));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
        String suspect_pn = getString(getColumnIndex(CrimeTable.Cols.SUSPECT_PN));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTtitle(title);
        crime.setDate(new Date(date));
//        crime.setTime(new Date(time));
        crime.setSolved(isSolved != 0);

        crime.setSuspect(suspect);
        crime.setSuspectPN(suspect_pn);

        return crime;
    }
}
