package org.netcs.model.mongo;


import org.springframework.data.annotation.Id;

import java.util.List;

public class AlgorithmStatistics {

    @Id
    private String id;

    private Algorithm algorithm;
    private List<ExecutionStatistics> statistics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public List<ExecutionStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<ExecutionStatistics> statistics) {
        this.statistics = statistics;
    }
}