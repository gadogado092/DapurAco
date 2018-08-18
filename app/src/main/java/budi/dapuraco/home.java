package budi.dapuraco;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import budi.dapuraco.adapter.ViewPagerAdapter;


public class home extends Fragment {
    private View v;
    private String JSON_STRING;
    private ArrayList<makanan> makananList=new ArrayList<>();
    private RecyclerView recyclerView;
    private makananadapter mAdapter;


    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.layouthome, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        mAdapter = new makananadapter(getContext());
        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlayoutmanager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        getJSON();


        viewPager=(ViewPager) v.findViewById(R.id.viewpager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getContext());
        viewPager.setAdapter(viewPagerAdapter);

        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new MytimerTask(),2000,4000);
        return v;
    }
    public class MytimerTask extends TimerTask{
        Activity activity=getActivity();
        @Override
        public void run() {
            if(activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem()==0){
                            viewPager.setCurrentItem(1);
                        }else if (viewPager.getCurrentItem()==1){
                            viewPager.setCurrentItem(2);
                        }else if (viewPager.getCurrentItem()==2){
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }


        }
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            //ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(getActivity(),"Mengambil Data","Mohon Tunggu...",false,false);
            }



            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ALL);
                //System.out.println("TEST"+s);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                JSON_STRING = s;
                showmakan();

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showmakan() {
        //System.out.println("TEST"+JSON_STRING);
        JSONObject jsonObject = null;
        //ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_ID);
                String name = jo.getString(konfigurasi.TAG_NAMA);
                String harga= jo.getString(konfigurasi.TAG_HARGA);
                String path=jo.getString(konfigurasi.TAG_PATH);

                //HashMap<String,String> employees = new HashMap<>();
                //employees.put(konfigurasi.TAG_ID,id);
                //employees.put(konfigurasi.TAG_NAMA,name);
                //System.out.println("TEST"+id);
                makanan makanan = new makanan(id,name,harga,path);
                makananList.add(makanan);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.setlistmakananadapter(makananList);
        recyclerView.setAdapter(mAdapter);
        //makanan makanan = new makanan("3","sasa","12000","asa");
        //makananList.add(makanan);
        //mAdapter.notifyDataSetChanged();
    }


}
