package com.example.ecomerce.dto;



public class RegisterDTO {
    private String name;
    private String lastname;
    private String password;
    private String email;



    public RegisterDTO() {
    }

    public RegisterDTO(String name, String lastname, String password, String email) {
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }





    @Override
    public String toString() {
        return "RegisterDTO{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
