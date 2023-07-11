package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final static String SQL_QUERY = "CREATE TABLE IF NOT EXISTS users " +
            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(64), lastname VARCHAR(64), age TINYINT)";
    private final static String SQL_QUERY_DROP = "DROP TABLE IF EXISTS users";
    private final static String SQL_QUERY_TRUNCATE = "TRUNCATE users";
    private Transaction transaction = null;
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session openSession = sessionFactory.openSession()) {
            transaction = openSession.beginTransaction();
            openSession.createNativeQuery(SQL_QUERY).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session openSession = sessionFactory.openSession()) {
            transaction = openSession.beginTransaction();
            openSession.createNativeQuery(SQL_QUERY_DROP).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session openSession = sessionFactory.openSession()) {
            transaction = openSession.beginTransaction();
            openSession.save(new User(name, lastName, age));
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session openSession = sessionFactory.openSession()) {
            transaction = openSession.beginTransaction();
            openSession.delete(openSession.get(User.class, id));
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            list = session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session openSession = sessionFactory.openSession()) {
            transaction = openSession.beginTransaction();
            openSession.createNativeQuery(SQL_QUERY_TRUNCATE).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
