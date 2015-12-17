package org.netcs.model.sql;

import javax.persistence.*;

@Entity
@Table(name = "user_algorithm")
public class UserAlgorithm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "algorithm_id", nullable = false)
    private String algorithmId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(String algorithmId) {
        this.algorithmId = algorithmId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}