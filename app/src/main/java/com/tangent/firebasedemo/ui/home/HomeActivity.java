package com.tangent.firebasedemo.ui.home;

import android.content.Context;
import android.os.Bundle;

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
import com.tangent.firebasedemo.utils.PreferenceManager;

import org.jetbrains.annotations.NotNull;

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

        mUserModel = preferenceManager.getCurrentUserModel();


        setupTabWithViewPager();
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

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mContext.getResources().getString(TAB_TITLES[position]);
//    }

        public Context getContext() {
            return mActivity;
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return ChatsFragment.newInstance(position + 1);
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}