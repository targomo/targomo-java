package net.motionintelligence.client.api.statistic;

/**
 * Created by gerb on 22/03/2017.
 */
public class PoiType {

    public String key;
    public String value;

    // needed for json de/serialization
    public PoiType(){}

    /**
     * Create a new POI type with format: `key=value`
     *
     * @param string
     */
    public PoiType(String string) {

        final String[] split = string.split("=");
        this.key   = split[0];
        this.value = split[1];
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PoiType poiType = (PoiType) o;

        if (key != null ? !key.equals(poiType.key) : poiType.key != null) return false;
        return value != null ? value.equals(poiType.value) : poiType.value == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
