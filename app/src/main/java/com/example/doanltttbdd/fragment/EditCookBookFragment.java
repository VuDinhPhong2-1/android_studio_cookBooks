package com.example.doanltttbdd.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.doanltttbdd.Activity.DetailsFoodActivity;
import com.example.doanltttbdd.R;
import com.example.doanltttbdd.model.Cookbook;
import com.example.doanltttbdd.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EditCookBookFragment extends Fragment {

    private Uri ImageUri = null;
    private String creatorId;
    private String imageUrl;
    private String cookBookId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_cook_book, container, false);
//        TextView choose_image_food_btn = view.findViewById(R.id.choose_image_food_btn);
        EditText ten_mon_an_ET = view.findViewById(R.id.ten_mon_an_ET);
        EditText mo_ta_mon_an_ET = view.findViewById(R.id.mo_ta_mon_an_ET);
        EditText khau_phan_an_ET = view.findViewById(R.id.khau_phan_an_ET);
        EditText thoi_gian_nau_ET = view.findViewById(R.id.thoi_gian_nau_ET);
        TextView them_nguyen_lieu_btn = view.findViewById(R.id.them_nguyen_lieu_btn);
        TextView them_buoc_btn = view.findViewById(R.id.them_buoc_btn);
        LinearLayout nguyen_lieu_layout_cha = view.findViewById(R.id.nguyen_lieu_layout_cha);
        LinearLayout nguyen_lieu_layout_cha_2 = view.findViewById(R.id.nguyen_lieu_layout_cha_2);
        AppCompatButton luu_cook_book_btn = view.findViewById(R.id.luu_cook_book_btn);
        ImageView image_food_da_chon = view.findViewById(R.id.image_food_da_chon);
        AppCompatButton xoa_cook_book_btn = view.findViewById(R.id.xoa_cook_book_btn);
        ImageView dismis_btn = view.findViewById(R.id.dismis_btn);
        dismis_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            String cookbookId = bundle.getString("cookbookId");
            if (cookbookId != null) {
                Utils.getCookbookByKey(cookbookId, new Utils.OnGetCookbookCompleteListener() {

                    @Override
                    public void onGetCookbookSuccess(Cookbook cookbook) {
                        RequestOptions requestOptions = new RequestOptions();
                        Glide.with(EditCookBookFragment.this)
                                .load(cookbook.getImage())
                                .apply(requestOptions)
                                .into(image_food_da_chon);
                        ten_mon_an_ET.setText(cookbook.getRecipeName());
                        mo_ta_mon_an_ET.setText(cookbook.getDescription());
                        ten_mon_an_ET.setText(String.valueOf(cookbook.getRecipeName()));
                        khau_phan_an_ET.setText(String.valueOf(cookbook.getServingSize()));
                        thoi_gian_nau_ET.setText(cookbook.getPreparationTime());
                        creatorId = cookbook.getCreatorId();
                        imageUrl = cookbook.getImage();
                        cookBookId = cookbookId;
                        if (cookbook.getIngredients().size() < 1) {
                            return;
                        } else {
                            for (int i = 0; i < cookbook.getIngredients().size(); i++) {
                                // Inflate layout XML của một item nguyên liệu mới
                                View nguyenLieuItemView = LayoutInflater.from(requireContext()).inflate(R.layout.buoc_item, null);
                                // Lấy TextView từ layout nguyen_lieu_details_item
                                EditText buoc_txt = nguyenLieuItemView.findViewById(R.id.buoc_txt);

                                // Set text cho TextView từ danh sách nguyên liệu
                                buoc_txt.setText(cookbook.getIngredients().get(i));
                                // Thêm item nguyên liệu mới vào nguyen_lieu_layout_cha
                                nguyen_lieu_layout_cha.addView(nguyenLieuItemView);
                            }
                        }
                        if (cookbook.getInstructions().size() < 1) {
                            return;
                        } else {

                            for (int i = 0; i < cookbook.getInstructions().size(); i++) {
                                // Inflate layout XML của một item nguyên liệu mới
                                View nguyenLieuItemView = LayoutInflater.from(requireContext()).inflate(R.layout.buoc_item, null);
                                // Lấy TextView từ layout nguyen_lieu_details_item
                                EditText buoc_txt = nguyenLieuItemView.findViewById(R.id.buoc_txt);

                                // Set text cho TextView từ danh sách nguyên liệu
                                buoc_txt.setText(cookbook.getInstructions().get(i));
                                // Thêm item nguyên liệu mới vào nguyen_lieu_layout_cha
                                nguyen_lieu_layout_cha_2.addView(nguyenLieuItemView);

                            }
                        }
                    }

                    @Override
                    public void onGetCookbookFailure(String errorMessage) {

                    }
                });
            }
        }

        them_nguyen_lieu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View nguyenLieuItemView = LayoutInflater.from(requireContext()).inflate(R.layout.nguyen_lieu_item, null);
                nguyen_lieu_layout_cha.addView(nguyenLieuItemView);
            }
        });
        them_buoc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View nguyenLieuItemView = LayoutInflater.from(requireContext()).inflate(R.layout.buoc_item, null);
                nguyen_lieu_layout_cha_2.addView(nguyenLieuItemView);
            }
        });

        luu_cook_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkSession(requireContext())) {
                    String tenMonAn = String.valueOf(ten_mon_an_ET.getText());
                    String moTa = String.valueOf(mo_ta_mon_an_ET.getText());
                    String khauPhanText = khau_phan_an_ET.getText().toString();
                    String thoiGianNau = String.valueOf(thoi_gian_nau_ET.getText());

                    int khauPhan;
                    if (khauPhanText.isEmpty()) {
                        Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        khauPhan = Integer.parseInt(khauPhanText);
                    }

                    ArrayList<String> nguyenLieuList = new ArrayList<>();
                    for (int i = 0; i < nguyen_lieu_layout_cha.getChildCount(); i++) {
                        View childView = nguyen_lieu_layout_cha.getChildAt(i);
                        if (childView instanceof LinearLayout) {
                            LinearLayout nguyenLieuLayout = (LinearLayout) childView;
                            for (int j = 0; j < nguyenLieuLayout.getChildCount(); j++) {
                                View grandChildView = nguyenLieuLayout.getChildAt(j);
                                if (grandChildView instanceof EditText) {
                                    EditText nguyenLieuEditText = (EditText) grandChildView;
                                    String nguyenLieu = nguyenLieuEditText.getText().toString();
                                    nguyenLieuList.add(nguyenLieu);
                                }
                            }
                        }
                    }
                    ArrayList<String> cachLamList = new ArrayList<>();
                    for (int i = 0; i < nguyen_lieu_layout_cha_2.getChildCount(); i++) {
                        View childView = nguyen_lieu_layout_cha_2.getChildAt(i);
                        if (childView instanceof LinearLayout) {
                            LinearLayout cachLamLayout = (LinearLayout) childView;
                            for (int j = 0; j < cachLamLayout.getChildCount(); j++) {
                                View grandChildView = cachLamLayout.getChildAt(j);
                                if (grandChildView instanceof EditText) {
                                    EditText cachLamEditText = (EditText) grandChildView;
                                    String cachLam = cachLamEditText.getText().toString();
                                    cachLamList.add(cachLam);
                                }
                            }
                        }
                    }
                    if (TextUtils.isEmpty(moTa) || TextUtils.isEmpty(tenMonAn) || nguyenLieuListContainsEmpty(nguyenLieuList) || cachLamListContainsEmpty(cachLamList) || TextUtils.isEmpty(thoiGianNau) || khauPhan <= 0) {
                        Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }else{
                        Cookbook updatedCookbook = new Cookbook(cookBookId,creatorId, moTa, tenMonAn, nguyenLieuList, cachLamList, thoiGianNau, khauPhan, imageUrl);
                        updateCookbookToFirebase(cookBookId, updatedCookbook);
                    }
                } else {
                    Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });

        xoa_cook_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference cookbookRef = FirebaseDatabase.getInstance().getReference("cookBooks");
                if (cookBookId != null && !cookBookId.isEmpty()) {
                    // Sử dụng cookbookRef để truy cập đến cookbook cần xóa và gọi phương thức removeValue()
                    cookbookRef.child(cookBookId).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Xóa thành công
                                    Toast.makeText(requireContext(), "Cookbook đã được xóa", Toast.LENGTH_SHORT).show();
                                    // Tùy chỉnh các hành động cần thiết sau khi xóa cookbook thành công
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xảy ra lỗi khi xóa cookbook
                                    Toast.makeText(requireContext(), "Lỗi khi xóa cookbook: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Nếu cookBookID rỗng, hiển thị thông báo lỗi
                    Toast.makeText(requireContext(), "Không thể xóa cookbook với cookBookID rỗng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean nguyenLieuListContainsEmpty(ArrayList<String> nguyenLieuList) {
        for (String nguyenLieu : nguyenLieuList) {
            if (TextUtils.isEmpty(nguyenLieu)) {
                return true;
            }
        }
        return false;
    }

    private boolean cachLamListContainsEmpty(ArrayList<String> cachLamList) {
        for (String cachLam : cachLamList) {
            if (TextUtils.isEmpty(cachLam)) {
                return true;
            }
        }
        return false;
    }

    private void updateCookbookToFirebase(String cookbookId, Cookbook cookbook) {
        DatabaseReference cookbookRef = FirebaseDatabase.getInstance().getReference("cookBooks").child(cookbookId);
        cookbookRef.setValue(cookbook)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Cookbook updated successfully", Toast.LENGTH_SHORT).show();
                        // Nếu cần thực hiện các hành động sau khi cập nhật cookbook thành công, bạn có thể thêm ở đây
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Failed to update cookbook", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
