package com.tangent.firebasedemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.adapter.KeyValueAdapter;
import com.tangent.firebasedemo.data.MessagesDatabase;
import com.tangent.firebasedemo.model.BaseModel;
import com.tangent.firebasedemo.model.KeyValueRealTimeModel;
import com.tangent.firebasedemo.model.firebasemodel.User;
import com.tangent.firebasedemo.ui.main.HomeActivity;

import java.util.ArrayList;
import java.util.Objects;

import timber.log.Timber;

public class HomeActivity2 extends AppCompatActivity {

    //widgets
    private Button btnLogout;
    private Button btnAddData;
    private Button btnMessages;
    private Button btnHomeAct1;
    private RecyclerView rvRealTimeData;
    private EditText etKey;
    private EditText etValue;
    private SwipeRefreshLayout srlDataListContainer;

    //vars
    private ArrayList<KeyValueRealTimeModel> keyValueModelArrayList;
    private KeyValueAdapter keyValueAdapter;
    private DatabaseReference databaseRefCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        btnLogout = findViewById(R.id.btnLogout);
        btnAddData = findViewById(R.id.btnAddData);
        btnMessages = findViewById(R.id.btnMessages);
        btnHomeAct1 = findViewById(R.id.btnHomeAct1);
        rvRealTimeData = findViewById(R.id.rvRealTimeData);
        etKey = findViewById(R.id.etKey);
        etValue = findViewById(R.id.etValue);
        srlDataListContainer = findViewById(R.id.srlDataListContainer);

        srlDataListContainer.setOnRefreshListener(() -> getAllDataFromDatabase(true));

        keyValueModelArrayList = new ArrayList<>();
        keyValueAdapter = new KeyValueAdapter(this, keyValueModelArrayList,
                (position, keyValueRealTimeModel) -> removeData(position,
                        keyValueRealTimeModel.getKey(),
                        keyValueRealTimeModel.getValue()));
        rvRealTimeData.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvRealTimeData.setAdapter(keyValueAdapter);

        databaseRefCities = FirebaseDatabase.getInstance().getReference().child("cities");
        messagesDatabase = new MessagesDatabase();

        getAllDataFromDatabase(false);

//        btnAddData.setOnClickListener(v -> {
//            String key, value;
//            key = etKey.getText().toString().trim();
//            value = etValue.getText().toString().trim();
//            addData(key, value);
//        });
        btnAddData.setOnClickListener(v -> test(etKey.getText().toString().trim(), etValue.getText().toString().trim()));


        btnMessages.setOnClickListener(v -> startActivity(new Intent(HomeActivity2.this, MessagesActivity.class)));
        btnHomeAct1.setOnClickListener(v -> startActivity(new Intent(HomeActivity2.this, HomeActivity.class)));

        databaseRefCities.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Timber.d("Added, snap:%s, prevName:%s", snapshot.toString(), previousChildName);
                int prevPos = keyValueModelArrayList.indexOf(new KeyValueRealTimeModel(BaseModel.ifNullThenString(previousChildName), ""));
                Timber.d("Added, Index of prevValue:%d", prevPos);
                Timber.d("Added, Index of snapshot:%d", keyValueModelArrayList.indexOf(new KeyValueRealTimeModel(BaseModel.ifNullThenString(snapshot.getKey()), "")));

                keyValueModelArrayList.add(prevPos + 1, new KeyValueRealTimeModel(snapshot.getKey(), BaseModel.ifNullThenString(snapshot.getValue()).toString()));
                keyValueAdapter.notifyItemInserted(prevPos + 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Timber.d("Changed, snap:%s, prevName:%s", snapshot.toString(), previousChildName);
                Timber.d("Changed, Index of prevValue:%d", keyValueModelArrayList.indexOf(new KeyValueRealTimeModel(BaseModel.ifNullThenString(previousChildName), "")));
                int changedPos = keyValueModelArrayList.indexOf(new KeyValueRealTimeModel(BaseModel.ifNullThenString(snapshot.getKey()), ""));
                Timber.d("Changed, Index of snapshot:%d", changedPos);

                keyValueModelArrayList.get(changedPos).setValue(Objects.requireNonNull(snapshot.getValue()).toString());
                keyValueAdapter.notifyItemChanged(changedPos);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Timber.d("Removed, snap:%s", snapshot.toString());
                int removedPos = keyValueModelArrayList.indexOf(new KeyValueRealTimeModel(BaseModel.ifNullThenString(snapshot.getKey()), ""));
                Timber.d("Removed, Index of snapshot:%d", removedPos);

                keyValueModelArrayList.remove(removedPos);
                keyValueAdapter.notifyItemRemoved(removedPos);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Timber.d("Moved, snap:%s, prevName:%s", snapshot.toString(), previousChildName);
                Timber.d("Moved, Index of prevValue:%d", keyValueModelArrayList.indexOf(new KeyValueRealTimeModel(BaseModel.ifNullThenString(previousChildName), "")));
                Timber.d("Moved, Index of snapshot:%d", keyValueModelArrayList.indexOf(new KeyValueRealTimeModel(BaseModel.ifNullThenString(snapshot.getKey()), "")));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Timber.d("Cancelled, error:%s", error.toString());
            }
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignupFirstActivity.class));
        });
        findViewById(R.id.btngoto).setOnClickListener(v -> startActivity(new Intent(HomeActivity2.this, SignupFirstActivity.class)));
    }

    private void getAllDataFromDatabase(boolean fromRefresh) {
        srlDataListContainer.setRefreshing(true);
        databaseRefCities.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    keyValueModelArrayList.clear();
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        keyValueModelArrayList.add(new KeyValueRealTimeModel(BaseModel.ifNullThenString(snapshot.getKey()), BaseModel.ifNullThenString(snapshot.getValue()).toString()));
                    }
                    keyValueAdapter.notifyDataSetChanged();
                    Timber.v("All data loaded, snapshot:%s, fromRefresh:%s", task.getResult().toString(), fromRefresh);
                    srlDataListContainer.setRefreshing(false);
                }
            }
        });
    }

    private void addData(String key, String value) {
        Timber.v("AddData button pressed");
        (TextUtils.isEmpty(key) ? databaseRefCities.push() : databaseRefCities.child(key)).setValue(value)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Timber.v("Child Added: key:%s, value:%s", key, value);
                    }
                });
    }

    private void removeData(int pos, String key, String value) {
        databaseRefCities.child(key).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Timber.v("Child removed: kew:%s, value:%s, listPos:%d", key, value, pos);
            }
        });
    }

    MessagesDatabase messagesDatabase;
    private void test(String key, String value) {
        messagesDatabase.createUser(new User("", "Taaha", "01700000", "empty", "empty", new ArrayList<>()));
    }
}