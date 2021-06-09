package com.tangent.firebasedemo.ui.main.chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tangent.firebasedemo.databinding.FragmentChatsBinding;
import com.tangent.firebasedemo.ui.main.CreateChatActivity;
import com.tangent.firebasedemo.ui.main.HomeActivityViewModel;
import com.tangent.firebasedemo.utils.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_VIEW_MODEL = "view_model";
    public static final int REQUEST_CODE_READ_CONTACTS = 1;

    private HomeActivityViewModel homeActivityViewModel;
    private FragmentChatsBinding binding;

    public static ChatsFragment newInstance(int index, HomeActivityViewModel viewModel) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putSerializable(ARG_VIEW_MODEL, viewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            homeActivityViewModel = (HomeActivityViewModel) getArguments().getSerializable(ARG_VIEW_MODEL);
        } else {
            if (getActivity() != null) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
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
        View root = binding.getRoot();

//        final TextView textView = binding.sectionLabel;
//        chatsFragmentViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), CreateChatActivity.class)));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}