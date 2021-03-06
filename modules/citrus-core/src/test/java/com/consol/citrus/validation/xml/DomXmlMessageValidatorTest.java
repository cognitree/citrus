/*
 * Copyright 2006-2010 the original author or authors.
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

package com.consol.citrus.validation.xml;

import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.exceptions.ValidationException;
import com.consol.citrus.message.DefaultMessage;
import com.consol.citrus.message.Message;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import com.consol.citrus.xml.XsdSchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * @author Christoph Deppisch
 */
public class DomXmlMessageValidatorTest extends AbstractTestNGUnitTest {
    
    @Autowired
    @Qualifier("defaultXmlMessageValidator")
    private DomXmlMessageValidator validator;
    
    @Test
    public void validateXMLSchema() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/test'>"
                        + "<correlationId>Kx1R123456789</correlationId>"
                        + "<bookingId>Bx1G987654321</bookingId>"
                        + "<test>Hello TestFramework</test>"
                    + "</message>");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        
        XsdSchemaRepository schemaRepository = new XsdSchemaRepository();
        Resource schemaResource = new ClassPathResource("com/consol/citrus/validation/test.xsd");
        SimpleXsdSchema schema = new SimpleXsdSchema(schemaResource);
        schema.afterPropertiesSet();
        
        schemaRepository.getSchemas().add(schema);
        
        validator.addSchemaRepository(schemaRepository);
        
        validator.validateXMLSchema(message, new XmlMessageValidationContext());
    }
    
    @Test
    public void validateWithExplicitXMLSchema() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/test'>"
                        + "<correlationId>Kx1R123456789</correlationId>"
                        + "<bookingId>Bx1G987654321</bookingId>"
                        + "<test>Hello TestFramework</test>"
                    + "</message>");
        
        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();
        validationContext.setSchema("testSchema2"); // defined as bean in application context
        validator.validateXMLSchema(message, validationContext);
    }
    
    @Test
    public void validateWithExplicitSpringSchemaRepository() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/test'>"
                        + "<correlationId>Kx1R123456789</correlationId>"
                        + "<bookingId>Bx1G987654321</bookingId>"
                        + "<test>Hello TestFramework</test>"
                    + "</message>");
        
        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();
        validationContext.setSchemaRepository("testSchemaRepository1"); // defined as bean in application context
        validator.validateXMLSchema(message, validationContext);
    }
    
    @Test
    public void validateWithExplicitCitrusSchemaRepository() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/test'>"
                        + "<correlationId>Kx1R123456789</correlationId>"
                        + "<bookingId>Bx1G987654321</bookingId>"
                        + "<test>Hello TestFramework</test>"
                    + "</message>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();
        validationContext.setSchemaRepository("testSchemaRepository2"); // defined as bean in application context
        validator.validateXMLSchema(message, validationContext);
    }

    @Test
    public void validateWithDefaultSchemaRepository() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/test'>"
                        + "<correlationId>Kx1R123456789</correlationId>"
                        + "<bookingId>Bx1G987654321</bookingId>"
                        + "<test>Hello TestFramework</test>"
                    + "</message>");
        
        validator.validateXMLSchema(message, new XmlMessageValidationContext());
    }
    
    @Test
    public void validateNoDefaultSchemaRepository() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/test'>"
                        + "<correlationId>Kx1R123456789</correlationId>"
                        + "<bookingId>Bx1G987654321</bookingId>"
                        + "<test>Hello TestFramework</test>"
                    + "</message>");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        
        XsdSchemaRepository schemaRepository = new XsdSchemaRepository();
        schemaRepository.setBeanName("schemaRepository1");
        Resource schemaResource = new ClassPathResource("com/consol/citrus/validation/test.xsd");
        SimpleXsdSchema schema = new SimpleXsdSchema(schemaResource);
        schema.afterPropertiesSet();
        
        schemaRepository.getSchemas().add(schema);
        
        validator.addSchemaRepository(schemaRepository);
        
        XsdSchemaRepository schemaRepository2 = new XsdSchemaRepository();
        schemaRepository2.setBeanName("schemaRepository2");
        Resource schemaResource2 = new ClassPathResource("com/consol/citrus/validation/sample.xsd");
        SimpleXsdSchema schema2 = new SimpleXsdSchema(schemaResource2);
        schema2.afterPropertiesSet();

        schemaRepository2.getSchemas().add(schema2);

        validator.addSchemaRepository(schemaRepository2);
        
        validator.validateXMLSchema(message, new XmlMessageValidationContext());

        message = new DefaultMessage("<message xmlns='http://citrusframework.org/sample'>"
                + "<correlationId>Kx1R123456789</correlationId>"
                + "<bookingId>Bx1G987654321</bookingId>"
                + "<test>Hello TestFramework</test>"
                + "</message>");

        validator.validateXMLSchema(message, new XmlMessageValidationContext());
    }

    @Test
    public void validateNoMatchingSchemaRepository() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/special'>"
                + "<correlationId>Kx1R123456789</correlationId>"
                + "<bookingId>Bx1G987654321</bookingId>"
                + "<test>Hello TestFramework</test>"
                + "</message>");

        DomXmlMessageValidator validator = new DomXmlMessageValidator();

        XsdSchemaRepository schemaRepository = new XsdSchemaRepository();
        schemaRepository.setBeanName("schemaRepository1");
        Resource schemaResource = new ClassPathResource("com/consol/citrus/validation/test.xsd");
        SimpleXsdSchema schema = new SimpleXsdSchema(schemaResource);
        schema.afterPropertiesSet();

        schemaRepository.getSchemas().add(schema);

        validator.addSchemaRepository(schemaRepository);

        XsdSchemaRepository schemaRepository2 = new XsdSchemaRepository();
        schemaRepository2.setBeanName("schemaRepository2");
        Resource schemaResource2 = new ClassPathResource("com/consol/citrus/validation/sample.xsd");
        SimpleXsdSchema schema2 = new SimpleXsdSchema(schemaResource2);
        schema2.afterPropertiesSet();

        schemaRepository2.getSchemas().add(schema2);

        validator.addSchemaRepository(schemaRepository2);

        try {
            validator.validateXMLSchema(message, new XmlMessageValidationContext());
            Assert.fail("Missing exception due to no matching schema repository error");
        } catch (CitrusRuntimeException e) {
            Assert.assertTrue(e.getMessage().startsWith("Failed to find proper schema repository"), e.getMessage());
        }
    }

    @Test
    public void validateNoMatchingSchema() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/special'>"
                + "<correlationId>Kx1R123456789</correlationId>"
                + "<bookingId>Bx1G987654321</bookingId>"
                + "<test>Hello TestFramework</test>"
                + "</message>");

        DomXmlMessageValidator validator = new DomXmlMessageValidator();

        XsdSchemaRepository schemaRepository = new XsdSchemaRepository();
        schemaRepository.setBeanName("schemaRepository");
        Resource schemaResource = new ClassPathResource("com/consol/citrus/validation/test.xsd");
        SimpleXsdSchema schema = new SimpleXsdSchema(schemaResource);
        schema.afterPropertiesSet();
        Resource schemaResource2 = new ClassPathResource("com/consol/citrus/validation/sample.xsd");
        SimpleXsdSchema schema2 = new SimpleXsdSchema(schemaResource2);
        schema2.afterPropertiesSet();

        schemaRepository.getSchemas().add(schema);
        schemaRepository.getSchemas().add(schema2);

        validator.addSchemaRepository(schemaRepository);

        try {
            validator.validateXMLSchema(message, new XmlMessageValidationContext());
            Assert.fail("Missing exception due to no matching schema repository error");
        } catch (CitrusRuntimeException e) {
            Assert.assertTrue(e.getMessage().startsWith("Unable to find proper XML schema definition"), e.getMessage());
        }
    }

    @Test
    public void validateNoSchemaRepositoryAtAll() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/test'>"
                + "<correlationId>Kx1R123456789</correlationId>"
                + "<bookingId>Bx1G987654321</bookingId>"
                + "<test>Hello TestFramework</test>"
                + "</message>");

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateXMLSchema(message, new XmlMessageValidationContext());
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void validateXMLSchemaError() throws SAXException, IOException, ParserConfigurationException {
        Message message = new DefaultMessage("<message xmlns='http://citrusframework.org/test'>"
                        + "<correlationId>Kx1R123456789</correlationId>"
                        + "<bookingId>Bx1G987654321</bookingId>"
                        + "<test>Hello TestFramework</test>"
                        + "<wrongElement>totally wrong</wrongElement>"
                    + "</message>");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        
        XsdSchemaRepository schemaRepository = new XsdSchemaRepository();
        Resource schemaResource = new ClassPathResource("com/consol/citrus/validation/test.xsd");
        SimpleXsdSchema schema = new SimpleXsdSchema(schemaResource);
        schema.afterPropertiesSet();
        
        schemaRepository.getSchemas().add(schema);
        
        validator.addSchemaRepository(schemaRepository);
        
        validator.validateXMLSchema(message, new XmlMessageValidationContext());
    }
    
    @Test
    public void testExpectDefaultNamespace() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/test'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/test");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test
    public void testExpectNamespace() {
    	Message message = new DefaultMessage("<ns1:root xmlns:ns1='http://citrusframework.org/ns1'>"
                        + "<ns1:element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<ns1:sub-element attribute='A'>text-value</ns1:sub-element>"
                        + "</ns1:element>" 
                    + "</ns1:root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test
    public void testExpectMixedNamespaces() {
    	Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/default");
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test
    public void testExpectMultipleNamespaces() {
    	Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/default");
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1");
        expectedNamespaces.put("ns2", "http://citrusframework.org/ns2");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testExpectDefaultNamespaceError() {
    	Message message = new DefaultMessage("<root xmlns='http://citrusframework.org'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/wrong");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testExpectNamespaceError() {
    	Message message = new DefaultMessage("<ns1:root xmlns:ns1='http://citrusframework.org/ns1'>"
                        + "<ns1:element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<ns1:sub-element attribute='A'>text-value</ns1:sub-element>"
                        + "</ns1:element>" 
                    + "</ns1:root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1/wrong");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testExpectMixedNamespacesError() {
    	Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/default/wrong");
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testExpectMultipleNamespacesError() {
    	Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/default");
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1/wrong");
        expectedNamespaces.put("ns2", "http://citrusframework.org/ns2");
     
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testExpectWrongNamespacePrefix() {
    	Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/default");
        expectedNamespaces.put("nswrong", "http://citrusframework.org/ns1");
        expectedNamespaces.put("ns2", "http://citrusframework.org/ns2");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testExpectDefaultNamespaceButNamespace() {
    	Message message = new DefaultMessage("<ns0:root xmlns:ns0='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2'>"
                        + "<ns0:element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<ns0:sub-element attribute='A'>text-value</ns0:sub-element>"
                        + "</ns0:element>" 
                    + "</ns0:root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/default");
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1");
        expectedNamespaces.put("ns2", "http://citrusframework.org/ns2");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testExpectNamespaceButDefaultNamespace() {
    	Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("ns0", "http://citrusframework.org/default");
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1");
        expectedNamespaces.put("ns2", "http://citrusframework.org/ns2");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testExpectAdditionalNamespace() {
    	Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/default");
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1");
        expectedNamespaces.put("ns2", "http://citrusframework.org/ns2");
        expectedNamespaces.put("ns4", "http://citrusframework.org/ns4");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testExpectNamespaceButNamespaceMissing() {
    	Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:ns4='http://citrusframework.org/ns4'>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>" 
                    + "</root>");
        
        Map<String, String> expectedNamespaces = new HashMap<String, String>();
        expectedNamespaces.put("", "http://citrusframework.org/default");
        expectedNamespaces.put("ns1", "http://citrusframework.org/ns1");
        expectedNamespaces.put("ns2", "http://citrusframework.org/ns2");
        
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateNamespaces(expectedNamespaces, message);
    }
    
    @Test
    public void testValidateMessagePayloadSuccess() {
        Message message = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>"
                    + "</root>");

        Message controlMessage = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='A'>text-value</sub-element>"
                        + "</element>"
                    + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }
    
    @Test
    public void testValidateMessagePayloadWithIgnoresSuccess() {
        Message message = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element1 attribute='A'>THIS_IS_IGNORED_BY_XPATH</sub-element1>"
                        + "<sub-element2 attribute='A'>THIS IS IGNORED BY IGNORE-EXPR</sub-element2>"
                        + "<sub-element3 attribute='A'>a text</sub-element3>"
                        + "</element>"
                    + "</root>");

        Message controlMessage = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element1 attribute='A'>text-value</sub-element1>"
                        + "<sub-element2 attribute='A'>@ignore@</sub-element2>"
                        + "<sub-element3 attribute='A'>a text</sub-element3>"
                        + "</element>"
                    + "</root>");


        Set<String> ignoreExpressions = new HashSet<String>();
        ignoreExpressions.add("//root/element/sub-element1");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();
        validationContext.setIgnoreExpressions(ignoreExpressions);
        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testValidateMessagePayloadWithValidationMatchersFailsBecauseOfAttribute() {
        Message message = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='text-attribute'>text-element</sub-element>"
                        + "</element>"
                    + "</root>");

        Message controlMessage = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='@startsWith(FAIL)@'>@startsWith(text)@</sub-element>"
                        + "</element>"
                    + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testValidateMessagePayloadWithValidationMatcherOnElementFails() {
        Message message = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='text-attribute'>text-element</sub-element>"
                        + "</element>"
                    + "</root>");

        Message controlMessage = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='text-attribute'>@startsWith(FAIL)@</sub-element>"
                        + "</element>"
                    + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }
    
    @Test(expectedExceptions = {ValidationException.class})
    public void testValidateMessagePayloadWithValidationMatcherOnAttributeFails() {
        Message message = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='text-attribute'>text-element</sub-element>"
                        + "</element>"
                    + "</root>");

        Message controlMessage = new DefaultMessage("<root>"
                        + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                        + "<sub-element attribute='@startsWith(FAIL)@'>text-element</sub-element>"
                        + "</element>"
                    + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

    @Test
    public void testNamespaceQualifiedAttributeValue() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                        + "<element xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                        + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                        + "</element>"
                    + "</root>");

        Message controlMessage = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                        + "<element xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                        + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                        + "</element>"
                    + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

    @Test
    public void testNamespaceQualifiedAttributeValueParentDeclaration() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xmlns:ns1='http://citrusframework.org/ns1' xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        Message controlMessage = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

    @Test
    public void testNamespaceQualifiedAttributeValueParentDeclarationInSource() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xmlns:ns1='http://citrusframework.org/ns1' xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        Message controlMessage = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xmlns:ns1='http://citrusframework.org/ns1' xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

    @Test
    public void testNamespaceQualifiedAttributeValueDifferentPrefix() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        Message controlMessage = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:cit='http://citrusframework.org/ns1' xmlns:cit2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='cit:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='cit2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

    @Test
    public void testNamespaceQualifiedLikeAttributeValues() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element credentials='username:password' attributeB='attribute-value'>"
                + "<sub-element>text-value</sub-element>"
                + "</element>"
                + "</root>");

        Message controlMessage = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element credentials='username:password' attributeB='attribute-value'>"
                + "<sub-element>text-value</sub-element>"
                + "</element>"
                + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

    @Test(expectedExceptions = {ValidationException.class})
    public void testNamespaceQualifiedAttributeValueFails() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        Message controlMessage = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='ns1:wrong-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

    @Test(expectedExceptions = {ValidationException.class})
    public void testNamespaceQualifiedAttributeValueUriMismatch() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        Message controlMessage = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:cit='http://citrusframework.org/cit' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='cit:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

    @Test(expectedExceptions = {ValidationException.class})
    public void testNamespaceQualifiedAttributeMissingPrefix() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        Message controlMessage = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/cit' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

    @Test(expectedExceptions = {ValidationException.class})
    public void testNamespaceQualifiedAttributeValueMissingDeclaration() {
        Message message = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns1='http://citrusframework.org/ns1' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='ns1:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        Message controlMessage = new DefaultMessage("<root xmlns='http://citrusframework.org/default' xmlns:ns2='http://citrusframework.org/ns2' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<element xsi:type='cit:attribute-value' attributeB='attribute-value'>"
                + "<sub-element xsi:type='ns2:AType'>text-value</sub-element>"
                + "</element>"
                + "</root>");

        XmlMessageValidationContext validationContext = new XmlMessageValidationContext();

        DomXmlMessageValidator validator = new DomXmlMessageValidator();
        validator.validateMessage(message, controlMessage, context, validationContext);
    }

}
