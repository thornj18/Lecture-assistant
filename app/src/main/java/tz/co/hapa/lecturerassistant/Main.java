package tz.co.hapa.lecturerassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity {

    @OnClick(R.id.card_take_attendance) void onAttendance(){
        startActivity(new Intent(Main.this, Subjects.class));
    }

    @OnClick(R.id.card_manage_student) void onManageStudents(){
        startActivity(new Intent(Main.this, ManageStudents.class));
    }

    @OnClick(R.id.card_manage_subjects) void onManageSubjects(){
        startActivity(new Intent(Main.this, ManageSubjects.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


}
