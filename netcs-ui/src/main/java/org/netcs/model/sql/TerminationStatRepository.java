package org.netcs.model.sql;

/**
 * @author Dimitrios Amaxilatis.
 */

import org.netcs.model.mongo.AlgorithmStatistics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TerminationStatRepository extends CrudRepository<TerminationStat, Long> {
    Set<TerminationStat> findByExperimentAlgorithm(final String experimentAlgorithm);

    Set<TerminationStat> findByExperimentAlgorithmAndExperimentScheduler(final String experimentAlgorithm, final String experimentScheduler);

    Set<TerminationStat> findByExperimentAlgorithmAndExperimentSchedulerAndExperimentPopulationSize(final String experimentAlgorithm, final String experimentScheduler, final Long populationSize);

    Set<TerminationStat> findByExperiment(Experiment experiments);
}

