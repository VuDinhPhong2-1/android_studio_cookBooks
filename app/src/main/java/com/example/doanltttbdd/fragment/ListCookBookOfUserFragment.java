package com.example.doanltttbdd.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanltttbdd.Adapter.ListCookBookOfUserAdapter;
import com.example.doanltttbdd.R;
import com.example.doanltttbdd.model.Cookbook;
import com.example.doanltttbdd.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListCookBookOfUserFragment extends Fragment {
    private static final String TAG = "ListCookBookOfUserFragme";

    private RecyclerView recyclerView;
    private List<Cookbook> items;
    private ListCookBookOfUserAdapter adapter;
    private String cookbookId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_cook_book_of_user, container, false);
        if (getArguments() != null && getArguments().containsKey("cookbookId")) {
            cookbookId = getArguments().getString("cookbookId");
        }
        recyclerView = view.findViewById(R.id.recyclerview_id);
        items = new ArrayList<>();
        adapter = new ListCookBookOfUserAdapter(requireContext(), items);


        // Set up item click listener
        adapter.setOnItemClickListener(new ListCookBookOfUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String cookbookId) {
                // Navigate to EditCookBookFragment and pass data if needed
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                EditCookBookFragment editCookBookFragment = new EditCookBookFragment();

                Bundle bundle = new Bundle();
                bundle.putString("cookbookId", cookbookId);
                editCookBookFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container, editCookBookFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // Check if user is logged in
        if (Utils.checkSession(requireContext())) {
            // Get userId from session
            String creatorID = Utils.getCurrentSessionUserId(requireContext());
            // Query data from Firebase
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cookBooks");
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    items.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Cookbook cookbook = dataSnapshot.getValue(Cookbook.class);
                        if (cookbook != null && cookbook.getCreatorId().equals(creatorID)) {
                            items.add(cookbook);
                        }
                    }
                    if (items.isEmpty()) {
                        // Hiển thị thông báo khi không có cookbook cá nhân
                        TextView noCookbookTextView = view.findViewById(R.id.no_cookbook_text_view);
                        noCookbookTextView.setVisibility(View.VISIBLE);
                        noCookbookTextView.setText("Chưa có món ăn nào!!!");
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        // Hiển thị danh sách cookbook
                        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                        recyclerView.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error reading data from Firebase: " + error.getMessage());
                }
            });
        }

        return view;
    }
}
