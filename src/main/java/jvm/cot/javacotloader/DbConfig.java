package jvm.cot.javacotloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * <a href="https://www.baeldung.com/spring-boot-sqlite">Resource Article</a>
 * <br/>
 * <a href="https://www.baeldung.com/spring-boot-configure-data-source-programmatic">Configuring a Data Source</a>
 * <br/>
 * <a href="https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-data-rest/src/main/java/com/baeldung/books/config/DbConfig.java">
 *     Reference GitHub Project
 * </a>
 */
@Configuration
@PropertySource("classpath:persistence.properties")
public class DbConfig {
    private final Environment env;

    @Autowired
    public DbConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        String driverClassName = env.getProperty("driverClassName") != null ?
                env.getProperty("driverClassName") : "com.mysql.jdbc.Driver";
        String url = env.getProperty("url") != null ?
                env.getProperty("url") : "jdbc:sqlite:memory:myDb?cache=shared";
        String username = env.getProperty("username") != null ?
                env.getProperty("username") : "sa";
        String password = env.getProperty("password") != null ?
                env.getProperty("password") : "sa";

        assert driverClassName != null;
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
