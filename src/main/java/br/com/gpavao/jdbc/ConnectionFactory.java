package br.com.gpavao.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final String PROPS_FILE = "application.properties";
    private static HikariDataSource dataSource;

    static {
        try {
            Properties props = loadProperties();
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));

            // Configurações do pool
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(300000); // 5 minutos
            config.setConnectionTimeout(20000); // 20 segundos


            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configurações do banco", e);
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();

        try (InputStream is = ConnectionFactory.class.getClassLoader()
                .getResourceAsStream(PROPS_FILE)) {
            if (is == null) {
                throw new IOException("Arquivo " + PROPS_FILE + " não encontrado");
            }
            props.load(is);
        }

        return props;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
