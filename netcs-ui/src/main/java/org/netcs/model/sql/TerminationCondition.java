package org.netcs.model.sql;

import javax.persistence.*;

@Entity
@Table(name = "termination_condition")
public class TerminationCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "experiment_id", nullable = false)
    private Experiment experiment;

    @Column(name = "nodes", nullable = false)
    private String nodes;

    @Column(name = "edges", nullable = false)
    private String edges;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getEdges() {
        return edges;
    }

    public void setEdges(String edges) {
        this.edges = edges;
    }
}