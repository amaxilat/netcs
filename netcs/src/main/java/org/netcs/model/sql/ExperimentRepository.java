package org.netcs.model.sql;

/**
 * @author Dimitrios Amaxilatis.
 */

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExperimentRepository extends CrudRepository<Experiment, Long> {


    List<Experiment> findByScheduler(String scheduler);
}

