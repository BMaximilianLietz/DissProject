package Data;

import java.sql.*;
import java.util.ArrayList;

public class ProjectConnector {

    private static String connString = "database-2.cwkcvdulkq9a.eu-west-2.rds.amazonaws.com/postgres";

    public static ArrayList<Object> insertIntoProject(String projectName, String projectDescription,
                                                      Date creationDate, String businessModel){
        Connection c = null;
        Statement stmt = null;
        ArrayList<Object> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://"+connString,
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
            queryResults.add(returnedProjectId.getDouble(6));

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
                    .getConnection("jdbc:postgresql://"+connString,
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
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation insertIntoProject done successfully");
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
                    .getConnection("jdbc:postgresql://"+connString,
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
                temp.add(rs.getString(6));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation getAllProjects done successfully");
        return queryResults;
    }

    public static void deleteProjectByProjectName(Integer projectId) {
        Connection c = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        Object[] returnArray = new Object[2];

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://"+connString,
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Delete by project Id");

            PreparedStatement sql = c.prepareStatement(
                    "DELETE FROM public.\"Projects\" WHERE \"projectId\" = ?");
            sql.setInt(1, projectId);
            sql.execute();

            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation deleteProjectByProjectName done successfully");

        ArrayList<ArrayList<Object>> productsToDelete = ProductConnector.getAllByProjectId(projectId);
        for (int i = 0; i < productsToDelete.size(); i++) {
            ProductConnector.deleteProductByProductId((Integer) productsToDelete.get(i).get(0));
        }
    }

}
