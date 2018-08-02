package tz.co.hapa.lecturerassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tz.co.hapa.lecturerassistant.api.Client;
import tz.co.hapa.lecturerassistant.api.Interface;

public class RegisterStudent extends AppCompatActivity {
    private ProgressDialog progressDialog;
    @BindView(R.id.edt_full_name)
    EditText edtStudentName;

    @BindView(R.id.edt_registration_number)
    EditText edtRegistrationNumber;

    @BindView(R.id.edt_department)
    EditText edtDepartment;

    @OnClick(R.id.btn_submit)
    void onRegister() {
        progressDialog = new ProgressDialog(RegisterStudent.this,
                android.R.style.Theme_Holo_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        registerStudents(edtStudentName.getText().toString(), edtRegistrationNumber.getText().toString(),edtDepartment.getText().toString());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Register Student");



    }

    private void registerStudents(String name, String regNo, String department) {

        Interface apiService = Client.getClient().create(Interface.class);
        Call<ResponseBody> pharmacyCall = apiService.registerStudents(name, regNo, department);
        pharmacyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    parseData(response.body().string());
                    progressDialog.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO: Handle event of failure to login
                progressDialog.cancel();
                Toast.makeText(RegisterStudent.this, "Failed to create Student, check server", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void parseData(String response) throws JSONException {
        progressDialog.cancel();
        JSONObject js = new JSONObject(response);
        Toast.makeText(this, js.getString("success"), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterStudent.this, Main.class));

    }


}
