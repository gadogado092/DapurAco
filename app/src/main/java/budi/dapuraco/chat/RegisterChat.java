package budi.dapuraco.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import budi.dapuraco.R;

public class RegisterChat extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mRegProggress;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_chat);

        mRegProggress= new ProgressDialog(this);

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mDisplayName=(TextInputLayout) findViewById(R.id.reqDiplayName);
        mEmail=(TextInputLayout) findViewById(R.id.regEmail);
        mPassword=(TextInputLayout) findViewById(R.id.regPassword);
        btnRegister=(Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayName=mDisplayName.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();
                if (!TextUtils.isEmpty(displayName) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    mRegProggress.setTitle("Registering User");
                    mRegProggress.setMessage("Please Wait...");
                    mRegProggress.setCancelable(false);
                    mRegProggress.show();
                    registerUser(displayName,email,password);
                }

            }
        });
    }

    private void registerUser(final String displayName, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mRegProggress.dismiss();
                if (task.isSuccessful()){
                    FirebaseUser currentUser= mAuth.getCurrentUser();
                    String uID=currentUser.getUid();
                    String token= FirebaseInstanceId.getInstance().getToken();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);
                    HashMap<String,String> userMap=new HashMap<>();
                    userMap.put("name",displayName);
                    userMap.put("status","I am Budiman");
                    userMap.put("image","default");
                    userMap.put("device_token",token);
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                sendToMain();
                            }else {
                                Toast.makeText(RegisterChat.this, "Registration Error"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(RegisterChat.this, "Registration Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void sendToMain(){
        Intent i=new Intent(RegisterChat.this, Main2Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }


}
