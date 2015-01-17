package org.netcs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * LookupService is used as an intermediate Caching Layer.
 * Caches need to added in the {@see Application} to be used.
 *
 * @author Dimitrios Amaxilatis.
 */
public class LookupService {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(LookupService.class);

    private static final String CACHE_NAME = "netcs-cache-";
    private static final String NODE_DEGREE_FORMAT = "%s%d-%s-degree";
    private static final String NODE_FORMAT = "%s%d-node-%s";
    private static final String NODES_FORMAT = "%s%d-nodes";
    private static final String EXPERIMENT_EDGES_FORMAT = "%s%d-edges";
    private static final String EXPERIMENT_EDGE_FORMAT = "%s%d-edge-%s-%s";
    /**
     * Connection to REDIS server.
     */
    @Autowired
    private org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate;


    public void updateDegree(final long experimentId, final String nodeId, final long degree) {
        // keep result in cache
        redisTemplate.opsForValue().set(String.format(NODE_DEGREE_FORMAT, CACHE_NAME, experimentId, nodeId), degree);
    }

    public Long getDegree(final long experimentId, final String nodeName) {
        return (Long) redisTemplate.opsForValue().get(String.format(NODE_DEGREE_FORMAT, CACHE_NAME, experimentId, nodeName));
    }
}
