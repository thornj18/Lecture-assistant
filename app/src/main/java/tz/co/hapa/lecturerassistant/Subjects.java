package tz.co.hapa.lecturerassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tz.co.hapa.lecturerassistant.api.Client;
import tz.co.hapa.lecturerassistant.api.Interface;

public class Subjects extends AppCompatActivity {
    @BindView(R.id.list_departments)
    ListView departmentList;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Subjects");


        progressDialog = new ProgressDialog(Subjects.this,
                android.R.style.Theme_Holo_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving Modules");
        progressDialog.show();

        getModules();


    }

    private void getModules() {

        Interface apiService = Client.getClient().create(Interface.class);
        Call<ResponseBody> pharmacyCall = apiService.getSubjects();
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
                Toast.makeText(Subjects.this, "Modules aren't available at the moment", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void parseData(String response) throws JSONException {
        final ArrayList<String> moduleName = new ArrayList<>();
        final ArrayList<String> moduleCode = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            moduleName.add(obj.getString("module_name"));
            moduleCode.add(obj.getString("module_code"));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Subjects.this, android.R.layout.simple_list_item_1, moduleName);
        departmentList.setAdapter(arrayAdapter);

        departmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String selectedFromList = (String) departmentList.getItemAtPosition(i);
                Toast.makeText(Subjects.this, moduleCode.get(i),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Subjects.this,TakeAttendance.class);
                intent.putExtra("subject", moduleCode.get(i));
                startActivity(intent);
            }
        });

    }
}
