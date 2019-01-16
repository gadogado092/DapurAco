package budi.dapuraco.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import budi.dapuraco.R;
import budi.dapuraco.Users;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserActivity extends AppCompatActivity {

    private RecyclerView mUsersList;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencenotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseReferencenotif = FirebaseDatabase.getInstance().getReference().child("Notifications");

        mUsersList=(RecyclerView)findViewById(R.id.recylerViewUser);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");
        FirebaseRecyclerOptions<UsersModel> options =
                new FirebaseRecyclerOptions.Builder<UsersModel>()
                        .setQuery(query, UsersModel.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<UsersModel, UsersViewHolder>(options) {

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_single_layout, parent, false);
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, final int position, @NonNull UsersModel model) {
                    holder.setName(model);
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String userclick=getRef(position).getKey();

                            /*Intent i=new Intent(AllUserActivity.this, ChatActivity.class);
                            i.putExtra("user",userclick);
                            startActivity(i);*/

                            String usercurrent= FirebaseAuth.getInstance().getCurrentUser().getUid();
                            HashMap<String,String> notifdata= new HashMap<>();
                            notifdata.put("from",usercurrent);
                            notifdata.put("notification","new daa asda weqe ada asqwd dada adawq adadw adadq ada");
                            mDatabaseReferencenotif.child(userclick).push().setValue(notifdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AllUserActivity.this,"BERHASIl",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
            }
        };
        mUsersList.setAdapter(adapter);
        adapter.startListening();


    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(final UsersModel model){
            TextView name=(TextView)mView.findViewById(R.id.userSingleName);
            TextView status=(TextView)mView.findViewById(R.id.userSingleStatus);
            final CircleImageView image=(CircleImageView)mView.findViewById(R.id.userSingleImage);


            name.setText(model.getName());
            status.setText(model.getStatus());

            final int MAX_WIDTH = 250;
            final int MAX_HEIGHT = 250;

            final int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

            // Loads given image
            if (!model.getImage().toString().equals("default")){
                //Picasso.get().load(image).placeholder(R.drawable.ic_action_person).into(mDisplayImage);
                Picasso.get().load(model.getImage().toString()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_action_person).into(image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(model.getImage().toString())
                                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                                .resize(size, size)
                                .centerInside()
                                .into(image);
                    }
                });
            }


        }
    }
}
