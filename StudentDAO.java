
package com.college.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.college.model.Student;
import com.college.util.DBConnection;

public class StudentDAO {

    // 1. Register a new student
    public boolean addStudent(Student s) throws Exception {

        String sql = "INSERT INTO students "
                + "(full_name, dob, gender, email, phone, address, "
                + "course, department, admission_year) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setDate(2, Date.valueOf(s.getDob()));
            ps.setString(3, s.getGender());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setString(6, s.getAddress());
            ps.setString(7, s.getCourse());
            ps.setString(8, s.getDepartment());
            ps.setInt(9, s.getAdmissionYear());

            return ps.executeUpdate() > 0;
        }
    }

    // 2. Get all students or search by ID, name, or email
    public List<Object[]> getAllStudents(String keyword)
            throws Exception {

        List<Object[]> students = new ArrayList<>();

        String sql = "SELECT student_id, full_name, email, phone, "
                + "course, department, admission_year "
                + "FROM students "
                + "WHERE CAST(student_id AS CHAR) LIKE ? "
                + "OR full_name LIKE ? OR email LIKE ? "
                + "ORDER BY student_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String search = "%" +
                    (keyword == null ? "" : keyword.trim()) + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(new Object[] {
                        rs.getInt("student_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("course"),
                        rs.getString("department"),
                        rs.getInt("admission_year")
                    });
                }
            }
        }

        return students;
    }

    // 3. Delete a student using their ID
    public boolean deleteStudent(int studentId) throws Exception {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            return ps.executeUpdate() == 1;
        }
    }

    // 4. Update a student's details
    public boolean updateStudent(
            int studentId,
            String fullName,
            String email,
            String phone,
            String course,
            String department,
            int admissionYear) throws Exception {

        String sql = "UPDATE students SET "
                + "full_name = ?, email = ?, phone = ?, "
                + "course = ?, department = ?, admission_year = ? "
                + "WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, course);
            ps.setString(5, department);
            ps.setInt(6, admissionYear);
            ps.setInt(7, studentId);

            return ps.executeUpdate() == 1;
        }
    }
}
