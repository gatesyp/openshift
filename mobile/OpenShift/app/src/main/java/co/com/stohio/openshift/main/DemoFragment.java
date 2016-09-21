package co.com.stohio.openshift.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import co.com.stohio.openshift.R;

@SuppressWarnings("deprecation")
public class DemoFragment extends Fragment {

    Integer[] imageIDs=  {
            R.raw.happy, R.raw.bloated, R.raw.homestate_1, R.raw.homestate_2, R.raw.sick, R.raw.sick_1, R.raw.tired_pt1, R.raw.tired_pt2, R.drawable.home

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_demo, container, false);
        // Note that Gallery view is deprecated in Android 4.1---
        Gallery gallery = (Gallery) v.findViewById(R.id.gallery1);
//        gallery.setAdapter(new ImageAdapter(this));
//        gallery.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position,long id)
//            {
//                 display the images selected
//                ImageView imageView = (ImageView) v.findViewById(R.id.image1);
//                imageView.setImageResource(imageIDs[position]);
//            }
//        });

//        ImageView imageView1 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget1 = new GlideDrawableImageViewTarget(imageView1);
//        Glide.with(this).load(R.raw.homestate_1).into(imageViewTarget1);
//        ImageView imageView2 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget2 = new GlideDrawableImageViewTarget(imageView2);
//        Glide.with(this).load(R.raw.happy).into(imageViewTarget2);
//        ImageView imageView3 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget3 = new GlideDrawableImageViewTarget(imageView3);
//        Glide.with(this).load(R.raw.tired_pt1).into(imageViewTarget3);
//        ImageView imageView4 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget4 = new GlideDrawableImageViewTarget(imageView4);
//        Glide.with(this).load(R.raw.sick).into(imageViewTarget4);
//        ImageView imageView5 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget5 = new GlideDrawableImageViewTarget(imageView5);
//        Glide.with(this).load(R.raw.bloated).into(imageViewTarget5);

        return v;
    }

    public static DemoFragment newInstance(String text) {
        DemoFragment f = new DemoFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;

    }
    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        public ImageAdapter(Context c)
        {
            context = c;
            // sets a grey background; wraps around the images
//            TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
//            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
//            a.recycle();
        }
        // returns the number of images
        public int getCount() {
            return imageIDs.length;
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(imageIDs[position]);
            imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }
}

