package tz.co.hapa.lecturerassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

public class EditSubject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Edit Module");
    }
}
