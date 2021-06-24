package com.tangent.firebasedemo.ui.home.chatModels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.databinding.ChatsRecyclerViewItemBinding;
import com.tangent.firebasedemo.model.uimodel.ChatThreadModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatThreadsListAdapter extends RecyclerView.Adapter<ChatThreadsListAdapter.VHThreadItem> {

    private final Context mContext;
    private final List<ChatThreadModel> mChatThreadModelList;

    public ChatThreadsListAdapter(Context context, List<ChatThreadModel> chatThreadModelList) {
        this.mContext = context;
        this.mChatThreadModelList = chatThreadModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ChatThreadsListAdapter.VHThreadItem onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new VHThreadItem(ChatsRecyclerViewItemBinding.inflate(LayoutInflater.from(getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatThreadsListAdapter.VHThreadItem holder, int position) {
        ChatThreadModel model = getItem(position);
        ChatsRecyclerViewItemBinding binding = holder.chatsRecyclerViewItemBinding;
        binding.tvUserName.setText(model.getChatUserName());
        if (!model.isLastMessagePhoto()){
            binding.tvMiniMessage.setText(model.getFirstMessage());
        } else {
            binding.tvMiniMessage.setText(R.string.when_last_message_was_photo);
        }
        Glide.with(getContext()).load(model.getImageUrl()).centerInside().placeholder(R.drawable.user_avatar).into(binding.ivProPic);

    }

    @Override
    public int getItemCount() {
        return mChatThreadModelList.size();
    }

    protected static class VHThreadItem extends RecyclerView.ViewHolder {
        protected ChatsRecyclerViewItemBinding chatsRecyclerViewItemBinding;
        public VHThreadItem(ChatsRecyclerViewItemBinding chatsRecyclerViewItemBinding) {
            super(chatsRecyclerViewItemBinding.getRoot());
            this.chatsRecyclerViewItemBinding = chatsRecyclerViewItemBinding;
        }
    }

    private ChatThreadModel getItem(int position) {
        return mChatThreadModelList.get(position);
    }

    private Context getContext() {
        return mContext;
    }
}
