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
public class MyEventsAdapter extends RecyclerView
        .Adapter<MyEventsAdapter
        .DataObjectHolder> {

    private ArrayList<DataObjectEvents> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView category;
        TextView exp_change;
        TextView exp_after;
        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.textView4);
            exp_change = (TextView) itemView.findViewById(R.id.textView5);
            exp_after = (TextView) itemView.findViewById(R.id.textView6);
            image = (ImageView) itemView.findViewById(R.id.imageView1);

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

    public MyEventsAdapter(ArrayList<DataObjectEvents> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_events, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.category.setText(mDataset.get(position).getmText4());
        holder.exp_change.setText(mDataset.get(position).getmText5());
        holder.exp_after.setText(mDataset.get(position).getmText6());
        holder.image.setImageResource(mDataset.get(position).getmImgId());
    }

    public void addItem(DataObjectEvents dataObj, int index) {
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
