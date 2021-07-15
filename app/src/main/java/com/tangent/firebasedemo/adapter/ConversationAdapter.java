package com.tangent.firebasedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.databinding.ChatMessageItemMeBinding;
import com.tangent.firebasedemo.databinding.ChatMessageItemYouBinding;
import com.tangent.firebasedemo.model.firebasemodel.ChatBubble;
import com.tangent.firebasedemo.utils.PreferenceManager;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.VHChat> {

    private final Context mContext;
    private final ArrayList<ChatBubble> mChatBubbles;
    private final SimpleDateFormat mDateFormatFull;
    private final SimpleDateFormat mDateFormatShort;
    private ChatClickListener mClickListener;
    private ChatLongClickListener mLongClickListener;
    private final PreferenceManager prefMan;

    public Context getContext() {
        return mContext;
    }

    private final int VIEW_TYPE_ME = 0;
    private final int VIEW_TYPE_YOU = 1;

    public ConversationAdapter(Context context, ArrayList<ChatBubble> chatBubbles) {
        this.mContext = context;
        this.mChatBubbles = chatBubbles;
        this.mDateFormatFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault());
        this.mDateFormatShort = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        this.prefMan = new PreferenceManager(context);
    }

    public ChatBubble getItem(int position) {
        return mChatBubbles.get(position);
    }

    protected static class VHChat extends RecyclerView.ViewHolder {
        TextView tvText;
        TextView tvTime;
        ImageView ivCorner;
        ViewBinding binding;

        public VHChat(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            tvText = itemView.findViewById(R.id.tvText);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivCorner = itemView.findViewById(R.id.ivCorner);
            this.binding = binding;
        }

        public void setClickListener(int position, ChatClickListener listener) {
            itemView.setOnClickListener(v -> listener.onChatClicked(v, position));
        }

        public void setLongClickListener(int position, ChatLongClickListener listener) {
            itemView.setOnLongClickListener(v -> {
                listener.onChatLongClicked(v, position);
                return true;
            });
        }
    }

    protected static class VHMe extends VHChat {
        ChatMessageItemMeBinding binding;

        public VHMe(@NonNull ChatMessageItemMeBinding binding) {
            super(binding);
            this.binding = binding;
        }
    }

    protected static class VHYou extends VHChat {
        ChatMessageItemYouBinding binding;

        public VHYou(@NonNull ChatMessageItemYouBinding binding) {
            super(binding);
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public VHChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ME) {
            return new VHMe(ChatMessageItemMeBinding.inflate(LayoutInflater.from(getContext()), parent, false));
        } else {
            return new VHYou(ChatMessageItemYouBinding.inflate(LayoutInflater.from(getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VHChat holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ME || getItemViewType(position) == VIEW_TYPE_YOU) {
            ChatBubble chatBubble = getItem(position);

            String dateStr;
            try {
                dateStr = mDateFormatShort.format(Objects.requireNonNull(mDateFormatFull.parse(chatBubble.getTime())));
            } catch (ParseException | NullPointerException e) {
                e.printStackTrace();
                dateStr = "";
            }

            holder.tvText.setText(MessageFormat.format("{0}            ", chatBubble.getText()));
//            vhChat.tvText.setText(Html.fromHtml(Html.toHtml(vhChat.tvText.getText().toString()) + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;")); // 10 spaces

            holder.tvTime.setText(dateStr);

            if (mClickListener != null) {
                holder.setClickListener(position, mClickListener);
            }

            if (mLongClickListener != null) {
                holder.setLongClickListener(position, mLongClickListener);
            }

            if ((position != 0) && (getItemViewType(position - 1) == getItemViewType(position)))
                holder.ivCorner.setVisibility(View.INVISIBLE);
            else {
                holder.ivCorner.setVisibility(View.VISIBLE);
            }

            if (mClickListener != null) {
                holder.setClickListener(position, mClickListener);
            }
            if (mLongClickListener != null) {
                holder.setLongClickListener(position, mLongClickListener);
            }
        } else { //for error detecting if no view type matched
            holder.tvText.setText(R.string.blank_chat_bubble_when_error);
            holder.tvTime.setText(R.string.blank_chat_bubble_when_error);
        }
    }

    @Override
    public int getItemCount() {
        return mChatBubbles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mChatBubbles.get(position).getSender_id().equals(prefMan.getUserId()) ? VIEW_TYPE_ME : VIEW_TYPE_YOU;
    }

    public ArrayList<ChatBubble> getList() {
        return mChatBubbles;
    }

    public void setChatClickListener(ChatClickListener chatClickListener) {
        this.mClickListener = chatClickListener;
    }

    public void setChatLongClickListener(ChatLongClickListener chatLongClickListener) {
        this.mLongClickListener = chatLongClickListener;
    }

    public interface ChatClickListener {
        void onChatClicked(View v, int position);
    }

    public interface ChatLongClickListener {
        void onChatLongClicked(View v, int position);
    }
}
