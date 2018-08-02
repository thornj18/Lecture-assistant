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

public class CreateSubject extends AppCompatActivity {

    private ProgressDialog progressDialog;
    @BindView(R.id.edt_module_code)
    EditText edtmoduleCode;

    @BindView(R.id.edt_module_name)
    EditText edtmoduleName;

    @OnClick(R.id.btn_create_module) void
    onCreateModule() {
        progressDialog = new ProgressDialog(CreateSubject.this,
                android.R.style.Theme_Holo_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating ...");
        progressDialog.show();
        createModule(edtmoduleName.getText().toString(), edtmoduleCode.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Create Module");

    }

    private void createModule(String moduleName, String moduleCode) {

        Interface apiService = Client.getClient().create(Interface.class);
        Call<ResponseBody> pharmacyCall = apiService.createModule(moduleName, moduleCode);
        pharmacyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.cancel();
                    if (response.isSuccessful()){
                        parseData(response.body().string());
                    }

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
                Toast.makeText(CreateSubject.this, "Failed to create Subject, check server", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void parseData(String response) throws JSONException {
        progressDialog.cancel();
        JSONObject js = new JSONObject(response);
        Toast.makeText(this, js.getString("success"), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CreateSubject.this, Main.class));

    }
}
