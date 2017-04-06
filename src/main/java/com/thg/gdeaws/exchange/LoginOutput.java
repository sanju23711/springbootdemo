package com.thg.gdeaws.exchange;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LoginOutput {
	
	private boolean valid;
	private String badge;
	private String account;
	private Long userId;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss a z", timezone="America/New_York")
	private Date lastLoggedOn;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss a z", timezone="America/New_York")
	private Date badgeExpiresOn;
	
	public boolean isValid() {
		return this.valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getBadge() {
		return this.badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	public String getAccount() {
		return this.account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Long getUserId() {
		return this.userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getLastLoggedOn() {
		return this.lastLoggedOn;
	}
	public void setLastLoggedOn(Date lastLoggedOn) {
		this.lastLoggedOn = lastLoggedOn;
	}
	public Date getBadgeExpiresOn() {
		return this.badgeExpiresOn;
	}
	public void setBadgeExpiresOn(Date badgeExpiresOn) {
		this.badgeExpiresOn = badgeExpiresOn;
	}
}
