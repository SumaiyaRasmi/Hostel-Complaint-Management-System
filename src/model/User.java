package model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String role;
    private String roomNumber;
    private String phone;
    private Timestamp createdAt;

    public User() {}

    public User(String fullName, String email, String username,
                String password, String role, String roomNumber, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.roomNumber = roomNumber;
        this.phone = phone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() { return fullName + " (@" + username + ")"; }
}