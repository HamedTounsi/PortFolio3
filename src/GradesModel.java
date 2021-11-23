import java.sql.*;
import java.util.ArrayList;

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

    //Method to Query all the course names from our database
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

    //Returns the TeacherID of the teacher of a given Course
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

    //Finds the Teacher Name of a given TeacherID
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

    //Finds the average grade of a given course
    public Double findCourseGradeAvr(String courseName) {
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

    //Finds the studentID of a given student
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

    //Finds the grade of the student and stores them in the object "gradesAndCourse"
    public ArrayList<gradesAndCourse> findStudentGrade(String studentName) {
        Integer studentID = findStudentID(studentName);
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

    //Takes the average of the students grades from the database and returns it as a Double.
    public Double findStudentGradeAvr(String studentName) {
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

    //Updates the grade info for a given student, if the grade is null
    public void updateGrade(String studentName, Integer newGrade){
        Integer studentID = findStudentID(studentName);
        String sql = "UPDATE Grade SET Grade = ? WHERE StudentID = ? AND Grade IS NULL;";
        try{
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, newGrade);
            pstmt.setInt(2, studentID);
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

    //constructor
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
