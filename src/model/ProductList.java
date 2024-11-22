package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "products")
public class ProductList {

	private ArrayList<Product> products = new ArrayList<>();

	public ProductList() {
	};

	public ProductList(ArrayList<Product> products) {
		this.products = products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	@XmlElement(name = "product")
	public ArrayList<Product> getProducts() {
		return this.products;
	}

	@XmlAttribute(name = "total")
	public int getTotal() {
		return this.products.size();
	}

	public void add(Product product) {
		if (product != null) {
			products.add(product);
		}
	}

	public void remove(Product product) {
		if (product != null) {
			products.remove(product);
		}
	}

	public Product get(int id) {
		if (id >= 0 && id < products.size()) {
			return products.get(id);
		}
		return null;
	}
}
