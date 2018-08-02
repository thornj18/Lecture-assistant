package tz.co.hapa.lecturerassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageSubjects extends AppCompatActivity {
    @OnClick(R.id.card_create_subject)
    void onCreateModule() {
        startActivity(new Intent(ManageSubjects.this, CreateSubject.class));
    }

    @OnClick(R.id.card_view_modules)
    void onViewStudentModules() {
        startActivity(new Intent(ManageSubjects.this, ViewSubjects.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subject);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Manage Modules");

    }
}
