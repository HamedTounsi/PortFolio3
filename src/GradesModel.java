import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;

public class GradesModel {
    Connection connection = null;
    String url = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;

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

    public ArrayList<Integer> SQLQueryCourseGrade(String courseID) throws SQLException {
        ArrayList<Integer> courseGrade = new ArrayList<>();
        String sql = "SELECT CourseID, Grade FROM Grade WHERE CourseID LIKE '"+courseID+"';";
        resultSet = stmt.executeQuery(sql);
        while (resultSet != null && resultSet.next()){
            Integer grade = resultSet.getInt(2);
            System.out.println(grade);
            courseGrade.add(grade);
        }
        return courseGrade;
    }

    public void preparedStmsCourseGradeAvr(String courseID){
        String sql = "SELECT AVG(Grade) FROM Grade WHERE CourseID = ?";
        try{
            pstmt = connection.prepareStatement(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
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

    public ArrayList<String> SQLQueryCourseNames() throws SQLException{
        ArrayList<String> courses = new ArrayList<>();
        String sql = "SELECT Name FROM Course;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet != null && resultSet.next()){
            String name = resultSet.getString(1);
            courses.add(name);
        }
        return courses;
    }


}
