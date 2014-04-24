package org.netcs.config;

import junit.framework.TestCase;

/**
 * Created by amaxilatis on 4/19/14.
 */
public class ConfigurationParserTest extends TestCase {
    public void testParseConfigFile() throws Exception {

        ConfigurationParser.parseConfigFile("test.prop");
    }
}
