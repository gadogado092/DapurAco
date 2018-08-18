package budi.dapuraco.models;

import android.app.Activity;

public class Header extends RecyclerViewItem {
    private String HeaderText;
    private String Category;
    private String ImageUrl;
    Activity activity;
    public Header(Activity a){
        activity=a;
    }
    public Header(String headerText, String category, String imageUrl) {
        HeaderText = headerText;
        Category = category;
        ImageUrl = imageUrl;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getHeaderText() {
        return HeaderText;
    }

    public void setHeaderText(String headerText) {
        HeaderText = headerText;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
