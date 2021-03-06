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

package com.consol.citrus.dsl.junit;

import com.consol.citrus.Citrus;
import com.consol.citrus.TestAction;
import com.consol.citrus.TestCase;
import com.consol.citrus.TestCaseMetaInfo;
import com.consol.citrus.actions.*;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.builder.*;
import com.consol.citrus.dsl.design.DefaultTestDesigner;
import com.consol.citrus.dsl.design.TestBehavior;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.dsl.util.PositionHandle;
import com.consol.citrus.endpoint.Endpoint;
import com.consol.citrus.junit.AbstractJUnit4CitrusTest;
import com.consol.citrus.junit.CitrusJUnit4Runner;
import com.consol.citrus.server.Server;
import com.consol.citrus.ws.client.WebServiceClient;
import com.consol.citrus.ws.server.WebServiceServer;
import org.springframework.core.io.Resource;
import org.springframework.util.ReflectionUtils;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import java.util.Date;
import java.util.Map;

/**
 * JUnit Citrus test provides Java DSL access to builder pattern methods in
 * CitrusTestDesigner by simple method delegation.
 *
 * @author Christoph Deppisch
 * @since 2.3
 */
public class JUnit4CitrusTestDesigner extends AbstractJUnit4CitrusTest implements TestDesigner {

    /** Test builder delegate */
    private DefaultTestDesigner testDesigner;

    /**
     * Initialize test case and variables. Must be done with each test run.
     */
    public void init() {
        testDesigner = new DefaultTestDesigner(applicationContext);
        name(this.getClass().getSimpleName());
        packageName(this.getClass().getPackage().getName());
    }

    @Override
    protected void run(CitrusJUnit4Runner.CitrusFrameworkMethod frameworkMethod) {
        if (frameworkMethod.getMethod().getAnnotation(CitrusTest.class) != null) {
            init();
            name(frameworkMethod.getTestName());
            packageName(frameworkMethod.getPackageName());

            ReflectionUtils.invokeMethod(frameworkMethod.getMethod(), this);

            if (citrus == null) {
                citrus = Citrus.newInstance(applicationContext);
            }

            TestContext ctx = prepareTestContext(citrus.createTestContext());
            TestCase testCase = testDesigner.getTestCase();
            citrus.run(testCase, ctx);
        } else {
            super.run(frameworkMethod);
        }
    }

    @Override
    protected void executeTest() {
        init();
        configure();
        super.executeTest();
    }

    /**
     * Main entrance method for builder pattern usage. Subclasses may override
     * this method and call Java DSL builder methods for adding test actions and
     * basic test case properties.
     */
    protected void configure() {
    }

    @Override
    public TestCase getTestCase() {
        return testDesigner.getTestCase();
    }

    @Override
    public void name(String name) {
        testDesigner.name(name);
    }

    @Override
    public void description(String description) {
        testDesigner.description(description);
    }

    @Override
    public void author(String author) {
        testDesigner.author(author);
    }

    @Override
    public void packageName(String packageName) {
        testDesigner.packageName(packageName);
    }

    @Override
    public void status(TestCaseMetaInfo.Status status) {
        testDesigner.status(status);
    }

    @Override
    public void creationDate(Date date) {
        testDesigner.creationDate(date);
    }

    @Override
    public void variable(String name, Object value) {
        testDesigner.variable(name, value);
    }

    @Override
    public CreateVariablesAction createVariable(String variableName, String value) {
        return testDesigner.createVariable(variableName, value);
    }

    @Override
    public void action(TestAction testAction) {
        testDesigner.action(testAction);
    }

    @Override
    public void applyBehavior(TestBehavior behavior) {
        testDesigner.applyBehavior(behavior);
    }

    @Override
    public AntRunBuilder antrun(String buildFilePath) {
        return testDesigner.antrun(buildFilePath);
    }

    @Override
    public EchoAction echo(String message) {
        return testDesigner.echo(message);
    }

    @Override
    public ExecutePLSQLBuilder plsql(DataSource dataSource) {
        return testDesigner.plsql(dataSource);
    }

    @Override
    public ExecuteSQLBuilder sql(DataSource dataSource) {
        return testDesigner.sql(dataSource);
    }

    @Override
    public ExecuteSQLQueryBuilder query(DataSource dataSource) {
        return testDesigner.query(dataSource);
    }

    @Override
    public ReceiveTimeoutBuilder receiveTimeout(Endpoint messageEndpoint) {
        return testDesigner.receiveTimeout(messageEndpoint);
    }

    @Override
    public ReceiveTimeoutBuilder receiveTimeout(String messageEndpointName) {
        return testDesigner.receiveTimeout(messageEndpointName);
    }

    @Override
    public FailAction fail(String message) {
        return testDesigner.fail(message);
    }

    @Override
    public InputActionBuilder input() {
        return testDesigner.input();
    }

    @Override
    public JavaActionBuilder java(String className) {
        return testDesigner.java(className);
    }

    @Override
    public JavaActionBuilder java(Class<?> clazz) {
        return testDesigner.java(clazz);
    }

    @Override
    public JavaActionBuilder java(Object instance) {
        return testDesigner.java(instance);
    }

    @Override
    public LoadPropertiesAction load(String filePath) {
        return testDesigner.load(filePath);
    }

    @Override
    public PurgeJmsQueuesBuilder purgeQueues(ConnectionFactory connectionFactory) {
        return testDesigner.purgeQueues(connectionFactory);
    }

    @Override
    public PurgeJmsQueuesBuilder purgeQueues() {
        return testDesigner.purgeQueues();
    }

    @Override
    public PurgeChannelsBuilder purgeChannels() {
        return testDesigner.purgeChannels();
    }

    @Override
    public PurgeEndpointsBuilder purgeEndpoints() {
        return testDesigner.purgeEndpoints();
    }

    @Override
    public ReceiveSoapMessageBuilder receive(WebServiceServer server) {
        return testDesigner.receive(server);
    }

    @Override
    public ReceiveMessageBuilder receive(Endpoint messageEndpoint) {
        return testDesigner.receive(messageEndpoint);
    }

    @Override
    public ReceiveMessageBuilder receive(String messageEndpointName) {
        return testDesigner.receive(messageEndpointName);
    }

    @Override
    public SendSoapMessageBuilder send(WebServiceClient client) {
        return testDesigner.send(client);
    }

    @Override
    public SendMessageBuilder send(Endpoint messageEndpoint) {
        return testDesigner.send(messageEndpoint);
    }

    @Override
    public SendMessageBuilder send(String messageEndpointName) {
        return testDesigner.send(messageEndpointName);
    }

    @Override
    public SendSoapFaultBuilder sendSoapFault(String messageEndpointName) {
        return testDesigner.sendSoapFault(messageEndpointName);
    }

    @Override
    public SendSoapFaultBuilder sendSoapFault(Endpoint messageEndpoint) {
        return testDesigner.sendSoapFault(messageEndpoint);
    }

    @Override
    public SleepAction sleep() {
        return testDesigner.sleep();
    }

    @Override
    public SleepAction sleep(long milliseconds) {
        return testDesigner.sleep(milliseconds);
    }

    @Override
    public SleepAction sleep(double seconds) {
        return testDesigner.sleep(seconds);
    }

    @Override
    public WaitActionBuilder waitFor() {
        return testDesigner.waitFor();
    }

    @Override
    public StartServerAction start(Server... servers) {
        return testDesigner.start(servers);
    }

    @Override
    public StartServerAction start(Server server) {
        return testDesigner.start(server);
    }

    @Override
    public StopServerAction stop(Server... servers) {
        return testDesigner.stop(servers);
    }

    @Override
    public StopServerAction stop(Server server) {
        return testDesigner.stop(server);
    }

    @Override
    public StopTimeAction stopTime() {
        return testDesigner.stopTime();
    }

    @Override
    public StopTimeAction stopTime(String id) {
        return testDesigner.stopTime(id);
    }

    @Override
    public TraceVariablesAction traceVariables() {
        return testDesigner.traceVariables();
    }

    @Override
    public TraceVariablesAction traceVariables(String... variables) {
        return testDesigner.traceVariables(variables);
    }

    @Override
    public GroovyActionBuilder groovy(String script) {
        return testDesigner.groovy(script);
    }

    @Override
    public GroovyActionBuilder groovy(Resource scriptResource) {
        return testDesigner.groovy(scriptResource);
    }

    @Override
    public TransformActionBuilder transform() {
        return testDesigner.transform();
    }

    @Override
    public AssertExceptionBuilder assertException(TestAction testAction) {
        return testDesigner.assertException(testAction);
    }

    @Override
    public AssertExceptionBuilder assertException() {
        return testDesigner.assertException();
    }

    @Override
    public CatchExceptionBuilder catchException(TestAction ... actions) {
        return testDesigner.catchException(actions);
    }

    @Override
    public CatchExceptionBuilder catchException() {
        return testDesigner.catchException();
    }

    @Override
    public AssertSoapFaultBuilder assertSoapFault(TestAction testAction) {
        return testDesigner.assertSoapFault(testAction);
    }

    @Override
    public AssertSoapFaultBuilder assertSoapFault() {
        return testDesigner.assertSoapFault();
    }

    @Override
    public ConditionalBuilder conditional(TestAction ... actions) {
        return testDesigner.conditional(actions);
    }

    @Override
    public ConditionalBuilder conditional() {
        return testDesigner.conditional();
    }

    @Override
    public IterateBuilder iterate(TestAction ... actions) {
        return testDesigner.iterate(actions);
    }

    @Override
    public IterateBuilder iterate() {
        return testDesigner.iterate();
    }

    @Override
    public ParallelBuilder parallel(TestAction ... actions) {
        return testDesigner.parallel(actions);
    }

    @Override
    public ParallelBuilder parallel() {
        return testDesigner.parallel();
    }

    @Override
    public RepeatOnErrorBuilder repeatOnError(TestAction ... actions) {
        return testDesigner.repeatOnError(actions);
    }

    @Override
    public RepeatOnErrorBuilder repeatOnError() {
        return testDesigner.repeatOnError();
    }

    @Override
    public RepeatBuilder repeat(TestAction ... actions) {
        return testDesigner.repeat(actions);
    }

    @Override
    public RepeatBuilder repeat() {
        return testDesigner.repeat();
    }

    @Override
    public SequenceBuilder sequential(TestAction ... actions) {
        return testDesigner.sequential(actions);
    }

    @Override
    public SequenceBuilder sequential() {
        return testDesigner.sequential();
    }

    @Override
    public DockerActionBuilder docker() {
        return testDesigner.docker();
    }

    @Override
    public HttpActionBuilder http() {
        return testDesigner.http();
    }

    @Override
    public CamelRouteActionBuilder camel() {
        return testDesigner.camel();
    }

    @Override
    public TemplateBuilder applyTemplate(String name) {
        return testDesigner.applyTemplate(name);
    }

    @Override
    public FinallySequenceBuilder doFinally(TestAction ... actions) {
        return testDesigner.doFinally(actions);
    }

    @Override
    public FinallySequenceBuilder doFinally() {
        return testDesigner.doFinally();
    }

    @Override
    public PositionHandle positionHandle() {
        return testDesigner.positionHandle();
    }

    /**
     * Get the test variables.
     * @return
     */
    protected Map<String, Object> getVariables() {
        return testDesigner.getVariables();
    }

}
