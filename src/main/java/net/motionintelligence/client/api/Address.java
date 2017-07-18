package net.motionintelligence.client.api;

import net.motionintelligence.client.api.util.POJOUtil;

/**
 * Created by David on 18.07.2017.
 */
public class Address {

    public final String street; //incl. number
    public final String streetDetails;
    public final String city;
    public final String postalCode;
    public final String country;

    public Address(String address, String addressExtra, String city, String postalCode, String countryCode ) {
        this.street         = address;
        this.streetDetails  = addressExtra;
        this.city           = city;
        this.postalCode     = postalCode;
        this.country        = countryCode;
    }

    @Override
    public String toString() {
        return POJOUtil.prettyPrintPOJO(this);
    }
}
