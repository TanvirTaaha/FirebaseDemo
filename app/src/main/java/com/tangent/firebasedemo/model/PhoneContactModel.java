package com.tangent.firebasedemo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Entity
public class PhoneContactModel extends BaseModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "display_name")
    private String name;


    @ColumnInfo(name = "phone_numbers")
    private ArrayList<String> phoneNoList;

    @Ignore
    public PhoneContactModel() {
    }

    public PhoneContactModel(int id, @NonNull String name, @NonNull ArrayList<String> phoneNoList) {
        this.id = id;
        this.name = name;
        this.phoneNoList = phoneNoList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull ArrayList<String> getPhoneNoList() {
        return phoneNoList;
    }

    public void setPhoneNoList(@NotNull ArrayList<String> phoneNoList) {
        this.phoneNoList = phoneNoList;
    }
}
