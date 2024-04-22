package com.example.doanltttbdd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.doanltttbdd.Activity.Login;
import com.example.doanltttbdd.Activity.MainActivity;
import com.example.doanltttbdd.R;
import com.example.doanltttbdd.model.User;
import com.example.doanltttbdd.utils.Utils;

public class UserIsLoginFragment extends Fragment {

    TextView name_txt, phone_txt, email_txt, logout_btn, slot_cook_book;
    ImageView avatar_image, edit_btn;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_is_login, container, false);
        name_txt = view.findViewById(R.id.name_txt);
        phone_txt = view.findViewById(R.id.phone_txt);
        email_txt = view.findViewById(R.id.email_txt);
        logout_btn = view.findViewById(R.id.logout_btn);
        slot_cook_book = view.findViewById(R.id.slot_cook_book);
        avatar_image = view.findViewById(R.id.avatar_image);
        edit_btn = view.findViewById(R.id.edit_btn);
        String session = Utils.getCurrentSessionUserId(requireContext());
        slot_cook_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.replaceFragment(new ListCookBookOfUserFragment(), getParentFragmentManager());
            }
        });
        Utils.getUserById(session, new Utils.UserCallback() {
            @Override
            public void onUserRetrieved(User user, String errorMessage) {
                if (user != null) {
                    name_txt.setText(user.getFullName());
                    String phone = user.getPhone();
                    phone = phone.substring(1); // Bỏ đi số 0 ở đầu
                    String formattedPhone = "84+ " + phone;
                    phone_txt.setText(formattedPhone);
                    email_txt.setText(user.getEmail());
                    edit_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openGallery();
                        }
                    });
                    logout_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.deleteSession(requireContext());
                            // Chuyển về HomeFragment
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.home_fragment, new HomeFragment()).commit();
                            // Cập nhật trạng thái checked của BottomNavigationView
                            ((MainActivity) requireActivity()).updateBottomNavigationCheckedState(R.id.menu_home);
                        }
                    });
                    Utils.getCountOfCookbooksByUserId(session, new Utils.OnCountCookbooksListener() {
                        @Override
                        public void onCountCookbooks(int count) {
                            Log.d("Dem", "" + count);
                            slot_cook_book.setText("Bạn có " + count);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Xử lý khi có lỗi xảy ra trong quá trình lấy số lượng cookbooks
                            Log.e("GetCookbooksError", errorMessage);
                        }
                    });
                } else {
                    startActivity(new Intent(requireContext(), Login.class));
                }
            }
        });

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

}