package com.thg.gdeaws.exchange;

public class Request<I> {
	
	private String badge;
	private String account;
	private String username;
	private String password;
	private I input;
	
	public String getBadge() {
		return this.badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	public I getInput() {
		return this.input;
	}
	public void setInput(I input) {
		this.input = input;
	}
	public String getAccount() {
		return this.account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
