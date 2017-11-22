package com.edu.bunz.ftcriminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks{
//    CrimeFragment.OnDeleteCrimeListener
    public static  final String EXTRA_CRIME_ID=
            "com.edu.bunz.ftcriminalintent.crime_id";

    private Button mFirstButton;
    private Button mLastButton;
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private boolean mSubtitleVisible;

//DELETE
        public void onCrimeIdSelected (UUID crimeId) {
            Intent data = new Intent();
            data.putExtra(EXTRA_CRIME_ID, crimeId);
            setResult(Activity.RESULT_OK, data);
            finish();
        }

    @Override
    public void onCrimeUpdated(Crime crime){

    }

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for (int i = 0;i<mCrimes.size();i++){
            if (mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener () {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mViewPager.getCurrentItem() == 0) {
                    mFirstButton.setEnabled(false);
                } else {
                    mFirstButton.setEnabled(true);
                }

                if (mViewPager.getCurrentItem() == (mCrimes.size() - 1)) {
                    mLastButton.setEnabled(false);
                } else {
                    mLastButton.setEnabled(true);
                }

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });



        mFirstButton = (Button) findViewById(R.id.first_button);
        mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);

            }
        });

        mLastButton = (Button) findViewById(R.id.last_button);
        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getAdapter().getCount());

            }
        });
    }
    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        if (intent == null) {
            intent = new Intent(this, CrimeListActivity.class);
        }
        intent.putExtra(CrimeListFragment.SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
        return intent;
    }



}
