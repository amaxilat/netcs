package org.netcs.model.mongo;

import org.springframework.data.annotation.Id;

/**
 * Created by amaxilatis on 1/10/2014.
 */
public class ExecutionStatistics {
    @Id
    private String id;
    private Long time;
    private Long interactions;
    private Long effectiveInteractions;
    private Long populationSize;
    private Boolean success;
    private String terminationMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getInteractions() {
        return interactions;
    }

    public void setInteractions(Long interactions) {
        this.interactions = interactions;
    }

    public Long getEffectiveInteractions() {
        return effectiveInteractions;
    }

    public void setEffectiveInteractions(Long effectiveInteractions) {
        this.effectiveInteractions = effectiveInteractions;
    }

    public Long getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Long populationSize) {
        this.populationSize = populationSize;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean isSuccess() {
        return (success == null || success);
    }

    public void setTerminationMessage(String terminationMessage) {
        this.terminationMessage = terminationMessage;
    }

    public String getTerminationMessage() {
        return terminationMessage;
    }
}
