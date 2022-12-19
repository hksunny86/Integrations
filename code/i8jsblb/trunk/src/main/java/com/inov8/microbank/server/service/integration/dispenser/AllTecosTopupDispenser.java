package com.inov8.microbank.server.service.integration.dispenser;

import java.util.List;

import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;

public class AllTecosTopupDispenser
{
	private ProductDispenseController productDispenseController ;
	private MessageSource messageSource;
	
	private String [] productIds;
	
	public AllTecosTopupDispenser()
	{		
		
	}
	public AllTecosTopupDispenser( String... arguments )
	{		
		productIds = arguments ;
	}

	public WorkFlowWrapper doSale( WorkFlowWrapper workFlowWrapper ) throws FrameworkCheckedException
	{
		
		WorkFlowWrapper tempWorkFlowWrapper = new WorkFlowWrapperImpl();
		tempWorkFlowWrapper.setProductModel(new ProductModel());
		tempWorkFlowWrapper.getProductModel().setProductId(workFlowWrapper.getProductModel().getProductId());
		
		
		ProductDispenser productDispense;
		
		String [] otherProductIds = this.getOtherProductIds(workFlowWrapper);
		
		
		for( int count=0; count < this.productIds.length; count++ )
		{
			try
			{
				productDispense = (ProductDispenser)this.productDispenseController.loadProductDispenserByProductId( workFlowWrapper );
				workFlowWrapper.setProductVO( this.productDispenseController.loadProductVOWithoutLoadingProd(workFlowWrapper) ) ;
				((TransactionDetailModel)((List)workFlowWrapper.getTransactionModel().getTransactionIdTransactionDetailModelList()).get(0)).setProductIdProductModel(workFlowWrapper.getProductModel()) ;
				
				workFlowWrapper = productDispense.doSale( workFlowWrapper ) ;
				workFlowWrapper.setProductDispenser(productDispense);				
			}
			catch (FrameworkCheckedException e1)
			{
				if( !workFlowWrapper.isCallNextProductDispenser() )
				{
					throw e1;
				}
				e1.printStackTrace();
			}
			catch (Exception e1)
			{
				if( !workFlowWrapper.isCallNextProductDispenser() )
				{
					throw new FrameworkCheckedException(e1.getMessage(),e1) ;
				}
				e1.printStackTrace();
			}
			
			if( workFlowWrapper.isCallNextProductDispenser() )
			{
				workFlowWrapper.setProductModel(new ProductModel());
				workFlowWrapper.getProductModel().setProductId( Long.parseLong( otherProductIds[count])) ;
			}
			else
				break;			
		}	
		
		return workFlowWrapper;
	}

	private String[] getOtherProductIds(WorkFlowWrapper workFlowWrapper)
	{
		String [] otherProductIds = new String[productIds.length];
		int otherProductIdsIndex = 0 ;
		
		for( int count=0 ; count < this.productIds.length; count++ )
		{
			if( !productIds[count].equals(workFlowWrapper.getProductModel().getProductId().toString()) )
			{
				otherProductIds[otherProductIdsIndex++] = productIds[count];				
			}
		}
		
		
		return otherProductIds;
	}

	public void setProductDispenseController(ProductDispenseController productDispenseController)
	{
		this.productDispenseController = productDispenseController;
	}

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
	
}
