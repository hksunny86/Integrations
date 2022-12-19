/**
 * @author AtifHu
 */
package com.inov8.microbank.common.vo.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import com.inov8.microbank.common.model.ProductLimitRuleModel;

public class ProductLimitRuleVo implements Serializable {

	private Long productLimitRuleId;
	private Long productId;
	private Long deviceTypeId;
	private Long segmentId;
	private Long distributorId;
	private Long accountTypeId;
	private boolean active;

	private Collection<ProductLimitRuleModel> productLimitRuleModelList = LazyList
			.decorate(new ArrayList<ProductLimitRuleModel>(), new Factory() {
				public Object create() {
					return new ProductLimitRuleModel();
				}
			});

	public void addProductLimitRuleModel(
			ProductLimitRuleModel productLimitRuleModel) {
		productLimitRuleModelList.add(productLimitRuleModel);
	}

	public ProductLimitRuleVo() {
		// TODO Auto-generated constructor stub
	}

	public Collection<ProductLimitRuleModel> getProductLimitRuleModelList() {
		return productLimitRuleModelList;
	}

	public void setProductLimitRuleModelList(
			Collection<ProductLimitRuleModel> productLimitRuleModelList) {
		this.productLimitRuleModelList = productLimitRuleModelList;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public Long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getProductLimitRuleId() {
		return productLimitRuleId;
	}

	public void setProductLimitRuleId(Long productLimitRuleId) {
		this.productLimitRuleId = productLimitRuleId;
	}

	public Long getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
}
