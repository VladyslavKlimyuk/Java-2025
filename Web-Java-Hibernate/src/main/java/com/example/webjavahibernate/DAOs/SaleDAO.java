package com.example.webjavahibernate.DAOs;

import com.example.webjavahibernate.Models.*;
import com.example.webjavahibernate.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;
import java.util.List;

public class SaleDAO {
    public void create(Sale sale) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(sale);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(Sale sale) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(sale);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                }
            }
            e.printStackTrace();
            throw new RuntimeException("Помилка оновлення бази даних.", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Sale sale = session.find(Sale.class, id);
            if (sale != null) {
                session.remove(sale);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Sale findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Sale.class, id);
        }
    }

    public List<Sale> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT s FROM Sale s JOIN FETCH s.seller JOIN FETCH s.customer JOIN FETCH s.product", Sale.class).list();
        }
    }

    // 1. Угоди в конкретну дату
    public List<Sale> findByDate(LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Sale s WHERE s.saleDate = :date", Sale.class)
                    .setParameter("date", date)
                    .list();
        }
    }

    // 2. Угоди в вказаний діапазон дат
    public List<Sale> findByDateRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Sale s WHERE s.saleDate BETWEEN :start AND :end", Sale.class)
                    .setParameter("start", startDate)
                    .setParameter("end", endDate)
                    .list();
        }
    }

    // 3. Угоди конкретного продавця
    public List<Sale> findBySeller(Long sellerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Sale s WHERE s.seller.id = :sellerId", Sale.class)
                    .setParameter("sellerId", sellerId)
                    .list();
        }
    }

    // 4. Угоди конкретного покупця
    public List<Sale> findByCustomer(Long customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Sale s WHERE s.customer.id = :customerId", Sale.class)
                    .setParameter("customerId", customerId)
                    .list();
        }
    }

    // 5. Найуспішніший продавець (максимальна сума продажів)
    public Seller findMostSuccessfulSeller() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s.seller, SUM(s.salePrice) AS totalSum " +
                    "FROM Sale s GROUP BY s.seller ORDER BY totalSum DESC";

            List<Object[]> results = session.createQuery(hql, Object[].class)
                    .setMaxResults(1)
                    .list();

            return results.isEmpty() ? null : (Seller) results.get(0)[0];
        }
    }

    // 6. Найуспішніший покупець (максимальна сума купівлі)
    public Customer findMostSuccessfulCustomer() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s.customer, SUM(s.salePrice) AS totalSum " +
                    "FROM Sale s GROUP BY s.customer ORDER BY totalSum DESC";

            List<Object[]> results = session.createQuery(hql, Object[].class)
                    .setMaxResults(1)
                    .list();

            return results.isEmpty() ? null : (Customer) results.get(0)[0];
        }
    }

    // 7. Середня сума покупки
    public Double findAverageSaleAmount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT AVG(s.salePrice) FROM Sale s", Double.class).uniqueResult();
        }
    }

    // 8. Товар, що найбільше користується попитом (за кількістю продажів)
    public Product findMostPopularProduct() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s.product, COUNT(s.id) AS saleCount " +
                    "FROM Sale s GROUP BY s.product ORDER BY saleCount DESC";

            List<Object[]> results = session.createQuery(hql, Object[].class)
                    .setMaxResults(1)
                    .list();

            return results.isEmpty() ? null : (Product) results.get(0)[0];
        }
    }
}