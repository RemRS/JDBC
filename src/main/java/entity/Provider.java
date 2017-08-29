package entity;

import enums.Country;
import enums.Currency;

import java.util.*;

public class Provider implements Comparable<Provider> {

    private String name;
    private List<enums.Currency> currencies = new ArrayList<>();
    private List<Country> countries = new ArrayList<>();


    public Provider(String name, List<enums.Currency> currencies, List<Country> countries) {
        this.name = name;
        this.currencies = currencies;
        this.countries = countries;
    }


    @Override
    public String toString() {
        return "Provider " + name + "\n Currencies: " + currencies.toString() + "\n" + "Countries" + countries.toString() + "\n";
    }

    public static TreeSet<Provider> sortByName(Set<Provider> values) {
        Comparator<Provider> comp = (Provider o1, Provider o2) -> (o1.compareTo(o2));
        TreeSet<Provider> result = new TreeSet<>(comp);
        values.stream().forEach(provider -> result.add(provider));
        System.out.println(result);
        return result;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<enums.Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public void addCurrency(String currency){
        for(int i = 0; i<Currency.values().length; i++){
            if(Currency.values()[i].toString().equals(currency)){
                currencies.add(Currency.values()[i]);
            }
        }
    }

    public void addCountry(String country){
        for(int i = 0; i<Country.values().length; i++){
            if(Country.values()[i].toString().equals(country)){
                countries.add(Country.values()[i]);
            }
        }
    }

    public boolean containsCurrencsy(String currency){
        for(int i = 0; i<Currency.values().length; i++){
            if(currency.equals(Currency.values()[i].toString())){
                return currencies.contains(Currency.values()[i]);
            }
        }
        return false;
    }

    public boolean containsCountry(String country){
        for(int i = 0; i<Country.values().length; i++){
            if(country.equals(Country.values()[i].toString())){
                return countries.contains(Country.values()[i]);
            }
        }
        return false;
    }

    @Override
    public int compareTo(Provider o) {
        return (o.name.compareTo(this.name));
    }
}