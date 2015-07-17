/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.dsl.runner;

import com.consol.citrus.TestCase;
import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.container.Iterate;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.builder.IterateBuilder;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class IterateTestRunnerTest extends AbstractTestNGUnitTest {
    @Test
    public void testIterateBuilder() {      
        MockTestRunner builder = new MockTestRunner(getClass().getSimpleName(), applicationContext) {
            @Override
            public void execute() {
                iterate(new TestActionConfigurer<IterateBuilder>() {
                    @Override
                    public void configure(IterateBuilder builder) {
                        builder.index("i")
                                .startsWith(0)
                                .step(1)
                                .condition("i lt 5");
                    }
                }).actions(createVariable("index", "${i}"));
            }
        };

        TestCase test = builder.getTestCase();
        assertEquals(test.getActionCount(), 1);
        assertEquals(test.getActions().get(0).getClass(), Iterate.class);
        assertEquals(test.getActions().get(0).getName(), "iterate");
        
        Iterate container = (Iterate)test.getActions().get(0);
        assertEquals(container.getActionCount(), 1);
        assertEquals(container.getIndexName(), "i");
        assertEquals(container.getCondition(), "i lt 5");
        assertEquals(container.getStep(), 1);
        assertEquals(container.getStart(), 0);
    }

    @Test
    public void testIterateBuilderWithAnonymousAction() {
        MockTestRunner builder = new MockTestRunner(getClass().getSimpleName(), applicationContext) {
            @Override
            public void execute() {
                iterate(new TestActionConfigurer<IterateBuilder>() {
                    @Override
                    public void configure(IterateBuilder builder) {
                        builder.index("i")
                                .startsWith(1)
                                .step(1)
                                .condition("i lt 5");
                    }
                }).actions(createVariable("index", "${i}"), new AbstractTestAction() {
                    @Override
                    public void doExecute(TestContext context) {
                        Assert.assertTrue(Integer.valueOf(context.getVariable("index")) > 0);
                    }
                });
            }
        };

        TestCase test = builder.getTestCase();
        assertEquals(test.getActionCount(), 1);
        assertEquals(test.getActions().get(0).getClass(), Iterate.class);
        assertEquals(test.getActions().get(0).getName(), "iterate");

        Iterate container = (Iterate)test.getActions().get(0);
        assertEquals(container.getActionCount(), 2);
        assertEquals(container.getIndexName(), "i");
        assertEquals(container.getCondition(), "i lt 5");
        assertEquals(container.getStep(), 1);
        assertEquals(container.getStart(), 1);
    }
}