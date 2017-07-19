package net.motionintelligence.client.api;

import net.motionintelligence.client.api.util.POJOUtil;

/**
 * A simple immutable POJO object representing a number of address fields. Not all have to be set.
 *
 * Created by David on 18.07.2017.
 */
public class Address {

    /**
     * The street and house number of the address.
     */
    public final String street;

    /**
     * Further details of the street address, e.g. "2. Hinterhaus"
     */
    public final String streetDetails;

    /**
     * The addresses' city
     */
    public final String city;

    /**
     * The addresses' postal code
     */
    public final String postalCode;

    /**
     * Here a three digit country code is expected. (e.g. "DEU" for Germany) Please see https://developers.arcgis.com/rest/geocode/api-reference/geocode-coverage.htm
     */
    public final String country;

    public Address(String address, String addressExtra, String city, String postalCode, String countryCode) {
        this.street         = address;
        this.streetDetails  = addressExtra;
        this.city           = city;
        this.postalCode     = postalCode;
        this.country        = countryCode;
    }

    /**
     * Pretty prints the {@link Address}, i.e. with indention and line breaks.
     * @return
     */
    @Override
    public String toString() {
        return POJOUtil.prettyPrintPOJO(this);
    }
}
