package com.springboot.j2ee.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    private static final long OTP_VALID_DURATION = 5*60*1000; //5 minutes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String username;

    private String email;

    private String password;

    private String phone;

    private String avatar;

    private String background;

    private String role;


    @Column(name = "otp")
    private String otp;
    @Column(name = "otp_request_time")
    private Timestamp otpRequestTime;

    @Column(name = "otp_confirm")
    private boolean otpConfirm;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name="updated_at")
    private Timestamp updatedAt;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Post> posts = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
            mappedBy = "userFrom",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Friend> lsFriendFrom = new ArrayList<>();

    @OneToMany(
            mappedBy = "userTo",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Friend> lsFriendTo = new ArrayList<>();

    @OneToMany(
            mappedBy = "userEmote",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Like> likes = new ArrayList<>();

    @OneToOne(mappedBy = "userInfo")
    private UserInfo userInfo;

    public User(String firstName, String lastName, String email, String password, String phone, String role,Timestamp createdAt,Timestamp updatedAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }

    public boolean isOTPRequired(){
        if (this.getOtp()==null){
            return false;
        }
        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = this.otpRequestTime.getTime();
        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
            // OTP expires
            return false;
        }

        return true;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }



}
