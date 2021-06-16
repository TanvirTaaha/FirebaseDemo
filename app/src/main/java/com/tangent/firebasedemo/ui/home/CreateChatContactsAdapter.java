package com.tangent.firebasedemo.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tangent.firebasedemo.databinding.ContactRecyclerViewItemBinding;
import com.tangent.firebasedemo.model.PhoneContactModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreateChatContactsAdapter extends RecyclerView.Adapter<CreateChatContactsAdapter.VHContact> {
    private Context context;
    private List<PhoneContactModel> mList;
    public CreateChatContactsAdapter(Context context, List<PhoneContactModel> list) {
        this.context = context;
        this.mList = list;
    }

    protected static class VHContact extends RecyclerView.ViewHolder {
        protected ContactRecyclerViewItemBinding binding;
        public VHContact(@NonNull @NotNull ContactRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @NotNull
    @Override
    public VHContact onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new VHContact(ContactRecyclerViewItemBinding.inflate(LayoutInflater.from(getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VHContact holder, int position) {
        PhoneContactModel model = getItem(position);
        holder.binding.tvName.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public PhoneContactModel getItem(int position) {
        return mList.get(position);
    }

    private Context getContext() {
        return context;
    }

    public List<PhoneContactModel> getList() {
        return mList;
    }
}
