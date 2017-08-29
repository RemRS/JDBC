package workers;

import entity.Provider;
import enums.Country;
import enums.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProviderDBWorker extends AbstractDBWorker<Provider> {

    private static String INSERT_PROVIDER = "INSERT INTO provider (name) VALUES (?)";
    private static String INSERT_CURRENCY = "INSERT INTO provcurr (idprov, idcurr) VALUES" +
            "                               (?, (SELECT provider_curr.id" +
            "                                    FROM provider_curr" +
            "                                    WHERE (currency = ?)));";

    private static String INSERT_COUNTRY = "INSERT INTO provcountry (idprov, idcountry) VALUES" +
            "                               (?, (SELECT provider_country.id " +
            "                                    FROM provider_country " +
            "                                    WHERE country = ?));";

    private static String GET_LAST_ID = "SELECT provider.id" +
            "                            FROM provider" +
            "                            WHERE (provider.id) " +
            "                            NOT IN (SELECT provcurr.idprov" +
            "                                    FROM provcurr)";

    private static String SELECT_ALL = "SELECT * " +
            "                                          FROM provider" +
            "                                          LEFT JOIN provcurr" +
            "                                          ON provider.id = provcurr.idprov" +
            "                                          JOIN provider_curr" +
            "                                          ON provider_curr.id = idcurr" +
            "                                          LEFT JOIN provcountry" +
            "                                          ON provider.id = provcountry.idprov" +
            "                                          JOIN provider_country" +
            "                                          ON provider_country.id = idcountry";

    private static String SELECT_BY_COUNTRY = "SELECT * " +
            "                                           FROM provider " +
            "                                           INNER JOIN provcurr" +
            "                                           ON provider.id = provcurr.idprov" +
            "                                           INNER JOIN provider_curr " +
            "                                           ON provider_curr.id = idcurr" +
            "                                           INNER JOIN provcountry" +
            "                                           ON provider.id = provcountry.idprov" +
            "                                           INNER JOIN provider_country" +
            "                                           ON provider_country.id = idcountry" +
            "                                           WHERE provider.id IN" +
            "                                                             (SELECT idprov " +
            "                                                              FROM provcountry p, provider_country c " +
            "                                                              WHERE c.country=? AND p.idcountry=c.id)";

    private static String SELECT_BY_CURRENCY = "SELECT * " +
            "                                           FROM provider " +
            "                                           INNER JOIN provcurr" +
            "                                           ON provider.id = provcurr.idprov" +
            "                                           INNER JOIN provider_curr " +
            "                                           ON provider_curr.id = idcurr" +
            "                                           INNER JOIN provcountry" +
            "                                           ON provider.id = provcountry.idprov" +
            "                                           INNER JOIN provider_country" +
            "                                           ON provider_country.id = idcountry" +
            "                                           WHERE provider.id IN" +
            "                                                             (SELECT idprov " +
            "                                                              FROM provcurr p, provider_curr c " +
            "                                                              WHERE c.currency=? AND p.idcurr =c.id)";


    public void insert(Provider object) {
        try {
            insertNewProviderToDatabase(object);
            int lastID = getLastInsertedProviderID();
            insertDataInCurrencyLinkTable(object, lastID);
            insertDataInCountryLinkTable(object, lastID);
            connection.commit();
            System.out.println("Provider was inserted successful \n\n");

        } catch (SQLException e) {
            System.err.println("Unable to insert \n" + e.toString());
            System.err.println("Try to rollback...");
            try {
                connection.rollback();
            } catch (SQLException j) {
                System.err.println("Unable to rollback \n" + j.toString());
            }
            System.err.println("rolled back");
        }
    }

    @Override
    public ArrayList<Provider> select() {
        ArrayList<Provider> result = null;
        try {
            ResultSet provider = statement.executeQuery(SELECT_ALL);

            System.out.println("All providers was selected from database");
            System.out.println("Generating provider's list");

            result = createProviderList(provider);

        } catch (SQLException e) {
            System.err.println("Unable to select providers \n" + e.toString());
        } finally {
            return result;
        }
    }

    public ArrayList<Provider> select(Country country) {
        ArrayList<Provider> result = null;
        try {
            preparedStatement = connection.prepareStatement(SELECT_BY_COUNTRY);
            preparedStatement.setString(1, country.toString());
            ResultSet provider = preparedStatement.executeQuery();

            result = createProviderList(provider);

            System.out.println("All providers was selected from database");
            System.out.println("Generating provider's list");

        } catch (SQLException e) {
            System.err.println("Unable to select providers \n" + e.toString());
        } finally {
            return result;
        }
    }

    public ArrayList<Provider> select(Currency currency) {
        ArrayList<Provider> result = null;
        try {
            preparedStatement = connection.prepareStatement(SELECT_BY_CURRENCY);
            preparedStatement.setString(1, currency.toString());
            ResultSet provider = preparedStatement.executeQuery();

            result = createProviderList(provider);
            
            System.out.println("All providers was selected from database");
            System.out.println("Generating provider's list");
        }catch (SQLException e) {
            System.err.println("Unable to select providers \n" + e.toString());
        } finally {
            return result;
        }

    }


    private ArrayList<Provider> createProviderList(ResultSet provider) throws SQLException {
        Map<String, Provider> providers = new HashMap<>();
        while (provider.next()) {
            String providerName = provider.getString("name");
            if (!providers.containsKey(providerName)) {
                providers.put(providerName, new Provider(providerName, new ArrayList<>(), new ArrayList<>()));
            }
            if (!providers.get(providerName).containsCurrencsy(provider.getString("currency"))) {
                providers.get(providerName).addCurrency(provider.getString("currency"));
            }
            if (!providers.get(providerName).containsCountry(provider.getString("country"))) {
                providers.get(providerName).addCountry(provider.getString("country"));
            }
        }
        System.out.println("List was generated");
        return new ArrayList<Provider>(providers.values());
    }


    private void insertDataInCountryLinkTable(Provider object, int lastID) throws SQLException {
        preparedStatement = connection.prepareStatement(INSERT_COUNTRY);
        for (int i = 0; i < object.getCountries().size(); i++) {
            String country = object.getCountries().get(i).toString();
            preparedStatement.setInt(1, lastID);
            preparedStatement.setString(2, country);
            preparedStatement.execute();
            System.out.println("Link to Country " + country + " inserted");
        }
    }

    private void insertDataInCurrencyLinkTable(Provider object, int lastID) throws SQLException {
        preparedStatement = connection.prepareStatement(INSERT_CURRENCY);
        for (int i = 0; i < object.getCurrencies().size(); i++) {
            String currency = object.getCurrencies().get(i).toString();
            preparedStatement.setInt(1, lastID);
            preparedStatement.setString(2, currency);
            preparedStatement.execute();
            System.out.println("Link to Currency " + currency + " inserted");
        }
    }

    private int getLastInsertedProviderID() throws SQLException {
        ResultSet resultSet = statement.executeQuery(GET_LAST_ID);
        int lastID = 0;
        while (resultSet.next()) {
            lastID = resultSet.getInt(1);
        }
        return lastID;
    }

    private void insertNewProviderToDatabase(Provider object) throws SQLException {
        connection.setAutoCommit(false);
        preparedStatement = connection.prepareStatement(INSERT_PROVIDER);
        preparedStatement.setString(1, object.getName());
        preparedStatement.execute();
        System.out.println("Provider " + object.getName() + " inserted");
    }

}
