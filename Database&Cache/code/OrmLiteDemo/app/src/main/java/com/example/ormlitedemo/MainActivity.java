package com.example.ormlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ormlitedemo.bean.UserItemDB;
import com.example.ormlitedemo.db.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.this.getClass().getSimpleName();
    EditText enterIndex, enterName, enterAge;
    Button btnSubmit, btnViewUsers;
    List<UserItemDB> userItemDBList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterIndex = findViewById(R.id.txt_index);
        enterName = findViewById(R.id.txt_name);
        enterAge = findViewById(R.id.txt_age);

        btnSubmit = findViewById(R.id.btn_submit);
        btnViewUsers = findViewById(R.id.btn_view_list);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        btnViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);
            }
        });


    }

    private void addUser() {
        String name = enterName.getText().toString().trim();
        String index = enterIndex.getText().toString().trim();
        String age = enterAge.getText().toString().trim();
        if((name != null && !name.isEmpty())&& (index != null && !index.isEmpty()) && (age != null && !age.isEmpty())){
            UserItemDB userItemDB = new UserItemDB();
            userItemDB.setIndex(index);
            userItemDB.setName(name);
            userItemDB.setAge(age);
            addUserToDB(userItemDB);
            userItemDBList = getAllUsers();
            for(UserItemDB itemDB: userItemDBList){
                Log.i(TAG,"name: "+itemDB.getName());
            }
        }
    }

    public int addUserToDB(UserItemDB userItemDB){
        int isSuccess;
        isSuccess = DatabaseManager.getInstance(getApplicationContext()).insertUserItem(userItemDB,false);
        if(isSuccess == 0){
            Toast.makeText(getApplicationContext(),"Save User",Toast.LENGTH_SHORT).show();
        }else if(isSuccess == 1){
            Toast.makeText(getApplicationContext(),"User with this id exist",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"User adding failed",Toast.LENGTH_SHORT).show();
        }
        return isSuccess;
    }

    public void presentToast(){
        Toast.makeText(getApplicationContext(),"Save User", Toast.LENGTH_SHORT).show();
    }

    public List<UserItemDB> getAllUsers(){
        return  DatabaseManager.getInstance(getApplicationContext()).getAllUsers();
    }

}