package com.example.doanltttbdd.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanltttbdd.Adapter.HomeAdapter;
import com.example.doanltttbdd.R;
import com.example.doanltttbdd.model.Cookbook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment {

    private AutoCompleteTextView search_auto_complete_txt;
    private List<String> cookbookNames = new ArrayList<>();
    private RecyclerView recyclerView;
    private List<Cookbook> items;
    private HomeAdapter adapter;

    private EditText search_txt;

    private ImageView search_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        search_auto_complete_txt = view.findViewById(R.id.search_auto_complete_txt);

        DatabaseReference cookbookRef = FirebaseDatabase.getInstance().getReference("cookBooks");
        cookbookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot cookbookSnapshot : dataSnapshot.getChildren()) {
                    String recipeName = cookbookSnapshot.child("recipeName").getValue(String.class);
                    Log.d("RecipeName", recipeName);
                    cookbookNames.add(recipeName);
                }
                // Sau khi đã thêm tất cả các tên cookbook vào danh sách
                // Gọi phương thức setupAutoCompleteTextView để thiết lập Adapter cho AutoCompleteTextView
                setupAutoCompleteTextView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
        recyclerView = view.findViewById(R.id.recyclerview_id);
        search_btn = view.findViewById(R.id.search_btn);
        items = new ArrayList<>();
        adapter = new HomeAdapter(requireContext(), items);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cookBooks");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cookbook cookbook = dataSnapshot.getValue(Cookbook.class);
                    if (cookbook != null) {
                        cookbook.setCreatorId(cookbook.getCreatorId());
                        items.add(cookbook);
                    } else {
                        Log.d("Error", "Cookbook is null");
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Error reading data from Firebase: " + error.getMessage());
            }
        });

        // Xử lý sự kiện khi người dùng nhấn nút tìm kiếm
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy nội dung từ AutoCompleteTextView search_auto_complete_txt
                String searchQuery = search_auto_complete_txt.getText().toString();
                // Tìm kiếm và hiển thị các cookbook phù hợp với từ khóa
                searchCookbooks(searchQuery);
            }
        });

        return view;
    }


    // Phương thức để thiết lập Adapter cho AutoCompleteTextView
    private void setupAutoCompleteTextView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, cookbookNames);
        search_auto_complete_txt.setAdapter(arrayAdapter);
    }

    // Phương thức để tìm kiếm và hiển thị các cookbook phù hợp với từ khóa
    @SuppressLint("NotifyDataSetChanged")
    private void searchCookbooks(String searchQuery) {
        List<Cookbook> filteredItems = new ArrayList<>();

        // Lặp qua tất cả các cookbook và kiểm tra xem tên có chứa từ khóa tìm kiếm không
        for (Cookbook cookbook : items) {
            if (cookbook.getRecipeName().toLowerCase().contains(searchQuery.toLowerCase())) {
                filteredItems.add(cookbook);
            }
        }

        // Cập nhật danh sách hiển thị với các cookbook phù hợp
        adapter.setItems(filteredItems);
        adapter.notifyDataSetChanged();
    }
}
