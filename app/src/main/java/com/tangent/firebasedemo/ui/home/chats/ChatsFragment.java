package com.tangent.firebasedemo.ui.home.chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tangent.firebasedemo.databinding.FragmentChatsBinding;
import com.tangent.firebasedemo.ui.home.CreateChatActivity;
import com.tangent.firebasedemo.ui.home.HomeActivity;
import com.tangent.firebasedemo.ui.home.HomeActivityViewModel;
import com.tangent.firebasedemo.utils.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int REQUEST_CODE_READ_CONTACTS = 1;

    private HomeActivityViewModel homeActivityViewModel;
    private FragmentChatsBinding binding;

    public static ChatsFragment newInstance(int index) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            homeActivityViewModel = ((HomeActivity) getActivity()).getViewModel();
        } else {
            Util.closeFragment(this);
        }
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        chatsFragmentViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);

//        final TextView textView = binding.sectionLabel;
//        chatsFragmentViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), CreateChatActivity.class)));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}