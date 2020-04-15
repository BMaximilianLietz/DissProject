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
            System.out.println("Opened database successfully (for insert Product Commoditization)");

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

    public static void insertAllProductPricing(Integer productId, String preferredPricingStrategy,
                                               Double desiredMargin, String target, String industryPriceClustering,
                                               String itemQualityImportance, String marketSaturation,
                                               String marketSegmentation, String brandValue, String distributionChannel,
                                               String priceElasticity, Integer numberCustomers, String preProcessing,
                                               String itemImitability, String degreePriceCompetition,
                                               Double desiredMarkup, Double allowedVariance, Double priceRangeLow,
                                               Double priceRangeHigh, Integer subsidizationDegree,
                                               Double itemQualitySlider, Double customerHighestPrice,
                                               Double priceIndex, String competitorOrientation, Double customersMaxPrice,
                                               Double clusteringImportance, Integer pricingGoal, Integer timePeriod,
                                               Double depreciation, Integer customerPriceExpectation,
                                               Integer customerExpectationImportance, Integer competitionPriceReduction,
                                               Double minimumPrice, Double valueAdded) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (for productPricing insert)");

            try {
                stmt = c.createStatement();
                PreparedStatement sql = c.prepareStatement("INSERT INTO public.\"ProductPricing\" " +
                        "(\"productId\",\"pricingStrategy\",\"desiredMargin\", \"target\", \"priceClustering\", " +
                        "\"itemQuality\", \"marketSaturation\", \"isMarketSegmented\", \"brandValue\", " +
                        "\"distributionChannel\", \"priceElasticity\", \"numberCustomers\", \"preProcessing\", \"itemImitability\", " +
                        "\"degreePriceCompetition\", \"desiredMarkup\", \"allowedVariance\", " +
                        "\"priceRangeLow\", \"priceRangeHigh\", \"subsidizationDegree\", " +
                        "\"itemQualitySlider\", \"customerHighestPrice\", \"priceIndex\", " +
                        "\"competitorOrientation\", \"customersMaxPrice\", \"clusteringImportance\", " +
                        "\"pricingGoal\", \"timePeriod\", \"depreciation\", \"customerPriceExpectation\", " +
                        "\"customerExpectationImportance\", \"competitionPriceReduction\", " +
                        "\"minimumPrice\", \"valueAdded\") " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
                sql.setInt(1, productId);
                sql.setString(2, preferredPricingStrategy);
                sql.setDouble(3, desiredMargin);
                sql.setString(4, target);
                sql.setString(5, industryPriceClustering);
                sql.setString(6, itemQualityImportance);
                sql.setString(7, marketSaturation);
                sql.setString(8, marketSegmentation);
                sql.setString(9, brandValue);
                sql.setString(10, distributionChannel);
                sql.setString(11, priceElasticity);
                sql.setInt(12, numberCustomers);
                sql.setString(13, preProcessing);
                sql.setString(14, itemImitability);
                sql.setString(15, degreePriceCompetition);
                sql.setDouble(16, desiredMarkup);
                sql.setDouble(17, allowedVariance);
                sql.setDouble(18, priceRangeLow);
                sql.setDouble(19, priceRangeHigh);
                sql.setInt(20, subsidizationDegree);
                sql.setDouble(21, itemQualitySlider);
                sql.setDouble(22, customerHighestPrice);
                sql.setDouble(23, priceIndex);
                sql.setString(24, competitorOrientation);
                sql.setDouble(25, customersMaxPrice);
                sql.setDouble(26, clusteringImportance);
                sql.setInt(27, pricingGoal);
                sql.setInt(28, timePeriod);
                sql.setDouble(29, depreciation);
                sql.setInt(30, customerPriceExpectation);
                sql.setInt(31, customerExpectationImportance);
                sql.setInt(32, competitionPriceReduction);
                sql.setDouble(33, minimumPrice);
                sql.setDouble(34, valueAdded);

                sql.execute();
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
    }

    public static ArrayList<Object> getAllProductPricingByProductId(int productId) {
        Connection c = null;
        Statement stmt = null;
        ArrayList<Object> queryResults = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully - Product Pricing Select by product name and project");

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
                queryResults.add(rs.getDouble(18));
                queryResults.add(rs.getDouble(19));
                queryResults.add(rs.getInt(20));
                queryResults.add(rs.getDouble(21));
                queryResults.add(rs.getDouble(22));
                queryResults.add(rs.getDouble(23));
                queryResults.add(rs.getString(24));
                queryResults.add(rs.getDouble(25));
                queryResults.add(rs.getDouble(26));
                queryResults.add(rs.getInt(27));
                queryResults.add(rs.getInt(28));
                queryResults.add(rs.getDouble(29));
                queryResults.add(rs.getInt(30));
                queryResults.add(rs.getInt(31));
                queryResults.add(rs.getInt(32));
                queryResults.add(rs.getDouble(33));
                queryResults.add(rs.getDouble(34));
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
        System.out.println("Operation getAllProductPricingByProductId done successfully");
        return queryResults;
    }

    public static void updateProductPricing(Integer productId, String preferredPricingStrategy,
                                            Double desiredMargin, String target, String industryPriceClustering,
                                            String itemQualityImportance, String marketSaturation,
                                            String marketSegmentation, String brandValue, String distributionChannel,
                                            String priceElasticity, Integer numberCustomers, String preProcessing,
                                            String itemImitability, String degreePriceCompetition,
                                            Double desiredMarkup, Double allowedVariance,
                                            Double priceRangeLow, Double priceRangeHigh, Integer subsidizationDegree,
                                            Double itemQualitySlider, Double customerHighestPrice,
                                            Double priceIndex, String competitorOrientation, Double customerMaxPrice,
                                            Double clusteringImportance, Integer pricingGoal, Integer timePeriod,
                                            Double depreciation, Integer customerPriceExpectation,
                                            Integer customerExpectationImportance, Integer competitionPriceReduction,
                                            Double minimumPrice, Double valueAdded) {

        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (for productPricing table update)");

            try {
                PreparedStatement  sql = c.prepareStatement("UPDATE public.\"ProductPricing\" " +
                        "SET \"pricingStrategy\" = ?" +
                        ", \"desiredMargin\" = ?" +
                        ", \"target\" = ?" +
                        ", \"priceClustering\" = ?" +
                        ", \"itemQuality\" = ?" +
                        ", \"marketSaturation\" = ?" +
                        ", \"isMarketSegmented\" = ?" +
                        ", \"brandValue\" = ?" +
                        ", \"distributionChannel\" = ?" +
                        ", \"priceElasticity\" = ?" +
                        ", \"numberCustomers\" = ?" +
                        ", \"preProcessing\" = ?" +
                        ", \"itemImitability\" = ?" +
                        ", \"degreePriceCompetition\" = ?" +
                        ", \"desiredMarkup\" = ?" +
                        ", \"allowedVariance\" = ?" +
                        ", \"priceRangeLow\" = ?" +
                        ", \"priceRangeHigh\" = ?" +
                        ", \"subsidizationDegree\" = ?" +
                        ", \"itemQualitySlider\" = ?" +
                        ", \"customerHighestPrice\" = ?" +
                        ", \"priceIndex\" = ?" +
                        ", \"competitorOrientation\" = ?" +
                        ", \"customersMaxPrice\" = ?" +
                        ", \"clusteringImportance\" = ?" +
                        ", \"pricingGoal\" = ?" +
                        ", \"timePeriod\" = ?" +
                        ", \"depreciation\" = ?" +
                        ", \"customerPriceExpectation\" = ?" +
                        ", \"customerExpectationImportance\" = ?" +
                        ", \"competitionPriceReduction\" = ?" +
                        ", \"minimumPrice\" = ?" +
                        ", \"valueAdded\" = ?" +
                        "WHERE \"productId\" = ?;");

                sql.setString(1, preferredPricingStrategy);
                sql.setDouble(2, desiredMargin);
                sql.setString(3, target);
                sql.setString(4, industryPriceClustering);
                sql.setString(5, itemQualityImportance);
                sql.setString(6, marketSaturation);
                sql.setString(7, marketSegmentation);
                sql.setString(8, brandValue);
                sql.setString(9, distributionChannel);
                sql.setString(10, priceElasticity);
                sql.setInt(11, numberCustomers);
                sql.setString(12, preProcessing);
                sql.setString(13, itemImitability);
                sql.setString(14, degreePriceCompetition);
                sql.setDouble(15, desiredMarkup);
                sql.setDouble(16, allowedVariance);
                sql.setDouble(17, priceRangeLow);
                sql.setDouble(18, priceRangeHigh);
                sql.setInt(19, subsidizationDegree);
                sql.setDouble(20, itemQualitySlider);
                sql.setDouble(21, customerHighestPrice);
                sql.setDouble(22, priceIndex);
                sql.setString(23, competitorOrientation);
                sql.setDouble(24, customerMaxPrice);
                sql.setDouble(25, clusteringImportance);
                sql.setInt(26, pricingGoal);
                sql.setInt(27, timePeriod);
                sql.setDouble(28, depreciation);
                sql.setInt(29, customerPriceExpectation);
                sql.setInt(30, customerExpectationImportance);
                sql.setInt(31, competitionPriceReduction);
                sql.setDouble(32, minimumPrice);
                sql.setDouble(33, valueAdded);
                sql.setInt(34, productId);

                sql.execute();
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

    }

    public static void updateProductPriceIndex(Integer productId, Double priceIndex) {

        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/gdeltBig",
                            "postgres", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (for productPricing price index update)");

            try {
                PreparedStatement  sql = c.prepareStatement("UPDATE public.\"ProductPricing\" " +
                        "SET \"priceIndex\" = ?" +
                        "WHERE \"productId\" = ?;");
                sql.setDouble(1, priceIndex);
                sql.setInt(2, productId);

                sql.execute();
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

    }

}