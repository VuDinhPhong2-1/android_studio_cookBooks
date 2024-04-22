package com.example.doanltttbdd.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.doanltttbdd.R;
import com.example.doanltttbdd.model.Cookbook;
import com.example.doanltttbdd.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddFragment extends Fragment {
    private TextView add_food_btn;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Dialog dialog;
    private Uri ImageUri = null;
   private  String imageURL;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        add_food_btn = view.findViewById(R.id.add_food_btn);
        add_food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddFoodDialog();
            }
        });

        return view;
    }

    private void openAddFoodDialog() {
        dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_layout_dialog_add_cook_book);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView choose_image_food_btn = dialog.findViewById(R.id.choose_image_food_btn);
        EditText ten_mon_an_ET = dialog.findViewById(R.id.ten_mon_an_ET);
        EditText mo_ta_mon_an_ET = dialog.findViewById(R.id.mo_ta_mon_an_ET);
        EditText khau_phan_an_ET = dialog.findViewById(R.id.khau_phan_an_ET);
        EditText thoi_gian_nau_ET = dialog.findViewById(R.id.thoi_gian_nau_ET);
        TextView them_nguyen_lieu_btn = dialog.findViewById(R.id.them_nguyen_lieu_btn);
        TextView them_buoc_btn = dialog.findViewById(R.id.them_buoc_btn);
        LinearLayout nguyen_lieu_layout_cha = dialog.findViewById(R.id.nguyen_lieu_layout_cha);
        LinearLayout nguyen_lieu_layout_cha_2 = dialog.findViewById(R.id.nguyen_lieu_layout_cha_2);
        ImageView dismis_btn = dialog.findViewById(R.id.dismis_btn);
        AppCompatButton luu_cook_book_btn = dialog.findViewById(R.id.luu_cook_book_btn);
        choose_image_food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        them_nguyen_lieu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate layout XML của một item nguyên liệu mới
                View nguyenLieuItemView = LayoutInflater.from(requireContext()).inflate(R.layout.nguyen_lieu_item, null);

                // Thêm item nguyên liệu mới vào nguyen_lieu_layout_cha
                nguyen_lieu_layout_cha.addView(nguyenLieuItemView);
            }
        });
        them_buoc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate layout XML của một item nguyên liệu mới
                View nguyenLieuItemView = LayoutInflater.from(requireContext()).inflate(R.layout.buoc_item, null);

                // Thêm item nguyên liệu mới vào nguyen_lieu_layout_cha
                nguyen_lieu_layout_cha_2.addView(nguyenLieuItemView);
            }
        });
        dismis_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        luu_cook_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkSession(requireContext())) {
                    // Lấy ID của người dùng hiện tại từ session
                    String creatorID = Utils.getCurrentSessionUserId(requireContext());

                    // Lấy các giá trị từ các trường nhập liệu trong dialog
                    String tenMonAn = String.valueOf(ten_mon_an_ET.getText());
                    String moTa = String.valueOf(mo_ta_mon_an_ET.getText());
                    String khauPhanText = khau_phan_an_ET.getText().toString();
                    String thoiGianNau = String.valueOf(thoi_gian_nau_ET.getText());

                    // Kiểm tra trường nhập liệu 'khauPhanText' có rỗng hay không
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
                    if (TextUtils.isEmpty(creatorID) || TextUtils.isEmpty(moTa) || TextUtils.isEmpty(tenMonAn) || nguyenLieuListContainsEmpty(nguyenLieuList) || cachLamListContainsEmpty(cachLamList) || TextUtils.isEmpty(thoiGianNau) || khauPhan <= 0 || ImageUri == null) {
                        Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Upload ảnh lên Firebase
                    uploadImageToFirebase(ImageUri, new OnImageUploadListener() {
                        @Override
                        public void onImageUploadSuccess(String imageUrl) {
                            // Sau khi ảnh được tải lên thành công, lưu cookbook vào Firebase
                            Cookbook cookbook = new Cookbook(creatorID, moTa, tenMonAn, nguyenLieuList, cachLamList, thoiGianNau, khauPhan, imageUrl);
                            saveCookbookToFirebase(cookbook, dialog);
                        }

                        @Override
                        public void onImageUploadFailure(String errorMessage) {
                            // Xử lý khi tải ảnh lên không thành công
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
    // Hàm kiểm tra xem danh sách nguyên liệu có chứa chuỗi rỗng không
    private boolean nguyenLieuListContainsEmpty(ArrayList<String> nguyenLieuList) {
        for (String nguyenLieu : nguyenLieuList) {
            if (TextUtils.isEmpty(nguyenLieu)) {
                return true;
            }
        }
        return false;
    }

    // Hàm kiểm tra xem danh sách cách làm có chứa chuỗi rỗng không
    private boolean cachLamListContainsEmpty(ArrayList<String> cachLamList) {
        for (String cachLam : cachLamList) {
            if (TextUtils.isEmpty(cachLam)) {
                return true;
            }
        }
        return false;
    }
    private void saveCookbookToFirebase(Cookbook cookbook, Dialog dialog) {
        DatabaseReference cookbookRef = FirebaseDatabase.getInstance().getReference("cookBooks");
        String cookBookID = cookbookRef.push().getKey(); // Lấy cookBookID từ Firebase
        cookbook.setKey(cookBookID);
        if (cookBookID != null) {
            cookbookRef.child(cookBookID).setValue(cookbook)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(requireContext(), "Cookbook saved successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss(); // Đóng hộp thoại sau khi lưu thành công
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Failed to save cookbook", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "Failed to generate cookbook ID", Toast.LENGTH_SHORT).show();
        }
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // Gọi phương thức setChosenImage để đặt ảnh đã chọn vào ImageView
            setChosenImage(selectedImageUri);
            // Không upload ảnh lên Firebase ở đây
            ImageUri = selectedImageUri;
        }
    }

    private void setChosenImage(Uri imageUri) {
        // Kiểm tra xem dialog đã được khởi tạo chưa
        if (dialog != null) {
            // Lấy ImageView từ dialog
            ImageView image_food_da_chon = dialog.findViewById(R.id.image_food_da_chon);
            // Đặt ảnh đã chọn vào ImageView
            image_food_da_chon.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri, OnImageUploadListener listener) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images").child(imageUri.getLastPathSegment());
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        listener.onImageUploadSuccess(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi upload ảnh không thành công
                    listener.onImageUploadFailure("Failed to upload image to Firebase.");
                });
    }

    // Interface để xử lý sự kiện khi tải ảnh lên Firebase
    interface OnImageUploadListener {
        void onImageUploadSuccess(String imageUrl);

        void onImageUploadFailure(String errorMessage);
    }
}
