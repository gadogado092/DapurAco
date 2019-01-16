package budi.dapuraco;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder>{

    private List<Users> usersList;
    private Context context;
    private String mCurrentid;
    private FirebaseFirestore mFirebaseFirestore;

    public UsersAdapter(Context context,List<Users>usersList){
        this.context=context;
        this.usersList=usersList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutlistusers,parent,false);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mCurrentid= FirebaseAuth.getInstance().getUid();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Nama.setText(usersList.get(position).getName());

        final String user_ID = usersList.get(position).User_id;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "HALO APA KABAR";


                Map<String, Object> notificationMessage = new HashMap<>();
                notificationMessage.put("message",message);
                notificationMessage.put("from",mCurrentid);

                mFirebaseFirestore.collection("Users/" + user_ID + "/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context,"SENT KE "+user_ID,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private TextView Nama;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            Nama= (TextView)mView.findViewById(R.id.textViewnama);
        }
    }
}
