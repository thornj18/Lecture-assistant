package tz.co.hapa.lecturerassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


public class Login extends AppCompatActivity {
    private ProgressDialog progressDialog;


    @BindView(R.id.edt_regno)
    EditText edtRegNo;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    @OnClick(R.id.btn_login) void loginNow(){
        //TODO: To start authentication
        progressDialog = new ProgressDialog(Login.this,
                android.R.style.Theme_Holo_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in Lecturer");
        progressDialog.show();

        login(edtRegNo.getText().toString(),edtPassword.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    private void login(String regno, String password) {

        Interface apiService = Client.getClient().create(Interface.class);
        Call<ResponseBody> teacherCall = apiService.loginTeacher(regno, password);
        teacherCall.enqueue(new Callback<ResponseBody>() {
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
                Toast.makeText(Login.this, "Failed to Login, check server", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void parseData(String response) throws JSONException {
        progressDialog.cancel();
        JSONObject js = new JSONObject(response);
        Toast.makeText(this, js.getString("teacher"), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Login.this, Main.class));

    }
}
