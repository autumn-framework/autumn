package com.dt.autumn.reporting.dataObjects;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class APIStatusTimeDTO {

    String basePath;
    Long averageResponseTime = 0L;
    Long minTime = 99999L;
    Long maxTime = 0L;
    Long totalHits = 0L;

    public APIStatusTimeDTO(String basePath) {
        this.basePath = basePath;
    }

    public void setResponseTime(Long responseTime){
        if(responseTime!=0){
            setAverageResponseTime(responseTime);
            setMaxTime(responseTime);
            setMinTime(responseTime);
            this.totalHits++;
        }
    }

    public void setAverageResponseTime(Long responseTime) {
        if(this.averageResponseTime!=0){
            this.averageResponseTime = ((this.averageResponseTime + responseTime) / 2);
        }else{
            this.averageResponseTime=responseTime;
        }
    }

    public void setMinTime(Long minTime) {
        if (this.minTime > minTime)
            this.minTime = minTime;
    }

    public void setMaxTime(Long maxTime) {
        if (this.maxTime < maxTime)
            this.maxTime = maxTime;
    }

}
