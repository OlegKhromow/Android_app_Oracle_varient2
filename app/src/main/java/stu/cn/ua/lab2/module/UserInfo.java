package stu.cn.ua.lab2.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserInfo implements Parcelable {
    private String name;
    private String surname;
    private Calendar birthDate;
    private String sex;

    public UserInfo(String name, String surname, Calendar birthDate, String sex) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.sex = sex;
    }

    /**
     * @return instance of the class with default empty values, not null
     */
    public static UserInfo EmptyUserInfo() {
        Calendar date = Calendar.getInstance();
        date.set(2000, 0, 1);
        return new UserInfo("", "", date, "");
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    /**
     * @return birthday date string in format 'dd.MM.yyyy'
     */
    public String getBirthDateString(){
        SimpleDateFormat dateOnly = new SimpleDateFormat("dd.MM.yyyy");
        return dateOnly.format(birthDate.getTime());
    }

    /**
     * Setter for name with a check for emptiness
     */
    public void setName(String name) {
        if (name.trim().isEmpty())
            throw new IllegalArgumentException();
        this.name = name.trim();
    }

    /**
     * Setter for surname with a check for emptiness
     */
    public void setSurname(String surname) {
        if (surname.trim().isEmpty())
            throw new IllegalArgumentException();
        this.surname = surname.trim();
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + getBirthDateString() +
                ", Sex='" + sex + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.surname);
        dest.writeSerializable(this.birthDate);
        dest.writeString(this.sex);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.surname = source.readString();
        this.birthDate = (Calendar) source.readSerializable();
        this.sex = source.readString();
    }

    protected UserInfo(Parcel in) {
        this.name = in.readString();
        this.surname = in.readString();
        this.birthDate = (Calendar) in.readSerializable();
        this.sex = in.readString();
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
