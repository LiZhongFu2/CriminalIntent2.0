package com.example.hufan.criminalintent20;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hufan on 2016/4/30.
 */
public class Crime {

    private static final String JSON_ID="id";
    private static final String JSON_TITLE="title";
    private static final String JSON_SOLVED="solved";
    private static final String JSON_DATE="date";
    private static final String JSON_PHOTO="photo";
    private static final String JSON_SUSPECT="suspect";

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private Photo mPhoto;
    private String mSuspect;


    public Crime(){

        mId=UUID.randomUUID();
        mDate=new Date();
    }

    //将Crime对象数据转化为可写入JSON文件的JSONObject对象数据
    public JSONObject toJSON()throws JSONException{
        JSONObject json=new JSONObject();
        json.put(JSON_ID,mId.toString());
        json.put(JSON_TITLE,mTitle);
        json.put(JSON_SOLVED,mSolved);
        json.put(JSON_DATE,mDate.getTime());
        if(mPhoto!=null)
            json.put(JSON_PHOTO,mPhoto.toJSON());
        json.put(JSON_SUSPECT,mSuspect);

        return json;
    }
    //逆向操作
    public Crime(JSONObject json)throws JSONException{
        mId=UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE)){
            mTitle=json.getString(JSON_TITLE);
        }
        mSolved=json.getBoolean(JSON_SOLVED);
        mDate=new Date(json.getLong(JSON_DATE));
        if(json.has(JSON_PHOTO))
            mPhoto=new Photo(json.getJSONObject(JSON_PHOTO));
        if(json.has(JSON_SUSPECT))
            mSuspect=json.getString(JSON_SUSPECT);

    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }


    @Override
    public String toString() {
        return mTitle;
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmTitle() {
        return mTitle;
    }

    public Photo getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }
}
