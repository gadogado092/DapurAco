package budi.dapuraco;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import budi.dapuraco.adapter.Adapter;
import budi.dapuraco.models.FoodItem;
import budi.dapuraco.models.Footer;
import budi.dapuraco.models.Header;
import budi.dapuraco.models.RecyclerViewItem;
import budi.dapuraco.utils.Space;

public class beranda extends Fragment{
    private View v;
    private String JSON_STRING;
    private List<RecyclerViewItem> recyclerViewItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.layoutberanda, container, false);

        //init RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //add space item decoration and pass space you want to give
        recyclerView.addItemDecoration(new Space(20));
        //finally set adapter
        recyclerView.setAdapter(new Adapter(createDummyList(), getContext()));
        //getdata();

        return v;
    }

    private void initRecyclerView() {

    }

    //Method to create dummy data
    private List<RecyclerViewItem> createDummyList() {

        Header header = new Header(getActivity());
        //Header header = new Header("Welcome To Food Express", "Non-Veg Menu",
        //        "https://cdn.pixabay.com/photo/2017/09/30/15/10/pizza-2802332_640.jpg");
        //add header
        recyclerViewItems.add(header);


        //getJSON();

        StringRequest stringRequest = new StringRequest(konfigurasi.URL_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                //ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    jsonObject = new JSONObject(response);
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
                        //makanan makanan = new makanan(id,name,harga,path);
                        //makananList.add(makanan);
                        FoodItem foodItem = new FoodItem(name, "TES", konfigurasi.URL_IMAGE+path, harga,true);
                        //add food items
                        recyclerViewItems.add(foodItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Terjadi Kesalahan " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity(), "Terjadi Kesalahan " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);



        Footer footer = new Footer("Your diet is a bank account. Good food choices are good investments.",
                "Bethenny Frankel", "https://cdn.pixabay.com/photo/2016/12/26/17/28/background-1932466_640.jpg");
        //add footer
        //recyclerViewItems.add(footer);

        return recyclerViewItems;
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

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
                //System.out.println("JSON1"+s);
                return getdata();
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
                //makanan makanan = new makanan(id,name,harga,path);
                //makananList.add(makanan);
                FoodItem foodItem = new FoodItem(name, "TES", konfigurasi.URL_IMAGE+path, harga,true);
                //add food items
                recyclerViewItems.add(foodItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Terjadi Kesalahan " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        //mAdapter.setlistmakananadapter(makananList);
        //recyclerView.setAdapter(mAdapter);
        //makanan makanan = new makanan("3","sasa","12000","asa");
        //makananList.add(makanan);
        //mAdapter.notifyDataSetChanged();
    }
    private String getdata(){

        /*RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(konfigurasi.URL_GET_ALL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        //data=jsonObject.getString("user");
                        //printData(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("user",e.getMessage());
                }
            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);*/


        final String[] data = new String[1];
        StringRequest stringRequest = new StringRequest(konfigurasi.URL_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                data[0] =response;
                System.out.println("JSON"+data[0]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return data[0];
    }





}
