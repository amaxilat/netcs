package org.netcs.model.sql;

/**
 * @author Dimitrios Amaxilatis.
 */

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ExperimentRepository extends CrudRepository<Experiment, Long> {


    List<Experiment> findByScheduler(String scheduler);

    Set<Experiment> findByAlgorithmAndScheduler(String algorithm, String scheduler);

    @Query("SELECT u.populationSize FROM Experiment u where u.algorithm=?1")
    Set<Long> findPopulationSizeByAlgorithm(String algorithm);

    @Query("SELECT u.populationSize FROM Experiment u where u.algorithm=?1 and u.scheduler=?2")
    Set<Long> findPopulationSizeByAlgorithmAndScheduler(String algorithm, String scheduler);

    @Query("SELECT u.scheduler,u.populationSize,count(u.id),avg(u.interactions) FROM Experiment u where u.algorithm=?1 group by u.scheduler,u.populationSize")
    Set<Experiment> findStatsByAlgorithmAndScheduler(String algorithm);

    @Query("SELECT count(u.id) FROM Experiment u where u.algorithm=?1 and u.scheduler=?2 and u.populationSize=?3")
    Long findExperimentsForAlgorithmAndScheduler(String algorithm, String scheduler, Long populationSize);
}

