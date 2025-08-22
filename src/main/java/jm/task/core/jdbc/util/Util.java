package jm.task.core.jdbc.util;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import jm.task.core.jdbc.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String DB_DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "78853";

    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.driver_class", DB_DRIVER)
                .applySetting("hibernate.connection.url", DB_URL)
                .applySetting("hibernate.connection.username", DB_USER)
                .applySetting("hibernate.connection.password", DB_PASSWORD)
                .applySetting("hibernate.dialect", DB_DIALECT)
                .applySetting("hibernate.show_sql", "true")
                .applySetting("hibernate.hbm2ddl.auto", "update")
                .build();

            MetadataSources metadataSources = new MetadataSources(registry);
            metadataSources.addAnnotatedClass(User.class);

            Metadata metadata = metadataSources.getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        }
        return sessionFactory;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}