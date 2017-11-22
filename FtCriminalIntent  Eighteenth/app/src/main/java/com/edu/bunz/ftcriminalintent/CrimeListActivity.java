package com.edu.bunz.ftcriminalintent;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.UUID;


/**
 * Created by asuss on 2017/9/27.
 */

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks ,CrimeFragment.Callbacks{
    @Override

    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime){
        if (findViewById(R.id.detail_fragment_container) == null){
//            Intent intent = CrimePagerActivity.newIntent(this,crime.getId());
//            startActivity(intent);


            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
                    intent.putExtra(CrimeListFragment.SAVED_SUBTITLE_VISIBLE,  ((CrimeListFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.fragment_container)).mSubtitleVisible);
                    startActivityForResult(intent, CrimeListFragment.START_CRIME_DETAILS);
        }else {

                Fragment newDetail = CrimeFragment.newInstance(crime.getId());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
        }

    }

    public void onCrimeUpdated(Crime crime){
        CrimeListFragment listFragment = (CrimeListFragment)
                getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }


}
