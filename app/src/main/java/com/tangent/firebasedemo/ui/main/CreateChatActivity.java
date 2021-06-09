package com.tangent.firebasedemo.ui.main;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.databinding.ActivityCreateChatBinding;
import com.tangent.firebasedemo.utils.IntentExtraTag;

import java.util.ArrayList;

import timber.log.Timber;

public class CreateChatActivity extends AppCompatActivity {
    private ActivityCreateChatBinding binding;
    private CreateChatViewModel viewModel;
    private CreateChatContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarCreateChat);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(CreateChatViewModel.class);

        proceedAfterPermission();

        contactsAdapter = new CreateChatContactsAdapter(this,
                new ArrayList<>());
        binding.rcvContacts.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvContacts.setAdapter(contactsAdapter);

        setupSearchView();
    }

    /**
     * https://github.com/MiguelCatalan/MaterialSearchView
     * needs jCenter()
     */
    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        binding.searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_contact_menu, menu);

        binding.searchView.setMenuItem(menu.findItem(R.id.menu_search));
        menu.findItem(R.id.menu_refresh).setOnMenuItemClickListener(item -> {
            viewModel.refreshContacts();
            return true;
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    public void proceedAfterPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                Timber.d("sending permission");
                requestForPermission();
            } else {
                Timber.d("already granted");
                initializeWithViewModel();
            }
        }
    }

    private void requestForPermission() {
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            Timber.d("Permission callback");
            if (isGranted) {
                Timber.v("Granted");
                initializeWithViewModel();
            } else {
                Timber.i("Not granted");
                Toast.makeText(getApplicationContext(), "Needed permission for reading contacts to create chat", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_CANCELED, new Intent().putExtra(IntentExtraTag.PERMISSION_CONTACT_READ.getTag(), false));
                finish();
            }
        }).launch(Manifest.permission.READ_CONTACTS);
    }

    private void initializeWithViewModel() {
        ProgressDialog _progressDialog = new ProgressDialog(this);
        _progressDialog.setCanceledOnTouchOutside(false);
        _progressDialog.setMessage("Loading");
        _progressDialog.show();

        viewModel.loadContacts();
        viewModel.getContactCount().observe(this,
                integer -> binding.toolbarCreateChat.setSubtitle("" + integer + " Contacts"));

        viewModel.getContactList().observe(this, list -> {
            _progressDialog.dismiss();
            contactsAdapter.getList().addAll(0, list);
            contactsAdapter.notifyDataSetChanged();
        });

    }
}