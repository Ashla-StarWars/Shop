package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@Embeddable
public class Amount {
	@Transient
	@Column(insertable = false, updatable = false)
	private double value;
	@Transient
	@Column(insertable = false, updatable = false)
	private String currency;

	public Amount() {
	}

	/*
	 * public Amount(double value) { super(); this.value = value; }
	 */

	public Amount(double value, String currency) {
		super();
		this.value = value;
		this.currency = currency;
	}

	@XmlValue
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = Math.round(value * 100.00) / 100.00;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@XmlAttribute(name = "currency")
	public String getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		return this.value + " " + this.currency;
	}

	public String toList() {
		return this.value + this.currency;
	}

}
