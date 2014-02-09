package ppsim.scheduler;

import ppsim.model.Scheduler;
import ppsim.scheduler.graph.edge.*;
import ppsim.scheduler.graph.node.*;
import ppsim.scheduler.graph.state.*;

/**
 * Factory for instantiating Schedulers.
 *
 * @param <State> the State of the Protocol
 */
public class SchedulerFactory<State> {

    /**
     * Total Number of available Schedulers.
     */
    public static final int TOTAL = 36;

    /**
     * Constant ID for Random Scheduler.
     */
    public static final int RANDOM = 0;

    /**
     * Constant ID for Transition map scheduler.
     */
    public static final int TRANSMAP = 1;

    /**
     * Constant ID for State Scheduler.
     */
    public static final int STATE = 2;

    /**
     * Constant ID for G_np Edge Scheduler.
     */
    public static final int GNP_EDGE = 3;

    /**
     * Constant ID for 2D Grid Edge Scheduler.
     */
    public static final int GRID2D_EDGE = 4;

    /**
     * Constant ID for 3D Grid Edge Scheduler.
     */
    public static final int GRID3D_EDGE = 5;

    /**
     * Constant ID for Lollipop Edge Scheduler.
     */
    public static final int LOLLIPOP_EDGE = 6;

    /**
     * Constant ID for G_np Node Scheduler.
     */
    public static final int GNP_NODE = 7;

    /**
     * Constant ID for 2D Grid Node Scheduler.
     */
    public static final int GRID2D_NODE = 8;

    /**
     * Constant ID for 3D Grid Node Scheduler.
     */
    public static final int GRID3D_NODE = 9;

    /**
     * Constant ID for Lollipop Node Scheduler.
     */
    public static final int LOLLIPOP_NODE = 10;

    /**
     * Constant ID for G_np State Scheduler.
     */
    public static final int GNP_STATE = 11;

    /**
     * Constant ID for 2D Grid State Scheduler.
     */
    public static final int GRID2D_STATE = 12;

    /**
     * Constant ID for 3D Grid State Scheduler.
     */
    public static final int GRID3D_STATE = 13;

    /**
     * Constant ID for Lollipop State Scheduler.
     */
    public static final int LOLLIPOP_STATE = 14;

    /**
     * Constant ID for Reality State Scheduler.
     */
    public static final int REALITY = 15;

    /**
     * New graph schedulers
     */

    /**
     * Constant ID for Path Edge Scheduler.
     */
    public static final int PATH_EDGE = 16;

    /**
     * Constant ID for Path Node Scheduler.
     */
    public static final int PATH_NODE = 17;

    /**
     * Constant ID for Path State Scheduler.
     */
    public static final int PATH_STATE = 18;

    /**
     * Constant ID for Binary Tree Edge Scheduler.
     */
    public static final int BINARY_TREE_EDGE = 19;

    /**
     * Constant ID for Binary Tree Node Scheduler.
     */
    public static final int BINARY_TREE_NODE = 20;

    /**
     * Constant ID for Binary Tree State Scheduler.
     */
    public static final int BINARY_TREE_STATE = 21;

    /**
     * Constant ID for Star Edge Scheduler.
     */
    public static final int STAR_EDGE = 22;

    /**
     * Constant ID for Star Node Scheduler.
     */
    public static final int STAR_NODE = 23;

    /**
     * Constant ID for Star State Scheduler.
     */
    public static final int STAR_STATE = 24;

    /**
     * Constant ID for Cycle Edge Scheduler.
     */
    public static final int CYCLE_EDGE = 25;

    /**
     * Constant ID for Cycle Node Scheduler.
     */
    public static final int CYCLE_NODE = 26;

    /**
     * Constant ID for Cycle State Scheduler.
     */
    public static final int CYCLE_STATE = 27;

    /**
     * Constant ID for Tournament Edge Scheduler.
     */
    public static final int TOURNAMENT_EDGE = 28;

    /**
     * Constant ID for Tournament Node Scheduler.
     */
    public static final int TOURNAMENT_NODE = 29;

    /**
     * Constant ID for Tournament State Scheduler.
     */
    public static final int TOURNAMENT_STATE = 30;

    /**
     * Constant ID for Bigraph Edge Scheduler.
     */
    public static final int BIGRAPH_EDGE = 31;

    /**
     * Constant ID for Bigraph Node Scheduler.
     */
    public static final int BIGRAPH_NODE = 32;

    /**
     * Constant ID for Bigraph State Scheduler.
     */
    public static final int BIGRAPH_STATE = 33;


    /**
     * New dataset schedulers
     */

    public static final int DATASET = 34;

    public static final int RANDOM_EDGE_FROM_DATASET = 35;


    /**
     * Instantiate a new scheduler.
     *
     * @param sched the ID of the scheduler to instantiate
     * @return the scheduler object
     */
    public Scheduler<State> createScheduler(final int sched) {

        Scheduler<State> scheduler;
        switch (sched) {
            case RANDOM:
                scheduler = new RandomScheduler<State>();
                break;

            case TRANSMAP:
                scheduler = new TransMapScheduler<State>();
                break;

            case STATE:
                scheduler = new StateScheduler<State>();
                break;

            case GNP_EDGE:
                scheduler = new GnpEdgeScheduler<State>();
                break;

            case GRID2D_EDGE:
                scheduler = new Grid2DEdgeScheduler<State>();
                break;

            case GRID3D_EDGE:
                scheduler = new Grid3DEdgeScheduler<State>();
                break;

            case LOLLIPOP_EDGE:
                scheduler = new LollipopEdgeScheduler<State>();
                break;

            case GNP_NODE:
                scheduler = new GnpNodeScheduler<State>();
                break;

            case GRID2D_NODE:
                scheduler = new Grid2DNodeScheduler<State>();
                break;

            case GRID3D_NODE:
                scheduler = new Grid3DNodeScheduler<State>();
                break;

            case LOLLIPOP_NODE:
                scheduler = new LollipopNodeScheduler<State>();
                break;

            case GNP_STATE:
                scheduler = new GnpStateScheduler<State>();
                break;

            case GRID2D_STATE:
                scheduler = new Grid2DStateScheduler<State>();
                break;

            case GRID3D_STATE:
                scheduler = new Grid3DStateScheduler<State>();
                break;

            case LOLLIPOP_STATE:
                scheduler = new LollipopStateScheduler<State>();
                break;

            case REALITY:
                scheduler = new RealityScheduler<State>();
                break;

            case PATH_EDGE:
                scheduler = new PathEdgeScheduler<State>();
                break;

            case PATH_NODE:
                scheduler = new PathNodeScheduler<State>();
                break;

            case PATH_STATE:
                scheduler = new PathStateScheduler<State>();
                break;

            case BINARY_TREE_EDGE:
                scheduler = new BinaryTreeEdgeScheduler<State>();
                break;

            case BINARY_TREE_NODE:
                scheduler = new BinaryTreeNodeScheduler<State>();
                break;

            case BINARY_TREE_STATE:
                scheduler = new BinaryTreeStateScheduler<State>();
                break;

            case STAR_EDGE:
                scheduler = new StarEdgeScheduler<State>();
                break;

            case STAR_NODE:
                scheduler = new StarNodeScheduler<State>();
                break;

            case STAR_STATE:
                scheduler = new StarStateScheduler<State>();
                break;

            case CYCLE_EDGE:
                scheduler = new CycleEdgeScheduler<State>();
                break;

            case CYCLE_NODE:
                scheduler = new CycleNodeScheduler<State>();
                break;

            case CYCLE_STATE:
                scheduler = new CycleStateScheduler<State>();
                break;

            case TOURNAMENT_EDGE:
                scheduler = new TournamentEdgeScheduler<State>();
                break;

            case TOURNAMENT_NODE:
                scheduler = new TournamentNodeScheduler<State>();
                break;

            case TOURNAMENT_STATE:
                scheduler = new TournamentStateScheduler<State>();
                break;

            case BIGRAPH_EDGE:
                scheduler = new BiGraphUndirEdgeScheduler<State>();
                break;

            case BIGRAPH_NODE:
                scheduler = new BiGraphUndirNodeScheduler<State>();
                break;

            case BIGRAPH_STATE:
                scheduler = new BiGraphUndirStateScheduler<State>();
                break;

            case DATASET:
                scheduler = new DatasetScheduler<State>();
                break;

            case RANDOM_EDGE_FROM_DATASET:
                scheduler = new DatasetRandomGraphEdgeScheduler<State>();
                break;


            default:
                scheduler = new RandomScheduler<State>();


        }

        return scheduler;
    }

}
