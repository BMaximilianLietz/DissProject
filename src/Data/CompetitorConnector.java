package Data;

import java.sql.*;
import java.util.ArrayList;

public class CompetitorConnector {

    public static ArrayList<ArrayList<Object>> getCompetitorsByProjectId(Integer projectId) {
        Connection c = null;
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for Competitor SELECT");

            PreparedStatement sql = c.prepareStatement(
                    "SELECT * FROM public.\"Competitors\" WHERE \"projectId\" = ?");
            sql.setInt(1, projectId);
            sql.execute();

            ResultSet rs = sql.getResultSet();

            while ( rs.next() ) {
                ArrayList<Object> temp = new ArrayList<>();
                queryResults.add(temp);
                temp.add(rs.getInt(1));
                temp.add(rs.getString(2));
                temp.add(rs.getDouble(3));
                temp.add(rs.getInt(4));
            }

            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");

        return queryResults;
    }

    public static void insertIntoCompetitors(String competitorName, Double competitorPrice, Integer projectId) {
        Connection c = null;
        ArrayList<Object> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for Competitor Insert");

            PreparedStatement sql = c.prepareStatement("INSERT INTO public.\"Competitors\" " +
                            "(\"competitorName\",\"competitorPrice\",\"projectId\") " +
                            "VALUES(?,?,?);");
            sql.setString(1, competitorName);
            sql.setDouble(2, competitorPrice);
            sql.setInt(3, projectId);
            sql.execute();

            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public static void updateCompetitorById() {

    }
}
