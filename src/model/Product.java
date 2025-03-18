package model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import utils.Constants;

@Entity
@Table(name = "inventory")
@XmlRootElement(name = "product")
@XmlType(propOrder = { "id", "name", "available", "wholesalerPrice", "publicPrice", "stock" })
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private Amount publicPrice = new Amount(0.0, Constants.AMOUNT.SYMBOL.EUR);
	private Amount wholesalerPrice = new Amount(0.0, Constants.AMOUNT.SYMBOL.EUR);
	private boolean available;
	private int stock;

	@Column(name = "wholesalerPrice")
	private double price;

	public static int totalProducts;

	static double EXPIRATION_RATE = 0.60;

	public Product() {
		//CUIDADO CON EL ID, NO BORRAR LINEA !!
		//this.id = totalProducts + 1; 
		totalProducts++;
	}             

	public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
		super();
		this.name = name;
		setWholesalerPrice(wholesalerPrice);
		this.available = available;
		this.stock = stock;
		this.publicPrice = wholesalerPrice;
		this.price = wholesalerPrice.getValue();
		this.id = totalProducts + 1;
		totalProducts++;
	}

	public Product(int id, String name, double price, boolean available, int stock) {
		super();
		this.id = id;
		this.name = name;
		setWholesalerPrice(new Amount(price, Constants.AMOUNT.SYMBOL.EUR));
		this.publicPrice = new Amount(price * 2, Constants.AMOUNT.SYMBOL.EUR);
		this.price = price*2;
		this.available = available;
		this.stock = stock;
		totalProducts++;
	}
	public Product(int id, String name, Amount price, boolean available, int stock) {
		super();
		this.id = id;
		this.name = name;
		setWholesalerPrice(price);
		this.publicPrice = new Amount(price.getValue() * 2, price.getCurrency());
		this.price = price.getValue()*2;
		this.available = available;
		this.stock = stock;
		totalProducts++;
	}

	@XmlAttribute(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "publicPrice")
	public Amount getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(Amount publicPrice) {
		this.publicPrice = publicPrice;
		if (publicPrice != null) {
            this.price = publicPrice.getValue();  // Sincroniza price con publicPrice
        }
	}

	@XmlElement(name = "wholesalerPrice")
	public Amount getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(Amount wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
		if (wholesalerPrice != null) {
			this.publicPrice = new Amount(wholesalerPrice.getValue() * 2, wholesalerPrice.getCurrency());
			this.price = wholesalerPrice.getValue();
        }
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@XmlElement(name = "stock")
	@Column(name = "stock")
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
		if (this.stock > 0) {
			this.available = true;
		}
	}

    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
    	this.price = price;
        if (this.publicPrice != null) {
            this.publicPrice.setValue(price);  // Sincroniza publicPrice con price
        }
    }
	
	public void expire() {
		this.publicPrice.setValue(this.getPublicPrice().getValue() * EXPIRATION_RATE);
	}

	@Override
	public String toString() {
		return this.id + " " + this.name + ", publicPrice = " + this.publicPrice.toString() + ", wholesalerPrice = "
				+ this.wholesalerPrice.toString() + ", stock = " + this.stock + ", available = " + this.isAvailable();
	}

	public String toList() {
		return this.name + "," + wholesalerPrice.toList() + ";";
	}

}
