package budi.dapuraco.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Set;

import budi.dapuraco.R;
import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.Compressor;

public class SettingActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseUser;
    private FirebaseUser currentUser;
    private StorageReference mStorageReference;
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private String uID;
    private Button btnChangeStatus,btnChangeImage;
    private static final int GALLERY_PICK=1;
    private ProgressDialog mStatusProggress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mDisplayImage=(CircleImageView)findViewById(R.id.settingImage);
        mName=(TextView)findViewById(R.id.settingtvDisplayName);
        mStatus=(TextView)findViewById(R.id.settingtvStatus);
        btnChangeStatus=(Button) findViewById(R.id.settingChangeStatus);
        btnChangeImage=(Button) findViewById(R.id.settingChangeImage);

        mStorageReference= FirebaseStorage.getInstance().getReference();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        uID= currentUser.getUid();
        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("Users").child(uID);
        //mDatabaseUser.keepSynced(true);
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                final String image=dataSnapshot.child("image").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();


                mName.setText(name);
                mStatus.setText(status);
                if (!image.equals("default")){
                    //Picasso.get().load(image).placeholder(R.drawable.ic_action_person).into(mDisplayImage);
                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_action_person).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).placeholder(R.drawable.ic_action_person).into(mDisplayImage);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SettingActivity.this, StatusActivity.class);
                i.putExtra("status",mStatus.getText().toString());
                startActivity(i);
            }
        });

        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingActivity.this);*/

                Intent galeryIntent = new Intent();
                galeryIntent.setType("image/*");
                galeryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galeryIntent,"SELECT IMAGE"), GALLERY_PICK);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_PICK && resultCode==RESULT_OK){
            Uri imageURL=data.getData();

            CropImage.activity(imageURL)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mStatusProggress= new ProgressDialog(SettingActivity.this);
                mStatusProggress.setTitle("Uploading Image");
                mStatusProggress.setMessage("Please Wait...");
                mStatusProggress.setCancelable(false);
                mStatusProggress.show();

                Uri resultUri = result.getUri();



                final StorageReference filepath=mStorageReference.child("profile_images").child(uID+".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            //mStatusProggress.dismiss();
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url=uri.toString();
                                    mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("Users").child(uID);
                                    mDatabaseUser.child("image").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                mStatusProggress.dismiss();
                                                Toast.makeText(SettingActivity.this, "Success Uploading", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });

                        }else {
                            mStatusProggress.dismiss();
                            Toast.makeText(SettingActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
