package edu.school21.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "edu.school21")
@PropertySource("classpath:db.properties")
public class FixMeApplicationConfig {

    @Value("${db.user}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.url}")
    private String jdbcUrl;
    @Value("${db.driver.name}")
    private String driverName;

    @Bean
    public DataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(username);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverName);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate() {
        return new NamedParameterJdbcTemplate(hikariDataSource());
    }

}
