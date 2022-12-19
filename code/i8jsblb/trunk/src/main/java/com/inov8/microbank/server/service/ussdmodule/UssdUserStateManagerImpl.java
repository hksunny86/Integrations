package com.inov8.microbank.server.service.ussdmodule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;

import com.ibm.icu.util.Calendar;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.UserStateModel;
import com.inov8.microbank.common.vo.ussd.UserState;
import com.inov8.microbank.server.dao.ussdmodule.UssdUserStateDAO;

public class UssdUserStateManagerImpl implements UssdUserStateManager{
	private UssdUserStateDAO ussdUserStateDAO;
	private GenericDao genericDAO;

	public UserState findUserState(String msisdn, int senderID) throws FrameworkCheckedException {
		UserState retVal=null;
		StringBuilder queryBuiler=new StringBuilder();
		queryBuiler.append("from UserStateModel ");
		queryBuiler.append("where msisdn =? or senderId=?");
		List result = genericDAO.findByHQL(queryBuiler.toString(),
				new Object[] { msisdn,senderID});
		if(result!=null && result.size() >0){
			UserStateModel userStateModel= (UserStateModel)result.get(0);
			if(userStateModel!=null && userStateModel.getState()!=null){
				retVal=(UserState)this.toObject( this.toByteArray(userStateModel.getState()));
			}
		}
		return retVal;
	}

	public UserState findUserState(String mobileNo) throws FrameworkCheckedException {
		UserState retVal=null;
		StringBuilder queryBuiler=new StringBuilder();
		queryBuiler.append("from UserStateModel ");
		queryBuiler.append("where  msisdn =?");
		List result = genericDAO.findByHQL(queryBuiler.toString(),
				new Object[] {mobileNo});
		if(result!=null && result.size() >0){
			UserStateModel userStateModel= (UserStateModel)result.get(0);
			if(userStateModel!=null && userStateModel.getState()!=null){
				retVal=(UserState)this.toObject( this.toByteArray(userStateModel.getState()));
				if(retVal.getUserStateModelId()==null){
					retVal.setUserStateModelId(userStateModel.getPrimaryKey());
				}
			}
		}
		return retVal;
	}
	public UserStateModel saveUserState(UserState userState) throws FrameworkCheckedException {
		UserStateModel retVal=null;
		
		if (userState!=null) {
			retVal=new UserStateModel();
			if(userState.getUserStateModelId()!=null){
				retVal.setUserStateId(userState.getUserStateModelId());
			}
			retVal.setCreationDate(userState.getCreationDate());
			retVal.setMsisdn(userState.getUserMsisdn());
//			retVal.setSenderId(Integer.valueOf(userState.getSenderID()).longValue());
			byte[] userStateBytes=toByteArray(userState);
			Blob userStateBlob=Hibernate.createBlob(userStateBytes);
			retVal.setState(userStateBlob);
			retVal.setAccessDate(Calendar.getInstance().getTime());
			retVal=ussdUserStateDAO.saveOrUpdate(retVal);
		}
		return null;
	}

	public boolean deleteUserState(String msisdn){
		return this.ussdUserStateDAO.deleteUserState(msisdn);
	}
	
	private byte[] toByteArray(Blob fromBlob) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			return toByteArrayImpl(fromBlob, baos);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos) throws SQLException, IOException {
		byte[] buf = new byte[4000];
		InputStream is = fromBlob.getBinaryStream();
		try {
			for (;;) {
				int dataSize = is.read(buf);
				if (dataSize == -1)
					break;
				baos.write(buf, 0, dataSize);
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
				}
			}
		}
		return baos.toByteArray();
	}
	private byte[] toByteArray (Object obj)
	{
	  byte[] bytes = null;
	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
	  try {
	    ObjectOutputStream oos = new ObjectOutputStream(bos); 
	    oos.writeObject(obj);
	    oos.flush(); 
	    oos.close(); 
	    bos.close();
	    bytes = bos.toByteArray ();
	  }
	  catch (IOException ex) {
	    //TODO: Handle the exception
	  }
	  return bytes;
	}
	    
	private Object toObject (byte[] bytes)
	{
	  Object obj = null;
	  try {
	    ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
	    ObjectInputStream ois = new ObjectInputStream (bis);
	    obj = ois.readObject();
	  }
	  catch (IOException ex) {
	    //TODO: Handle the exception
	  }
	  catch (ClassNotFoundException ex) {
	    //TODO: Handle the exception
	  }
	  return obj;
	}
		  
		

	public GenericDao getGenericDAO() {
		return genericDAO;
	}

	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}

	public UssdUserStateDAO getUssdUserStateDAO() {
		return ussdUserStateDAO;
	}

	public void setUssdUserStateDAO(UssdUserStateDAO ussdUserStateDAO) {
		this.ussdUserStateDAO = ussdUserStateDAO;
	}

	

}
