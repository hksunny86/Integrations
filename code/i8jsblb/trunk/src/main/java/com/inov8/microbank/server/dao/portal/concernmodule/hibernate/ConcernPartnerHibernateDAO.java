package com.inov8.microbank.server.dao.portal.concernmodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerDAO;

public class ConcernPartnerHibernateDAO extends BaseHibernateDAO<ConcernPartnerModel, Long,ConcernPartnerDAO>
implements ConcernPartnerDAO{

	
	
	
	
	public static String HQL_PARTNER_ASSOCIATION = "SELECT DISTINCT cp.concernPartnerId  FROM ConcernPartnerModel cp, ConcernPartnerAsociationModel cpa WHERE " +
	" cp.concernPartnerId = cpa.relationAssociatedPartnerIdConcernPartnerModel.concernPartnerId " +
	"  AND cpa.relationPartnerIdConcernPartnerModel.concernPartnerId = ? " +
	"  AND cpa.active = 1";

	
//	public static String HQL_OP_ASSOCIATION = "SELECT DISTINCT cp.concernPartnerId FROM ConcernPartnerModel cp, " +
//	" ConcernModel c WHERE " +
//	" c.concernCode = ? AND cp.concernPartnerId != ? AND cp.concernPartnerId NOT IN ( " +
//	" SELECT c1.relationInitiatorPartnerIdConcernPartnerModel.concernPartnerId FROM ConcernModel c1 where c1.concernCode = ? ) " +
//	" AND cp.concernPartnerId NOT IN ( " +
//	" SELECT c2.relationRecipientPartnerIdConcernPartnerModel.concernPartnerId FROM ConcernModel c2 where c2.concernCode = ? " +
//	" ) " +
//	" ";
	
	
	public List<ConcernPartnerModel> findConcernPartnerByRetailerId(long retailerId) throws FrameworkCheckedException
	{
		List<ConcernPartnerModel> concernPartnerModelModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ConcernPartnerModel.class);
        detachedCriteria.add( Restrictions.eq("relationRetailerIdRetailerModel.retailerId", retailerId));
        concernPartnerModelModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return concernPartnerModelModelList;
	}
	
	
	public static String HQL_MISSING_PARTNER_IN_RULES = "FROM ConcernPartnerModel c WHERE c.concernPartnerId != ? AND c.active = 1";

	public static String HQL_PARTNERS_IN_RULES = "FROM ConcernPartnerModel c WHERE c.active = 1";
	
	public List findAssociatedPartnerPartners(Long partnerId)
	{
		List list = this.getHibernateTemplate().find(HQL_PARTNER_ASSOCIATION, partnerId);
		return list;
	}

//	public List findAssociatedOPPartners(Long partnerId,String concernCode)
//	{
//		Object param[] = {concernCode, partnerId, concernCode, concernCode};
//		List list = this.getHibernateTemplate().find(HQL_OP_ASSOCIATION, param);
//		return list;
//	}	

	public List findAllOtherPartners(Long partnerId)
	{
		Object param[] = {partnerId};
		List <ConcernPartnerModel>list = this.getHibernateTemplate().find(HQL_MISSING_PARTNER_IN_RULES, param);
		return list;
	}	
	
	public List findAllPartnersIncludingMyself()
	{		
		List <ConcernPartnerModel>list = this.getHibernateTemplate().find(HQL_PARTNERS_IN_RULES);
		return list;
	}
	/* 	 * 
	 * SELECT NAME FROM CONCERN_PARTNER WHERE CONCERN_PARTNER.CONCERN_PARTNER_ID NOT IN ( 2001, 2002, 2000  )
	 * 
	 * 
	 * HQL_PARTNER_ASSOCIATION query
		SELECT CONCERN_PARTNER.CONCERN_PARTNER_ID  
		       FROM CONCERN_PARTNER, CONCERN_PARTNER_ASOCIATION
		       WHERE  
		       CONCERN_PARTNER.CONCERN_PARTNER_ID = CONCERN_PARTNER_ASOCIATION.ASSOCIATED_PARTNER_ID 
		       AND CONCERN_PARTNER_ASOCIATION.PARTNER_ID = 2000       
		       AND CONCERN_PARTNER_ASOCIATION.IS_ACTIVE = 1 
   	 */
	
	
	/* HQL_OP_ASSOCIATION query
		SELECT DISTINCT CONCERN_PARTNER_ID 
		FROM CONCERN_PARTNER, CONCERN_PARTNER_TYPE, CONCERN
		WHERE CONCERN_PARTNER.CONCERN_PARTNER_TYPE_ID = CONCERN_PARTNER_TYPE.CONCERN_PARTNER_TYPE_ID
		AND CONCERN.CONCERN_CODE = 'CCMORH9PN3'
		AND CONCERN_PARTNER.CONCERN_PARTNER_ID != 2002
		AND CONCERN_PARTNER.CONCERN_PARTNER_ID NOT IN (
			SELECT CONCERN.INITIATOR_PARTNER_ID FROM CONCERN WHERE CONCERN.CONCERN_CODE = 'CCMORH9PN3'
		)
		AND CONCERN_PARTNER.CONCERN_PARTNER_ID NOT IN (
			SELECT CONCERN.RECIPIENT_PARTNER_ID FROM CONCERN WHERE CONCERN.CONCERN_CODE = 'CCMORH9PN3'
			
		)
			 
	
	/// QUERY FOR FIND PARTNER ASSSCOATION IN RULES		 
			 
			 
	*/
	
}
