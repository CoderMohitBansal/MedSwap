package com.example.medswap.SIGNUP;

public class SignUpViewModel {
    private String userId;
    private String firstName;
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;

    public SignUpViewModel() {
    }

    public SignUpViewModel(String userId, String firstName, String userName, String email, String phoneNumber) {
        this.userId = userId;
        this.firstName = firstName;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
