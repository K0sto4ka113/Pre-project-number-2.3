package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;



public class UserDaoHibernateImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoHibernateImpl.class.getName());
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT PRIMARY KEY," +
            " name VARCHAR(255), lastname VARCHAR(255), age TINYINT)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS users";
    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {this.sessionFactory = new Util().getSessionFactory();}


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(CREATE_TABLE).executeUpdate();
            session.getTransaction().commit();
            LOGGER.info("Users таблица создана");
        } catch (Exception e) {
            LOGGER.severe("Ошибка создания таблицы. " + e.getMessage());
            throw new RuntimeException("Ошибка создания таблицы. ", e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(DROP_TABLE).executeUpdate();
            session.getTransaction().commit();
            LOGGER.info("Users таблица удалена");
        } catch (Exception e) {
            LOGGER.severe("Ошибка удаления таблицы. " + e.getMessage());
            throw new RuntimeException("Ошибка удаления таблицы. ", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            LOGGER.info("User с именем - " + name + " добавлен в базу данных");
        } catch (Exception e) {
            LOGGER.severe("Ошибка сохранения пользователя. " + e.getMessage());
            throw new RuntimeException("Ошибка сохранения пользователя. ", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
            LOGGER.info("User с id - " + id + " удален из базы данных");
        } catch (Exception e) {
            LOGGER.severe("Ошибка удаления пользователя. " + e.getMessage());
            throw new RuntimeException("Ошибка удаления пользователя. ", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> userList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            userList = session.createQuery("from User", User.class).list();
            for (User user : userList) {
                System.out.println(user);
            }
            transaction.commit();
            LOGGER.info("Список пользователей получен успешно!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.severe("Ошибка получения списка пользователей. " + e.getMessage());
            throw new RuntimeException("Ошибка получения списка пользователей. ", e);
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            transaction.commit();
            LOGGER.info("Users таблица очищена");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.severe("Ошибка очистки таблицы. " + e.getMessage());
            throw new RuntimeException("Ошибка очистки таблицы. ", e);
        }
    }
}


