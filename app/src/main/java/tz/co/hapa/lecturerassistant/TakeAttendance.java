package tz.co.hapa.lecturerassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tz.co.hapa.lecturerassistant.api.Client;
import tz.co.hapa.lecturerassistant.api.Interface;

public class TakeAttendance extends AppCompatActivity {
    @BindView(R.id.list_take_attendance)
    RecyclerView takeAttendanceList;


    @BindView(R.id.edt_period_amount)
    EditText edtPeriods;
    private ProgressDialog progressDialog;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<AttendanceItem> attendanceItems;
    private JSONArray attendantItems;

    @OnClick(R.id.btn_submit_attendance)
    void submitAttendance() {
        progressDialog = new ProgressDialog(TakeAttendance.this,
                android.R.style.Theme_Holo_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving Attendance");
        progressDialog.show();
        submitAttendence(Integer.valueOf(edtPeriods.getText().toString()), attendantItems);
        startActivity(new Intent(TakeAttendance.this, Main.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Take Attendance");


        mLayoutManager = new LinearLayoutManager(TakeAttendance.this);
        takeAttendanceList.setLayoutManager(mLayoutManager);


        progressDialog = new ProgressDialog(TakeAttendance.this,
                android.R.style.Theme_Holo_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving Students");
        progressDialog.show();

        getStudents(getIntent().getStringExtra("subject"));
        attendanceItems = new ArrayList<>();

    }

    private void getStudents(String subject) {

        Interface apiService = Client.getClient().create(Interface.class);
        Call<ResponseBody> pharmacyCall = apiService.getSubjStudents(subject);
        pharmacyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.cancel();
                    if (response.isSuccessful())
                        parseData(response.body().string());
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
                Toast.makeText(TakeAttendance.this, "Students from this Module aren't available at the moment", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void parseData(String response) throws JSONException {
        ArrayList<Student> dataArray = new ArrayList<>();
//        Toast.makeText(TakeAttendance.this, response, Toast.LENGTH_LONG).show();

        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject student = jsonArray.getJSONObject(i);
            Student current = new Student(student.getString("name"), student.getString("reg_no"), student.getJSONObject("subject").getString("module_name"));
            dataArray.add(current);
        }

        mAdapter = new AttendanceAdapter(TakeAttendance.this, dataArray);
        takeAttendanceList.setAdapter(mAdapter);

    }


    public void submitAttendence(Integer periods, JSONArray dataArray) {
        Interface apiService = Client.getClient().create(Interface.class);
        Call<ResponseBody> pharmacyCall = apiService.saveAttendance(periods, dataArray);
        pharmacyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("case", response.body().string());
                    Toast.makeText(TakeAttendance.this, "Attendance Saved", Toast.LENGTH_LONG).show();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO: Handle event of failure to login
                progressDialog.cancel();
                Toast.makeText(TakeAttendance.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

        private LayoutInflater inflater;

        List<Student> data = Collections.emptyList();

        public AttendanceAdapter(Context context, List<Student> data) {
            this.data = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public AttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.custom_attendance_item, parent, false);
            AttendanceAdapter.ViewHolder holder = new AttendanceAdapter.ViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final AttendanceAdapter.ViewHolder holder, final int position) {
            final Student current = data.get(position);
            holder.txtStudentName.setText(current.name);
            holder.txtStudentRegno.setText(current.regNo);
//            holder.txtSubject.setText(current.moduleName);

            holder.btnPresent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AttendanceItem attendanceItem = new AttendanceItem(current.regNo, "present");
                    attendanceItems.add(attendanceItem);
                    Gson gson = new Gson();

                    String listString = gson.toJson(
                            attendanceItems,
                            new TypeToken<ArrayList<AttendanceItem>>() {
                            }.getType());

                    try {
                        attendantItems = new JSONArray(listString);
                        Toast.makeText(TakeAttendance.this, current.name+" is Present", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

            holder.btnAbsent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AttendanceItem attendanceItem = new AttendanceItem(current.regNo, "absent");
                    attendanceItems.add(attendanceItem);
                    Gson gson = new Gson();

                    String listString = gson.toJson(
                            attendanceItems,
                            new TypeToken<ArrayList<AttendanceItem>>() {
                            }.getType());

                    try {
                        attendantItems = new JSONArray(listString);
                        Toast.makeText(TakeAttendance.this, current.name+" is Absent", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView txtStudentName;
            public TextView txtStudentRegno;
            public RadioButton btnPresent;
            public RadioButton btnAbsent;
//            public TextView txtSubject;

            public ViewHolder(View itemView) {
                super(itemView);
                txtStudentName = (TextView) itemView.findViewById(R.id.txt_student_name);
                txtStudentRegno = (TextView) itemView.findViewById(R.id.txt_student_id);
                btnAbsent = (RadioButton) itemView.findViewById(R.id.rdbtn_absent_attendance);
                btnPresent = (RadioButton) itemView.findViewById(R.id.rdbtn_present_attendance);
//                txtSubject = (TextView) itemView.findViewById(R.id.txt_subject_name);


            }

        }


    }
}