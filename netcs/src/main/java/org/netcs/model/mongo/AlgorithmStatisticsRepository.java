package org.netcs.model.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlgorithmStatisticsRepository extends MongoRepository<AlgorithmStatistics, String> {
    public AlgorithmStatistics findById(String id);

    public AlgorithmStatistics findByAlgorithm(Algorithm algorithm);

}
