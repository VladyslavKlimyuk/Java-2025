package com.example.webjavahibernate.Models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Sale> purchases = new HashSet<>();

    public Customer() {
    }

    public Customer(Long id, String name, String phone, String email, Set<Sale> purchases) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.purchases = purchases;
    }

    public void addSale(Sale sale) {
        if (purchases == null) {
            purchases = new HashSet<>();
        }
        purchases.add(sale);
        if (sale.getCustomer() != this) {
            sale.setCustomer(this);
        }
    }

    public void removeSale(Sale sale) {
        if (purchases != null) {
            purchases.remove(sale);
        }
        if (sale.getCustomer() == this) {
            sale.setCustomer(null);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Sale> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Sale> purchases) {
        this.purchases = purchases;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", purchases=" + purchases +
                '}';
    }
}