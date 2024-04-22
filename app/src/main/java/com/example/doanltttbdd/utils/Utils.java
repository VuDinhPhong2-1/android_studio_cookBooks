package com.example.doanltttbdd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.doanltttbdd.R;
import com.example.doanltttbdd.model.Cookbook;
import com.example.doanltttbdd.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Utils {

    public static void checkDuplicateEmail(String email, final OnCheckDuplicateEmailListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Email đã tồn tại trong cơ sở dữ liệu
                    listener.onDuplicateEmailFound();
                } else {
                    // Email không bị trùng lặp
                    listener.onUniqueEmail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public static void checkDuplicatePhone(String phone, final OnCheckDuplicatePhoneListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild("phone").equalTo(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Số điện thoại đã tồn tại trong cơ sở dữ liệu
                    listener.onDuplicatePhoneFound();
                } else {
                    // Số điện thoại không bị trùng lặp
                    listener.onUniquePhone();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public static boolean checkSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        long expirationTime = sharedPreferences.getLong("expirationTime", 0);

        // Kiểm tra nếu userId không rỗng và thời gian hết hạn vẫn còn
        return !userId.isEmpty() && System.currentTimeMillis() < expirationTime;
    }

    public static void deleteSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Xóa tất cả dữ liệu trong SharedPreferences
        editor.apply(); // Áp dụng thay đổi
    }

    public static void saveSession(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        // Thời gian hết hạn là 24 giờ
        long expirationTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000; // 24 giờ sau
        editor.putLong("expirationTime", expirationTime);
        boolean isSuccess = editor.commit(); // Lưu session và trả về true nếu thành công
        if (isSuccess) {
            Log.d("Session", "Session saved successfully with userId: " + userId);
        } else {
            Log.e("Session", "Failed to save session");
        }
    }

    public static String getCurrentSessionUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

    public static void getUserById(String userId, final UserCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        callback.onUserRetrieved(user, null); // Trả về user và không có lỗi
                    } else {
                        callback.onUserRetrieved(null, "Failed to retrieve user data");
                    }
                } else {
                    callback.onUserRetrieved(null, "User not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onUserRetrieved(null, databaseError.getMessage());
            }
        });
    }

    public static void replaceFragment(Fragment fragment, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.farther_user_fragment, fragment)
                .commit();
    }

    public static void getCountOfCookbooksByUserId(String userId, final OnCountCookbooksListener listener) {
        DatabaseReference cookbookRef = FirebaseDatabase.getInstance().getReference().child("cookBooks");
        Log.d("checkCookBook", "" + cookbookRef);
        Query query = FirebaseDatabase.getInstance().getReference().child("cookBooks").orderByChild("creatorId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                listener.onCountCookbooks(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }


    public static void getUserName(String userId, final OnUserNameFetchedListener listener) {
        String safeUserId = replaceSpecialCharacters(userId);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(safeUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("fullName").getValue(String.class);
                    // Gửi tên người dùng đã lấy được đến listener
                    listener.onUserNameFetched(userName);
                } else {
                    // Trường hợp không tìm thấy người dùng
                    listener.onUserNameFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn
                listener.onCancelled(databaseError.getMessage());
            }
        });
    }
    public static void getCookbookByKey(String cookBookKey, final CookbookCallback callback) {
        DatabaseReference cookbookRef = FirebaseDatabase.getInstance().getReference().child("cookBooks").child(cookBookKey);
        cookbookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Cookbook cookbook = dataSnapshot.getValue(Cookbook.class);
                    if (cookbook != null) {
                        callback.onCookbookRetrieved(cookbook, null); // Trả về cookbook và không có lỗi
                    } else {
                        callback.onCookbookRetrieved(null, "Failed to retrieve cookbook data");
                    }
                } else {
                    callback.onCookbookRetrieved(null, "Cookbook not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onCookbookRetrieved(null, databaseError.getMessage());
            }
        });
    }
    public static void getAllCookbookNames(final CookbookNameCallback callback) {
        DatabaseReference cookbookRef = FirebaseDatabase.getInstance().getReference("cookbooks");
        cookbookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> cookbookNames = new ArrayList<>();
                // Duyệt qua tất cả các cookbook
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy tên của cookbook từ snapshot
                    String cookbookName = snapshot.child("recipeName").getValue(String.class);
                    // Thêm tên của cookbook vào danh sách
                    if (cookbookName != null) {
                        cookbookNames.add(cookbookName);
                    }
                }
                // Gửi danh sách tên cookbook đến callback
                callback.onCookbookNamesReceived(cookbookNames);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                callback.onFailed(databaseError.toException().getMessage());
            }
        });
    }

    // Interface để gửi kết quả trả về
    public interface CookbookNameCallback {
        void onCookbookNamesReceived(List<String> cookbookNames);
        void onFailed(String errorMessage);
    }
    public interface CookbookCallback {
        void onCookbookRetrieved(Cookbook cookbook, String errorMessage);
    }
    public interface OnUserNameFetchedListener {
        void onUserNameFetched(String userName);

        void onCancelled(String errorMessage);
    }

    public static String replaceSpecialCharacters(String userId) {
        return userId.replaceAll("[.#$\\[\\]]", "_");
    }

    public static String hashPassword(String password) {
        // Sử dụng hàm hashpw để mã hóa mật khẩu
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        return hashedPassword;
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        // Sử dụng hàm verifyer để kiểm tra tính đúng đắn của mật khẩu
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
    public static void getCookbookByKey(String key, final OnGetCookbookCompleteListener listener) {
        DatabaseReference cookbookRef = FirebaseDatabase.getInstance().getReference().child("cookBooks").child(key);
        cookbookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Cookbook cookbook = dataSnapshot.getValue(Cookbook.class);
                    listener.onGetCookbookSuccess(cookbook);
                } else {
                    listener.onGetCookbookFailure("Cookbook not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onGetCookbookFailure(databaseError.getMessage());
            }
        });
    }

    // Interface để xử lý sự kiện khi lấy cookbook thành công hoặc thất bại
    public interface OnGetCookbookCompleteListener {
        void onGetCookbookSuccess(Cookbook cookbook);
        void onGetCookbookFailure(String errorMessage);
    }
    public interface OnCountCookbooksListener {
        void onCountCookbooks(int count);

        void onError(String errorMessage);
    }

    public interface UserCallback {
        void onUserRetrieved(User user, String errorMessage);
    }


    public interface OnCheckDuplicateEmailListener {
        void onDuplicateEmailFound();

        void onUniqueEmail();

        void onError(String errorMessage);
    }

    public interface OnCheckDuplicatePhoneListener {
        void onDuplicatePhoneFound();

        void onUniquePhone();

        void onError(String errorMessage);
    }
}

