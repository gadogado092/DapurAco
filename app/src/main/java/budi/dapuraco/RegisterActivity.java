package budi.dapuraco;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText nama,email,password,c_password,nohp;
    private Button btn_regist;
    private ProgressBar loading;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseFirestore mFirestore;
    private DatabaseReference database;
    private DatabaseReference mDatabase;
// ...

    //private DatabaseReference database;
    //private static String URL_REGIST="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        database = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loading=findViewById(R.id.loading);
        nama=findViewById(R.id.name);
        email=findViewById(R.id.email);
        nohp=findViewById(R.id.nohp);
        password=findViewById(R.id.password);
        c_password=findViewById(R.id.c_password);
        btn_regist=findViewById(R.id.btnreg);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!nama.getText().toString().trim().isEmpty()&&!nohp.getText().toString().trim().isEmpty()&&!c_password.getText().toString().trim().isEmpty()&&!email.getText().toString().trim().isEmpty()&& !password.getText().toString().trim().isEmpty() ){
                    Regist();
                }else {
                    nama.setError("Diperlukan");
                    email.setError("Diperlukan");
                    password.setError("Diperlukan");
                    c_password.setError("Diperlukan");
                    nohp.setError("Diperlukan");
                }*/

                RegisterFiresbase();

            }
        });




    }
    private void RegisterFiresbase(){
        final String Nama= nama.getText().toString().trim();
        String Email= email.getText().toString().trim();
        String Password= password.getText().toString().trim();
        String Nohp= nohp.getText().toString().trim();
        if (!TextUtils.isEmpty(Nama) && !TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password) && !TextUtils.isEmpty(Nohp)){
            loading.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                                loading.setVisibility(View.GONE);
                                final String user_id = mAuth.getCurrentUser().getUid();
                                String token_id= FirebaseInstanceId.getInstance().getToken();
                                Map<String,Object> userMap= new HashMap<>();
                                userMap.put("name",Nama);
                                userMap.put("token_id",token_id);
                                mFirestore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        sendToMain();
                                    }
                                });


                    }else {
                        Toast.makeText(RegisterActivity.this, "Registration Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.INVISIBLE);
                        Log.d("TESS",task.getException().toString());
                    }
                }
            });
        }
    }


    private void Regist(){
        loading.setVisibility(View.VISIBLE);
        btn_regist.setVisibility(View.GONE);

        final String nama= this.nama.getText().toString().trim();
        final String email= this.email.getText().toString().trim();
        final String password= this.password.getText().toString().trim();
        final String cpassword= this.c_password.getText().toString().trim();
        final String nohp= this.nohp.getText().toString().trim();
        if (password.equals(cpassword)){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, konfigurasi.URL_REGIS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String sukses = jsonObject.getString("sukses");
                                if (sukses.equals("1")) {
                                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(RegisterActivity.this, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(RegisterActivity.this, "Pendaftaran Errorr " + e.toString(), Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                btn_regist.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Pendaftaran Error " + error.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_regist.setVisibility(View.VISIBLE);
                        }
                    })

            {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nama", nama);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("nohp", nohp);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(RegisterActivity.this,"Confirm Password Salah",Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        //Intent i=new Intent(context, LoginActivity.class);
        //context.startActivity(i);
        finish();
    }

    private void sendToMain(){
        loading.setVisibility(View.GONE);
        Intent i=new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
