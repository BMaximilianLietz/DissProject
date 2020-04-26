package Data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProductConnector {

    private static String connString = "database-2.cwkcvdulkq9a.eu-west-2.rds.amazonaws.com/postgres";

    public static ArrayList<Object> insertIntoProduct(int projectId, String productName,
                                         String productDescription, Date creationDate, Double productCosts,
                                         Double productPrice, String productVersion, Boolean isSubsidized,
                                                      Boolean isSubsidizing, Double runningCosts) {
        Connection c = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://"+connString,
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (for insertIntoProduct)");

            try {
                PreparedStatement sql = c.prepareStatement("INSERT INTO public.\"Products\" " +
                        "(\"projectId\",\"productName\",\"productDescription\"," +
                        "\"creationDate\",\"productCosts\",\"productPrice\",\"version\", " +
                        "\"isSubsidized\",\"isSubsidizing\",\"productRunningCosts\") " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING *;");
                sql.setInt(1, projectId);
                sql.setString(2, productName);
                sql.setString(3, productDescription);
                sql.setDate(4, creationDate);
                sql.setDouble(5, productCosts);
                sql.setDouble(6, productPrice);
                sql.setString(7, productVersion);
                sql.setBoolean(8, isSubsidized);
                sql.setBoolean(9, isSubsidizing);
                sql.setDouble(10, runningCosts);
                sql.execute();

                ResultSet returnedProductId = sql.getResultSet();
                returnedProductId.next();

                queryResults.add(returnedProductId.getInt(1));
                queryResults.add(returnedProductId.getInt(2));
                queryResults.add(returnedProductId.getString(3));
                queryResults.add(returnedProductId.getString(4));
                queryResults.add(returnedProductId.getDate(5));
                queryResults.add(returnedProductId.getDouble(6));
                queryResults.add(returnedProductId.getDouble(7));
                queryResults.add(returnedProductId.getString(8));
                queryResults.add(returnedProductId.getBoolean(9));
                queryResults.add(returnedProductId.getBoolean(10));
                queryResults.add(returnedProductId.getDouble(11));
                sql.close();

            } catch (Exception e) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                System.exit(0);
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

    // TODO add change so that product id is actually passed
    public static ArrayList<ArrayList<Object>> getAllByProjectId(int projectId) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://"+connString,
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Product Select All by project ID");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM public.\"Products\"" +
                    "WHERE \"projectId\"=" + projectId + " ORDER BY \"productId\" ASC;" );

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
                temp.add(rs.getBoolean(9));
                temp.add(rs.getBoolean(10));
                temp.add(rs.getDouble(11));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation getAllByProjectId done successfully");
        return queryResults;
    }

    public static ArrayList<Object> getProductByProjectAndProduct(int projectId, String productName) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://"+connString,
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Product Select by product name and project");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM public.\"Products\"" +
                    "WHERE \"projectId\"=" + projectId + " AND \"productName\"='" + productName +"';" );

            while ( rs.next() ) {
                queryResults.add(rs.getInt(1));
                queryResults.add(rs.getInt(2));
                queryResults.add(rs.getString(3));
                queryResults.add(rs.getString(4));
                queryResults.add(rs.getDate(5));
                queryResults.add(rs.getDouble(6));
                queryResults.add(rs.getDouble(7));
                queryResults.add(rs.getString(8));
                queryResults.add(rs.getBoolean(9));
                queryResults.add(rs.getBoolean(10));
                queryResults.add(rs.getDouble(11));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation getProductByProjectAndProduct done successfully");
        return queryResults;
    }

    public static ArrayList<Object> updateProductById(Integer productId, Integer projectId, String productName,
                                                      String productDescription, Date creationDate, Double productCosts,
                                                      Double productPrice, String productVersion,
                                                      Boolean isSubsidized, Boolean isSubsidizing,
                                                      Double runningCosts) {
        Connection c = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://"+connString,
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Product Update");

            PreparedStatement sql = c.prepareStatement("UPDATE public.\"Products\" " +
                    "SET \"projectId\" = ? " +
                    ",\"productName\" = ?" +
                    ",\"productDescription\" = ? " +
                    ",\"creationDate\" = ? " +
                    ",\"productCosts\" = ?" +
                    ",\"productPrice\" = ?" +
                    ",\"version\" = ?" +
                    ",\"isSubsidized\" = ?" +
                    ",\"isSubsidizing\" = ?" +
                    ",\"productRunningCosts\" = ?" +
                    " WHERE \"productId\" = ? " +
                    "RETURNING *;");
            sql.setInt(1, projectId);
            sql.setString(2, productName);
            sql.setString(3, productDescription);
            sql.setDate(4, creationDate);
            sql.setDouble(5, productCosts);
            sql.setDouble(6, productPrice);
            sql.setString(7, productVersion);
            sql.setBoolean(8, isSubsidized);
            sql.setBoolean(9, isSubsidizing);
            sql.setBoolean(9, isSubsidizing);
            sql.setDouble(10, runningCosts);
            sql.setInt(11, productId);
            sql.execute();

            ResultSet returnedProduct = sql.getResultSet();

            returnedProduct.next();
            queryResults.add(returnedProduct.getInt(1));
            queryResults.add(returnedProduct.getInt(2));
            queryResults.add(returnedProduct.getString(3));
            queryResults.add(returnedProduct.getString(4));
            queryResults.add(returnedProduct.getDate(5));
            queryResults.add(returnedProduct.getDouble(6));
            queryResults.add(returnedProduct.getDouble(7));
            queryResults.add(returnedProduct.getString(8));
            queryResults.add(returnedProduct.getBoolean(9));
            queryResults.add(returnedProduct.getBoolean(10));
            queryResults.add(returnedProduct.getDouble(11));

            sql.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        System.out.println("Operation updateProductById done successfully");
        return queryResults;
    }

    public static ArrayList<Object> getProductByProductId(int productId) {
        Connection c = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://"+connString,
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Product Select by product by product ID");

            PreparedStatement sql = c.prepareStatement(
                    "SELECT * FROM public.\"Products\" WHERE " +
                            "\"productId\" = ?");
            sql.setInt(1, productId);
            sql.execute();

            ResultSet rs = sql.getResultSet();

            while ( rs.next() ) {
                queryResults.add(rs.getInt(1));
                queryResults.add(rs.getInt(2));
                queryResults.add(rs.getString(3));
                queryResults.add(rs.getString(4));
                queryResults.add(rs.getDate(5));
                queryResults.add(rs.getDouble(6));
                queryResults.add(rs.getDouble(7));
                queryResults.add(rs.getString(8));
                queryResults.add(rs.getBoolean(9));
                queryResults.add(rs.getBoolean(10));
                queryResults.add(rs.getDouble(11));
            }
            rs.close();
            sql.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation getProductByProductId done successfully");
        return queryResults;
    }

    public static void deleteProductByProductId(Integer productId) {
        Connection c = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://"+connString,
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Product Update");

            PreparedStatement productSql = c.prepareStatement("DELETE FROM public.\"Products\" " +
                    " WHERE \"productId\" = ?;");
            productSql.setInt(1, productId);
            productSql.execute();
            productSql.close();

            PreparedStatement pricingSql = c.prepareStatement("DELETE FROM public.\"ProductPricing\" " +
                    " WHERE \"productId\" = ?;");
            pricingSql.setInt(1, productId);
            pricingSql.execute();
            pricingSql.close();

            PreparedStatement costingSql = c.prepareStatement("DELETE FROM public.\"ProductCosting\" " +
                    " WHERE \"productId\" = ?;");
            costingSql.setInt(1, productId);
            costingSql.execute();
            costingSql.close();

            PreparedStatement userStorySql = c.prepareStatement("DELETE FROM public.\"UserStories\" " +
                    " WHERE \"productId\" = ?;");
            userStorySql.setInt(1, productId);
            userStorySql.execute();
            userStorySql.close();

            PreparedStatement equipmentSql = c.prepareStatement("DELETE FROM public.\"Equipment\" " +
                    " WHERE \"productId\" = ?;");
            equipmentSql.setInt(1, productId);
            equipmentSql.execute();
            equipmentSql.close();


            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        System.out.println("Operation deleteProductById done successfully");
    }
}
