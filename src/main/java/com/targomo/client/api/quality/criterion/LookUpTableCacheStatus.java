package com.targomo.client.api.quality.criterion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;

@Getter
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@JsonDeserialize(builder = LookUpTableCacheStatus.LookUpTableCacheStatusBuilderImpl.class)
public class LookUpTableCacheStatus {
    private DateTime startTimestamp;
    private DateTime endTimestamp;

    public LookUpTableCacheStatus(DateTime startTimestamp){
        this.startTimestamp = startTimestamp;
        this.endTimestamp= null;
    }

    public Boolean needsToBeUpdated(DateTime startTimestampLimit, DateTime nowTimestamp, Long maxDelay){
        if(this.startTimestamp == null){
            return true;
        }
        else if(this.endTimestamp == null){
            return this.startTimestamp.isBefore(startTimestampLimit) || this.startTimestamp.plus(maxDelay).isBefore(nowTimestamp);
        }
        else {
            return this.startTimestamp.isBefore(startTimestampLimit);
        }
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class LookUpTableCacheStatusBuilderImpl extends LookUpTableCacheStatus.LookUpTableCacheStatusBuilder{
    }
}
