/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.proxy.init.impl;

import org.apache.shardingsphere.infra.auth.yaml.config.YamlAuthenticationConfiguration;
import org.apache.shardingsphere.infra.context.SchemaContexts;
import org.apache.shardingsphere.proxy.config.ProxyConfiguration;
import org.apache.shardingsphere.proxy.config.YamlProxyConfiguration;
import org.apache.shardingsphere.proxy.config.yaml.YamlProxyServerConfiguration;
import org.apache.shardingsphere.transaction.context.TransactionContexts;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class StandardBootstrapInitializerTest {
    
    private StandardBootstrapInitializer standardBootstrapInitializer;
    
    @Before
    public void setUp() {
        standardBootstrapInitializer = new StandardBootstrapInitializer();
    }
    
    @Test
    public void assertDecorateSchemeContextsCorrect() {
        SchemaContexts schemaContexts = mock(SchemaContexts.class);
        standardBootstrapInitializer.decorateSchemaContexts(schemaContexts);
        verify(schemaContexts, never()).getDatabaseType();
        verify(schemaContexts, never()).getSchemaContexts();
        verify(schemaContexts, never()).getDefaultSchemaContext();
        verify(schemaContexts, never()).getAuthentication();
        verify(schemaContexts, never()).getProps();
        verify(schemaContexts, never()).isCircuitBreak();
    }
    
    @Test
    public void assertDecorateTransactionContexts() {
        TransactionContexts transactionContexts = mock(TransactionContexts.class);
        standardBootstrapInitializer.decorateTransactionContexts(transactionContexts);
        verify(transactionContexts, never()).getEngines();
        verify(transactionContexts, never()).getDefaultTransactionManagerEngine();
    }
    
    @Test
    public void assertGetProxyConfiguration() {
        YamlProxyServerConfiguration serverConfiguration = mock(YamlProxyServerConfiguration.class);
        when(serverConfiguration.getAuthentication()).thenReturn(mock(YamlAuthenticationConfiguration.class));
        YamlProxyConfiguration yamlConfig = new YamlProxyConfiguration(serverConfiguration, Collections.emptyMap());
        ProxyConfiguration proxyConfiguration = standardBootstrapInitializer.getProxyConfiguration(yamlConfig);
        assertThat(proxyConfiguration.getProps(), is(yamlConfig.getServerConfiguration().getProps()));
    }
}
