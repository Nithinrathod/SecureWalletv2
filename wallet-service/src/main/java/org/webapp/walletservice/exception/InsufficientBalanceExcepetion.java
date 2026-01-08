package org.webapp.walletservice.exception;

public class InsufficientBalanceExcepetion extends RuntimeException {

	public InsufficientBalanceExcepetion(String msg) {
		super(msg);
	}

}
