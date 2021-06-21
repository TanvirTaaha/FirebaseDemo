package com.tangent.firebasedemo.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.databinding.ActivityHomeBinding;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;
import com.tangent.firebasedemo.ui.home.chats.ChatsFragment;
import com.tangent.firebasedemo.utils.IntentExtraTag;
import com.tangent.firebasedemo.utils.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class HomeActivity extends AppCompatActivity {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private ActivityHomeBinding binding;
    private UserModel mUserModel;
    private HomeActivityViewModel viewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
        viewModel.setUserId(preferenceManager.getUserId());

        mUserModel = viewModel.getUserModel();

        setupTabWithViewPager();
        startContactLoading();
    }

    private void startContactLoading() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                Timber.d("sending permission");
                requestForPermission();
            } else {
                Timber.d("already was granted");
                //already contacts are loaded
            }
        } else {
            Timber.d("version is less than 'M'");
            viewModel.refreshContacts();
        }
    }

    private void requestForPermission() {
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            Timber.d("Permission callback");
            if (isGranted) {
                Timber.v("Granted");
                viewModel.refreshContacts(); //First time after app install
            } else {
                Timber.i("Not granted");
                Toast.makeText(getApplicationContext(), "App needs permission to read your contacts", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_CANCELED, new Intent().putExtra(IntentExtraTag.PERMISSION_CONTACT_READ.getTag(), false));
                finish();
            }
        }).launch(Manifest.permission.READ_CONTACTS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserModel = viewModel.getUserModel();
    }

    private void setupTabWithViewPager() {
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(this);
        binding.viewPager.setAdapter(homePagerAdapter);
        new TabLayoutMediator(binding.tabLayout,
                binding.viewPager,
                (tab, position) -> tab.setText(TAB_TITLES[position])).attach();
    }

    public HomeActivityViewModel getViewModel() {
        return viewModel;
    }

    public static class HomePagerAdapter extends FragmentStateAdapter {

        private final HomeActivity mActivity;

        public HomePagerAdapter(HomeActivity activity) {
            super(activity.getSupportFragmentManager(), activity.getLifecycle());
            this.mActivity = activity;
        }

        public Context getContext() {
            return mActivity;
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 1:
                    return TestFragment.newInstance("Tab 2", "test");
                case 0:
                default:
                    return ChatsFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}