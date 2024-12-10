package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import utils.Constants;

@XmlRootElement(name = "product")
@XmlType(propOrder = { "id", "name", "available", "wholesalerPrice", "publicPrice", "stock" })
public class Product {
	private int id;
	private String name;
	private Amount publicPrice = new Amount(0.0, Constants.AMOUNT_SYMBOL);
	private Amount wholesalerPrice = new Amount(0.0, Constants.AMOUNT_SYMBOL);
	private boolean available;
	private int stock;

	private static int totalProducts;

	static double EXPIRATION_RATE = 0.60;

	public Product() {
		this.id = totalProducts + 1;
		this.available = true;
		totalProducts++;
	}

	public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
		super();
		this.name = name;
		this.wholesalerPrice = wholesalerPrice;
		this.available = available;
		this.stock = stock;
		this.publicPrice = wholesalerPrice;
		this.id = totalProducts + 1;
		totalProducts++;
	}

	public Product(int id, String name, double price, boolean available, int stock) {
		super();
		this.id = id;
		this.name = name;
		this.wholesalerPrice = new Amount(price, Constants.AMOUNT_SYMBOL);
		this.publicPrice = new Amount(price * 2, Constants.AMOUNT_SYMBOL);
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
	}

	@XmlElement(name = "wholesalerPrice")
	public Amount getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(Amount wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;

		Amount newPublicPrice = new Amount(this.wholesalerPrice.getValue() * 2, this.wholesalerPrice.getCurrency());
		this.publicPrice = newPublicPrice;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@XmlElement(name = "stock")
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void expire() {
		this.publicPrice.setValue(this.getPublicPrice().getValue() * EXPIRATION_RATE);
	}

	@Override
	public String toString() {
		return this.id + " " + this.name + ", publicPrice = " + this.publicPrice.toString() + ", wholesalerPrice = "
				+ this.wholesalerPrice.toString() + ", stock = " + this.stock;
	}

	public String toList() {
		return this.name + "," + wholesalerPrice.toList() + ";";
	}

}
