package com.targomo.client.api.quality.criterion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.time.DateTime;

@Getter
@AllArgsConstructor
public class LookUpTableCacheStatus {
    private final DateTime startTimestamp;
    private final DateTime endTimestamp;

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
}
