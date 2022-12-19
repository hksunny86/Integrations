package com.inov8.microbank.common.util;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;

import com.inov8.framework.common.util.EncoderUtils;

import edu.vt.middleware.dictionary.ArrayWordList;
import edu.vt.middleware.dictionary.WordListDictionary;
import edu.vt.middleware.dictionary.WordLists;
import edu.vt.middleware.dictionary.sort.ArraysSort;
import edu.vt.middleware.password.AlphabeticalSequenceRule;
import edu.vt.middleware.password.DictionarySubstringRule;
import edu.vt.middleware.password.NumericalSequenceRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.QwertySequenceRule;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;



public class PasswordConfigUtil {

	
	
	static PasswordResultDTO passwordResultDTO = new PasswordResultDTO( );
	static String PASSWORD_PATTERN = "((?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\\\!\"#$%&()*+,`./:;<=>?@\\[\\]^_{|}~]).{8,40})";
	static String PASSWORD_SPECIAL_CHARACTERS = "[\\\\!\"#$%&()*+,`./:;<=>?@\\[\\]^_{|}~]";
	static String PASSWORD_DIGITS = "[0-9]";
	static String PASSWORD_ALPHABETS = "[a-zA-Z]";
	
	public static PasswordResultDTO isValidPasswordStrength( PasswordInputDTO passwordInputDTO ){
		passwordResultDTO = new PasswordResultDTO();
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(passwordInputDTO.getPassword());
		
		if (matcher.matches()){
			passwordResultDTO.setIsValid(true);
		}else{
			passwordResultDTO.setIsValid(false);
			passwordResultDTO.getErrorMessages().add("New password does not match the password policy i.e, minimum 8 characters including digits, alphabets and special characters");
		}
		
		return passwordResultDTO;
	}
	
	public static PasswordResultDTO isPasswordContainsDictionaryWord( PasswordInputDTO passwordInputDTO, File dictionary ){
		passwordResultDTO = new PasswordResultDTO();
		ArrayWordList awl = null;
		try {
			awl = WordLists.createFromReader(
			  new FileReader[] {new FileReader(dictionary)},
			  true,
			  new ArraysSort());
		} catch (Exception e) {
			e.printStackTrace();
			passwordResultDTO.getErrorMessages().add(e.getMessage());
		}
		
		String digitFilterdPassword = passwordInputDTO.getPassword().replaceAll(PASSWORD_DIGITS, "");
		String charDigitFilteredPassword = digitFilterdPassword.replaceAll(PASSWORD_SPECIAL_CHARACTERS, ""); 

		// create a dictionary for searching
		WordListDictionary dict = new WordListDictionary(awl);

		DictionarySubstringRule dictRule = new DictionarySubstringRule(dict);
		dictRule.setWordLength(4); // size of words to check in the password
		dictRule.setMatchBackwards(false); // match dictionary words backwards

		List<Rule> ruleList = new ArrayList<Rule>();
		ruleList.add(dictRule);

		PasswordValidator validator = new PasswordValidator(ruleList);
		PasswordData passwordData = new PasswordData(new Password(charDigitFilteredPassword.toLowerCase()));

		RuleResult result = validator.validate(passwordData);
		passwordResultDTO.setIsValid(result.isValid());
		if (!result.isValid()){
			passwordResultDTO.getErrorMessages().add("Password should not contain common english dictionary words");
		}
		return passwordResultDTO;
}
	public static PasswordResultDTO validateAlphaSequenceRule ( PasswordInputDTO passwordInputDTO ){
		passwordResultDTO = new PasswordResultDTO();
		
		AlphabeticalSequenceRule 	 alphaSeqRule	 = null;
		
		QwertySequenceRule 			 qwertySeqRule 	 = null;
		
		List < Rule > ruleList =     new ArrayList<Rule>();
		
		String digitFilterdPassword = passwordInputDTO.getPassword().replaceAll(PASSWORD_DIGITS, "");
		String charDigitFilteredPassword = digitFilterdPassword.replaceAll(PASSWORD_SPECIAL_CHARACTERS, "");
		
		alphaSeqRule = new AlphabeticalSequenceRule(3, false );
		ruleList.add(alphaSeqRule);
		
		qwertySeqRule = new QwertySequenceRule();
		ruleList.add(qwertySeqRule);
		
		PasswordValidator validator = new PasswordValidator(ruleList);
		PasswordData passwordData = new PasswordData(new Password(charDigitFilteredPassword));

		RuleResult ruleResult = validator.validate(passwordData);
		passwordResultDTO.setIsValid(ruleResult.isValid());
		if(!ruleResult.isValid()){
			passwordResultDTO.getErrorMessages().add("Password should not be in a predictable sequence");
		}
		return passwordResultDTO;
	}
	
	public static PasswordResultDTO validateNumericSequenceRule (PasswordInputDTO passwordInputDTO){
		passwordResultDTO = new PasswordResultDTO();
		NumericalSequenceRule 		 numSeqRule 	 = null;
		List < Rule > ruleList =     new ArrayList<Rule>();
		String alphaFilterdPassword = passwordInputDTO.getPassword().replaceAll(PASSWORD_ALPHABETS, "");
		String charAlphaFilteredPassword = alphaFilterdPassword.replaceAll(PASSWORD_SPECIAL_CHARACTERS, "");
		numSeqRule = new NumericalSequenceRule(3, false);
		ruleList.add(numSeqRule);
		PasswordValidator validator = new PasswordValidator(ruleList);
		PasswordData passwordData = new PasswordData(new Password(charAlphaFilteredPassword));

		RuleResult ruleResult = validator.validate(passwordData);
		passwordResultDTO.setIsValid(ruleResult.isValid());
		if(!ruleResult.isValid()){
			passwordResultDTO.getErrorMessages().add("Password should not be in a predictable sequence");
		}
		return passwordResultDTO;

	}
	
	public static PasswordResultDTO isPasswordContainsUserIDOrName ( PasswordInputDTO passwordInputDTO ){
		passwordResultDTO = new PasswordResultDTO();
		passwordResultDTO.setIsValid(true);
		String digitFilterdPassword = passwordInputDTO.getPassword().replaceAll(PASSWORD_DIGITS, "");
		String charDigitFilteredPassword = digitFilterdPassword.replaceAll(PASSWORD_SPECIAL_CHARACTERS, "");
		
		if (passwordInputDTO.getPassword().toLowerCase().contains(passwordInputDTO.getFirstName().toLowerCase()) || passwordInputDTO.getPassword().toLowerCase().contains(passwordInputDTO.getLastName().toLowerCase()) ||
				digitFilterdPassword.toLowerCase().contains(passwordInputDTO.getFirstName().toLowerCase() ) || digitFilterdPassword.toLowerCase().contains(passwordInputDTO.getLastName().toLowerCase())||
				charDigitFilteredPassword.contains(passwordInputDTO.getFirstName()) || charDigitFilteredPassword.contains(passwordInputDTO.getLastName())){
			passwordResultDTO.setIsValid(false);
		}
		
		if (passwordInputDTO.getUserName().equalsIgnoreCase(charDigitFilteredPassword)){
			passwordResultDTO.setIsValid(false);
		}
		
		if (passwordInputDTO.getFirstName().equalsIgnoreCase(charDigitFilteredPassword)){
			passwordResultDTO.setIsValid(false);
		}
		
		if (passwordInputDTO.getLastName().equalsIgnoreCase(charDigitFilteredPassword)){
			passwordResultDTO.setIsValid(false);
		}
		
		if(!passwordResultDTO.getIsValid()){
			passwordResultDTO.getErrorMessages().add("First Name, Last Name and User Name cannot be used in password");
		}
		
		return passwordResultDTO;
	}
	
	public static PasswordResultDTO isPassowrdContainsHistoryPassword( PasswordInputDTO passwordInputDTO ){
		passwordResultDTO = new PasswordResultDTO();
		passwordResultDTO.setIsValid(true);
		List<String> historyPasswords = passwordInputDTO.getHistoryPasswords();
		if (CollectionUtils.isNotEmpty(historyPasswords)){
			for (String oldPassword : historyPasswords) {
				if ( oldPassword.equals(EncoderUtils.encodeToSha( passwordInputDTO.getPassword() )) ){
					passwordResultDTO.setIsValid(false);
					passwordResultDTO.getErrorMessages().add("New password can not be from last five passwords");
					break;
				}
			}
		}
		return passwordResultDTO;
	}

	public static PasswordResultDTO validatePasswordPolicy ( PasswordInputDTO passwordInputDTO, File dictionary ){
		if(passwordInputDTO.getPassword() == null){
			passwordResultDTO = new PasswordResultDTO();
			return passwordResultDTO;
		}
		passwordResultDTO = new PasswordResultDTO();
		passwordResultDTO.setIsValid(true);
		passwordResultDTO = isValidPasswordStrength(passwordInputDTO);
		if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0 ){
			return passwordResultDTO;
		}
		/*
		passwordResultDTO = new PasswordResultDTO();
		passwordResultDTO = isPasswordContainsDictionaryWord(passwordInputDTO, dictionary);
		if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0 ){
			return passwordResultDTO;
		}
		passwordResultDTO = new PasswordResultDTO();
		passwordResultDTO = validateAlphaSequenceRule(passwordInputDTO);
		if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0 ){
			return passwordResultDTO;
		}
		passwordResultDTO = new PasswordResultDTO();
		passwordResultDTO = validateNumericSequenceRule(passwordInputDTO);
		if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0 ){
			return passwordResultDTO;
		}*/
		passwordResultDTO = new PasswordResultDTO();
		passwordResultDTO = isPasswordContainsUserIDOrName(passwordInputDTO); 
		if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0 ){
			return passwordResultDTO;
		}
		passwordResultDTO = new PasswordResultDTO();
		passwordResultDTO = isPassowrdContainsHistoryPassword(passwordInputDTO);
		if ( passwordResultDTO.getErrorMessages() != null && passwordResultDTO.getErrorMessages().size() > 0 ){
			return passwordResultDTO;
		}
		
		return passwordResultDTO;
		
	}
	
}
