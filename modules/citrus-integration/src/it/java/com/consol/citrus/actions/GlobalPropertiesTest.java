package com.consol.citrus.actions;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.consol.citrus.testng.AbstractTestNGCitrusTest;

/**
 * 
 * @author deppisch Christoph Deppisch Consol* Software GmbH
 * @since 31.10.2008
 */
public class GlobalPropertiesTest extends AbstractTestNGCitrusTest {
    @Test
    public void globalPropertiesTest(ITestContext testContext) {
        executeTest(testContext);
    }
}