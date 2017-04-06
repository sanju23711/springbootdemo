package com.thg.gdeaws.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thg.gdeaws.db.HibernateUtil;
import com.thg.gdeaws.db.model.Account;
import com.thg.gdeaws.db.model.Badge;
import com.thg.gdeaws.db.model.User;
import com.thg.gdeaws.exception.ObjectValidationException;
import com.thg.gdeaws.exchange.LoginOutput;
import com.thg.gdeaws.exchange.Request;
import com.thg.gdeaws.util.EncryptionUtil;

@Service(value="authService")
@Transactional(propagation = Propagation.REQUIRED)
public class AuthService {
	
	@Autowired
	private HibernateUtil hibernateUtil;
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AuthService.class);
	
	public LoginOutput login(Request<Object> request) throws ObjectValidationException {
		
		if(StringUtils.isEmpty(request.getAccount()))
			throw new ObjectValidationException("2.0.1.1", "'account' is not valid"); 
		if(request.getAccount().length() > 50)
			throw new ObjectValidationException("2.0.1.2", "'account' is not in valid length"); 
		
		Account account = hibernateUtil.getDao(Account.class).findBy("accountName", request.getAccount().toUpperCase());
		if(account == null)
			throw new ObjectValidationException("2.0.1.3", "'account' is not found");
		
		if(!account.getActive())
			throw new ObjectValidationException("2.0.1.4", "'account' is not active");
		
		if(StringUtils.isEmpty(request.getUsername()))
			throw new ObjectValidationException("2.0.2.1", "'credentials' are not valid"); 
		if(request.getUsername().length() < 8 || request.getUsername().length() > 16)
			throw new ObjectValidationException("2.0.2.1", "'credentials' are not valid"); 
		
		if(StringUtils.isEmpty(request.getPassword()))
			throw new ObjectValidationException("2.0.2.1", "'credentials' are not valid"); 
		if(request.getPassword().length() > 100)
			throw new ObjectValidationException("2.0.2.1", "'credentials' are not valid"); 
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("account", account);
		criteria.put("username", request.getUsername());
		criteria.put("password", EncryptionUtil.encryptPassword(request.getPassword()));
		
		List<User> users = hibernateUtil.getDao(User.class).findAll(criteria, null);
		if(users.size() > 1)
			throw new ObjectValidationException("2.0.2.2", "'credentials' are not valid. Please contact your Hibbert Administrator"); 
		
		if(users.size() < 1)
			throw new ObjectValidationException("2.0.2.1", "'credentials' are not valid"); 
		
		User user = users.get(0);
		if(!user.getActive())
			throw new ObjectValidationException("2.0.2.3", "'username' is not active. Please contact Hibbert Administrator");
		
		user.setLastLoginDate(new Date());
		hibernateUtil.getDao(User.class).save(user);
		
		Badge badge = genetareBadge(user, user.getBadgeExpiryInMins() == null ?
				account.getBadgeExpiryInMins() : user.getBadgeExpiryInMins());
		
		return convertBadge(badge);
	}
	
	public LoginOutput validate(Request<Object> request) throws ObjectValidationException {
		if(StringUtils.isEmpty(request.getBadge()))
			return login(request);
		
		Badge badge = hibernateUtil.getDao(Badge.class).findBy("badge", request.getBadge());
		
		if(badge == null || badge.getExpiresOn().before(new Date())){
			try{
				return login(request);
			}catch(ObjectValidationException exception){
				throw new ObjectValidationException("2.0.3.1", "'badge' is invalid or expired. Please try again with "
						+ "correct badge and/or credentials");
			}
		}
		
		return convertBadge(badge);
	}
	
	public Badge genetareBadge(User user, Long expiryInMinutes){
		if(expiryInMinutes == null)
			expiryInMinutes = 24l * 60l;
		Badge badge = user.getBadge();
		if(badge == null){
			badge = new Badge();
			badge.setBadgeId(user.getUserId());
		}
		user.setBadge(badge);
		badge.setUser(user);
		badge.setLastAccessedOn(new Date());
		if(StringUtils.isEmpty(badge.getBadge()) || badge.getExpiresOn().before(new Date())){
			badge.setBadge(EncryptionUtil.generateBadge());
			badge.setExpiresOn(DateUtils.addMinutes(badge.getLastAccessedOn(), expiryInMinutes.intValue()));
		}
		hibernateUtil.getDao(Badge.class).save(badge);
		return badge;
	}
	
	public LoginOutput convertBadge(Badge badge){
		LoginOutput loginOutput = new LoginOutput();
		loginOutput.setAccount(badge.getUser().getAccount().getAccountName());
		loginOutput.setUserId(badge.getUser().getUserId());
		loginOutput.setBadge(badge.getBadge());
		loginOutput.setLastLoggedOn(badge.getUser().getLastLoginDate());
		loginOutput.setValid(true);
		loginOutput.setBadgeExpiresOn(badge.getExpiresOn());
		loginOutput.setValid(true);
		return loginOutput;
	}
	
}
