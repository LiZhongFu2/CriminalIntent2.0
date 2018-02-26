package com.example.hufan.criminalintent20;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hufan on 2016/8/12.
 */
public class Photo {
    private static final String JSON_FILENAME="filename";
    private String mFilename;

    public Photo(String filename){
        mFilename=filename;
    }
    //序列化方法，在加载保存Photo类型的数据时，Crime会用到
    public Photo(JSONObject json)throws JSONException{

        mFilename=json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON()throws JSONException{
        JSONObject json=new JSONObject();
        json.put(JSON_FILENAME,mFilename);
        return json;
    }

    public String getmFilename(){
        return mFilename;
    }
}
