package tz.co.hapa.lecturerassistant.api;

import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by TKPC on 3/10/2017.
 */

public interface Interface {
    @GET("subject/list")
    Call<ResponseBody> getSubjects();

    @GET("student/getSubjStudents")
    Call<ResponseBody> getSubjStudents(@Query("module_code") String moduleCode);

    @GET("student/listStudents")
    Call<ResponseBody> getStudents();


    @FormUrlEncoded
    @POST("attendance")
    Call<ResponseBody> saveAttendance(@Field("periods") Integer periodCount,
                                      @Field("attendance") JSONArray dataArray);

    @FormUrlEncoded
    @POST("student/register")
    Call<ResponseBody> registerStudents(@Field("name") String name, @Field("reg_no") String regNo,
                                        @Field("subject") String dept);

    @FormUrlEncoded
    @POST("subject/create")
    Call<ResponseBody> createModule(@Field("module_name") String name, @Field("module_code") String moduleCode);

    @FormUrlEncoded
    @POST("teacher/login")
    Call<ResponseBody> loginTeacher(@Field("regno") String regno, @Field("password") String password);

    @FormUrlEncoded
    @POST("student/delete")
    Call<ResponseBody> deleteStudent(@Field("reg_no") String regno);
}
