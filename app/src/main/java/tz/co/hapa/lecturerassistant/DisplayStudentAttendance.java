package tz.co.hapa.lecturerassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tz.co.hapa.lecturerassistant.api.Client;
import tz.co.hapa.lecturerassistant.api.Interface;

public class DisplayStudentAttendance extends AppCompatActivity {
    @BindView(R.id.list_view_attendance)
    RecyclerView attendanceList;


    ProgressDialog progressDialog;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispaly_students_attendance);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Student Attendance");


        mLayoutManager = new LinearLayoutManager(DisplayStudentAttendance.this);
        attendanceList.setLayoutManager(mLayoutManager);

        progressDialog = new ProgressDialog(DisplayStudentAttendance.this,
                android.R.style.Theme_Holo_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving Students");
        progressDialog.show();
        getStudents();
    }

    private void getStudents() {

        Interface apiService = Client.getClient().create(Interface.class);
        Call<ResponseBody> pharmacyCall = apiService.getStudents();
        pharmacyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
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
                Toast.makeText(DisplayStudentAttendance.this, "Students from this Subjects aren't available at the moment", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void parseData(String response) throws JSONException {
        ArrayList<Student> dataArray = new ArrayList<>();
        Log.d("bit", response);
        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject student = jsonArray.getJSONObject(i);
            Student current = new Student(student.getString("attendancePercentage"), student.getString("name"), student.getString("reg_no"), student.getJSONObject("subject").getString("module_name"));
            dataArray.add(current);
        }

        Log.d("array", dataArray.toString());
        progressDialog.cancel();
        mAdapter = new AttendanceAdapter(DisplayStudentAttendance.this, dataArray);
        attendanceList.setAdapter(mAdapter);

    }


    public class AttendanceAdapter extends RecyclerView.Adapter<DisplayStudentAttendance.AttendanceAdapter.ViewHolder> {

        private LayoutInflater inflater;

        List<Student> data = Collections.emptyList();

        public AttendanceAdapter(Context context, List<Student> data) {
            this.data = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public DisplayStudentAttendance.AttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.custom_view_attendance_item, parent, false);
            AttendanceAdapter.ViewHolder holder = new DisplayStudentAttendance.AttendanceAdapter.ViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final DisplayStudentAttendance.AttendanceAdapter.ViewHolder holder, final int position) {
            final Student current = data.get(position);
            holder.txtStudentName.setText(current.name);
            holder.txtStudentRegno.setText(current.regNo);
            holder.txtAttendancePercentage.setText("Att percentage: " + current.attendancePercentage + "%");
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog = new ProgressDialog(DisplayStudentAttendance.this,
                            android.R.style.Theme_Holo_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Removing Student");
                    progressDialog.show();

                    Interface apiService = Client.getClient().create(Interface.class);
                    Call<ResponseBody> pharmacyCall = apiService.deleteStudent(current.regNo);
                    pharmacyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                if (response.isSuccessful()) {
                                    Toast.makeText(DisplayStudentAttendance.this, response.body().string(), Toast.LENGTH_LONG).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //TODO: Handle event of failure to login
                            progressDialog.cancel();
                            Toast.makeText(DisplayStudentAttendance.this, "Error removing student", Toast.LENGTH_LONG).show();
                        }
                    });
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
            public TextView txtAttendancePercentage;
            public Button btnDelete;


            public ViewHolder(View itemView) {
                super(itemView);
                txtStudentName = (TextView) itemView.findViewById(R.id.txt_student_name);
                txtStudentRegno = (TextView) itemView.findViewById(R.id.txt_student_id);
                txtAttendancePercentage = (TextView) itemView.findViewById(R.id.txt_attendance_percentage);
                btnDelete = (Button) itemView.findViewById(R.id.btn_delete_student);

            }

        }


    }
}
