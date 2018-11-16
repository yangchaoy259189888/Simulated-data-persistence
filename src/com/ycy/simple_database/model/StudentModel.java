package com.ycy.simple_database.model;

import com.ycy.simple_database.annotation.Column;
import com.ycy.simple_database.annotation.ID;
import com.ycy.simple_database.annotation.Table;

@Table("student_model")
public class StudentModel {
    @ID
    private String id;
    private String name;
    @Column("people_id")
    private String peopleId;
    private String password;
    private String status;

    public StudentModel() {
    }

    public StudentModel(String id, String name, String peopleId,
                        String password, String status) {
        this.id = id;
        this.name = name;
        this.peopleId = peopleId;
        this.password = password;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", peopleId='" + peopleId + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
