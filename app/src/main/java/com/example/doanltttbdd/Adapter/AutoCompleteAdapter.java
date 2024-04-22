package com.example.doanltttbdd.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<String> {


    public AutoCompleteAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }
}