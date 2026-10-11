package model;

import java.sql.Timestamp;

public class Complaint {
    private int id;
    private int studentId;
    private String studentName;
    private String studentRoom;
    private int categoryId;
    private String categoryName;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String adminRemarks;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Complaint() {}

    public Complaint(int studentId, int categoryId, String title,
                     String description, String priority) {
        this.studentId = studentId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = "PENDING";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentRoom() { return studentRoom; }
    public void setStudentRoom(String studentRoom) { this.studentRoom = studentRoom; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAdminRemarks() { return adminRemarks; }
    public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}