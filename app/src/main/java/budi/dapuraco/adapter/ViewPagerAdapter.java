package budi.dapuraco.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import budi.dapuraco.R;

public class ViewPagerAdapter extends PagerAdapter{
    private Context context;
    private LayoutInflater layoutInflater;
    private String [] url={"https://dapuraco.000webhostapp.com/dapuraco/gambarmakan/ayambakar.jpg","https://dapuraco.000webhostapp.com/dapuraco/gambarmakan/esjeruk.jpg","https://dapuraco.000webhostapp.com/dapuraco/gambarmakan/esteh.jpg","https://dapuraco.000webhostapp.com/dapuraco/gambarmakan/nasigoreng.jpg"};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return url.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_layoutviewpager,null);
        ImageView imageView=(ImageView) view.findViewById(R.id.imageView2);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //imageView.setImageResource(url[position]);
        Glide.with(context)
                .load(url[position])
                //.override(300, 300)
                //.override()
                //.crossFade()
                .into(imageView);

        ViewPager vp=(ViewPager)container;
        vp.addView(view,0);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Slide "+position,Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp=(ViewPager)container;
        View view= (View) object;
        vp.removeView(view);
    }
}
