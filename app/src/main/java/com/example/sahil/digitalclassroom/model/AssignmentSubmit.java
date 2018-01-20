package com.example.sahil.digitalclassroom.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class AssignmentSubmit {
    private String _id,assign_id,student_id;
    private String assignName;
    private String id_studentId;
    private String path;
    private long submittedAt;
    private double marks;

    public String getId_studentId() {
        return id_studentId;
    }

    public void setId_studentId(String id_studentId) {
        this.id_studentId = id_studentId;
    }

    private boolean checked;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAssign_id() {
        return assign_id;
    }

    public void setAssign_id(String assign_id) {
        this.assign_id = assign_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(long submittedAt) {
        this.submittedAt = submittedAt;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public AssignmentSubmit(){
    }

    public AssignmentSubmit(String _id, String assign_id, String student_id, String assignName, String id_studentId, String path, long submittedAt, double marks, boolean checked){
        this._id = _id;
        this.assign_id = assign_id;
        this.student_id = student_id;
        this.assignName = assignName;
        this.id_studentId = id_studentId;
        this.path = path;
        this.submittedAt = submittedAt;
        this.marks = marks;
        this.checked = checked;
    }
}
