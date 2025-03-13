package com.targomo.client.api.enums;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WeekdayTest {

    @Test
    public void testFromString() {
        assertThat(Weekday.fromString("monday")).isEqualTo(Weekday.MONDAY);
        assertThat(Weekday.fromString("TUESDAY")).isEqualTo(Weekday.TUESDAY);
        assertThat(Weekday.fromString("Wednesday")).isEqualTo(Weekday.WEDNESDAY);
        assertThat(Weekday.fromString("ThUrSdAy")).isEqualTo(Weekday.THURSDAY);
        assertThat(Weekday.fromString("friday")).isEqualTo(Weekday.FRIDAY);
        assertThat(Weekday.fromString("saturday")).isEqualTo(Weekday.SATURDAY);
        assertThat(Weekday.fromString("sunday")).isEqualTo(Weekday.SUNDAY);
        assertThat(Weekday.fromString(null)).isEqualTo(null);
        assertThatThrownBy(() -> Weekday.fromString("badday")).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weekday 'badday' is not a valid day. Must be one of '[SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]'.");
    }

    @Test
    public void testFromIntMonSun() {
        assertThat(Weekday.fromIntMonSun(0)).isEqualTo(Weekday.MONDAY);
        assertThat(Weekday.fromIntMonSun(1)).isEqualTo(Weekday.TUESDAY);
        assertThat(Weekday.fromIntMonSun(2)).isEqualTo(Weekday.WEDNESDAY);
        assertThat(Weekday.fromIntMonSun(3)).isEqualTo(Weekday.THURSDAY);
        assertThat(Weekday.fromIntMonSun(4)).isEqualTo(Weekday.FRIDAY);
        assertThat(Weekday.fromIntMonSun(5)).isEqualTo(Weekday.SATURDAY);
        assertThat(Weekday.fromIntMonSun(6)).isEqualTo(Weekday.SUNDAY);
        assertThatThrownBy(() -> Weekday.fromIntMonSun(7)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weekday '7' is not a valid day. Must be in range 0-6.");
        assertThatThrownBy(() -> Weekday.fromIntMonSun(-1)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weekday '-1' is not a valid day. Must be in range 0-6.");
    }

    @Test
    public void testFromIntSunMon() {
        assertThat(Weekday.fromIntSunMon(0)).isEqualTo(Weekday.SUNDAY);
        assertThat(Weekday.fromIntSunMon(1)).isEqualTo(Weekday.MONDAY);
        assertThat(Weekday.fromIntSunMon(2)).isEqualTo(Weekday.TUESDAY);
        assertThat(Weekday.fromIntSunMon(3)).isEqualTo(Weekday.WEDNESDAY);
        assertThat(Weekday.fromIntSunMon(4)).isEqualTo(Weekday.THURSDAY);
        assertThat(Weekday.fromIntSunMon(5)).isEqualTo(Weekday.FRIDAY);
        assertThat(Weekday.fromIntSunMon(6)).isEqualTo(Weekday.SATURDAY);
        assertThatThrownBy(() -> Weekday.fromIntSunMon(7)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weekday '7' is not a valid day. Must be in range 0-6.");
        assertThatThrownBy(() -> Weekday.fromIntSunMon(-1)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weekday '-1' is not a valid day. Must be in range 0-6.");
    }
}