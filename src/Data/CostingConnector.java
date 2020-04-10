package Data;

import java.sql.*;
import java.util.ArrayList;

public class CostingConnector {

    public static void insertIntoProductCosting(int id, double fTEEq, double labourEq){
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for Product Costing Alteration");

            stmt = c.createStatement();
            String sql = "INSERT INTO public.\"ProductCosting\" " +
                    "(\"productId\",\"fteEquivalent\",\"labourCosts1WorkingHour\") "
                    + "VALUES (" + id + ", " + fTEEq +"," + labourEq + " );";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public static void insertIntoUserStories(ArrayList<ArrayList<Object>> storyList) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (for running costs insert)");

            PreparedStatement sql = c.prepareStatement("INSERT INTO public.\"UserStories\" " +
                    "(\"storyName\",\"storyPoints\",\"Iteration\", \"productId\", \"repeats\") VALUES " +
                    "(?,?,?,?,?)");

            try {
                for (int i = 0; i < storyList.size(); i++){
                    String storyName = (String) storyList.get(i).get(0);
                    int points = (int) storyList.get(i).get(1);
                    String iteration = (String) storyList.get(i).get(2);
                    int productId = (int) storyList.get(i).get(3);
                    Boolean repeats = (Boolean) storyList.get(i).get(4);

                    sql.setString(1, storyName);
                    sql.setInt(2, points);
                    sql.setString(3, iteration);
                    sql.setInt(4, productId);
                    sql.setBoolean(5, repeats);
                    sql.execute();
                }
            } catch (Exception e) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                System.exit(0);
            }

            sql.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public static void insertIntoEquipment(int id, ArrayList<ArrayList<Object>> equipmentList) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (for Equipment table alteration)");

            try {
                for (int i = 0; i < equipmentList.size(); i++){
                    System.out.println(equipmentList.get(i).get(1));

                    String equipmentName = (String) equipmentList.get(i).get(0);
                    int equipmentQuantity = Integer.parseInt((String) equipmentList.get(i).get(1));
                    Double equipmentPrice = Double.parseDouble((String) equipmentList.get(i).get(2));

                    stmt = c.createStatement();
                    String sql = "INSERT INTO public.\"Equipment\" " +
                            "(\"productId\",\"equipmentName\",\"equipmentQuantity\",\"equipmentPrice\") "
                            + "VALUES (" + id + ", '" + equipmentName +"'," + equipmentQuantity + "," + equipmentPrice + " );";

                    stmt.executeUpdate(sql);
                }
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
    }

    public static void updateProductCosting(int id, double fTEEq, double labourEq) {
        Connection c = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for Product Costing Alteration");

            PreparedStatement sql = c.prepareStatement("UPDATE public.\"ProductCosting\" " +
                    "SET \"fteEquivalent\" = ?" +
                    ",\"labourCosts1WorkingHour\" = ? "
                    + " WHERE \"productId\" = ? ;");
            sql.setDouble(1, fTEEq);
            sql.setDouble(2, labourEq);
            sql.setInt(3, id);
            sql.execute();

            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public static ArrayList<ArrayList<Object>> getCostingByProduct(int productId) {
        Connection c = null;
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Product Costing");

            PreparedStatement getCosting = c.prepareStatement("SELECT * FROM public.\"ProductCosting\" " +
                    "WHERE \"productId\" = ?;");
            getCosting.setInt(1, productId);

            ResultSet getCostingResultSet = getCosting.executeQuery();

            while ( getCostingResultSet.next() ) {
                ArrayList<Object> temp = new ArrayList<>();
                queryResults.add(temp);
                temp.add(getCostingResultSet.getInt(1));
                temp.add(getCostingResultSet.getDouble(2));
                temp.add(getCostingResultSet.getDouble(3));
            }

            getCosting.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Costing SELECT Successful");
        return queryResults;
    }

    public static ArrayList<ArrayList<Object>> getUserStoriesByProduct(int productId) {
        Connection c = null;
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - User Stories");

            PreparedStatement getUserStories = c.prepareStatement("SELECT * FROM public.\"UserStories\" " +
                    "WHERE \"productId\" = ?;");
            getUserStories.setInt(1, productId);

            ResultSet getCostingResultSet = getUserStories.executeQuery();

            while ( getCostingResultSet.next() ) {
                ArrayList<Object> temp = new ArrayList<>();
                queryResults.add(temp);
                temp.add(getCostingResultSet.getInt(1));
                temp.add(getCostingResultSet.getString(2));
                temp.add(getCostingResultSet.getInt(3));
                temp.add(getCostingResultSet.getString(4));
                temp.add(getCostingResultSet.getInt(5));
                temp.add(getCostingResultSet.getBoolean(6));
            }
            getUserStories.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("User Stories SELECT Successful");
        return queryResults;
    }

    public static ArrayList<ArrayList<Object>> getEquipmentByProduct(int productId) {
        Connection c = null;
        ArrayList<ArrayList<Object>> queryResults = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Equipment");

            PreparedStatement getEquipment = c.prepareStatement("SELECT * FROM public.\"Equipment\" " +
                    "WHERE \"productId\" = ?;");
            getEquipment.setInt(1, productId);

            ResultSet getCostingResultSet = getEquipment.executeQuery();

            while ( getCostingResultSet.next() ) {
                ArrayList<Object> temp = new ArrayList<>();
                queryResults.add(temp);
                temp.add(getCostingResultSet.getInt(1));
                temp.add(getCostingResultSet.getString(2));
                temp.add(getCostingResultSet.getInt(3));
                temp.add(getCostingResultSet.getDouble(4));
                temp.add(getCostingResultSet.getInt(5));
            }
            getEquipment.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Equipment SELECT Successful");
        return queryResults;
    }
}
