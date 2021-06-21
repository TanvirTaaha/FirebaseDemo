package com.tangent.firebasedemo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tangent.firebasedemo.app.App;
import com.tangent.firebasedemo.databinding.FragmentTestBinding;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;
import com.tangent.firebasedemo.repository.FirebaseDatabaseRepo;
import com.tangent.firebasedemo.repository.PhoneContactsRepo;

import org.jetbrains.annotations.NotNull;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentTestBinding binding;

    FirebaseFirestore firestore;
    CollectionReference users_db;
    CollectionReference messages_db;

    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        firestore = FirebaseFirestore.getInstance();
        users_db = firestore.collection("users_db");
        messages_db = firestore.collection("messages_db");

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTestBinding.inflate(inflater, container, false);

        binding.button.setOnClickListener(v -> {
            String str = binding.editTextTextPersonName.getText().toString().trim();
            userModelFetchTest(str);
        });
        return binding.getRoot();
    }

    private void userModelFetchTest(String str) {
        FirebaseDatabaseRepo
                .getInstance()
                .fetchUserModelFromInternet(PhoneContactsRepo
                        .getInstance()
                        .handleNationalPhoneNoForBD(str), task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserModel userModel;
                            userModel = document.toObject(UserModel.class);
                            Timber.d("user%s", userModel.toString());
                        }
                    } else {
                        Timber.d("UserModel not found for:%s", str);
                    }
                });
    }

    private void phoneNoUtilTest(String str) {
        Timber.d("The phone no enterred %s", str);
        PhoneNumberUtil numberUtil = PhoneNumberUtil.createInstance(App.getAppContext());
        Phonenumber.PhoneNumber number = null;
        try {
            number = numberUtil.parse(str, null);
            Timber.d("numberObj:%s, country code:%s, regionCode:%s, hasCountryCode:%s",
                    number.toString(),
                    number.getCountryCode(),
                    numberUtil.getRegionCodeForCountryCode(number.getCountryCode())
                    , number.hasCountryCode());
        } catch (NumberParseException e) {
            Timber.d("Unable to parse number parse exception  found");
            e.printStackTrace();
        }
    }
}