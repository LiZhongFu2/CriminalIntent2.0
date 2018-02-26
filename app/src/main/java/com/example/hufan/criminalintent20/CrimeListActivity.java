package com.example.hufan.criminalintent20;


import android.support.v4.app.Fragment;

/**
 * Created by hufan on 2016/5/1.
 */
public class CrimeListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
