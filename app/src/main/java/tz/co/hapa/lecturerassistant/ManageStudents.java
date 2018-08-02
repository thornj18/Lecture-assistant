package tz.co.hapa.lecturerassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageStudents extends AppCompatActivity {
    @OnClick(R.id.card_register_student) void register(){
        startActivity(new Intent(ManageStudents.this, RegisterStudent.class));
    }

    @OnClick(R.id.card_view_attendance) void viewAttendance(){
        startActivity(new Intent(ManageStudents.this, DisplayStudentAttendance.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Manage Students");



    }
}
