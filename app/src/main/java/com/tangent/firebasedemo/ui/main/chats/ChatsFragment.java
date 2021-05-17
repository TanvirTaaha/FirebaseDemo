package com.tangent.firebasedemo.ui.main.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tangent.firebasedemo.databinding.FragmentChatsBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ChatsFragmentViewModel chatsFragmentViewModel;
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
        chatsFragmentViewModel = new ViewModelProvider(this).get(ChatsFragmentViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        chatsFragmentViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}