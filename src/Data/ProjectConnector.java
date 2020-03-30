package Data;

import java.sql.*;
import java.util.ArrayList;

public class ProjectConnector {

    public static ArrayList<Object> insertIntoProject(String projectName, String projectDescription,
                                                      Date creationDate, String businessModel){
        Connection c = null;
        Statement stmt = null;
        ArrayList<Object> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for Project Insertion");

            stmt = c.createStatement();
            String sql = "INSERT INTO public.\"Projects\" " +
                    "(\"projectName\",\"projectDescription\",\"creationDate\",\"businessModel\") "
                    + "VALUES ('" + projectName + "', '" + projectDescription +"'," + creationDate + ", " +
                    "'" + businessModel + "') RETURNING *;";
            stmt.execute(sql);

            ResultSet returnedProjectId = stmt.getResultSet();
            returnedProjectId.next();
            queryResults.add(returnedProjectId.getInt(1));
            queryResults.add(returnedProjectId.getString(2));
            queryResults.add(returnedProjectId.getString(3));
            queryResults.add(returnedProjectId.getDate(4));
            queryResults.add(returnedProjectId.getString(5));
            queryResults.add(returnedProjectId.getString(5));
            System.out.println(queryResults);

            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
        return queryResults;
    }

    public static ArrayList<Object> getProjectByProjectName(String projectName) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        Object[] returnArray = new Object[2];
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Project Select by project name");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM public.\"Projects\"" +
                    "WHERE \"projectName\"='" + projectName + "';" );

            while ( rs.next() ) {
                queryResults.add(rs.getInt(1));
                queryResults.add(rs.getString(2));
                queryResults.add(rs.getString(3));
                queryResults.add(rs.getDate(4));
                queryResults.add(rs.getString(5));
                queryResults.add(rs.getDouble(6));
            }
//            System.out.println(queryResults);
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation getAllByProductNameAndProject done successfully");
        return queryResults;
    }

    public static ArrayList<ArrayList<Object>> getAllProjects() {
        Connection c = null;
        Statement stmt = null;
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<>();
        Object[] returnArray = new Object[2];
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Project Select by project name");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM public.\"Projects\";" );
            int i = 0;

            while ( rs.next() ) {
                ArrayList<Object> temp = new ArrayList<>();
                queryResults.add(temp);
                temp.add(rs.getInt(1));
                temp.add(rs.getString(2));
                temp.add(rs.getString(3));
                temp.add(rs.getDate(4));
                temp.add(rs.getString(5));
                temp.add(rs.getString(5));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation getAllByProductNameAndProject done successfully");
        return queryResults;
    }

    public static void deleteProjectByProjectName(String projectName) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        Object[] returnArray = new Object[2];
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Delete by project name");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "DELETE FROM public.\"Projects\"" +
                    "WHERE \"projectName\"='" + projectName + "';" );
//            System.out.println(queryResults);
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation getAllByProductNameAndProject done successfully");
    }

}
