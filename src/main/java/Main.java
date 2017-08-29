
import entity.Provider;
import enums.Country;
import enums.Currency;
import workers.CountryDBWorker;
import workers.CurrencyDBWorker;
import workers.ProviderDBWorker;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        addCountriesToDatabase();
        addCurrencyToDatabase();


        List<Provider> providers = getListWithMockData();
        ProviderDBWorker worker = new ProviderDBWorker();
        for (int i = 0; i < providers.size(); i++) {
            worker.insert(providers.get(i));
        }
        System.out.println(worker.select());
        System.out.println(worker.select(Currency.DOLLAR));
        System.out.println(worker.select(Country.ALBANIA));

    }

    private static List<Provider> getListWithMockData() {
        List<Provider> providers = new ArrayList<>();
        for (int id = 0; id < 10; id++) {
            List<Country> countries = getRandomCountries(2);
            List<Currency> currencies = getRandomCurrencies(2);
            providers.add(new Provider("provider number " + (id + 1), currencies, countries));
        }
        return providers;
    }


    private static void addCountriesToDatabase() {
        CountryDBWorker dbWorker = new CountryDBWorker();
        for (int i = 0; i < Country.values().length; i++) {
            dbWorker.insert(Country.values()[i]);
        }
        dbWorker.closeConnection();
    }

    private static void addCurrencyToDatabase() {
        CurrencyDBWorker dbWorker = new CurrencyDBWorker();
        for (int i = 0; i < Currency.values().length; i++) {
            dbWorker.insert(Currency.values()[i]);
        }
    }

    private static List<Country> getRandomCountries(int countryCount) {
        List<Country> list = new ArrayList<>();
        for (int i = 0; i < countryCount; i++) {
            list.add(Country.values()[(int) (Math.random() * Country.values().length)]);
        }
        return list;
    }

    private static List<Currency> getRandomCurrencies(int currencyCount) {
        List<Currency> list = new ArrayList<>();
        for (int i = 0; i < currencyCount; i++) {
            list.add(Currency.values()[(int) (Math.random() * Currency.values().length)]);
        }
        return list;
    }


}
