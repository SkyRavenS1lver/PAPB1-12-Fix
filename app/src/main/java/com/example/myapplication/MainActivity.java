package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText mNameEditText, mAddressEditTest, mUpdateNameEditText, mUpdatedAddressEditText;
    DatabaseReference databaseReference;
    Student studentTemp;
    String keyStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNameEditText = findViewById(R.id.etName);
        mAddressEditTest = findViewById(R.id.etAddress);
        mUpdateNameEditText = findViewById(R.id.etUpdateName);
        mUpdatedAddressEditText = findViewById(R.id.etUpdateAddress);
        databaseReference = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());
        studentTemp = new Student();
        findViewById(R.id.buttonRead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot currentData : snapshot.getChildren()){
                                keyStudent = currentData.getKey();
                                studentTemp.setAddress(currentData.child("address").getValue().toString());
                                studentTemp.setNama(currentData.child("nama").getValue().toString());
                            }
                        }

                        mUpdateNameEditText.setText(studentTemp.getNama());
                        mUpdatedAddressEditText.setText(studentTemp.getAddress());
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student updated = new Student();
                updated.setNama(mUpdateNameEditText.getText().toString());
                updated.setAddress(mUpdatedAddressEditText.getText().toString());
                //add data
                databaseReference.child(keyStudent).setValue(updated);
            }
        });
        findViewById(R.id.buttonInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();

            }
        });
        findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(keyStudent).removeValue();
            }
        });

    }
    public void insertData(){
        Student newStudent = new Student();
        String name = mNameEditText.getText().toString();
        String address = mAddressEditTest.getText().toString();
        if (name != "" && address != ""){
            newStudent.setNama(name);
            newStudent.setAddress(address);

            databaseReference.push().setValue(newStudent);
//                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        }
    }
}