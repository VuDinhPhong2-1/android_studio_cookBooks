package com.example.doanltttbdd.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.doanltttbdd.R;
import com.example.doanltttbdd.model.User;
import com.example.doanltttbdd.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Register extends AppCompatActivity {
    private boolean passwordShowing = false;
    private boolean conPasswordShowing = false;
    private String otp;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo database
//        CreateDatabase createDatabase = new CreateDatabase(this);
//        createDatabase.getWritableDatabase();

        final EditText fullNameET = findViewById(R.id.fullNameET);
        final EditText email = findViewById(R.id.emailET);
        final EditText mobile = findViewById(R.id.mobileET);
        final EditText password = findViewById(R.id.passwordET);
        final EditText conPassword = findViewById(R.id.conPasswordET);
        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
        final ImageView conPasswordIcon = findViewById(R.id.conPasswordIcon);
        final AppCompatButton signUpBtn = findViewById(R.id.signUpBtn);
        final TextView signInBtn = findViewById(R.id.signInBtn);


        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking if password is showing or not
                if (passwordShowing) {
                    passwordShowing = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show);
                } else {
                    passwordShowing = true;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide);
                }

                // Set the cursor at the end
                password.setSelection(password.length());
            }
        });
        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking if password is showing or not
                if (conPasswordShowing) {
                    conPasswordShowing = false;
                    conPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.show);
                } else {
                    conPasswordShowing = true;
                    conPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.hide);
                }

                // Set the cursor at the end
                conPassword.setSelection(conPassword.length());
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getFullNameTxt = fullNameET.getText().toString();
                final String getMobileTxt = mobile.getText().toString();
                final String getEmailTxt = email.getText().toString();
                final String getPasswordTxt = password.getText().toString();
                final String getConPasswordTxt = conPassword.getText().toString();

                if (getFullNameTxt.isEmpty() || getMobileTxt.isEmpty() || getEmailTxt.isEmpty() || getPasswordTxt.isEmpty() || getConPasswordTxt.isEmpty()) {
                    Toast.makeText(Register.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {

                    if (!getPasswordTxt.equals(getConPasswordTxt)) {
                        conPassword.setError("Xác nhận mật khẩu không khớp");
                    } else {
                        Utils.checkDuplicateEmail(getEmailTxt, new Utils.OnCheckDuplicateEmailListener() {
                            @Override
                            public void onDuplicateEmailFound() {
                                // Email đã tồn tại trong cơ sở dữ liệu
                                email.setError("Email đã được dùng!");
                            }

                            @Override
                            public void onUniqueEmail() {
                                // Email không bị trùng lặp, có thể thêm người dùng mới vào cơ sở dữ liệu
                                Utils.checkDuplicatePhone(getMobileTxt, new Utils.OnCheckDuplicatePhoneListener() {
                                    @Override
                                    public void onDuplicatePhoneFound() {
                                        mobile.setError("Số điện thoại đã được dùng!");
                                    }

                                    @Override
                                    public void onUniquePhone() {
                                        database = FirebaseDatabase.getInstance();
                                        reference = database.getReference("users");
                                        String hashPassword = Utils.hashPassword(getPasswordTxt);
                                        User userClass = new User(getFullNameTxt, getEmailTxt, getMobileTxt, hashPassword);
                                        String userId = UUID.randomUUID().toString();
                                        reference.child(userId).setValue(userClass);
                                        Toast.makeText(Register.this, "You have signup successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onError(String errorMessage) {
                                        // Xử lý khi có lỗi xảy ra trong quá trình kiểm tra
                                        Toast.makeText(Register.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onError(String errorMessage) {
                                // Xử lý khi có lỗi xảy ra trong quá trình kiểm tra
                                Toast.makeText(Register.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        });


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}