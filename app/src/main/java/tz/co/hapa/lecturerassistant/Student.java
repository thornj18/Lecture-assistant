package tz.co.hapa.lecturerassistant;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TKPC on 3/10/2017.
 */

public class Student {

    @SerializedName("name")
    public String name;

    @SerializedName("reg_no")
    public String regNo;

    @SerializedName("module_name")
    public String moduleName;

    @SerializedName("attendancePercentage")
    public String attendancePercentage;

    public Student(String attendancePercentage, String name, String regNo, String moduleName) {
        this.attendancePercentage = attendancePercentage;
        this.name = name;
        this.regNo = regNo;
        this.moduleName = moduleName;
    }

    public Student(String name, String regNo, String moduleName) {
        this.name = name;
        this.regNo = regNo;
        this.moduleName = moduleName;
    }

    public Student(String name, String regNo) {
        this.name = name;
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(String attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
}
