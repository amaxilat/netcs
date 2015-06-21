package org.netcs.model.sql;


import javax.persistence.*;

@Entity
@Table(indexes = {@Index(name = "experiment_index", columnList = "experiment_id")})
public class TerminationStat {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Experiment experiment;
    private String name;
    private String data;


    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
