package com.tangent.firebasedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.model.KeyValueRealTimeModel;

import java.util.ArrayList;

public class KeyValueAdapter extends RecyclerView.Adapter<KeyValueAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<KeyValueRealTimeModel> mList;
    private ItemClickListener mListener;

    public KeyValueAdapter(Context context, ArrayList<KeyValueRealTimeModel> list, ItemClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.real_time_database_list_item_key_value, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KeyValueRealTimeModel item = getItem(position);
        holder.tvKey.setText(item.getKey());
        holder.tvValue.setText(item.getValue());
        holder.btnRemove.setOnClickListener(v -> mListener.onRemoveButtonCLicked(position, item));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Context getContext() {
        return mContext;
    }

    public KeyValueRealTimeModel getItem(int position) {
        return mList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKey, tvValue;
        ImageButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKey = itemView.findViewById(R.id.tvKey);
            tvValue = itemView.findViewById(R.id.tvValue);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }


    public static interface ItemClickListener {
        public void onRemoveButtonCLicked(int position, KeyValueRealTimeModel keyValueRealTimeModel);
    }
}
