package com.example.webjavahibernate.Models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "seller")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Sale> sales = new HashSet<>();

    public Seller() {
    }

    public Seller(Long id, String name, String phone, String email, Set<Sale> sales) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.sales = sales;
    }

    public void addSale(Sale sale) {
        if (sales == null) {
            sales = new HashSet<>();
        }
        sales.add(sale);
        if (sale.getSeller() != this) {
            sale.setSeller(this);
        }
    }

    public void removeSale(Sale sale) {
        if (sales != null) {
            sales.remove(sale);
        }
        if (sale.getSeller() == this) {
            sale.setSeller(null);
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

    public Set<Sale> getSales() {
        return sales;
    }

    public void setSales(Set<Sale> sales) {
        this.sales = sales;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return Objects.equals(id, seller.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Seller{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", sales=" + sales +
                '}';
    }
}