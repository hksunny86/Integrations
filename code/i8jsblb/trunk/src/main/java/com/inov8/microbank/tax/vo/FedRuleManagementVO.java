package com.inov8.microbank.tax.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.tax.model.FEDRuleModel;

/**
 * @author Abu Turab
 *
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FedRuleManagementVO extends BasePersistableModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2935017116569335402L;


	List<FEDRuleModel> fedRuleModelList = new ArrayList<FEDRuleModel>();
	
	List <ProductModel> productModelList =  new ArrayList<ProductModel>();


	public List<FEDRuleModel> getFedRuleModelList() {
		return fedRuleModelList;
	}

	public void setFedRuleModelList(List<FEDRuleModel> fedRuleModelList) {
		this.fedRuleModelList = fedRuleModelList;
	}

	public List<ProductModel> getProductModelList() {
		return productModelList;
	}

	public void setProductModelList(List<ProductModel> productModelList) {
		this.productModelList = productModelList;
	}

	@Override
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
