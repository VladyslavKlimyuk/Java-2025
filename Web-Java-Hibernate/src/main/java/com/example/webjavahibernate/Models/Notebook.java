package com.example.webjavahibernate.Models;

import jakarta.persistence.*;
import java.util.Objects;

import com.example.webjavahibernate.Enums.*;

@Entity
@Table(name = "notebook")
public class Notebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manufacturer_name", nullable = false)
    private String manufacturerName;

    @Column(name = "notebook_code", nullable = false, unique = true)
    private String notebookCode;

    @Column(name = "page_count", nullable = false)
    private int pageCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "cover_type", nullable = false)
    private CoverType coverType;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "circulation", nullable = false)
    private int circulation;

    @Enumerated(EnumType.STRING)
    @Column(name = "page_layout", nullable = false)
    private PageLayout pageLayout;

    public Notebook() {
    }

    public Notebook(Long id, String manufacturerName, String notebookCode, int pageCount,
                    CoverType coverType, String country, int circulation, PageLayout pageLayout) {
        this.id = id;
        this.manufacturerName = manufacturerName;
        this.notebookCode = notebookCode;
        this.pageCount = pageCount;
        this.coverType = coverType;
        this.country = country;
        this.circulation = circulation;
        this.pageLayout = pageLayout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getNotebookCode() {
        return notebookCode;
    }

    public void setNotebookCode(String notebookCode) {
        this.notebookCode = notebookCode;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public CoverType getCoverType() {
        return coverType;
    }

    public void setCoverType(CoverType coverType) {
        this.coverType = coverType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCirculation() {
        return circulation;
    }

    public void setCirculation(int circulation) {
        this.circulation = circulation;
    }

    public PageLayout getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(PageLayout pageLayout) {
        this.pageLayout = pageLayout;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Notebook notebook = (Notebook) o;
        return pageCount == notebook.pageCount && circulation == notebook.circulation &&
                Objects.equals(id, notebook.id) && Objects.equals(manufacturerName,
                notebook.manufacturerName) && Objects.equals(notebookCode, notebook.notebookCode) &&
                coverType == notebook.coverType && Objects.equals(country, notebook.country) &&
                pageLayout == notebook.pageLayout;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manufacturerName, notebookCode, pageCount, coverType, country,
                circulation, pageLayout);
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "id=" + id +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", notebookCode='" + notebookCode + '\'' +
                ", pageCount=" + pageCount +
                ", coverType=" + coverType +
                ", country='" + country + '\'' +
                ", circulation=" + circulation +
                ", pageLayout=" + pageLayout +
                '}';
    }
}
