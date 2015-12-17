package org.netcs.component;

/**
 * Created by amaxilatis on 17/12/2015.
 */
public class CNode {
    private final String name;
    private final String state;

    public CNode(final String name, final String state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }
}
