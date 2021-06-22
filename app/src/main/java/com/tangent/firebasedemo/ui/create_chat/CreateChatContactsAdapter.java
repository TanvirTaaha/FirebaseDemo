package com.tangent.firebasedemo.ui.create_chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.databinding.ContactRecyclerViewItemBinding;
import com.tangent.firebasedemo.model.PhoneContactModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreateChatContactsAdapter extends RecyclerView.Adapter<CreateChatContactsAdapter.VHContact> {
    private final Context context;
    private final List<PhoneContactModel> mList;
    private OnItemClickListener onItemClickListener;

    public CreateChatContactsAdapter(Context context, List<PhoneContactModel> list) {
        this.context = context;
        this.mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
        holder.binding.tvBio.setText(model.getBio());

        Glide.with(getContext())
                .load(model.getProfilePicture())
                .placeholder(R.drawable.user_avatar)
                .into(holder.binding.ivProPic);

        holder.binding.getRoot()
                .setOnClickListener(v -> onItemClickListener.onItemClick(position, model));
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

    public interface OnItemClickListener {
        void onItemClick(int position, PhoneContactModel model);
    }
}
