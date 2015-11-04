package src.com.logistic.impl.model.person;

/**
 * Created by SnakE on 02.11.2015.
 */
public class Address implements com.logistic.api.model.person.Address {
    private String street;
    private String city;
    private String country;
    private int code;

    public Address(int code, String street, String city, String country) {
        this.code = code;
        this.street = street;
        this.city = city;
        this.country = country;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public int getCode() {
        return code;
    }
}
