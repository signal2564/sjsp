package com.improveit.simpleapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.improveit.simpleapp.intreface.IUserDao;
import com.improveit.simpleapp.model.User;
import com.improveit.simpleapp.model.UserSession;

@Service
public class UserService {
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private UserSession userScope;
	
	private boolean validateEmail(String paramValue) {
		if(paramValue.matches("[a-zA-Z0-9_.]+@[a-zA-Z0-9_.]+\\.[a-z]{1,4}"))
			return true;
		return false;
	}
	
	private boolean validateDate(String paramValue) {
		if(paramValue.matches("\\d{2}-\\d{2}-\\d{4}"))
			return true;
		return false;
	}
	
	private boolean validateUserUniqueParam(String paramName, String paramValue) {
		User someone = userDao.getUnique(paramName, paramValue);
		if(someone == null)
			return true;
		return false;
	}
	
	private boolean validatePhonenumber(String paramValue) {
		if(paramValue.matches("\\d{11}"))
			return true;
		return false;
	}
	
	private boolean validateSerialnumber(String paramValue) {
		if(paramValue.matches("\\d{10}"))
			return true;
		return false;
	}
	
	/**
	 * Validate param for future purpose 
	 * 
	 * @param paramName name of param
	 * @param paramValue value of param
	 * @return valid of param
	 */
	public boolean validate(String paramName, String paramValue) {
		boolean ret = true;
		if(paramName.equals("birthdate"))
			ret = validateDate(paramValue);
		if(paramName.equals("email"))
			ret = validateEmail(paramValue) ? validateUserUniqueParam(paramName, paramValue) : false;
		if(paramName.equals("phonenumber"))
			ret = validatePhonenumber(paramValue) ? validateUserUniqueParam(paramName, paramValue) : false;
		if(paramName.equals("serialnumber"))
			ret = validateSerialnumber(paramValue) ? validateUserUniqueParam(paramName, paramValue) : false;
		return ret;
	}
	
	/**
	 * Delegate create to dao layer.
	 * If user exist - update it.
	 * 
	 * @param user to insert
	 */
	@Transactional
	public int putUser(User user) {
		return userDao.update(user);
	}
	
	/**
	 * Delegate remove to dao layer.
	 * 
	 * @param user to remove
	 */
	@Transactional
	public void deleteUser(User user) {
		userDao.remove(user);
	}
	
	public User getCurrentUser() {
		return userScope.getUser();
	}
	
	public void setUserStep(String step) {
		userScope.setStep(step);
	}
	
	public String getUserStep() {
		return userScope.getStep();
	}
	
}
