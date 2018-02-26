package com.example.hufan.criminalintent20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by hufan on 2016/5/1.
 * 创建并管理V
 */
public class CrimePagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager=new ViewPager(this);
        mViewPager.setId(R.id.viewPager);//此处引用独立资源id
        setContentView(mViewPager);

        mCrimes=CrimeLab.get(this).getCrimes();//获取数据集

        FragmentManager fm=getSupportFragmentManager();

        //设置adapter为FragmentStatePagerAdapter的一个匿名实例
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime=mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getmId());//返回一个有效配置的CrimeFragment
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面滑向哪里
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //将Crime实例标题设置给CrimePagerActivity标题
            @Override
            public void onPageSelected(int position) {
            Crime crime=mCrimes.get(position);
                if(crime.getmTitle()!=null){
                    setTitle(crime.getmTitle());
                }
            }
            //页面所处行为状态
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //循环检索crime的id，找到所选crime在数组中的索引位置
        UUID crimeId=(UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for(int i=0;i<mCrimes.size();i++){
            if(mCrimes.get(i).getmId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
