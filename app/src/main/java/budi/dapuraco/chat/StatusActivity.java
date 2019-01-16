package budi.dapuraco.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import budi.dapuraco.R;

public class StatusActivity extends AppCompatActivity {

    private TextInputLayout mStatus;
    private Button btnUpdateStatus;
    private ProgressDialog mStatusProggress;

    private DatabaseReference mDatabaseUser;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        String uID= currentUser.getUid();
        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("Users").child(uID);

        mStatus=(TextInputLayout)findViewById(R.id.statusInput);
        mStatus.getEditText().setText(getIntent().getStringExtra("status"));

        btnUpdateStatus=(Button)findViewById(R.id.statusBtn);
        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusProggress= new ProgressDialog(StatusActivity.this);
                mStatusProggress.setTitle("Updating Status");
                mStatusProggress.setMessage("Please Wait...");
                mStatusProggress.setCancelable(false);
                mStatusProggress.show();
                String status=mStatus.getEditText().getText().toString();
                mDatabaseUser.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mStatusProggress.dismiss();
                        if (task.isSuccessful()){
                            Intent i=new Intent(StatusActivity.this, SettingActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(StatusActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
