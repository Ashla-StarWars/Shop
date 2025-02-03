package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "historical_inventory") // Define el nombre de la tabla a la que se mapea
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_product", nullable = false)
    private int productId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "wholesalerPrice", nullable = false, precision = 10, scale = 2) // Mapea la columna 'wholesalerPrice' con tipo DECIMAL
    private double wholesalerPrice;

    @Column(name = "available", nullable = false)
    private int available;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public ProductHistory() {
        // Constructor por defecto
    }

    public ProductHistory(int productId, String name, double wholesalerPrice, int available, int stock, Date createdAt) {
        this.productId = productId;
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.available = available;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(double wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ProductHistory{" +
                "id=" + id +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", wholesalerPrice=" + wholesalerPrice +
                ", available=" + available +
                ", stock=" + stock +
                ", createdAt=" + createdAt +
                '}';
    }
}