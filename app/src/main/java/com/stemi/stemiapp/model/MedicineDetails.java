package com.stemi.stemiapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Pooja on 06-09-2017.
 */

public class MedicineDetails implements Parcelable {

    private String date;
    private String personName;
    private   String medicineName;
    private  String medicineType;
    private  Integer medicineColor;
    private String morning;
    private String Afternoon;
    private String night;
    private ArrayList<MedicinesTakenOrNot> medicineMorning;
    private String medicineMorningTime;
    private  ArrayList<MedicinesTakenOrNot> medicineAfternoon;
    private   String medicineNoonTime;
    private  ArrayList<MedicinesTakenOrNot> medicineNight;
    private  String medicineNightTime;
    private  String medicineDays;
    private  Boolean medicineRemainder;
    private Boolean moringChecked;
    private Boolean afternoonChecked;
    private Boolean nightChecked;
    private Boolean allChecked;

    private MedicineDetails medicineDetails;

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getAfternoon() {
        return Afternoon;
    }

    public void setAfternoon(String afternoon) {
        Afternoon = afternoon;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getMoringChecked() {
        return moringChecked;
    }

    public void setMoringChecked(Boolean moringChecked) {
        this.moringChecked = moringChecked;
    }

    public Boolean getAfternoonChecked() {
        return afternoonChecked;
    }

    public void setAfternoonChecked(Boolean afternoonChecked) {
        this.afternoonChecked = afternoonChecked;
    }

    public Boolean getNightChecked() {
        return nightChecked;
    }

    public void setNightChecked(Boolean nightChecked) {
        this.nightChecked = nightChecked;
    }

    public MedicineDetails(){
    }

    public MedicineDetails(MedicineDetails medicineDetails){
        this.medicineDetails = medicineDetails;
    }
    public MedicineDetails(Parcel in) {
        personName = in.readString();
        medicineName = in.readString();
        medicineType = in.readString();
        if (in.readByte() == 0) {
            medicineColor = null;
        } else {
            medicineColor = in.readInt();
        }
        medicineMorning = in.readArrayList(MedicinesTakenOrNot.class.getClassLoader());
        medicineMorningTime = in.readString();
        medicineAfternoon = in.readArrayList(MedicinesTakenOrNot.class.getClassLoader());
        medicineNoonTime = in.readString();
        medicineNight = in.readArrayList(MedicinesTakenOrNot.class.getClassLoader());
        medicineNightTime = in.readString();
        medicineDays = in.readString();
        byte tmpMedicineRemainder = in.readByte();
        medicineRemainder = tmpMedicineRemainder == 0 ? null : tmpMedicineRemainder == 1;
    }

    public static final Creator<MedicineDetails> CREATOR = new Creator<MedicineDetails>() {
        @Override
        public MedicineDetails createFromParcel(Parcel in) {
            return new MedicineDetails(in);
        }

        @Override
        public MedicineDetails[] newArray(int size) {
            return new MedicineDetails[size];
        }
    };

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public int getMedicineColor() {
        return medicineColor;
    }

    public void setMedicineColor(int medicineColor) {
        this.medicineColor = medicineColor;
    }

    public ArrayList<MedicinesTakenOrNot> getMedicineMorning() {
        return medicineMorning;
    }

    public void setMedicineMorning(ArrayList<MedicinesTakenOrNot> medicineMorning) {
        this.medicineMorning = medicineMorning;
    }

    public String getMedicineMorningTime() {
        return medicineMorningTime;
    }

    public void setMedicineMorningTime(String medicineMorningTime) {
        this.medicineMorningTime = medicineMorningTime;
    }

    public ArrayList<MedicinesTakenOrNot> getMedicineAfternoon() {
        return medicineAfternoon;
    }

    public void setMedicineAfternoon(ArrayList<MedicinesTakenOrNot> medicineAfternoon) {
        this.medicineAfternoon = medicineAfternoon;
    }

    public String getMedicineNoonTime() {
        return medicineNoonTime;
    }

    public void setMedicineNoonTime(String medicineNoonTime) {
        this.medicineNoonTime = medicineNoonTime;
    }

    public ArrayList<MedicinesTakenOrNot> getMedicineNight() {
        return medicineNight;
    }

    public void setMedicineNight(ArrayList<MedicinesTakenOrNot> medicineNight) {
        this.medicineNight = medicineNight;
    }

    public String getMedicineNightTime() {
        return medicineNightTime;
    }

    public void setMedicineNightTime(String medicineNightTime) {
        this.medicineNightTime = medicineNightTime;
    }

    public String getMedicineDays() {
        return medicineDays;
    }

    public void setMedicineDays(String medicineDays) {
        this.medicineDays = medicineDays;
    }
    public Boolean getMedicineRemainder() {
        return medicineRemainder;
    }

    public void setMedicineRemainder(Boolean medicineRemainder) {
        this.medicineRemainder = medicineRemainder;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MedicineDetails)
                && this.personName.equals(((MedicineDetails) o).getPersonName())

                && this.medicineName.equals(((MedicineDetails) o).getMedicineName())
                && this.medicineColor.equals(((MedicineDetails) o).getMedicineColor())
                && this.medicineType.equals(((MedicineDetails) o).getMedicineType())

                && this.medicineMorning.equals(((MedicineDetails) o).getMedicineMorning())
                && this.medicineMorningTime.equals(((MedicineDetails) o).getMedicineMorningTime())

                && this.medicineAfternoon.equals(((MedicineDetails) o).getMedicineAfternoon())
                && this.medicineNoonTime.equals(((MedicineDetails) o).getMedicineNoonTime())

                && this.medicineNight.equals(((MedicineDetails) o).getMedicineNight())
                && this.medicineNightTime.equals(((MedicineDetails) o).getMedicineNightTime())

                && this.medicineDays.equals(((MedicineDetails) o).getMedicineDays())
                && this.medicineRemainder.equals(((MedicineDetails) o).getMedicineRemainder());

               /* && this.widthRequired.equals(((Variant) o).getWidthRequired())
                && this.heightRequired.equals(((Variant) o).getHeightRequired())
                && this.middlePaneType.equals(((Variant) o).getMiddlePaneType())
                && this.previewAction.equals(((Variant) o).getPreviewAction())
                && this.editorTemplate.equals(((Variant) o).getEditorTemplate())
                && this.previewTemplateUrl.equals(((Variant) o).getPreviewTemplateUrl())
                && this.isMultiAllowed.equals(((Variant) o).getIsMultiAllowed())
                && this.isTextEnabled.equals(((Variant) o).getIsTextEnabled())
                && this.isRotateEnabled.equals(((Variant) o).getIsRotateEnabled())
                && this.isCopyEnabled.equals(((Variant) o).getIsCopyEnabled())
                && this.isDeleteEnabled.equals(((Variant) o).getIsDeleteEnabled());*/
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(personName);
        dest.writeString(medicineName);
        dest.writeString(medicineType);
        if (medicineColor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(medicineColor);
        }
        dest.writeArray(new ArrayList[]{medicineMorning});
        dest.writeString(medicineMorningTime);
        dest.writeArray(new ArrayList[]{medicineAfternoon});
        dest.writeString(medicineNoonTime);
        dest.writeArray(new ArrayList[]{medicineNight});
        dest.writeString(medicineNightTime);
        dest.writeString(medicineDays);
        dest.writeByte((byte) (medicineRemainder == null ? 0 : medicineRemainder ? 1 : 2));
    }
}
