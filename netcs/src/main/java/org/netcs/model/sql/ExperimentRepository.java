package org.netcs.model.sql;

/**
 * @author Dimitrios Amaxilatis.
 */

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ExperimentRepository extends CrudRepository<Experiment, Long> {


    List<Experiment> findByScheduler(String scheduler);

    Set<Experiment> findByAlgorithmAndScheduler(String algorithm, String scheduler);
}

