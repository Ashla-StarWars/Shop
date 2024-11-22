package model;

import main.Payable;
import utils.Constants;

public class Client extends Person implements Payable {

	int memberID;
	Amount balance;

	final int MEMBER_ID = 465;
	final Amount BALANCE = new Amount(50.00, Constants.AMOUNT_SYMBOL);

	public Client(String name) {
		super(name);
		this.memberID = MEMBER_ID;
		this.balance = BALANCE;
	}

	protected int getMemberID() {
		return memberID;
	}

	protected void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	public Amount getBalance() {
		return balance;
	}

	protected void setBalance(Amount balance) {
		this.balance = balance;
	}

	@Override
	public boolean pay(Amount amount) {
		boolean paid = false;
		if (amount.getValue() < this.balance.getValue()) {
			paid = true;
		}
		this.balance.setValue(this.balance.getValue() - amount.getValue());
		return paid;
	}

}
