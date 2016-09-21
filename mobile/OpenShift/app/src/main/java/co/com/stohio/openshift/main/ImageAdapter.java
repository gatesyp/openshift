package co.com.stohio.openshift.main;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import co.com.stohio.openshift.R;

/**
 * Created by root on 9/21/16.
 */
public class ImageAdapter extends PagerAdapter {
    Context Context;
    LayoutInflater mLayoutInflater;
    final int[] mItems;
    Activity activity;
    public void setActivity(Activity activity){
        this.activity = activity;
    }
    public ImageAdapter(Context context, int[] items) {
        this.Context = context;
        this.mLayoutInflater = (LayoutInflater) Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.demos);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this.activity).load(mItems[position]).into(imageViewTarget);
//        imageView.setImageResource(mItems[0]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}


