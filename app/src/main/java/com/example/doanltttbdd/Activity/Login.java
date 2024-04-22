package com.example.doanltttbdd.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.doanltttbdd.R;
import com.example.doanltttbdd.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private boolean passwordShowing = false;
    EditText usernameET;
    EditText passwordET;
    ImageView passwordIcon;
    TextView signUpBtn;
    AppCompatButton signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        passwordIcon = findViewById(R.id.passwordIcon);
        signUpBtn = findViewById(R.id.signUpBtn);
        signInBtn = findViewById(R.id.signInBtn);

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking if password is showing or not
                if (passwordShowing) {
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show);
                } else {
                    passwordShowing = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide);
                }

                // Set the cursor at the end
                passwordET.setSelection(passwordET.length());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUserName() | !validatePassword()) {
                    return;
                } else {
                    checkUser();
                }
            }
        });


    }

    public boolean validateUserName() {
        String val = usernameET.getText().toString();
        if (val.isEmpty()) {
            usernameET.setError("User Name cannot be empty");
            return false;
        } else {
            usernameET.setError(null);
            return true;
        }

    }

    public boolean validatePassword() {
        String val = passwordET.getText().toString();
        if (val.isEmpty()) {
            passwordET.setError("Password cannot be empty");
            return false;
        } else {
            passwordET.setError(null);
            return true;
        }

    }

    public void checkUser() {
        String userUserName = usernameET.getText().toString();
        String userPassword = passwordET.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userUserName);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFormDB = userSnapshot.child("password").getValue(String.class);
                        boolean decodePassword = Utils.verifyPassword(userPassword, passwordFormDB);
                        if (decodePassword) {
                            // Xác thực thành công, lưu session và chuyển hướng
                            String userId = userSnapshot.getKey(); // Lấy ID của người dùng từ snapshot
                            Utils.saveSession(Login.this, userId);
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            return;
                        } else {
                            Log.d("test2", "User Password: " + userPassword + ", Password from DB: " + passwordFormDB);
                            passwordET.setError("Invalid Credentials");
                            passwordET.requestFocus();
                            return;
                        }
                    }
                } else {
                    usernameET.setError("UserName doesn't exist");
                    usernameET.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }


}
