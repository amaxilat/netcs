package org.netcs.model.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlgorithmStatisticsRepository extends MongoRepository<AlgorithmStatistics, String> {
    List<AlgorithmStatistics> findAll();

    AlgorithmStatistics findById(String id);

    AlgorithmStatistics findByAlgorithm(Algorithm algorithm);

    AlgorithmStatistics findByAlgorithmName(String algorithm);
}
