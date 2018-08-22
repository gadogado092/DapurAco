package budi.dapuraco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserActivity extends AppCompatActivity {
    private Button btnlogout,btnphoto;
    private Context context;
    private SessionManager sessionManager;
    String getId;
    private EditText nama,email,nohp;
    private static String URL_READ=konfigurasi.URL+"dapuraco/read_detail.php";
    private static String URL_EDIT=konfigurasi.URL+"dapuraco/edit_detail.php";
    private static String URL_UPLOAD=konfigurasi.URL+"dapuraco/uploadprofil.php";
    private Menu action;
    private Bitmap bitmap;
    CircleImageView profilImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        setContentView(R.layout.activity_edit_user);
        btnphoto= (Button)findViewById(R.id.btnphoto);
        btnlogout = (Button)findViewById(R.id.btnlogout);
        nama = (EditText) findViewById(R.id.nama);
        email = (EditText) findViewById(R.id.email);
        nohp = (EditText) findViewById(R.id.nohp);
        profilImage= findViewById(R.id.profile_image);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager= new SessionManager(getApplicationContext());
                sessionManager.logout();
                startActivity(new Intent(EditUserActivity.this,MainActivity.class));
                //Intent i=new Intent(context, LoginActivity.class);
                //context.startActivity(i);
                finish();

            }
        });
        getUserDetail();
        nama.setFocusableInTouchMode(false);
        email.setFocusableInTouchMode(false);
        nohp.setFocusableInTouchMode(false);
        nama.setFocusable(false);
        email.setFocusable(false);
        nohp.setFocusable(false);
        //session login
        sessionManager=new SessionManager(this);
        sessionManager.checklogin();
        HashMap<String,String> user=sessionManager.getUserDetail();
        getId=user.get(sessionManager.ID);

        btnphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihfile();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditUserActivity.this,MainActivity.class));
        //Intent i=new Intent(context, LoginActivity.class);
        //context.startActivity(i);
        finish();
    }

    private void getUserDetail(){
        final ProgressDialog progressDialog=new ProgressDialog(this,R.style.MyTheme);

        //progressDialog.setMessage("Loading");

        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject= new JSONObject(response);
                    String sukses=jsonObject.getString("sukses");
                    JSONArray jsonArray=jsonObject.getJSONArray("read");

                    if (sukses.equals("1")){

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object= jsonArray.getJSONObject(i);
                            String stringnama=object.getString("nama").trim();
                            String stringemail=object.getString("email").trim();

                            nama.setText(stringnama);
                            email.setText(stringemail);
                            nohp.setText(object.getString("nohp").trim());
                            if (!object.getString("photo").trim().isEmpty()){
                                Glide.with(context)
                                        .load(konfigurasi.URL_IMAGEPROFILE+""+object.getString("photo"))
                                        //.override(300, 300)
                                        //.override()
                                        //.crossFade()
                                        .into(profilImage);
                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(EditUserActivity.this,"Error Read Detail "+e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditUserActivity.this,"Error Read Detail "+error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("id",getId);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu_actionedit,menu);
        action=menu;
        action.findItem(R.id.menu_save).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit:
            btnlogout.setVisibility(View.GONE);
            nama.setFocusableInTouchMode(true);
            email.setFocusableInTouchMode(true);
            nohp.setFocusableInTouchMode(true);
            InputMethodManager imm= (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(nama, InputMethodManager.SHOW_IMPLICIT);

            action.findItem(R.id.menu_edit).setVisible(false);
            action.findItem(R.id.menu_save).setVisible(true);

            return true;
            case R.id.menu_save:
                btnlogout.setVisibility(View.VISIBLE);
                SaveEditDetail();
                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

                nama.setFocusableInTouchMode(false);
                email.setFocusableInTouchMode(false);
                nohp.setFocusableInTouchMode(false);
                nama.setFocusable(false);
                email.setFocusable(false);
                nohp.setFocusable(false);
                return true;

                default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void SaveEditDetail() {
        final ProgressDialog progressDialog=new ProgressDialog(this,R.style.MyTheme);

        //progressDialog.setMessage("Menyimpan Perubahan...");
        progressDialog.show();

        final String id=getId;
        final String nama=this.nama.getText().toString().trim();
        final String email=this.email.getText().toString().trim();
        final String nohp=this.nohp.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            String sukses= jsonObject.getString("sukses");
                            if (sukses.equals("1")){
                                Toast.makeText(EditUserActivity.this,"Sukses",Toast.LENGTH_SHORT).show();
                                sessionManager.createsession(nama,email,id);
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(EditUserActivity.this,"Error"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditUserActivity.this,"Error"+error,Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("id",id);
                params.put("nama",nama);
                params.put("email",email);
                params.put("nohp",nohp);
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void pilihfile(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null &&data.getData()!=null){
            Uri filePath= data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("TEST"+getStringgambar(bitmap).length());
            uploadgambar(getId,getStringgambar(bitmap));
        }
    }

    private String getStringgambar(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream);

        byte[] imBytesArray=byteArrayOutputStream.toByteArray();
        String encodeImage= Base64.encodeToString(imBytesArray,Base64.DEFAULT);
        return encodeImage;
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void uploadgambar(final String id,final String photo) {
        final ProgressDialog progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();




        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String sukses=jsonObject.getString("sukses");

                            if (sukses.equals("1")){
                                Toast.makeText(EditUserActivity.this,"Upload Berhasil",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(EditUserActivity.this,"Upload Gagal",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(EditUserActivity.this,"Coba Lagi! "+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditUserActivity.this,"Coba Lagi! "+error,Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("id",id);
                params.put("photo",photo);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        /*RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_UPLOAD, null, new
                Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(String.valueOf(response));
                            String sukses=jsonObject.getString("sukses");

                            if (sukses.equals("1")){
                                Toast.makeText(EditUserActivity.this,"Upload Berhasil",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(EditUserActivity.this,"Upload Gagal",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(EditUserActivity.this,"Coba Lagi! "+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    public void onErrorResponse(VolleyError arg0) {
                        progressDialog.dismiss();
                        Toast.makeText(EditUserActivity.this,"Coba Lagi! "+arg0,Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("id",id);
                        params.put("photo",photo);
                        return params;
                    }
                };


        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        mRequestQueue.add(request);*/
    }
}
