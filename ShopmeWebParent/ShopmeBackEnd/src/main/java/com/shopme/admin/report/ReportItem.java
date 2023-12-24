package com.shopme.admin.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
public class ReportItem {
    private String identifier;
    private float grossSales;
    private float netSales;
    private int ordersCount;
    private int productsCount;
    private int categoryCount;

    public ReportItem(String identifier) {
        this.identifier = identifier;
    }

    public ReportItem(String identifier, float grossSales, float netSales) {
        this.identifier = identifier;
        this.grossSales = grossSales;
        this.netSales = netSales;
    }

    public ReportItem(String identifier, float grossSales, float netSales, int productsCount) {
        super();
        this.identifier = identifier;
        this.grossSales = grossSales;
        this.netSales = netSales;
        this.productsCount = productsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportItem that = (ReportItem) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    public void addGrossSales(float amount) {
        this.grossSales += amount;
    }

    public void addNetSales(float amount) {
        this.netSales += amount;
    }

    public void increaseOrderCount() {
        this.ordersCount++;
    }

    public void increaseProductsCount(int productsCount) {
        this.productsCount += productsCount;
    }
}
