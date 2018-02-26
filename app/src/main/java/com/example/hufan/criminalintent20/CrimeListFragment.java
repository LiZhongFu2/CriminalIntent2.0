package com.example.hufan.criminalintent20;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;//曾经在这里寻觅许久，避免不同系统版本的兼容问题。
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by hufan on 2016/5/1.
 * 获取CrimeLab中的crime列表
 */
public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    private static final String TAG="CrimeListFragment";
    private boolean mSubtitleVisiable;//记录子标题的显示状态，解决旋转屏的问题

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//通知FragmentManager
        getActivity().setTitle(R.string.crime_title);//设置操作栏中的文字
        mCrimes=CrimeLab.get(getActivity()).getCrimes();
        //ArrayAdapter<Crime> adapter=new ArrayAdapter<Crime>(getActivity(),android.R.layout.simple_list_item_1,mCrimes);
        CrimeAdapter adapter=new CrimeAdapter(mCrimes);
        setListAdapter(adapter);//需要查找
        setRetainInstance(true);
        mSubtitleVisiable=false;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Crime c=(Crime)(getListAdapter()).getItem(position);
        Crime c=((CrimeAdapter)getListAdapter()).getItem(position);
        //Log.d(TAG,c.getmTitle()+"was clicked");
        //Intent i=new Intent(getActivity(),CrimeActivity.class);
        Intent i=new Intent(getActivity(),CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID,c.getmId());//创建键与键值
        startActivity(i);
    }

    //添加定制的adapter内部类
    private class CrimeAdapter extends ArrayAdapter<Crime>{
        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(),0,crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_crime,null);
            }
            Crime c=getItem(position);
            TextView titleTextView=(TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getmTitle());
            TextView dateTextView=(TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getmDate().toString());
            CheckBox solvedCheckBox=(CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.ismSolved());

            return convertView;
        }
    }

    //保持CrimeFragment数据修改 回退后的数据(刷新列表项显示)
    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
    }

    //解决旋转屏后小标题消失的问题
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=super.onCreateView(inflater, container, savedInstanceState);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            if(mSubtitleVisiable){
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }
        //为上下文菜单登记ListView
        ListView listView=(ListView)v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
        registerForContextMenu(listView);
        }else{
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);//设置列表视图的选择模式
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater=mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
                            CrimeLab crimeLab=CrimeLab.get(getActivity());
                            for(int i=adapter.getCount()-1;i>=0;i--){
                                if(getListView().isItemChecked(i)){
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }

                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });

        }
        return v;
    }

    //创建新的Crime实例并添加到CrimeLab中，启动一个CrimePagerActivity实例,返回布尔值
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime=new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i=new Intent(getActivity(),CrimePagerActivity.class);//添加设备图标
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getmId());
                startActivityForResult(i,0);
                return true;
            case R.id.menu_item_show_subtitle:   //动态添加小标题
                if(getActivity().getActionBar().getSubtitle()==null){
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    mSubtitleVisiable=true;
                }else {
                    getActivity().getActionBar().setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    mSubtitleVisiable=false;
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    //创建上写文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
    }

    //监听上下文菜单项选择事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position=info.position;
        CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
        Crime crime=adapter.getItem(position);
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
