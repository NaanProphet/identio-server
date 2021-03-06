/*
 * This file is part of Ident.io.
 *
 * Ident.io - A flexible authentication server
 * Copyright (c) 2017 Loeiz TANGUY
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.identio.server.service.data;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "data")
public class DataConfiguration implements InitializingBean {

    private HashMap<String, DataSourcesConfiguration> dataSourceIndex = new HashMap<>();

    /// Configuration mapping handled by Spring Cloud config

    private List<DataSourcesConfiguration> dataSources = new ArrayList<>();

    public List<DataSourcesConfiguration> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourcesConfiguration> dataSources) {
        this.dataSources = dataSources;
    }

    public static class DataSourcesConfiguration {

        private String name;
        private String type;
        private String driver;
        private String url;
        private String username;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /// End: Configuration mapping handled by Spring Cloud config

    @Override
    public void afterPropertiesSet() throws Exception {

        for (DataSourcesConfiguration config : dataSources) {

            dataSourceIndex.put(config.name, config);
        }
    }

    public DataSourcesConfiguration getDataSourceConfiguration(String name) {
        return dataSourceIndex.get(name);
    }
}
