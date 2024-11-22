package dao.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Amount;
import model.Product;
import model.ProductList;

public class SaxReader extends DefaultHandler {

	private ProductList products;
	private Product product;
	private Amount amount;
	private String value;
	private String parsedElement;

	/**
	 * @return the products
	 */
	public ProductList getProducts() {
		return products;
	}

	@Override
	public void startDocument() throws SAXException {
		this.products = new ProductList();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
		case "product":
			this.product = new Product(attributes.getValue("name") != null ? attributes.getValue("name") : "empty",
					null, true, 0);
			break;
		case "wholesalerPrice":
			String currency = attributes.getValue("currency");
			this.amount = new Amount();
			this.amount.setCurrency(currency);
			break;
		case "stock":
			break;
		}
		this.parsedElement = qName;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		value = new String(ch, start, length);
		switch (parsedElement) {
		case "product":
			break;
		case "wholesalerPrice":
			this.amount.setValue(Double.valueOf(value));
			this.product.setWholesalerPrice(this.amount);
			Amount publicPrice = new Amount();
			publicPrice.setValue(this.amount.getValue() * 2);
			publicPrice.setCurrency(this.amount.getCurrency());
			this.product.setPublicPrice(publicPrice);
			break;
		case "stock":
			this.product.setStock(Integer.valueOf(value));
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// we add the product into the arrayList
		if (qName.equals("product"))
			this.products.add(product);
		this.parsedElement = "";
	}
}
