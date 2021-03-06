package com.thg.gdeaws.db.model;

// Generated Oct 3, 2015 11:03:51 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Badge generated by hbm2java
 */
@Entity
@Table(name = "THG_DC_BADGE")
@SuppressWarnings("serial")
public class Badge implements java.io.Serializable {

	private Long badgeId;
	private User user;
	private String badge;
	private Date createdOn;
	private Date lastAccessedOn;
	private Date expiresOn;

	public Badge() {
	}

	public Badge(User user, String badge, Date createdOn, Date lastAccessedOn,
			Date expiresOn) {
		this.user = user;
		this.badge = badge;
		this.createdOn = createdOn;
		this.lastAccessedOn = lastAccessedOn;
		this.expiresOn = expiresOn;
	}

	@Id
	@Column(name = "USER_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getBadgeId() {
		return this.badgeId;
	}

	public void setBadgeId(Long badgeId) {
		this.badgeId = badgeId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "BADGE", length = 160)
	public String getBadge() {
		return this.badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	@Column(name = "CREATED_ON", length = 7)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "LAST_ACCESSED_ON", length = 7)
	public Date getLastAccessedOn() {
		return this.lastAccessedOn;
	}

	public void setLastAccessedOn(Date lastAccessedOn) {
		this.lastAccessedOn = lastAccessedOn;
	}

	@Column(name = "EXPIRES_ON", length = 7)
	public Date getExpiresOn() {
		return this.expiresOn;
	}

	public void setExpiresOn(Date expiresOn) {
		this.expiresOn = expiresOn;
	}

}
