package budi.dapuraco.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import budi.dapuraco.R;

public class LoginChat extends AppCompatActivity {

    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button btnLogin;
    private ProgressDialog mRegProggress;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_chat);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mRegProggress= new ProgressDialog(this);

        mEmail=(TextInputLayout) findViewById(R.id.logEmail);
        mPassword=(TextInputLayout) findViewById(R.id.logPassword);
        btnLogin=(Button) findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    mRegProggress.setTitle("Logging In");
                    mRegProggress.setMessage("Please Wait...");
                    mRegProggress.setCancelable(false);
                    mRegProggress.show();
                    loginUser(email,password);
                }
            }
        });
        Button btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegis();
            }
        });
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String UID=mAuth.getCurrentUser().getUid();
                    String token= FirebaseInstanceId.getInstance().getToken();
                    mDatabase.child(UID).child("device_token").setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mRegProggress.dismiss();
                            sendToMain();
                        }
                    });



                }else {
                    mRegProggress.dismiss();
                    Toast.makeText(LoginChat.this, "Login Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToRegis(){
        Intent i=new Intent(LoginChat.this, RegisterChat.class);
        startActivity(i);
        finish();
    }

    private void sendToMain(){
        Intent i=new Intent(LoginChat.this, Main2Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
