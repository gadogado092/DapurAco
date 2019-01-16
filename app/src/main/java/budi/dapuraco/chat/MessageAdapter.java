package budi.dapuraco.chat;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import budi.dapuraco.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessagesList;
    private FirebaseAuth mAuth;
    public MessageAdapter(List<Messages> mMessagesList){
        this.mMessagesList=mMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            mAuth=FirebaseAuth.getInstance();
            String UID=mAuth.getCurrentUser().getUid();
            Messages c=mMessagesList.get(position);

            if (c.getFrom().equals(UID)){
                holder.messageText.setBackgroundColor(Color.GRAY);
                holder.messageText.setTextColor(Color.WHITE);
                holder.messageText.setGravity(Gravity.RIGHT);
            }else {
                holder.messageText.setBackgroundColor(Color.GRAY);
                holder.messageText.setTextColor(Color.BLACK);
            }
            holder.messageText.setText(c.getMessage());

    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public CircleImageView profileImage;

        public MessageViewHolder(View itemView) {
            super(itemView);

            messageText =(TextView) itemView.findViewById(R.id.single_message_tv);
            profileImage =(CircleImageView) itemView.findViewById(R.id.single_message_image);

        }
    }
}
