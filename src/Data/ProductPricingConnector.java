package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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
                                               String itemImitability, String degreePriceCompetition) {
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
                        "(\"productId\",\"preferredPricingStrategy\",\"desiredMargin\", \"target\", \"industryPriceClustering\", " +
                        "\"itemQualityImportance\", \"marketSaturation\", \"marketSegmentation\", \"brandValue\", " +
                        "\"distributionChannel\", \"priceElasticity\", \"numberCustomers\", \"preProcessing\", \"itemImitability\", " +
                        "\"degreePriceCompetition\") " +
                        "VALUES (" + projectId + ", '" + preferredPricingStrategy +"'," + desiredMargin + ", " +
                        "'" + target + "', '" + industryPriceClustering +"', '" + itemQualityImportance + "', " +
                        "'" + marketSaturation + "', '" + marketSegmentation + "', '" + brandValue + "', " +
                        "'" + distributionChannel + "', '" + priceElasticity +"', " + numberCustomers +", " +
                        "'" + preProcessing + "', '" + itemImitability + "', '" + degreePriceCompetition +"');";

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

}
