import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javafx.scene.control.TextArea;

public class GradesModel {
    Connection connection;
    String url;
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet resultSet;
    String courseID;
    String teacherName;
    Integer teacherID;
    Integer studentID;

    GradesModel (String url){this.url = url;}

    //Connect to the database
    public void connectToUniDB() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    public void closeConnectionToUniDB() throws SQLException {
        if (connection != null){
            connection.close();
        }
    }

    public void createStatement() throws SQLException {
        this.stmt = connection.createStatement();
    }

    public ArrayList<String> SQLQueryStudentNames() throws SQLException{
        ArrayList<String> students = new ArrayList<>();
        String sql = "SELECT Name FROM Student;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet != null && resultSet.next()){
            String name = resultSet.getString(1);
            students.add(name);
        }
        return students;
    }

    public ArrayList<String> SQLQueryCourseName() throws SQLException{
        ArrayList<String> courses = new ArrayList<>();
        String sql = "SELECT Name FROM Course;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet != null && resultSet.next()){
            String name = resultSet.getString(1);
            courses.add(name);
        }
        return courses;
    }

    /*This method returns the courseID of a given course
    * We then use this method in the preparedStmtCourseGradeAvr method
    * to get the courseID for the course we want to know the grade average of */
    public String findCourseID(String CourseName){
        String sql = "SELECT ID FROM Course WHERE Name = ?;";
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, CourseName);
            resultSet = pstmt.executeQuery();
            if (resultSet != null){
                courseID = resultSet.getString(1);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return courseID;
    }

    public Integer findTeacherID(String CourseID){
        String sql = "SELECT TeacherID FROM Course WHERE ID = ?;";
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, CourseID);
            resultSet = pstmt.executeQuery();
            if (resultSet != null && resultSet.next()){
                teacherID = resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return teacherID;
    }

    public String findTeacherName(String courseName){
        String courseID = findCourseID(courseName);
        Integer tID = findTeacherID(courseID);
        String sql = "SELECT Name FROM Teacher WHERE ID = ?";
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, tID);
            resultSet = pstmt.executeQuery();
            if (resultSet != null){
                teacherName = resultSet.getString(1);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return teacherName;
    }

    public Double preparedStmtCourseGradeAvr(String courseName) {
        String courseID = findCourseID(courseName);
        String sql = "SELECT AVG(Grade) FROM Grade WHERE CourseID = ?;";
        Double avr = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, courseID);
            resultSet = pstmt.executeQuery();
            if (resultSet != null){
                avr = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return avr;
    }

    public Integer findStudentID(String studentName){
        String sql = "SELECT ID FROM Student WHERE Name = ?";
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, studentName);
            resultSet = pstmt.executeQuery();
            if (resultSet != null){
                studentID = resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return studentID;
    }

    public ArrayList<gradesAndCourse> findStudentGrade(String studentName) {
        Integer studentId = findStudentID(studentName);
        ArrayList<gradesAndCourse> Grades = new ArrayList<>();
        String sql = "SELECT CourseID,IFNULL(Grade, 400) FROM Grade WHERE StudentID = ?";
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            resultSet = pstmt.executeQuery();
            if (resultSet == null) {
                System.out.println("No records!");
            }
            while (resultSet != null && resultSet.next()){
                Grades.add(new gradesAndCourse(resultSet.getString(1),resultSet.getInt(2)));
            }
            } catch(SQLException e){
                System.out.println(e.getMessage());
            }
            return Grades;
    }

    public String getNullGrade(String studentName){
        Integer studentID = findStudentID(studentName);
        String result = null;
        String sql = "SELECT CourseID,Grade FROM Grade WHERE StudentID = ? AND Grade IS NULL";
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            resultSet = pstmt.executeQuery();
            if (resultSet != null){
                result = "No grades have been given";
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Double preparedStmtStudentGradeAvr(String studentName) {
        Integer studentID = findStudentID(studentName);
        String sql = "SELECT AVG(Grade) FROM Grade WHERE StudentID = ?;";
        Double avr = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            resultSet = pstmt.executeQuery();
            if (resultSet != null){
                avr = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return avr;
    }

    public void updateGrade(String studentName, Integer newGrade){
        Integer studentID = findStudentID(studentName);
        String sql = "UPDATE Grade SET Grade = ? WHERE StudentID = ? AND Grade IS NULL;";
        try{
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, newGrade);
            pstmt.setInt(2, studentID);
            System.out.println(pstmt);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}

class gradesAndCourse{
    Integer studentGrade;
    String CourseID;
    public static Boolean isGradeEmpty = false;

    public gradesAndCourse(String courseID, Integer grade) {
        studentGrade = grade;
        CourseID = courseID;
    }

    @Override
    public String toString() {
        return "gradesAndCourse{" +
                "Grade=" + studentGrade +
                ", CourseID='" + CourseID + '\'' +
                '}';
    }

    public static Boolean getIsGradeEmpty() {
        return isGradeEmpty;
    }

    public static void setIsGradeEmpty(Boolean isGradeEmpty) {
        gradesAndCourse.isGradeEmpty = isGradeEmpty;
    }
}
