package com.example.doanltttbdd.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.doanltttbdd.R;
import com.example.doanltttbdd.model.Cookbook;
import com.example.doanltttbdd.model.User;
import com.example.doanltttbdd.utils.Utils;

public class DetailsFoodActivity extends AppCompatActivity {

    ImageView imageView;
    TextView tenMonTextView, nameUserTextView, emailUserTextView, phoneUserTextView, moToMonAnTextView, thoi_gian_nau_txt;
    LinearLayout linear_layout_nguyen_lieu, linear_layout_cac_buoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_food);

        imageView = findViewById(R.id.image_food);
        tenMonTextView = findViewById(R.id.ten_mon_txt);
        nameUserTextView = findViewById(R.id.name_user_txt);
        emailUserTextView = findViewById(R.id.email_user_txt);
        phoneUserTextView = findViewById(R.id.phone_user_txt);
        moToMonAnTextView = findViewById(R.id.mo_to_mon_an_txt);
        thoi_gian_nau_txt = findViewById(R.id.thoi_gian_nau_txt);
        linear_layout_nguyen_lieu = findViewById(R.id.linear_layout_nguyen_lieu);
        linear_layout_cac_buoc = findViewById(R.id.linear_layout_cac_buoc);
        String cookBookID = getIntent().getStringExtra("cookBookID");

        Utils.getCookbookByKey(cookBookID, new Utils.CookbookCallback() {
            @Override
            public void onCookbookRetrieved(Cookbook cookbook, String errorMessage) {
                if (cookbook != null) {
                    RequestOptions requestOptions = new RequestOptions();
                    Glide.with(DetailsFoodActivity.this)
                            .load(cookbook.getImage())
                            .apply(requestOptions)
                            .into(imageView);
                    tenMonTextView.setText(cookbook.getRecipeName());
                    moToMonAnTextView.setText(cookbook.getDescription());
                    thoi_gian_nau_txt.setText(cookbook.getPreparationTime());
                    if (cookbook.getIngredients().size() < 1) {
                        return;
                    } else {
                        for (int i = 0; i < cookbook.getIngredients().size(); i++) {
                            // Inflate layout XML của một item nguyên liệu mới
                            View nguyenLieuItemView = LayoutInflater.from(DetailsFoodActivity.this).inflate(R.layout.nguyen_lieu_details_item, null);
                            // Lấy TextView từ layout nguyen_lieu_details_item
                            TextView nguyenLieuTextView = nguyenLieuItemView.findViewById(R.id.nguyen_lieu_txt);

                            // Set text cho TextView từ danh sách nguyên liệu
                            nguyenLieuTextView.setText(cookbook.getIngredients().get(i));
                            // Thêm item nguyên liệu mới vào nguyen_lieu_layout_cha
                            linear_layout_nguyen_lieu.addView(nguyenLieuItemView);
                        }
                    }
                    if (cookbook.getInstructions().size() < 1) {
                        return;
                    } else {
                        int a = 1;
                        for (int i = 0; i < cookbook.getInstructions().size(); i++) {
                            // Inflate layout XML của một item nguyên liệu mới
                            View nguyenLieuItemView = LayoutInflater.from(DetailsFoodActivity.this).inflate(R.layout.cac_buoc_nau_details_item, null);
                            // Lấy TextView từ layout nguyen_lieu_details_item
                            TextView numberStepTextView = nguyenLieuItemView.findViewById(R.id.number_step_txt);
                            TextView buoc_txt = nguyenLieuItemView.findViewById(R.id.buoc_txt);
                            // Set text cho TextView
                            buoc_txt.setText(cookbook.getInstructions().get(i));
                            numberStepTextView.setText(String.valueOf(a));
                            // Thêm item nguyên liệu mới vào nguyen_lieu_layout_cha
                            linear_layout_cac_buoc.addView(nguyenLieuItemView);
                            a++;
                        }
                    }
                    Utils.getUserById(cookbook.getCreatorId(), new Utils.UserCallback() {
                        @Override
                        public void onUserRetrieved(User user, String errorMessage) {
                            if (user != null) {
                                // Xử lý thông tin người dùng tại đây
                                nameUserTextView.setText(user.getFullName());
                                emailUserTextView.setText(user.getEmail());
                                phoneUserTextView.setText(user.getPhone());
                            } else {
                                Log.e("User", errorMessage != null ? errorMessage : "Unknown error occurred");
                            }
                        }
                    });
                } else {
                    Log.e("Cookbook", errorMessage != null ? errorMessage : "Unknown error occurred");
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        // Gọi phương thức finish() để đóng DetailsFoodActivity và quay lại Activity trước đó
        Log.d("back_btn", "Back button pressed");
        super.onBackPressed();
        finish();
    }

}
