package com.targomo.client.api.statistic;

/**
 * Created by gerb on 22/03/2017.
 */
public class PoiType implements Comparable<PoiType> {

    private String key;
    private String value;

    // needed for json de/serialization
    public PoiType(){}

    /**
     * Create a new POI type with format: `key=value`
     *
     * @param string format `key=value`
     */
    public PoiType(String string) {

        final String[] split = string.split("=");
        this.key   = split[0];
        this.value = split[1];
    }

    public PoiType(String key, String value) {

        this.key   = key;
        this.value = value;
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
    public String toString() {
        return key + "=" + value;
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

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>The implementor must ensure <code>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</code> for all <code>x</code> and <code>y</code>.  (This
     * implies that <code>x.compareTo(y)</code> must throw an exception iff
     * <code>y.compareTo(x)</code> throws an exception.)
     * <p>The implementor must also ensure that the relation is transitive:
     * <code>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</code> implies
     * <code>x.compareTo(z)&gt;0</code>.
     * <p>Finally, the implementor must ensure that <code>x.compareTo(y)==0</code>
     * implies that <code>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</code>, for
     * all <code>z</code>.
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <code>(x.compareTo(y)==0) == (x.equals(y))</code>.  Generally speaking, any
     * class that implements the <code>Comparable</code> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>In the foregoing description, the notation
     * <code>sgn(</code><i>expression</i><code>)</code> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <code>-1</code>,
     * <code>0</code>, or <code>1</code> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(PoiType o) {
        return this.toString().compareTo(o.toString());
    }
}
