package Data;

import java.sql.*;
import java.util.ArrayList;

public class ProductPricingConnector {

    public static void insertProductCommoditization(int projectId, String productName, double productCosts) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (for Product table alteration)");

            try {
                stmt = c.createStatement();
                String sql = "INSERT INTO public.\"ProductPricing\" " +
                        "(\"projectId\",\"productName\",\"productCosts\") "
                        + "VALUES (" + projectId + ", '" + productName +"'," + productCosts + " );";

                stmt.executeUpdate(sql);
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

    public static void insertAllProductPricing(int projectId, String preferredPricingStrategy,
                                               Double desiredMargin, String target, String industryPriceClustering,
                                               String itemQualityImportance, String marketSaturation,
                                               String marketSegmentation, String brandValue, String distributionChannel,
                                               String priceElasticity, int numberCustomers, String preProcessing,
                                               String itemImitability, String degreePriceCompetition,
                                               Double desiredMarkup, Double allowedVariance) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (for Product table alteration)");

            try {
                stmt = c.createStatement();
                String sql = "INSERT INTO public.\"ProductPricing\" " +
                        "(\"productId\",\"pricingStrategy\",\"desiredMargin\", \"target\", \"priceClustering\", " +
                        "\"itemQuality\", \"marketSaturation\", \"marketSegmentation\", \"brandValue\", " +
                        "\"distributionChannel\", \"priceElasticity\", \"numberCustomers\", \"preProcessing\", \"itemImitability\", " +
                        "\"degreePriceCompetition\", \"desiredMarkup\", \"allowedVariance\") " +
                        "VALUES (" + projectId + ", '" + preferredPricingStrategy +"'," + desiredMargin + ", " +
                        "'" + target + "', '" + industryPriceClustering +"', '" + itemQualityImportance + "', " +
                        "'" + marketSaturation + "', '" + marketSegmentation + "', '" + brandValue + "', " +
                        "'" + distributionChannel + "', '" + priceElasticity +"', " + numberCustomers +", " +
                        "'" + preProcessing + "', '" + itemImitability + "', '" + degreePriceCompetition +"', " +
                        "'" + desiredMarkup + "', '" + allowedVariance + ");";

                stmt.executeUpdate(sql);
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

    public static ArrayList<Object> getAllProductPricingByProductId(int productId) {
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
            System.out.println("Opened database successfully - Product Select by product name and project");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM public.\"ProductPricing\"" +
                    "WHERE \"productId\"=" + productId + ";" );

            while ( rs.next() ) {
                queryResults.add(rs.getInt(1));
                queryResults.add(rs.getString(2));
                queryResults.add(rs.getDouble(3));
                queryResults.add(rs.getString(4));
                queryResults.add(rs.getString(5));
                queryResults.add(rs.getString(6));
                queryResults.add(rs.getString(7));
                queryResults.add(rs.getString(8));
                queryResults.add(rs.getString(9));
                queryResults.add(rs.getString(10));
                queryResults.add(rs.getString(11));
                queryResults.add(rs.getInt(12));
                queryResults.add(rs.getString(13));
                queryResults.add(rs.getString(14));
                queryResults.add(rs.getString(15));
                queryResults.add(rs.getDouble(16));
                queryResults.add(rs.getDouble(17));
            }
            ResultSetMetaData rsmd = rs.getMetaData();
            ArrayList<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columnNames.add(rsmd.getColumnName(i));
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

}
