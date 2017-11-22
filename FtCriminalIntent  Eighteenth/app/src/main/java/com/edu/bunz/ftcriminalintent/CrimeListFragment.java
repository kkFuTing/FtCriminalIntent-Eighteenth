package com.edu.bunz.ftcriminalintent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.edu.bunz.ftcriminalintent.helper.CrimeItemTouchHelperCallback;
import com.edu.bunz.ftcriminalintent.helper.ItemTouchHelperAdapter;
import com.edu.bunz.ftcriminalintent.helper.ItemTouchHelperViewHolder;

import java.util.List;
import java.util.UUID;

/**
 * Created by asuss on 2017/9/27.
 */

public class CrimeListFragment extends Fragment {

     static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private int mLastAdapterClickPosition = -1;
    //10.4
    private  static  final  int REQUEST_CRIME = 1;
    static final int START_CRIME_DETAILS = 0;

    public RecyclerView mCrimeRecycleView;
    public CrimeAdapter mAdapter;
    public boolean mSubtitleVisible; //记录子标题状态


    private ConstraintLayout mEmptyView;
    private LinearLayout mEmptyCrimeView;
    private  Button mNewCrimeButton;
    private ItemTouchHelper mItemTouchHelper;

    private Callbacks mCallbacks;
    public interface  Callbacks{
        void onCrimeSelected(Crime crime);
    }

//    @Override
//    public void onAttach(Activity activity){
//        super.onAttach(Context);
//        mCallbacks = (Callbacks) activity;
//    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
            mCrimeRecycleView = (RecyclerView) view
                    .findViewById(R.id.crime_recycler_view);
            mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

            if(savedInstanceState != null){
                mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
            }

//            Bundle bundle = getActivity().getIntent().getExtras();
//            if (bundle != null) {
//                mSubtitleVisible = bundle.getBoolean(SAVED_SUBTITLE_VISIBLE);
//            }

            mEmptyView = (ConstraintLayout) view.findViewById(R.id.empty_list_layout);
            mEmptyCrimeView = (LinearLayout) view.findViewById(R.id.empty_crime_layout);

            mNewCrimeButton = (Button) view.findViewById(R.id.add_crime_button);
            mNewCrimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCrime();
                }
            });

            updateUI();

            return view;
        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ItemTouchHelper.Callback callback = new CrimeItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mCrimeRecycleView);
    }

        @Override
        public  void onResume(){
            super.onResume();
            updateUI();
        }

        @Override
        public void onSaveInstanceState(Bundle outState){
            super.onSaveInstanceState(outState);
            outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
        }

        @Override
        public void onDetach(){
            super.onDetach();
            mCallbacks = null;
        }
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.fragment_crime_list,menu);

            MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
            if (mSubtitleVisible){
                subtitleItem.setTitle(R.string.hide_subtitle);
            }else {
                subtitleItem.setTitle(R.string.show_subtitle);
            }
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()){
                case R.id.new_crime:
//                    Crime crime = new Crime();
//                    CrimeLab.get(getActivity()).addCrime(crime);
////                    Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
////                    startActivity(intent);
//
////                    Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
////                    intent.putExtra(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
////                    startActivityForResult(intent, START_CRIME_DETAILS);
//
//
//                    mCallbacks.onCrimeSelected(crime);
//                    updateUI();
                    addCrime();
                    return  true;
                case R.id.show_subtitle:
                    mSubtitleVisible = !mSubtitleVisible;
                    getActivity().invalidateOptionsMenu();
                    updateSubtitle();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        private void addCrime() {
//        Crime crime = new Crime();
//        CrimeLab.get(getActivity()).addCrime(crime);
//        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
//        startActivity(intent);

//            Crime crime = new Crime();
//            CrimeLab.get(getActivity()).addCrime(crime);
////                    Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
////                    startActivity(intent);
//
////                    Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
////                    intent.putExtra(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
////                    startActivityForResult(intent, START_CRIME_DETAILS);
//
//
//            mCallbacks.onCrimeSelected(crime);
//            updateUI();

            Crime crime = new Crime();
            CrimeLab.get(getActivity()).addCrime(crime);
//        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
//        intent.putExtra(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
//        startActivityForResult(intent, START_CRIME_DETAILS);
            mCallbacks.onCrimeSelected(crime);
            mLastAdapterClickPosition = RecyclerView.NO_POSITION;
            updateUI();
    }

        private void updateSubtitle(){
            CrimeLab crimeLab = CrimeLab.get(getActivity());
            int crimeCount = crimeLab.getCrimes().size();
            String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural,crimeCount,crimeCount);

            if (!mSubtitleVisible){
                subtitle = null;
            }

            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.getSupportActionBar().setSubtitle(subtitle);
        }
    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecycleView.setAdapter(mAdapter);

        }
        else if (mLastAdapterClickPosition!= RecyclerView.NO_POSITION) {
            mAdapter.setCrimes(crimes);

            mAdapter.notifyDataSetChanged();
        }
        else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        if (mAdapter.mCrimes.size() > 0) {
            mCrimeRecycleView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
        else {
            mCrimeRecycleView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        updateSubtitle();
    }
//
//        public void updateUI(){
//            CrimeLab crimeLab = CrimeLab.get(getActivity());
//            List<Crime> crimes = crimeLab.getCrimes();
//
//            mEmptyView.setVisibility(View.VISIBLE);
//            if (crimes.size() > 0 ) {
//                mEmptyView.setVisibility(View.INVISIBLE);
//            }
//
//            if (mAdapter == null  || mLastAdapterClickPosition ==RecyclerView.NO_POSITION) {
//                mAdapter = new CrimeAdapter(crimes);
//                mCrimeRecycleView.setAdapter(mAdapter);
//                mLastAdapterClickPosition = -1;
//            }else{
//
//                if (mLastAdapterClickPosition < 0) {
//                    mAdapter.setCrimes(crimes);
//                    mAdapter.notifyDataSetChanged();
//                } else {
//                    mAdapter.notifyItemChanged(mLastAdapterClickPosition);
//                    mLastAdapterClickPosition = -1;
//                }
//
//            }
//            updateSubtitle();
//        }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener,ItemTouchHelperViewHolder {
        private  Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private ImageView mSolvedImageView;
        protected ConstraintLayout mConstraintLayout;
        protected Drawable backgroundBuffer;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int viewType){
            super(inflater.inflate(viewType,parent,false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mTimeTextView = (TextView) itemView.findViewById(R.id.crime_time);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
            mConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.crime_item_container);
        }

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTtitle());
            //mDateTextView.setText(mCrime.getDate().toString());
            //DateFormat会依电脑上的区域设定显示时间格式，EE表示星期，MM表示月份、dd表示日期，而yyyy是西元
            mDateTextView.setText(DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate()));
            mTimeTextView.setText(DateFormat.format("a  hh :mm : ss",mCrime.getTime()));

            //因为自己做的挑战题，要进行是否是需要交给警察的，因为它是有另个一个xml，没有这个判断会强制退出运行app
            if (!crime.isRequiresPolice()) {
                mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            }
//            mConstraintLayout.setContentDescription(mCrime.getTtitle() + DateFormat.format("EEEE, dd MMMM yyyy, HH:mm", mCrime.getDate()).toString());
        }
        @Override
        public  void  onClick(View view){
            mLastAdapterClickPosition = getAdapterPosition();
          //  Intent intent = FtCrimeActivity.newIntent(getActivity(),mCrime.getId());

//            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
//            //10.4
//            startActivityForResult(intent,REQUEST_CRIME);
//            startActivity(intent);
            mCallbacks.onCrimeSelected(mCrime);
        }

        @Override
        public void onItemSelected() {
            backgroundBuffer = itemView.getBackground();
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackground(backgroundBuffer);
        }
    }


//   // 10.4
//   @Override
//   public void onActivityResult(int requestCode, int resultCode, Intent data) {
//       if (resultCode != Activity.RESULT_OK) {
//           return;
//       }
//       if (requestCode == REQUEST_CRIME) {
//           if (data == null) {
//               return;
//           }
//
//
//           UUID crimeId = (UUID) data.getSerializableExtra(CrimePagerActivity.EXTRA_CRIME_ID);
//           CrimeLab crimeLab = CrimeLab.get(getActivity());
//           Crime crime = crimeLab.getCrime(crimeId);
//           crimeLab.deleteCrime(crime);
//           mLastAdapterClickPosition = RecyclerView.NO_POSITION;
//       }
//   }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> implements ItemTouchHelperAdapter {

        public List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        //回写删除的写法
        @Override
        public void onItemDismiss(int position) {
//            CrimeLab.get(getActivity()).deleteCrime(mCrimes.get(position));
//            mCrimes.remove(position);

            CrimeLab.get(getActivity()).deleteCrime(mCrimes.get(position));
            mCrimes.remove(position);
//            想要使用RecyclerView的动态效果，就需要调用notifyItemRemoved。
// //           但是该方法不会使position及其之后位置的vitemiew重新
            notifyItemRemoved(position);
            Toast.makeText(getActivity(), R.string.item_deleted_toast, Toast.LENGTH_SHORT).show();
            updateUI();


            if (mAdapter.mCrimes.size() > 0) {
//                mCrimeRecycleView.setVisibility(View.VISIBLE);
//                mEmptyView.setVisibility(View.GONE);
                mCallbacks.onCrimeSelected(mCrimes.get(position));
            }
            else {
//                mCrimeRecycleView.setVisibility(View.GONE);
//                mEmptyView.setVisibility(View.VISIBLE);
                //空的 crimefragment
//                mEmptyCrimeView.setVisibility(View.GONE);
            }
        }


        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return   new CrimeHolder(layoutInflater,parent,viewType);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }
        @Override
        public int getItemViewType(int position) {
            if (mCrimes.get(position).isRequiresPolice()) {
                return R.layout.list_item_crime_police;
            } else {
                return R.layout.list_item_crime;
            }
        }
    }
}
