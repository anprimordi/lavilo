package com.makaryostudio.lavilo.data.model;

public class Employee {
    String id;
    String name;
    String email;
    String password;
    String salary;
    String type;

    public Employee(String name, String email, String password, String salary, String type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.salary = salary;
        this.type = type;
    }

    public Employee() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
