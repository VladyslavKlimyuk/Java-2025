package com.example.webjavahibernate.DAOs;

import com.example.webjavahibernate.Enums.CoverType;
import com.example.webjavahibernate.Enums.PageLayout;
import com.example.webjavahibernate.Models.Notebook;
import com.example.webjavahibernate.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotebookDAO {
    public void create(Notebook notebook) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(notebook);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(Notebook notebook) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(notebook);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Notebook notebook = session.find(Notebook.class, id);
            if (notebook != null) {
                session.remove(notebook);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<Notebook> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Notebook", Notebook.class).list();
        }
    }

    public Notebook findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Notebook.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Статистика по країнах
    public Map<String, Long> countByCountry() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createQuery(
                    "SELECT n.country, COUNT(n) FROM Notebook n GROUP BY n.country",
                    Object[].class).list();
            return results.stream()
                    .collect(Collectors.toMap(
                            arr -> (String) arr[0],
                            arr -> (Long) arr[1]
                    ));
        }
    }

    // Статистика по виробниках
    public Map<String, Long> countByManufacturer() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createQuery(
                    "SELECT n.manufacturerName, COUNT(n) FROM Notebook n GROUP BY n.manufacturerName",
                    Object[].class).list();
            return results.stream()
                    .collect(Collectors.toMap(
                            arr -> (String) arr[0],
                            arr -> (Long) arr[1]
                    ));
        }
    }

    // Min/Max статистика для країни з найбільшою кількістю блокнотів
    public Map.Entry<String, Long> getCountryWithMaxNotebooks() {
        return countByCountry().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }

    // Min/Max статистика для країни з найменшою кількістю блокнотів
    public Map.Entry<String, Long> getCountryWithMinNotebooks() {
        return countByCountry().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null);
    }

    // Min/Max статистика для виробника з найбільшою кількістю блокнотів
    public Map.Entry<String, Long> getManufacturerWithMaxNotebooks() {
        return countByManufacturer().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }

    // Min/Max статистика для виробника з найменшою кількістю блокнотів
    public Map.Entry<String, Long> getManufacturerWithMinNotebooks() {
        return countByManufacturer().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null);
    }

    // Пошук блокнота за виробником
    public List<Notebook> findByManufacturerName(String manufacturerName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notebook> query = session.createQuery(
                    "FROM Notebook n WHERE LOWER(n.manufacturerName) LIKE :searchName", Notebook.class);
            String searchPattern = "%" + manufacturerName.toLowerCase() + "%";
            query.setParameter("searchName", searchPattern);

            return query.list();
        }
    }

    // Пошук блокнота за типом обкладинки
    public List<Notebook> findByCoverType(CoverType coverType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notebook> query = session.createQuery(
                    "FROM Notebook n WHERE n.coverType = :coverType", Notebook.class);
            query.setParameter("coverType", coverType);
            return query.list();
        }
    }

    // Пошук блокнота за країною
    public List<Notebook> findByCountry(String country) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notebook> query = session.createQuery(
                    "FROM Notebook n WHERE n.country = :country", Notebook.class);
            query.setParameter("country", country);
            return query.list();
        }
    }

    // Пошук блокнота за країною (у вигляді випадаючого списку)
    public List<String> findDistinctCountries() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT n.country FROM Notebook n ORDER BY n.country",
                    String.class).list();
        }
    }

    // Пошук блокнота за зовнішнім виглядом сторінки
    public List<Notebook> filterByPageLayout(PageLayout pageLayout) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notebook> query = session.createQuery(
                    "FROM Notebook n WHERE n.pageLayout = :layout", Notebook.class);
            query.setParameter("layout", pageLayout);
            return query.list();
        }
    }

    // Пошук блокнота за кількістю сторінок
    public List<Notebook> filterByPageCount(int min, int max) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notebook> query = session.createQuery(
                    "FROM Notebook n WHERE n.pageCount BETWEEN :min AND :max", Notebook.class);
            query.setParameter("min", min);
            query.setParameter("max", max);
            return query.list();
        }
    }

    // Пошук блокнота за тиражем
    public List<Notebook> filterByCirculation(int min, int max) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notebook> query = session.createQuery(
                    "FROM Notebook n WHERE n.circulation BETWEEN :min AND :max", Notebook.class);
            query.setParameter("min", min);
            query.setParameter("max", max);
            return query.list();
        }
    }
}