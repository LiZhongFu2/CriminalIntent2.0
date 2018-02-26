package com.example.hufan.criminalintent20;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hufan on 2016/5/2.
 */
public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE=
            " com.example.hufan.criminalintent20.date";

    private Date mDate;
    //传递数据给该Fragment
    public static DatePickerFragment newInstance(Date date){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_DATE,date);
        DatePickerFragment fragment=new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate=(Date)getArguments().getSerializable(EXTRA_DATE);
        Calendar calender=Calendar.getInstance();
        calender.setTime(mDate);
        int year=calender.get(Calendar.YEAR);
        int month=calender.get(Calendar.MONTH);//此处与书本上不同
        int day=calender.get(Calendar.DAY_OF_MONTH);


        //加载自定义对话框视图
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);

        //初始化DatePicker
        DatePicker datePicker=(DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
             mDate=new GregorianCalendar(year,month,day).getTime();
                getArguments().putSerializable(EXTRA_DATE,mDate);
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                //.setPositiveButton(android.R.string.ok,null)//参数设置（资源id,监听器参数）
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener(){  //回调目标fragment
                    public void onClick(DialogInterface dialog,int which){
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode){
        if(getTargetFragment()==null)
            return;
        Intent i=new Intent();
        i.putExtra(EXTRA_DATE,mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
    }


}

