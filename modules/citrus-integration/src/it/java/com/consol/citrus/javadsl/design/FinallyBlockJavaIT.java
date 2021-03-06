/*
 * Copyright 2006-2013 the original author or authors.
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

package com.consol.citrus.javadsl.design;

import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import javax.sql.DataSource;

/**
 * @author Christoph Deppisch
 */
@Test
public class FinallyBlockJavaIT extends TestNGCitrusTestDesigner {
    
    @Autowired
    @Qualifier("testDataSource")
    private DataSource dataSource;
    
    @CitrusTest
    public void finallyBlock() {
        variable("orderId", "citrus:randomNumber(5)");

        sql(dataSource)
            .statement("INSERT INTO ORDERS (ORDER_ID, REQUEST_TAG, CONVERSATION_ID, CREATION_DATE) VALUES (${orderId},1,1,'citrus:currentDate(dd.MM.yyyy)')");
        
        echo("ORDER creation time: citrus:currentDate('dd.MM.yyyy')");
        
        doFinally(
                sql(dataSource).statement("DELETE FROM ORDERS WHERE ORDER_ID='${orderId}'")
        );
    }
}