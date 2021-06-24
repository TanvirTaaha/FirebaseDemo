package com.tangent.firebasedemo.ui.inbox;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tangent.firebasedemo.adapter.ConversationAdapter;
import com.tangent.firebasedemo.databinding.ActivityInboxBinding;
import com.tangent.firebasedemo.model.firebasemodel.ChatBubble;
import com.tangent.firebasedemo.utils.AbstractTextWatcher;
import com.tangent.firebasedemo.utils.AnimButton;

import java.util.ArrayList;
import java.util.Objects;

public class InboxActivity extends AppCompatActivity {

    private ActivityInboxBinding binding;
    private InboxActivityViewModel viewModel;

    private ArrayList<ChatBubble> mChatBubbles;
    private ConversationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInboxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.llToolbarBackPress.setOnClickListener(v -> onBackPressed());
        viewModel = new ViewModelProvider(this).get(InboxActivityViewModel.class);
        initializeRecyclerView();

        binding.tilMsgText.setStartIconOnClickListener(v -> hideKeyboard(binding.tietMsgText));
        binding.tietMsgText.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                binding.animButtonSendMsg.goToState(s.length() == 0 ? AnimButton.FIRST_STATE : AnimButton.SECOND_STATE);
            }
        });

        binding.animButtonSendMsg.setOnClickListener(v -> {
            if (binding.animButtonSendMsg.getState() == AnimButton.SECOND_STATE) { //SendText
                String message = Objects.requireNonNull(binding.tietMsgText.getText()).toString().trim();
                binding.tietMsgText.setText("");
                binding.tietMsgText.clearFocus();
                addMessage(message, message.toLowerCase().contains("me:"));
            } else { //RecordVoice
                Toast.makeText(InboxActivity.this, "Nothing to send", Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setChatClickListener((v, position) -> Toast.makeText(InboxActivity.this, mChatBubbles.get(position).getText(), Toast.LENGTH_SHORT).show());

    }

    private void initializeRecyclerView() {
        mChatBubbles = new ArrayList<>();
        mAdapter = new ConversationAdapter(this, mChatBubbles);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        binding.rcvConversation.setLayoutManager(llm);
        binding.rcvConversation.setAdapter(mAdapter);
        viewModel.getChatBubbleList().observe(this, chatBubbles -> {
            mChatBubbles.clear();
            mChatBubbles.addAll(0, chatBubbles);
            mAdapter.notifyDataSetChanged();
        });
    }

    private void addMessage(String message, boolean mine) {
//        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault()).format(new Date());
//        mChatBubbles.add(new ChatModel(message, null, dateStr, null, null, mine));
//        mAdapter.notifyItemInserted(mChatBubbles.size() - 1);
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}