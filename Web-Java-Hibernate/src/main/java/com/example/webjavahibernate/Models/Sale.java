package com.example.webjavahibernate.Models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "sale_price", nullable = false)
    private Double salePrice;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    public Sale() {
    }

    public Sale(Long id, Seller seller, Customer customer, Product product,
                Double salePrice, LocalDate saleDate) {
        this.id = id;
        this.seller = seller;
        this.customer = customer;
        this.product = product;
        this.salePrice = salePrice;
        this.saleDate = saleDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id) && Objects.equals(seller, sale.seller) &&
                Objects.equals(customer, sale.customer) && Objects.equals(product, sale.product) &&
                Objects.equals(salePrice, sale.salePrice) && Objects.equals(saleDate, sale.saleDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seller, customer, product, salePrice, saleDate);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", seller=" + seller +
                ", customer=" + customer +
                ", product=" + product +
                ", salePrice=" + salePrice +
                ", saleDate=" + saleDate +
                '}';
    }
}