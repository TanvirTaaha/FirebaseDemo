package com.tangent.firebasedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.model.uimodel.Chat;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private ArrayList<Chat> mChats;
    private SimpleDateFormat mDateFormatFull;
    private SimpleDateFormat mDateFormatShort;
    private ChatClickListener mClickListener;
    private ChatLongClickListener mLongClickListener;

    public Context getContext() {
        return mContext;
    }

    public ConversationAdapter(Context context, ArrayList<Chat> chats) {
        this.mContext = context;
        this.mChats = chats;
        this.mDateFormatFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault());
        this.mDateFormatShort = new SimpleDateFormat("HH:mm a", Locale.getDefault());
    }

    public Chat getItem(int position) {
        return mChats.get(position);
    }

    protected static class VHChat extends RecyclerView.ViewHolder {
        TextView tvText;
        TextView tvTime;
        ImageView ivCorner;

        public VHChat(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivCorner = itemView.findViewById(R.id.ivCorner);
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

    protected static class VH1 extends RecyclerView.ViewHolder {

        public VH1(@NonNull View itemView) {
            super(itemView);
        }
    }

    protected static class VH2 extends RecyclerView.ViewHolder {

        public VH2(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new VHChat(LayoutInflater.from(getContext()).inflate(R.layout.chat_message_item_you, parent, false));
        } else {
            return new VHChat(LayoutInflater.from(getContext()).inflate(R.layout.chat_message_item_me, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0 || getItemViewType(position) == 1) {
            Chat chat = getItem(position);
            VHChat vhChat = (VHChat) holder;

            String dateStr;
            try {
                dateStr = mDateFormatShort.format(Objects.requireNonNull(mDateFormatFull.parse(chat.getTime())));
            } catch (ParseException | NullPointerException e) {
                e.printStackTrace();
                dateStr = "";
            }

            vhChat.tvText.setText(MessageFormat.format("{0}            ", chat.getText()));
//            vhChat.tvText.setText(Html.fromHtml(Html.toHtml(vhChat.tvText.getText().toString()) + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;")); // 10 spaces

            vhChat.tvTime.setText(dateStr);

            if (mClickListener != null) {
                vhChat.setClickListener(position, mClickListener);
            }
            if (mLongClickListener != null) {
                vhChat.setLongClickListener(position, mLongClickListener);
            }

            if ((position != 0) && (getItemViewType(position - 1) == getItemViewType(position)))
                vhChat.ivCorner.setVisibility(View.INVISIBLE);
            else {
                vhChat.ivCorner.setVisibility(View.VISIBLE);
            }
        } else {
            VHChat vhChat = (VHChat) holder;
            vhChat.tvText.setText(R.string.blank_chat_bubble_when_error);
            vhChat.tvTime.setText(R.string.blank_chat_bubble_when_error);
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mChats.get(position).isMine() ? 0 : 1;
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
