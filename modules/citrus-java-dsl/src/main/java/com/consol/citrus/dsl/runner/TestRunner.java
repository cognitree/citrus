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

import com.consol.citrus.TestAction;
import com.consol.citrus.actions.*;
import com.consol.citrus.container.Template;
import com.consol.citrus.dsl.builder.*;
import com.consol.citrus.jms.actions.PurgeJmsQueuesAction;
import com.consol.citrus.script.GroovyAction;
import com.consol.citrus.server.Server;
import com.consol.citrus.ws.actions.SendSoapFaultAction;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Christoph Deppisch
 * @since 2.2.1
 */
public interface TestRunner extends ApplicationContextAware {

    /**
     * Set custom test case name.
     * @param name
     */
    void name(String name);

    /**
     * Sets custom package name for this test case.
     * @param packageName
     */
    void packageName(String packageName);

    /**
     * Starts the test case execution.
     */
    void start();

    /**
     * Stops test case execution.
     */
    void stop();

    /**
     * Adds a new variable definition to the set of test variables
     * for this test case and return its value.
     *
     * @param name
     * @param value
     * @return
     */
    <T> T variable(String name, T value);

    /**
     * Runs test action and returns same action after execution.
     * @param testAction
     * @return
     */
    <T extends TestAction> T run(T testAction);

    /**
     * Apply test apply with all test actions, finally actions and test
     * variables defined in given apply.
     *
     * @param behavior
     */
    void applyBehavior(TestBehavior behavior);

    /**
     * Action creating a new test variable during a test.
     *
     * @param variableName
     * @param value
     * @return
     */
    CreateVariablesAction createVariable(String variableName, String value);

    /**
     * Creates and executes a new ANT run action definition
     * for further configuration.
     * @param configurer
     * @return
     */
    AntRunAction antrun(TestActionConfigurer<AntRunBuilder> configurer);

    /**
     * Creates and executes a new echo action.
     * @param message
     * @return
     */
    EchoAction echo(String message);

    /**
     * Creates a new executePLSQL action definition
     * for further configuration.
     *
     * @param configurer
     * @return
     */
    ExecutePLSQLAction plsql(TestActionConfigurer<ExecutePLSQLBuilder> configurer);

    /**
     * Creates a new executeSQL action definition
     * for further configuration.
     *
     * @param configurer
     * @return
     */
    ExecuteSQLAction sql(TestActionConfigurer<ExecuteSQLBuilder> configurer);

    /**
     * Creates a new executesqlquery action definition
     * for further configuration.
     *
     * @param configurer
     * @return
     */
    ExecuteSQLQueryAction query(TestActionConfigurer<ExecuteSQLQueryBuilder> configurer);

    /**
     * Creates a new receive timeout action definition
     * for further configuration.
     *
     * @param configurer
     * @return
     */
    ReceiveTimeoutAction receiveTimeout(TestActionConfigurer<ReceiveTimeoutBuilder> configurer);

    /**
     * Creates a new fail action.
     *
     * @param message
     * @return
     */
    FailAction fail(String message);

    /**
     * Creates a new input action.
     *
     * @param configurer
     * @return
     */
    InputAction input(TestActionConfigurer<InputActionBuilder> configurer);

    /**
     * Creates a new load properties action.
     * @param filePath path to properties file.
     * @return
     */
    LoadPropertiesAction load(String filePath);

    /**
     * Creates a new purge jms queues action definition
     * for further configuration.
     *
     * @param configurer
     * @return
     */
    PurgeJmsQueuesAction purgeQueues(TestActionConfigurer<PurgeJmsQueuesBuilder> configurer);

    /**
     * Creates a new purge message channel action definition
     * for further configuration.
     *
     * @param configurer
     * @return
     */
    PurgeMessageChannelAction purgeChannels(TestActionConfigurer<PurgeChannelsBuilder> configurer);

    /**
     * Creates receive message action definition with message endpoint instance.
     *
     * @param configurer
     * @return
     */
    ReceiveMessageAction receive(TestActionConfigurer<ReceiveMessageBuilder> configurer);

    /**
     * Create send message action definition with message endpoint instance.
     *
     * @param configurer
     * @return
     */
    SendMessageAction send(TestActionConfigurer<SendMessageBuilder> configurer);

    /**
     * Create SOAP fault send message action definition with message endpoint instance. Returns SOAP fault definition with
     * specific properties for SOAP fault messages.
     *
     * @param configurer
     * @return
     */
    SendSoapFaultAction sendSoapFault(TestActionConfigurer<SendSoapFaultBuilder> configurer);

    /**
     * Add sleep action with default delay time.
     * @return
     */
    SleepAction sleep();

    /**
     * Add sleep action with time in milliseconds.
     *
     * @param milliseconds
     * @return
     */
    SleepAction sleep(long milliseconds);

    /**
     * Creates a new start server action definition
     * for further configuration.
     *
     * @param servers
     * @return
     */
    StartServerAction start(Server... servers);

    /**
     * Creates a new start server action definition
     * for further configuration.
     *
     * @param server
     * @return
     */
    StartServerAction start(Server server);

    /**
     * Creates a new stop server action definition
     * for further configuration.
     *
     * @param servers
     * @return
     */
    StopServerAction stop(Server... servers);

    /**
     * Creates a new stop server action definition
     * for further configuration.
     *
     * @param server
     * @return
     */
    StopServerAction stop(Server server);

    /**
     * Creates a new stop time action.
     * @return
     */
    StopTimeAction stopTime();

    /**
     * Creates a new stop time action.
     *
     * @param id
     * @return
     */
    StopTimeAction stopTime(String id);

    /**
     * Creates a new trace variables action definition
     * that prints variable values to the console/logger.
     *
     * @return
     */
    TraceVariablesAction traceVariables();

    /**
     * Creates a new trace variables action definition
     * that prints variable values to the console/logger.
     *
     * @param variables
     * @return
     */
    TraceVariablesAction traceVariables(String... variables);

    /**
     * Creates a new groovy action definition
     * for further configuration.
     *
     * @param configurer
     * @return
     */
    GroovyAction groovy(TestActionConfigurer<GroovyActionBuilder> configurer);

    /**
     * Creates a new transform action definition
     * for further configuration.
     *
     * @param configurer
     * @return
     */
    TransformAction transform(TestActionConfigurer<TransformActionBuilder> configurer);

    /**
     * Assert default exception to happen in nested test action.
     *
     * @return
     */
    ExceptionContainerRunner assertException();

    /**
     * Assert exception to happen in nested test action.
     *
     * @param configurer
     * @return
     */
    ExceptionContainerRunner assertException(TestActionConfigurer<AssertExceptionBuilder> configurer);

    /**
     * Catch default exception when thrown in nested test action.
     * @return
     */
    ExceptionContainerRunner catchException();

    /**
     * Catch exception when thrown in nested test action.
     *
     * @param configurer
     * @return
     */
    ExceptionContainerRunner catchException(TestActionConfigurer<CatchExceptionBuilder> configurer);

    /**
     * Assert SOAP fault during action execution.
     *
     * @param configurer
     * @return
     */
    ExceptionContainerRunner assertSoapFault(TestActionConfigurer<AssertSoapFaultBuilder> configurer);

    /**
     * Adds conditional container with nested test actions.
     *
     * @param configurer
     * @return
     */
    ContainerRunner conditional(TestActionConfigurer<ConditionalBuilder> configurer);

    /**
     * Run nested test actions in iteration.
     * @param configurer
     * @return
     */
    ContainerRunner iterate(TestActionConfigurer<IterateBuilder> configurer);

    /**
     * Run nested test actions in parallel to each other using multiple threads.
     * @return
     */
    ContainerRunner parallel();

    /**
     * Adds repeat on error until true container with nested test actions.
     *
     * @param configurer
     * @return
     */
    ContainerRunner repeatOnError(TestActionConfigurer<RepeatOnErrorBuilder> configurer);

    /**
     * Adds repeat until true container with nested test actions.
     *
     * @param configurer
     * @return
     */
    ContainerRunner repeat(TestActionConfigurer<RepeatBuilder> configurer);

    /**
     * Run nested test actions in sequence.
     * @return
     */
    ContainerRunner sequential();

    /**
     * Adds template container with nested test actions.
     *
     * @param configurer
     * @return
     */
    Template applyTemplate(TestActionConfigurer<TemplateBuilder> configurer);

    /**
     * Adds sequence of test actions to finally block.
     * @return
     */
    ContainerRunner doFinally();

    /**
     * Add test parameters to the test.
     * @param parameterNames
     * @param parameterValues
     */
    void parameter(String[] parameterNames, Object[] parameterValues);
}