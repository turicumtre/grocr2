package com.example.patrick.grocr2;

/**
 * Created by philipp on 17.09.16.
 */
public class account {
    String firstname,lastname, email,password;
    int score,id;

    public void setScore(int newScore){
        this.score = newScore;
    }

    public account(String firstname, String lastname, String email, String password,int score,int id){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.score = score;
        this.id = id;
    }
}
