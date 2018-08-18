package budi.dapuraco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText email,password;
    private Button btn_login;
    private TextView link_regist;
    private ProgressBar loading;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager=new SessionManager(this);
        loading=findViewById(R.id.loading);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btn_login=findViewById(R.id.btnlogin);
        link_regist=findViewById(R.id.daftar);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail=email.getText().toString().trim();
                String mPassword=password.getText().toString().trim();
                if (!mEmail.isEmpty()|| !mPassword.isEmpty() ){
                    Login(mEmail,mPassword);
                }else {
                    email.setError("Diperlukan");
                    password.setError("Diperlukan");
                }
                
            }
        });

        link_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void Login(final String email, final String password) {
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,  konfigurasi.URL_LOGIN,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String sukses =jsonObject.getString("sukses");
                    JSONArray jsonArray= jsonObject.getJSONArray("login");

                    if (sukses.equals("1")){
                        for (int i=0;i< jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            String nama=object.getString("nama").trim();
                            String email=object.getString("email").trim();
                            String id=object.getString("id").trim();

                            Toast.makeText(LoginActivity.this,"Selamat "+nama+"\n Login Berhasil",Toast.LENGTH_SHORT).show();
                            sessionManager.createsession(nama,email,id);

                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                            //((LoginActivity)getApplicationContext()).finish();

                            //loading.setVisibility(View.GONE);
                        }
                        loading.setVisibility(View.GONE);
                    }
                    else if (sukses.equals("0")){
                        Toast.makeText(LoginActivity.this,"Password Salah",Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
}
