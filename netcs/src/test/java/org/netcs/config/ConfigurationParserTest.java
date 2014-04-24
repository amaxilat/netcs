package org.netcs.config;

import junit.framework.TestCase;

/**
 * Tests parsing of Config files.
 */
public class ConfigurationParserTest extends TestCase {
    public void testParseConfigFile() throws Exception {

        ConfigurationParser.parseConfigFile("test.prop");
    }
}
