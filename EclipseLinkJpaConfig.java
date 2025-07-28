package com.oracle.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.oracle.repositories")
@EntityScan("com.oracle.entities")
public class EclipseLinkJpaConfig extends JpaBaseConfiguration{

	protected EclipseLinkJpaConfig(DataSource dataSource, JpaProperties properties,
            ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
       super(dataSource, properties, jtaTransactionManager);
}

@Override
protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
return new EclipseLinkJpaVendorAdapter();
}

@Override
protected Map<String, Object> getVendorProperties(DataSource dataSource) {
Map<String, Object> props = new HashMap<>();
props.put(PersistenceUnitProperties.WEAVING, "false");
props.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.CREATE_OR_EXTEND);
return props;
}

}
