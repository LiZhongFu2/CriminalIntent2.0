package com.example.hufan.criminalintent20;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by hufan on 2016/5/1.
 * crime数组的单例，一个类一个实例，长期存在于内存中。
 * 创建私有构造 方法和get方法的类。
 */
public class CrimeLab {
    private static final String TAG="CrimeLab";
    private static final String FILENAME="crimes.json";

    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;

    private static CrimeLab sCrimeLab; //静态变量
    private Context mAppContext;

    private CrimeLab(Context appContext){
        mAppContext=appContext;

        //加载从内存中读到的数据
        mSerializer=new CriminalIntentJSONSerializer(mAppContext,FILENAME);
        try{
            mCrimes=mSerializer.loadCrimes();
        }catch (Exception e){
            mCrimes=new ArrayList<>();
            Log.d(TAG,"Error loading crimes: ",e);
        }
        //添加Crime实例到mCrimes容器中
        //mCrimes=new ArrayList<Crime>();
        //for(int i=0;i<100;i++){
        //    Crime c=new Crime();
        //    c.setmTitle("Crime#"+i);
        //    c.setmSolved(i%2==0);
        //   mCrimes.add(c);
        //}
    }

    //返回一个sCrimeLab单例
    public static CrimeLab get(Context c){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }
    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }
    public ArrayList<Crime>getCrimes(){
        return mCrimes;
    }

    public  Crime getCrime(UUID id){     //通过遍历ID值，返回Crime
        for(Crime c:mCrimes){
            if(c.getmId().equals(id))
                return c;
        }
        return null;
    }

    //在CrimeLab中进行数据持久保存
    public boolean saveCrimes(){
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG,"crimes saved to file");
            return true;
        }catch (Exception e){
            Log.d(TAG,"Error saving crimes: ",e);
            return false;
        }
    }

}
