package org.netcs.model.sql;


import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "populationSize_index", columnList = "populationSize"),
        @Index(name = "algorithm_index", columnList = "algorithm"),
        @Index(name = "scheduler_index", columnList = "algorithm,scheduler")
})
public class Experiment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String algorithm;
    private String scheduler;
    private Long time;
    private Long interactions;
    private Long effectiveInteractions;
    private Long populationSize;
    private Boolean success;
    private String terminationMessage;

    public long getId() {
        return id;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getSuccess() {
        return success;
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

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }
}
