package co.com.stohio.openshift.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.stohio.openshift.R;

/**
 * Created by emerson on 9/17/16.
 */
public class MySocialAdapter extends RecyclerView
        .Adapter<MySocialAdapter
        .DataObjectHolder> {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObjectSocial> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView username;
        TextView dateTime;
        ImageView image;
        TextView petState;
        TextView petLevel;


        public DataObjectHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.textView4);
            image = (ImageView) itemView.findViewById(R.id.imageView1);
            petLevel = (TextView) itemView.findViewById(R.id.textView5);
            petState = (TextView) itemView.findViewById(R.id.textView6);

//            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MySocialAdapter(ArrayList<DataObjectSocial> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_social, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.username.setText(mDataset.get(position).getmText4());
        holder.petLevel.setText(mDataset.get(position).getmText5());
        holder.petState.setText(mDataset.get(position).getmText6());
        holder.image.setImageResource(mDataset.get(position).getmImgId());

    }

    public void addItem(DataObjectSocial dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
