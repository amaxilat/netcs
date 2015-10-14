package org.netcs.model.mongo;


import org.springframework.data.annotation.Id;

import java.util.List;

public class AlgorithmStatistics {

    @Id
    private String id;

    private Algorithm algorithm;
    private List<ExecutionStatistics> statistics;
    private int version;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AlgorithmStatistics{" +
                "algorithm=" + algorithm +
                ", id='" + id + '\'' +
                ", version=" + version +
                '}';
    }
}