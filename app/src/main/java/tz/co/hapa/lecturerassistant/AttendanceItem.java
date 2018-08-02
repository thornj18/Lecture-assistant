package tz.co.hapa.lecturerassistant;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TKPC on 3/10/2017.
 */

public class AttendanceItem {

    @SerializedName("reg_no")
    public String regNo;

    @SerializedName("availability")
    public String availability;

    public AttendanceItem(String regNo, String availability) {
        this.regNo = regNo;
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
}
