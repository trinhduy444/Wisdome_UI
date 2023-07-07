//package com.example.myapplication;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//public class UserActivity extends AppCompatActivity {
//
//    EditText nameEditText, genderEditText, dateEditText, phoneEditText, emailEditText, addressEditText;
//    SharedPreferences preferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user);
//
//        // Khởi tạo các trường EditText
//        nameEditText = findViewById(R.id.nameEditText);
//        genderEditText = findViewById(R.id.genderEditText);
//        dateEditText = findViewById(R.id.dateEditText);
//        phoneEditText = findViewById(R.id.phoneEditText);
//        emailEditText = findViewById(R.id.emailEditText);
//        addressEditText = findViewById(R.id.address_editText);
//
//        // Lấy các giá trị đã lưu từ SharedPreferences
//        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        String name = preferences.getString("Name", "");
//        String gender = preferences.getString("Gender", "");
//        String date = preferences.getString("Date", "");
//        String phone = preferences.getString("Phone", "");
//        String email = preferences.getString("Email", "");
//        String address = preferences.getString("Address", "");
//
//        // Hiển thị các giá trị đã lưu trên các trường EditText
//        nameEditText.setText(name);
//        genderEditText.setText(gender);
//        dateEditText.setText(date);
//        phoneEditText.setText(phone);
//        emailEditText.setText(email);
//        addressEditText.setText(address);
//
//        // Xử lý sự kiện khi người dùng nhấn vào button
//        Button saveButton = findViewById(R.id.update_Profile);
//        saveButton.setOnClickListener(v -> {
//            // Lưu các giá trị từ các trường EditText vào SharedPreferences
//            String name1 = nameEditText.getText().toString();
//            String gender1 = genderEditText.getText().toString();
//            String date1 = dateEditText.getText().toString();
//            String phone1 = phoneEditText.getText().toString();
//            String email1 = emailEditText.getText().toString();
//            String address1 = addressEditText.getText().toString();
//
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("Name", name1);
//            editor.putString("Gender", gender1);
//            editor.putString("Date", date1);
//            editor.putString("Phone", phone1);
//            editor.putString("Email", email1);
//            editor.putString("Address", address1);
//            editor.apply();
//
//            // Hiển thị thông báo đã lưu thành công
//            Toast.makeText(UserActivity.this, "Đã lưu thành công!", Toast.LENGTH_SHORT).show();
//        });
//    }
//}