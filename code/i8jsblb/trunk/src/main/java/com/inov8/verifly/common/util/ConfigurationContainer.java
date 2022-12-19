package com.inov8.verifly.common.util;

import java.util.HashMap;
import java.util.List;

import com.inov8.framework.common.util.CustomList;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.ThreadLocalEncryptionType;
import com.inov8.verifly.common.constants.ConfigurationConstants;
import com.inov8.verifly.common.model.VeriflyConfigurationModel;
import com.inov8.verifly.server.dao.mainmodule.VeriflyConfigurationDAO;



public class ConfigurationContainer {
	//private static ConfigurationContainer object = null;
	private HashMap <Long,String> configurationHashMap  = new HashMap <Long,String>();
	private VeriflyConfigurationDAO veriflyConfigurationDao;
	
//	public synchronized static EncryptionKeys getInstance () {
//		if(object==null)
//			object = new EncryptionKeys();
//		return object;
//	}

	public ConfigurationContainer (VeriflyConfigurationDAO veriflyConfigurationDao) {
		this.veriflyConfigurationDao = veriflyConfigurationDao ;
		this.initData();
	}
	
	
	
	
	private void initData () {
		
        CustomList<VeriflyConfigurationModel>
        customlist = this.veriflyConfigurationDao.findAll();
        List<VeriflyConfigurationModel> list = customlist.getResultsetList();	
        for (VeriflyConfigurationModel veriflyConfigurationModel : list) {
        	configurationHashMap.put(veriflyConfigurationModel.getPrimaryKey(),
                             veriflyConfigurationModel.getValue());
        }
	
	}
	
	public  String getValue (Long key) {
		String str = null ;
		if (key != null)
			str = configurationHashMap.get (key);
		return str;
	}
	
	 public  Object getEncrptionClassObject() {
	        Object object = null;
	        String encrptorClassName = null;
	        
	        /*********************************************************************************
	         * Updated by Soofia Faruq
	         * AES Encryption Support
	         */        
	  if(ThreadLocalEncryptionType.getEncryptionType() != null && 
			  ThreadLocalEncryptionType.getEncryptionType()==EncryptionUtil.ENCRYPTION_TYPE_AES) 
	  {
	   encrptorClassName = getValue(ConfigurationConstants.AES_ENCRYPTOR_CLASS);
	//   ThreadLocalEncryptionType.remove();
	  } else {
	   encrptorClassName = getValue(ConfigurationConstants.ENCRYPTOR_CLASS);
	  }
	  
	  encrptorClassName = getValue(ConfigurationConstants.AES_ENCRYPTOR_CLASS); // 	TODO Remove this line.
	  
	  /**********************************************************************************/
	         
	        if (encrptorClassName == null)
	            encrptorClassName = ConfigurationConstants.DEFAULT_ENCRYPT_CLASS;

	        try {
	            Class classDefinition = Class.forName(encrptorClassName);
	            object = classDefinition.newInstance();
	        } catch (InstantiationException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	        return object;
	    }
	
    /*public  Object getEncrptionClassObject() {
        Object object = null;
        String encrptorClassName = getValue(ConfigurationConstants.ENCRYPTOR_CLASS);
       
        if (encrptorClassName == null)
            encrptorClassName = ConfigurationConstants.DEFAULT_ENCRYPT_CLASS;

        try {
            Class classDefinition = Class.forName(encrptorClassName);
            object = classDefinition.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }*/


	public VeriflyConfigurationDAO getVeriflyConfigurationDao() {
		return veriflyConfigurationDao;
	}

	public void setVeriflyConfigurationDao(
			VeriflyConfigurationDAO veriflyConfigurationDao) {
		this.veriflyConfigurationDao = veriflyConfigurationDao;
	}
	

}
