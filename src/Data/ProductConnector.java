package Data;

import java.sql.*;
import java.util.ArrayList;

public class ProductConnector {

    public static ArrayList<Object> insertIntoProduct(int projectId, String productName,
                                         String productDescription, Date creationDate, Double productCosts,
                                         Double productPrice, String productVersion) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (for Product table alteration)");

            try {
                stmt = c.createStatement();
                String sql = "INSERT INTO public.\"Products\" " +
                        "(\"projectId\",\"productName\",\"productDescription\"," +
                        "\"creationDate\",\"productCosts\"\"productPrice\",\"productVersion\") "
                        + "VALUES (" + projectId + ", '" + productName +"', '" + productDescription +"', " +
                        "" + creationDate + ", " + productCosts + ", " + productPrice + ", '" + productVersion + ") " +
                        "RETURNING \"productId\",\"projectId\",\"productName\",\"productDescription\"," +
                        "\"creationDate\",\"productCosts\"\"productPrice\",\"productVersion\";";

                stmt.execute(sql);

                ResultSet returnedProductId = stmt.getResultSet();
                returnedProductId.next();

                queryResults.add(returnedProductId.getInt(1));
                queryResults.add(returnedProductId.getInt(2));
                queryResults.add(returnedProductId.getString(3));
                queryResults.add(returnedProductId.getString(4));
                queryResults.add(returnedProductId.getDate(5));
                queryResults.add(returnedProductId.getDouble(6));
                queryResults.add(returnedProductId.getDouble(7));
                queryResults.add(returnedProductId.getString(8));
                System.out.println(queryResults);

            } catch (Exception e) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                System.exit(0);
            }

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

    // TODO add change so that product id is actually passed
    public static ArrayList<ArrayList<Object>> getAllByProjectId(int projectId) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Product Select by product name and project");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM public.\"Products\"" +
                    "WHERE \"projectId\"=" + projectId + ";" );

            while ( rs.next() ) {
                ArrayList<Object> temp = new ArrayList<>();
                queryResults.add(temp);
                temp.add(rs.getInt(1));
                temp.add(rs.getInt(2));
                temp.add(rs.getString(3));
                temp.add(rs.getString(4));
                temp.add(rs.getDate(5));
                temp.add(rs.getDouble(6));
                temp.add(rs.getDouble(7));
                temp.add(rs.getString(8));
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

    public static ArrayList<Object> getProductByProjectAndProduct(int projectId, String productName) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Product Select by product name and project");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM public.\"Products\"" +
                    "WHERE \"projectId\"=" + projectId + " AND \"productName\"=" + productName +";" );

            while ( rs.next() ) {
                queryResults.add(rs.getInt(1));
                queryResults.add(rs.getInt(2));
                queryResults.add(rs.getString(3));
                queryResults.add(rs.getString(4));
                queryResults.add(rs.getDate(5));
                queryResults.add(rs.getDouble(6));
                queryResults.add(rs.getDouble(7));
                queryResults.add(rs.getString(8));
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
}
