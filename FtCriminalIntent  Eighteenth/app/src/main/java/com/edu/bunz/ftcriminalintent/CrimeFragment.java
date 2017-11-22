package com.edu.bunz.ftcriminalintent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.provider.ContactsContract.*;

/**
 * Created by asuss on 2017/9/23.
 */

public class CrimeFragment extends Fragment  {


    private static final String ARG_CRIME_ID ="crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private  static final String DIALOG_TIME = "DialogTime";
    private static final String DIALOG_IMAGE = "dialog_image";
    public  static final String EXTRA_WAS_REMOVING = "com.edu.bunz.ftcriminalintent.wsa_remove";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ=3;
    private static final int REQUESST_PHOTO = 4;

    private  Crime mCrime;
    private File mPhotoFile;
    private EditText mTitleField;

    private Button mDateButton,mTimeButton;
    private Button mSuspectButton;
    private Button mReportButton;
    private Button mCallSuspectButton;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private CheckBox mSolvedCheckBox;
    private boolean wasRemove;




// delete
//    OnDeleteCrimeListener mCallback;
//
//    public interface OnDeleteCrimeListener {
//        public void onCrimeIdSelected (UUID crimeId);
//    }

    private Callbacks mCallbacks;
    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }


    public  static  CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        CrimeFragment fragment= new CrimeFragment();
        fragment.setArguments(args);
        return  fragment;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
//        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        if (mCrime != null) {
            mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause(){
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }
    public  void permissionReadContacts(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ);
        }
        else{
            intentPhone();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceSatete) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTtitle());
//        if(mCrime.getTtitle()!= null) {
//            mTitleField.setText(mCrime.getTtitle());
//        }
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCrime.setTtitle(s.toString());
                updateCrime();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                // DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });



        mTimeButton = (Button) v.findViewById(R.id.crime_time);
//        mTimeButton.setText(mCrime.getTime().toString());
        mTimeButton.setText(DateFormat.format("a  hh :mm",mCrime.getTime()));

        mTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager mManager = getFragmentManager();
                TimePickerFragment mdialog = new  TimePickerFragment().newInstance(mCrime.getTime());
                mdialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                mdialog.show(mManager,DIALOG_TIME);
            }

        });
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent i = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(R.string.send_report)
                        .getIntent();
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT,
//                        getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i,getString(R.string.send_report));
                startActivity(i);

            }
        });


        final Intent pickContact = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
//        pickContact.addCategory(Intent.CATEGORY_HOME); //禁用任何和它匹配的信息，为了验证无匹配情况下按按钮会崩溃故禁用按钮的设计
        mSuspectButton  = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });

        mCallSuspectButton = (Button) v.findViewById(R.id.crime_call_suspect);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                CommonDataKinds.
                permissionReadContacts();
                Log.d("TAG3",mCrime.getSuspectPN());
            }
        });


        if (mCrime.getSuspect() != null){
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.edu.bunz.ftcriminalintent.fileprovider",mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage,REQUESST_PHOTO);
            }
        });



        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        mPhotoView.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v){
                if ((mPhotoFile == null) || (!mPhotoFile.exists())) {
                    Toast.makeText(getActivity(), "相片不存在", Toast.LENGTH_SHORT).show();
                } else {
                    FragmentManager manager = getFragmentManager();
                    DialogFragment dialog = PhotoDialogFragment.newInstance(mPhotoFile);
                    dialog.show(manager, DIALOG_IMAGE);
                }
            }
        });

//        updatePhotoView();
        ViewTreeObserver mPhotoObserver = mPhotoView.getViewTreeObserver();
        mPhotoObserver.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout(){
                        updatePhotoView(mPhotoView.getWidth(),mPhotoView.getHeight());
                        //当获得正确的宽高后，请移除这个观察者，否则回调会多次执行：
                        mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });


        return  v;
    }


public void intentPhone(){
        if (mCrime.getSuspectPN() != null) {
            Cursor c = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{mCrime.getSuspectPN()},
                    null
            );
            try {
                if ((c == null) || (c.getCount() == 0)) {
                    return;
                }
                c.moveToFirst();
                String number = c.getString(0);

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            } finally {
                assert c != null;
                c.close();
            }
        } else {
            Toast.makeText(getActivity(), R.string.suspect_no_phone, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);

//        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
//        if (mSubtitleVisible){
//            subtitleItem.setTitle(R.string.hide_subtitle);
//        }else {
//            subtitleItem.setTitle(R.string.show_subtitle);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        switch (item.getItemId()) {
            case R.id.delete_crime:
//                mCallback.onCrimeIdSelected(mCrime.getId());

                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                Intent intent = new Intent(getContext(),CrimeListActivity.class);
//                //Intent.FLAG_ACTIVITY_CLEAR_TOP    
                // 销毁目标Activity和它之上的所有Activity，重新创建目标Activity
                //如果activity在task存在，将Activity之上的所有Activity结束掉
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//delete
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//// This makes sure that the container activity has implemented
//// the callback interface. If not, it throws an exception
//        try {
//            mCallback = (OnDeleteCrimeListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement OnDeleteCrimeListener");
//        }
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateCrime();
            updateDate();
        }
        else if (requestCode == REQUEST_TIME) {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTime(time);
            mTimeButton.setText(DateFormat.format("a  hh :mm",mCrime.getTime()));
        }else if (requestCode == REQUEST_CONTACT && data != null){
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    Contacts.DISPLAY_NAME, Contacts._ID
            };
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri,queryFields,null,null,null);

            try{
                if(c.getCount() == 0){
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                updateCrime();
                mSuspectButton.setText(suspect);
                Log.d("TAG",suspect);
                String suspectPn = c.getString(1);

                mCrime.setSuspectPN(suspectPn);
                Log.d("TAG",suspectPn);
            }finally {
                c.close();
            }
        }else if (requestCode == REQUESST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.edu.bunz.ftcriminalintent.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updateCrime();
//            updatePhotoView();
            updatePhotoView(mPhotoView.getWidth(), mPhotoView.getHeight());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intentPhone();


                } else {
                    Toast.makeText(getActivity(), R.string.no_peimission, Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate()));
    }
    private void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE,MMM dd";
        String dateString = DateFormat.format(dateFormat,mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else {
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTtitle(),dateString,solvedString,suspect);
        return report;
    }

    private void updatePhotoView(int width,int height){
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        }else{
//            Bitmap bitmap = PictureUtils.getScaledBitmap(
//                    mPhotoFile.getPath(),getActivity());
//            mPhotoView.setImageBitmap(bitmap);

            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(),width,height);
            mPhotoView.setImageBitmap(bitmap);

        }
    }
    //10.4
    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK,null);
    }


}
