package com.example.ormlitedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ormlitedemo.bean.UserItemDB;
import com.example.ormlitedemo.db.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    List<UserItemDB> userItemDBList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Context context;
    private UserAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        userItemDBList = getAllUsers();
        mAdapter = new UserAdapter(userItemDBList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

    }



    public void presentAlert(String name, String age, final String index){
        final EditText nameEditText = new EditText(this);
        final EditText ageEditText = new EditText(this);

        nameEditText.setText(name);
        ageEditText.setText(age);
//        nameEditText.setWidth(100);
//        ageEditText.setWidth(100);

        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(nameEditText);
        linearLayout.addView(ageEditText);


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit User")
                .setView(linearLayout)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserItemDB userItemDB = new UserItemDB();
                        userItemDB.setName(nameEditText.getText().toString());
                        userItemDB.setAge(ageEditText.getText().toString());
                        userItemDB.setIndex(index);
                        DatabaseManager.getInstance(getApplicationContext()).insertUserItem(userItemDB,true);
                        //mAdapter.notifyDataSetChanged();
                        mAdapter.updateUserList(getAllUsers());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }



    private void deleteUser(int index) {
    }


    public List<UserItemDB> getAllUsers(){
        return  DatabaseManager.getInstance(getApplicationContext()).getAllUsers();
    }

}