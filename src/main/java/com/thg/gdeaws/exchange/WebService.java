package com.thg.gdeaws.exchange;

import java.util.ArrayList;
import java.util.List;

public class WebService {
	private String serviceName;
	private String owner;
	private String url;
	private String version;
	private String contact;
	private List<EndPoint> endPoints = new ArrayList<EndPoint>();
	public String getServiceName() {
		return this.serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getOwner() {
		return this.owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVersion() {
		return this.version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getContact() {
		return this.contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public List<EndPoint> getEndPoints() {
		return this.endPoints;
	}
	public void setEndPoints(List<EndPoint> endPoints) {
		this.endPoints = endPoints;
	}
}
