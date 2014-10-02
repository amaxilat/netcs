package org.netcs.model.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlgorithmRepository extends MongoRepository<Algorithm, String> {
    public Algorithm findById(String id);

    public Algorithm findByName(String name);

}
