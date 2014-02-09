package ppsim.scheduler;

import org.apache.log4j.Logger;
import ppsim.model.AbstractAgent;
import ppsim.model.AgentIntPair;
import ppsim.model.Population;
import ppsim.model.PopulationProtocol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Implements scheduler based on live traces.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class RealityScheduler<State> extends AbstractScheduler<State> {

    /**
     * Apache Log4J logger.
     */
    private static final Logger LOGGER = Logger.getLogger("ppsim.scheduler");

    /**
     * Pairs of interactions.
     */
    private ArrayList<AgentIntPair> history;

    /**
     * Local round counter.
     */
    private int round;


    /**
     * Connect Scheduler to specific population and protocol.
     *
     * @param pop  the population object.
     * @param prot the protocol object.
     */
    public void connect(final Population<State> pop, final PopulationProtocol<State> prot) {
        super.connect(pop, prot);

        history = new ArrayList<AgentIntPair>();
        round = 0;

        /**
         * Connection with the database containing the live traces.
         */
        Connection conn = null;
        try {
            final String userName = "realitymining";
            final String password = "rm123";
            final String url = "jdbc:mysql://localhost/realitymining";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            LOGGER.debug("Database connection established");

            // Retrieve interactions
            final Statement sment = conn.createStatement();
            sment.executeQuery("SELECT devicespan.person_oid, device.person_oid, "
                    + "devicespan.starttime, devicespan.endtime\n"
                    + "FROM `devicespan` , `device`\n"
                    + "WHERE devicespan.device_oid = device.oid\n"
                    + "AND device.person_oid <>0\n"
                    + "AND (\n"
                    + "(\n"
                    + "year( starttime ) = 2004\n"
                    + "AND month( starttime ) >= 10\n"
                    + ")\n"
                    + "OR (\n"
                    + "year( starttime ) = 2005\n"
                    + "AND month( starttime ) <= 3\n"
                    + ")\n"
                    + ")\n"
                    + "ORDER BY `devicespan`.`starttime` ASC");

            final ResultSet rset = sment.getResultSet();

            // Iterate records
            int count = 0;
            while (rset.next()) {
                // ID = 24, id = 51, id = 66, id = 86 remain disconnected                
                final int initID = rset.getInt("devicespan.person_oid") - 1;
                final int respID = rset.getInt("device.person_oid") - 1;
                final AgentIntPair aip = new AgentIntPair(initID, respID);
                history.add(aip);
                ++count;
            }
            rset.close();
            sment.close();
            LOGGER.debug(count + " rows were retrieved");

        } catch (Exception e) {
            LOGGER.error("Cannot connect to database server", e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    LOGGER.debug("Database connection terminated");
                    
                } catch (Exception e) {
                    LOGGER.debug("Error closing database connection");
                }
            }
        }
    }

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler uniformly randomly selects the initiator and responder agents from the population.
     */
    public void interact() {
        // Retrieve next interaction
        final AgentIntPair aip = history.get(round++);

        // Initialize to the same agent
        final AbstractAgent<State> initAgent = getAgent(aip.getInitiator());
        final AbstractAgent<State> respAgent = getAgent(aip.getResponder());

        try {
            // Conduct interaction for given pair of agents
            interact(initAgent, respAgent);
        } catch (Exception ex) {
            LOGGER.error(aip.getInitiator() + " vs " + aip.getResponder());
        }

        // Reset local round counter if reached limit.
        if (round >= history.size()) {
            round = 0;
        }
    }

}
