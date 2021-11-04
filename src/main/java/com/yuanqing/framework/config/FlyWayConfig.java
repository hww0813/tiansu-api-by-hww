package com.yuanqing.framework.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-08-20 17:13
 */
@Configuration
public class FlyWayConfig {

    @Autowired
    private DataSource dataSource;

    private Logger logger = LoggerFactory.getLogger(FlyWayConfig.class);


    @PostConstruct
    public void migrate() {

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .encoding("UTF-8")
                .outOfOrder(true)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();


        try {

            flyway.migrate();

        } catch (FlywayException e) {

            flyway.repair();

            logger.error("Flyway配置加载出错",e);

        }

    }
}
