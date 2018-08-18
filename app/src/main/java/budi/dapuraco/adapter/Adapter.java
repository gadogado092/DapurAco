package budi.dapuraco.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import budi.dapuraco.R;
import budi.dapuraco.models.FoodItem;
import budi.dapuraco.models.Footer;
import budi.dapuraco.models.Header;
import budi.dapuraco.models.RecyclerViewItem;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Adapter extends RecyclerView.Adapter{
    //Declare List of Recyclerview Items
    List<RecyclerViewItem> recyclerViewItems;
    //Header Item Type
    private static final int HEADER_ITEM = 0;
    //Footer Item Type
    private static final int FOOTER_ITEM = 1;
    ////Food Item Type
    private static final int FOOD_ITEM = 2;
    Context mContext;

    public Adapter(List<RecyclerViewItem> recyclerViewItems, Context mContext) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
        if (viewType == HEADER_ITEM) {
            row = inflater.inflate(R.layout.custom_row_header, parent, false);
            return new HeaderHolder(row);
        }  else if (viewType == FOOD_ITEM) {
            row = inflater.inflate(R.layout.custom_row_food, parent, false);
            return new FoodItemHolder(row);

        }
        else if (viewType == FOOTER_ITEM) {
            row = inflater.inflate(R.layout.custom_row_footer, parent, false);
            return new FooterHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);

        //Check holder instance to populate data  according to it
        if (holder instanceof HeaderHolder) {
            final HeaderHolder headerHolder = (HeaderHolder) holder;
            Header header = (Header) recyclerViewItem;
            //set data
            //headerHolder.texViewHeaderText.setText(header.getHeaderText());
            //headerHolder.textViewCategory.setText(header.getCategory());
            //Glide.with(mContext).load(header.getImageUrl()).into(headerHolder.imageViewHeader);
            final ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(mContext);
            headerHolder.viewPager.setAdapter(viewPagerAdapter);

            //headerHolder.dotscount=viewPagerAdapter.getCount();
            //headerHolder.dots=new ImageView[headerHolder.dotscount];


        } else if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;
            Footer footer = (Footer) recyclerViewItem;
            //set data
            footerHolder.texViewQuote.setText(footer.getQuote());
            footerHolder.textViewAuthor.setText(footer.getAuthor());
            Glide.with(mContext).load(footer.getImageUrl()).into(footerHolder.imageViewFooter);

        } else if (holder instanceof FoodItemHolder) {
            FoodItemHolder foodItemHolder = (FoodItemHolder) holder;
            FoodItem foodItem = (FoodItem) recyclerViewItem;
            //set data
            foodItemHolder.texViewFoodTitle.setText(foodItem.getTitle());
            foodItemHolder.texViewDescription.setText(foodItem.getDescription());
            foodItemHolder.textViewPrice.setText(foodItem.getPrice());
            Glide.with(mContext).load(foodItem.getImageUrl())
                    .thumbnail(/*sizeMultiplier=*/ 0.25f)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.indicator_circle))
                    .into(foodItemHolder.imageViewFood);
            //check food item is hot or not to set visibility of hot text on image
            if (foodItem.isHot()) {
                foodItemHolder.textViewIsHot.setVisibility(View.VISIBLE);
                foodItemHolder.textViewIsHot.setText("NEW");
            }
            else
                foodItemHolder.textViewIsHot.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemViewType(int position) {
        //here we can set view type
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        //if its header then return header item
        if (recyclerViewItem instanceof Header)
            return HEADER_ITEM;
            //if its Footer then return Footer item
        else if (recyclerViewItem instanceof Footer)
            return FOOTER_ITEM;
            //if its FoodItem then return Food item
        else if (recyclerViewItem instanceof FoodItem)
            return FOOD_ITEM;
        else
            return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    //Food item holder
    private class FoodItemHolder extends RecyclerView.ViewHolder {
        TextView texViewFoodTitle, texViewDescription, textViewPrice, textViewIsHot;
        ImageView imageViewFood;

        FoodItemHolder(View itemView) {
            super(itemView);
            texViewFoodTitle = itemView.findViewById(R.id.texViewFoodTitle);
            texViewDescription = itemView.findViewById(R.id.texViewDescription);
            imageViewFood = itemView.findViewById(R.id.imageViewFood);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewIsHot = itemView.findViewById(R.id.textViewIsHot);
        }
    }
    //header holder
    private class HeaderHolder extends RecyclerView.ViewHolder {
        //TextView texViewHeaderText, textViewCategory;
        //ImageView imageViewHeader;
        private ViewPager viewPager;
        private LinearLayout sliderdotspanel;
        private int dotscount;
        private ImageView[] dots;

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(mContext);
        //headerHolder.dotscount=viewPagerAdapter.getCount();
        //headerHolder.dots=new ImageView[headerHolder.dotscount];
        //Timer timer=new Timer();
          //  timer.scheduleAtFixedRate(new MytimerTask(header.getActivity(),headerHolder),1000,5000);

        HeaderHolder(View itemView) {
            super(itemView);
            //texViewHeaderText = itemView.findViewById(R.id.texViewHeaderText);
            //textViewCategory = itemView.findViewById(R.id.textViewCategory);
            //imageViewHeader = itemView.findViewById(R.id.imageViewHeader);
            viewPager=(ViewPager) itemView.findViewById(R.id.viewpager);
            sliderdotspanel=(LinearLayout)itemView.findViewById(R.id.SliderDots);
            dotscount=viewPagerAdapter.getCount();
            dots= new ImageView[dotscount];

            final int[] currentPage = {0};
            Timer timer;
            final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
            final long PERIOD_MS = 5000;
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (viewPager.getCurrentItem() == viewPagerAdapter.getCount()-1) {
                        currentPage[0] = 0;
                    }
                    viewPager.setCurrentItem(currentPage[0]++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer .schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);


            for (int i=0; i<dotscount;i++){
                dots[i] =new ImageView(mContext);
                dots[i].setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.nonactive_dot));
                LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,0,8,0);
                sliderdotspanel.addView(dots[i],params);
            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.active_dot));

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i=0;i<dotscount;i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.nonactive_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.active_dot));


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });



        }
    }
    //footer holder
    private class FooterHolder extends RecyclerView.ViewHolder {
        TextView texViewQuote, textViewAuthor;
        ImageView imageViewFooter;

        FooterHolder(View itemView) {
            super(itemView);
            texViewQuote = itemView.findViewById(R.id.texViewQuote);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            imageViewFooter = itemView.findViewById(R.id.imageViewFooter);
        }
    }


    private class MytimerTask extends TimerTask {
        Activity activity;
        HeaderHolder headerHolder;
        private MytimerTask(Activity a,HeaderHolder h){
            activity=a;
            headerHolder =h;
        }
        @Override
        public void run() {
            if(activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (headerHolder.viewPager.getCurrentItem()==0){
                            headerHolder.viewPager.setCurrentItem(1);
                        }else if (headerHolder.viewPager.getCurrentItem()==1){
                            headerHolder.viewPager.setCurrentItem(2);
                        }else if (headerHolder.viewPager.getCurrentItem()==2){
                            headerHolder.viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }


}
