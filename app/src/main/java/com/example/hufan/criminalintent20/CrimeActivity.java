
/**
 * Created by hufan on 2016/5/1.
 * 未应用，留做学习使用 CrimePagerActivity.java已代替工作
 */
package com.example.hufan.criminalintent20;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        //return new CrimeFragment();
        //通过调用CrimeFragment的newInstance(),传入extra参数
        UUID crimeId=(UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }


}
