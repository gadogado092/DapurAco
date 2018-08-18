package budi.dapuraco;




import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;

public class makananadapter extends RecyclerView.Adapter<makananadapter.MyViewHolder>{
    private List<makanan> makananList;
    private Context context;
    makananadapter(Context context) {
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, harga;
        ImageView makan;
        MyViewHolder(View itemView) {
            super(itemView);
            makan=(ImageView) itemView.findViewById(R.id.imageViewmakan);
            nama=(TextView) itemView.findViewById(R.id.textViewnama);
            harga=(TextView) itemView.findViewById(R.id.textViewharga);
            //pathh=(TextView) itemView.findViewById(R.id.textViewpath);
        }
    }
    void setlistmakananadapter(List<makanan> makananList) {
        this.makananList = makananList;
    }


    @Override
    public makananadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layoutlistmakan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull makananadapter.MyViewHolder holder, int position) {
        makanan makanan=makananList.get(position);
        if (holder.makan != null) {
            Glide.with(context)
                    .load(konfigurasi.URL_IMAGE+""+makanan.getPath())
                    //.override(300, 300)
                    //.override()
                    //.crossFade()
                    .into(holder.makan);
        }
        holder.nama.setText(makanan.getNama());
        holder.harga.setText(makanan.getHarga());
        //holder.pathh.setText(makanan.getPath());

        //System.out.println("TES"+makanan.getNama());
    }

    @Override
    public int getItemCount() {
        if (makananList==null)return 0;
        //System.out.println("SISE"+makananList.size());
        return makananList.size();
    }


}
