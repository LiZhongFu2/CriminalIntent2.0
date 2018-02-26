package com.example.hufan.criminalintent20;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hufan on 2016/4/30.
 */
public class CrimeFragment extends Fragment {

    private static final String DIALOG_IMAGE="image";

    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    public static final String TAG="CrimeFragment";

    public static final String EXTRA_CRIME_ID=
            "com.example.hufan.criminalintent20.crime_id";
    private static final String DIALOG_DATE="date";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_PHOTO=1;
    private static final int REQUEST_CONTACT=2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    //代码的封装，减少代码沉余
    public void updateDate(){
        mDateButton.setText(mCrime.getmDate().toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCrime=new Crime();
        //该类获取了Crime对象(以下2句)
        //UUID crimeId=(UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);//获取UUID并放入变量中
        UUID crimeId=(UUID)getArguments().getSerializable(EXTRA_CRIME_ID);//获取argument
        mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);//利用Crime中的id从CrimeLab中调取Crime对象

    }

    /* 将缩放后的图片设置给ImageView视图 */
    private void showPhoto(){
        Photo p=mCrime.getmPhoto();
        BitmapDrawable b=null;
        if(p!=null){
            String path=getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
            b=PictureUtils.getScaledDrawable(getActivity(),path);
        }
        mPhotoView.setImageDrawable(b);
    }

    //加载图片
    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    //卸载图片
    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    //对Crime视图的操作
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime,container,false);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            if(NavUtils.getParentActivityName(getActivity())!=null){
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }//启用应用图标的导航功能
        }
        mTitleField=(EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());//动态获取title
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateButton=(Button)v.findViewById(R.id.crime_date);
        //mDateButton.setText(mCrime.getmDate().toString());
        updateDate();
        //mDateButton.setEnabled(false);

        //显示DialogFragment
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getActivity().getSupportFragmentManager();
                //DatePickerFragment dialog=new DatePickerFragment();
                DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getmDate());//将参数传递
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(fm,DIALOG_DATE);
                //此处出现爆红，需添加show()方法，
                // 解决办法是在DatePickerFragment.java中倒入
                // import android.support.v4.app.DialogFragment;
    }
});
        mSolvedCheckBox=(CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());//动态设置√
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        //启动CrimeCameraActivity
        mPhotoButton=(ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),CrimeCameraActivity.class);
                //startActivity(i);
                startActivityForResult(i,REQUEST_PHOTO);
            }
        });

        mPhotoView=(ImageView)v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p=mCrime.getmPhoto();
                if(p==null)
                    return;
                FragmentManager fm=getActivity().getSupportFragmentManager();
                String path=getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm,DIALOG_IMAGE);
            }
        });

        //检查设备是否带有相机
        PackageManager pm=getActivity().getPackageManager();
        boolean hasACamera=pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)||
                Build.VERSION.SDK_INT<Build.VERSION_CODES.GINGERBREAD||
                android.hardware.Camera.getNumberOfCameras()>0;
        if(!hasACamera){
            mPhotoButton.setEnabled(false);
        }

        //发送陋习报告
        Button reportButton=(Button)v.findViewById(R.id.crime_reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");//数据类型
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                i=Intent.createChooser(i,getString(R.string.send_report));//使用选择器
                startActivity(i);
            }
        });

        //发送隐式Intent,打开联系人列表并进行选择
        mSuspectButton=(Button)v.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);//联系人数据获取位置
                startActivityForResult(i,REQUEST_CONTACT);
            }
        });

        if (mCrime.getmSuspect()!=null){
            mSuspectButton.setText(mCrime.getmSuspect());
        }

        return v;
    }

    // "私有地方" 附加argument给fragment(顺序性),返回fragment
    public static  CrimeFragment newInstance(UUID crimeId){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_CRIME_ID,crimeId);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //响应DatePacker对话框
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK) return;//此处曾经单词错误，导致结果无法得到返回结果
        if(requestCode==REQUEST_DATE){
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            //mDateButton.setText(mCrime.getmDate().toString());
            updateDate();
        }else if(requestCode==REQUEST_PHOTO){
            String filename=data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if(filename!=null){
                //Log.i(TAG,"filename: "+filename);
                Photo p=new Photo(filename);
                mCrime.setmPhoto(p);
                showPhoto();//确保从CrimeCameraActivity返回后，ImageView可以显示用户所拍摄的图片
                //Log.i(TAG,"crime:"+mCrime.getmTitle()+"has a photo");
            }
        }else if (requestCode==REQUEST_CONTACT){    //获取联系人姓名
            Uri contactUri=data.getData();
            String[] queryFields=new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor c=getActivity().getContentResolver()
                    .query(contactUri,queryFields,null,null,null);
            if (c.getCount()==0){
                c.close();
                return;
            }

            c.moveToFirst();
            String suspect=c.getString(0);//需要搜查
            mCrime.setmSuspect(suspect);
            mSuspectButton.setText(suspect);
            c.close();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity())!=null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //保存应用数据
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    //创建四段字符串信息，并返回拼接完整的陋习报告文本信息
    private String getCrimeReport(){
        String solvedString=null;
        if (mCrime.ismSolved()){
            solvedString=getString(R.string.crime_report_solved);
        }else {
            solvedString=getString(R.string.crime_report_unsolved);
        }
        String dateFormat="EEE,MMM dd";
        String dateString= DateFormat.format(dateFormat,mCrime.getmDate()).toString();
        String suspect=mCrime.getmSuspect();
        if (suspect==null){
            suspect=getString(R.string.crime_no_suspect);
        }else {
            suspect=getString(R.string.crime_report_suspect,suspect);
        }

        String report=getString(R.string.crime_report,
                mCrime.getmTitle(),dateString,solvedString,suspect);
        return  report;
    }



}
