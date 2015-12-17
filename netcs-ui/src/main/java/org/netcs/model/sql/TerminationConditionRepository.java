package org.netcs.model.sql;

/**
 * @author Dimitrios Amaxilatis.
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TerminationConditionRepository extends JpaRepository<TerminationCondition, Long> {

}

