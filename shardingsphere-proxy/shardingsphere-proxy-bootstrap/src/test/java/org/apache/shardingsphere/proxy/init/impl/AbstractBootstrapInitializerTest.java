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

import lombok.SneakyThrows;
import org.apache.shardingsphere.infra.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.infra.context.SchemaContexts;
import org.apache.shardingsphere.proxy.config.ProxyConfiguration;
import org.apache.shardingsphere.proxy.config.YamlProxyConfiguration;
import org.apache.shardingsphere.transaction.context.TransactionContexts;
import org.mockito.Mockito;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractBootstrapInitializerTest {
    
    @SneakyThrows
    @Test
    public void assertInit() {
        AbstractBootstrapInitializer abstractBootstrapInitializer = mock(AbstractBootstrapInitializer.class, Mockito.CALLS_REAL_METHODS);
        doReturn(mock(ProxyConfiguration.class)).when(abstractBootstrapInitializer).getProxyConfiguration(any());
        SchemaContexts schemaContexts = mock(SchemaContexts.class);
        ConfigurationProperties properties = mock(ConfigurationProperties.class);
        when(properties.getValue(any())).thenReturn(Boolean.FALSE);
        when(schemaContexts.getProps()).thenReturn(properties);
        doReturn(schemaContexts).when(abstractBootstrapInitializer).decorateSchemaContexts(any());
        doReturn(mock(TransactionContexts.class)).when(abstractBootstrapInitializer).decorateTransactionContexts(any());
        YamlProxyConfiguration yamlConfig = mock(YamlProxyConfiguration.class);
        int port = 2020;
        abstractBootstrapInitializer.init(yamlConfig, port);
        assertTrue(isAvailable(port));
    }
    
    private static boolean isAvailable(final int portNr) {
        boolean portFree;
        try (ServerSocket ignored = new ServerSocket(portNr)) {
            portFree = true;
        } catch (IOException e) {
            portFree = false;
        }
        return portFree;
    }
}
