/**
 * 
 */
package com.inov8.microbank.common.vo.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import com.inov8.microbank.common.model.ProductChargesRuleModel;

/**
 * @author NaseerUl
 *
 */
public class ProductChargesRuleVo implements Serializable
{
	private static final long serialVersionUID = -208083065945438117L;

	private Long productId;
	private String productName;

	private List<ProductChargesRuleModel> productChargesRuleModelList;

	@SuppressWarnings("unchecked")
	public ProductChargesRuleVo()
	{
		super();
		productChargesRuleModelList = LazyList.decorate(new ArrayList<ProductChargesRuleModel>(), new Factory() {
			@Override
			public ProductChargesRuleModel create() {
				return new ProductChargesRuleModel();
			}
		});
	}

	public ProductChargesRuleVo(Long productId, String productName)
	{
		this();
		this.productId = productId;
		this.productName = productName;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public ProductChargesRuleModel getProductChargesRuleModel(int index)
	{
		return productChargesRuleModelList.get(index);
	}

	public void addProductChargesRuleModel(ProductChargesRuleModel productChargesRuleModel)
	{
		productChargesRuleModelList.add(productChargesRuleModel);
	}

	public List<ProductChargesRuleModel> getProductChargesRuleModelList()
	{
		return productChargesRuleModelList;
	}

	public void setProductChargesRuleModelList(List<ProductChargesRuleModel> productChargesRuleModelList)
	{
		this.productChargesRuleModelList = productChargesRuleModelList;
	}

}
