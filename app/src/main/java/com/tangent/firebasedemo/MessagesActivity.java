package com.tangent.firebasedemo;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tangent.firebasedemo.adapter.ConversationAdapter;
import com.tangent.firebasedemo.model.Chat;
import com.tangent.firebasedemo.utils.AnimButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessagesActivity extends AppCompatActivity {

    //widgets
    private RecyclerView rcvConversation;
    private ImageButton ibBackPress;
    private ImageView ivProPic;
    private LinearLayout llToolbarBackPress;
    private RelativeLayout rlToolbar;
    private TextView tvToolbarName;
    private TextView tvToolbarSubtitle;
    private TextInputLayout tilMsgText;
    private TextInputEditText tietMsgText;

    //vars
    public static final String ANONYMOUS = "Anonymous";
    private AnimButton animButtonSendMsg;
    private ArrayList<Chat> mChats;
    private ConversationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Toolbar toolbarChat = findViewById(R.id.toolbarChat);


        //initialize widgets
        rcvConversation = findViewById(R.id.rcvConversation);
        ibBackPress = toolbarChat.findViewById(R.id.ibBackPress);
        ivProPic = toolbarChat.findViewById(R.id.ivProPic);
        llToolbarBackPress = toolbarChat.findViewById(R.id.llToolbarBackPress);
        tietMsgText = findViewById(R.id.tietMsgText);
        tilMsgText = findViewById(R.id.tilMsgText);
        rlToolbar = findViewById(R.id.rlToolbar);
        tvToolbarName = findViewById(R.id.tvToolbarName);
        tvToolbarSubtitle = findViewById(R.id.tvToolbarSubtitle);
        animButtonSendMsg = findViewById(R.id.animButtonSendMsg);

        mChats = new ArrayList<>();
        mAdapter = new ConversationAdapter(this, mChats);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        rcvConversation.setLayoutManager(llm);
        rcvConversation.setAdapter(mAdapter);

        llToolbarBackPress.setOnClickListener(v -> onBackPressed());

        tilMsgText.setStartIconOnClickListener(v -> {
            hideKeyboard(tietMsgText);
        });
        tietMsgText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String message = s.toString().trim();
                animButtonSendMsg.goToState(message.length() == 0 ? AnimButton.FIRST_STATE : AnimButton.SECOND_STATE);
            }
        });

        animButtonSendMsg.setOnClickListener(v -> {
            if (animButtonSendMsg.getState() == AnimButton.SECOND_STATE) {
                String message = tietMsgText.getText().toString().trim();
                tietMsgText.setText("");
                tietMsgText.clearFocus();
                addMessage(message, message.toLowerCase().contains("me:"));
            } else {
                Toast.makeText(MessagesActivity.this, "Nothing to send", Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setChatClickListener(new ConversationAdapter.ChatClickListener() {
            @Override
            public void onChatClicked(View v, int position) {
                Toast.makeText(MessagesActivity.this, mChats.get(position).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.ivTest).setOnClickListener(v -> {

        });
    }

    private void addMessage(String message, boolean mine) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault()).format(new Date());
        mChats.add(new Chat(message, null, dateStr, null, null, mine));
        mAdapter.notifyItemInserted(mChats.size() - 1);
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