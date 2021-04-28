package com.tangent.firebasedemo.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class KeyValueRealTimeModel extends BaseModel {
    private String key;
    private String value;

    public KeyValueRealTimeModel(@NonNull String key, @NonNull String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValueRealTimeModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((obj instanceof KeyValueRealTimeModel) && this.key.equals(((KeyValueRealTimeModel) obj).key));
    }
}
