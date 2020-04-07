package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SubsidyConnector {

    public static ArrayList<ArrayList<Object>> getSubsidizerByProductId(Integer productId) {
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
                    "SELECT * FROM public.\"Subsidies\" WHERE " +
                            "subsidizing_product = ?");
            sql.setInt(1, productId);
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

    public static ArrayList<ArrayList<Object>> getSubsidizerByProjectId(Integer projectId) {
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
                    "SELECT * FROM public.\"Subsidies\" WHERE \"projectId\" = ?");
            sql.setInt(1, projectId);
            sql.execute();

            ResultSet rs = sql.getResultSet();

            while ( rs.next() ) {
                ArrayList<Object> temp = new ArrayList<>();
                queryResults.add(temp);
                temp.add(rs.getInt(1));
                temp.add(rs.getInt(2));
                temp.add(rs.getInt(3));
            }

            sql.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");

        return queryResults;
    }

    public static ArrayList<ArrayList<Object>> getSubsidizedByProductId(Integer productId) {
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
                    "SELECT * FROM public.\"Subsidies\" WHERE " +
                            "subsidized_product = ?");
            sql.setInt(1, productId);
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

    public static ArrayList<ArrayList<Object>> updateSubsidizerByProjectId(Integer subsidizingProduct, Integer projectId) {
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
                    "UPDATE public.\"Subsidies\" " +
                            "SET subsidized_product = ? " +
                            "WHERE subsidized_product is null AND " +
                            "\"projectId\" = ? AND subsidizing_product is not null;");
            sql.setInt(1, subsidizingProduct);
            sql.setInt(2, projectId);
            System.out.println(sql);
            sql.execute();

            /*
            ResultSet rs = sql.getResultSet();

            while ( rs.next() ) {
                ArrayList<Object> temp = new ArrayList<>();
                queryResults.add(temp);
                temp.add(rs.getInt(1));
                temp.add(rs.getString(2));
                temp.add(rs.getDouble(3));
                temp.add(rs.getInt(4));
            }
             */
            sql.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");

        return queryResults;
    }

    public static void deleteSubsidizer(Integer subsidizerId)  {
        Connection c = null;
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for Subsidy DELETE");

            PreparedStatement sql = c.prepareStatement(
                    "DELETE FROM public.\"Subsidies\" WHERE " +
                            "subsidizing_product = ?");
            sql.setInt(1, subsidizerId);
            sql.execute();

            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records deleted successfully");
    }

    public static void deleteSubsidized(Integer subsidizedId)  {
        Connection c = null;
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for Subsidy DELETE");

            PreparedStatement sql = c.prepareStatement(
                    "DELETE FROM public.\"Subsidies\" WHERE " +
                            "subsidized_product = ?");
            sql.setInt(1, subsidizedId);
            sql.execute();

            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records deleted successfully");
    }

    public static void insertIntoSubsidies(Integer subsidizingId, Integer subsidizedId, Integer projectId) {
        Connection c = null;
        ArrayList<Object> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for Subsidies Insert");

            PreparedStatement sql = c.prepareStatement("INSERT INTO public.\"Subsidies\" " +
                    "(\"subsidizing_product\",\"subsidized_product\",\"projectId\") " +
                    "VALUES(?,?,?);");
            if (subsidizingId == null) {
                sql.setNull(1,  java.sql.Types.INTEGER);
            } else {
                sql.setInt(1, subsidizingId);
            }
            if (subsidizedId == null) {
                sql.setNull(2,  java.sql.Types.INTEGER);
            } else {
                sql.setInt(2, subsidizedId);
            }
            sql.setInt(3, projectId);
            System.out.println(sql);
            sql.execute();

            sql.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Subsidies records created successfully");
    }


}
