package com.example.hufan.criminalintent20;

import android.content.Context;
import android.renderscript.ScriptGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hufan on 2016/5/4.
 */
public class CriminalIntentJSONSerializer {

    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context c,String f){
        mContext=c;
        mFilename=f;
    }

    public void saveCrimes(ArrayList<Crime> crimes)throws JSONException,IOException{
        //建立JSON数据数组
        JSONArray array=new JSONArray();
        for(Crime c:crimes)
            array.put(c.toJSON());//数组中添加已经转化的数据
        Writer writer=null;
        try{
            OutputStream out=mContext.openFileOutput(mFilename,Context.MODE_PRIVATE);
            //TODO 2016.10.17.21:10
            writer=new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            if(writer!=null)
                writer.close();
        }
    }

    //读取加载Crimes
    public ArrayList<Crime> loadCrimes() throws IOException,JSONException{
        ArrayList<Crime> crimes=new ArrayList<>();
        BufferedReader reader=null;
        try{
            InputStream in=mContext.openFileInput(mFilename);
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString=new StringBuilder();//将读取到的数据转化为JSONObject类型的string
            String Line=null;
            while ((Line=reader.readLine())!=null){
                jsonString.append(Line);
            }
            JSONArray array=(JSONArray) new JSONTokener(jsonString.toString()).nextValue();//解析为JSONArray
            for(int i=0;i<array.length();i++){
                crimes.add(new Crime(array.getJSONObject(i))); //解析为JSONList
            }
        }catch (FileNotFoundException e){
        }finally {
            if (reader!=null)
                reader.close();
        }return  crimes;
    }

}
