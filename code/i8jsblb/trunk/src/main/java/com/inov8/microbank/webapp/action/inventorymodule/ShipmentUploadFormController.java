package com.inov8.microbank.webapp.action.inventorymodule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.webapp.action.BaseFormController;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.common.model.ShipmentModel;
import com.inov8.microbank.common.model.inventorymodule.ShipmentUploadModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.inventorymodule.ShipmentBaseWrapper;
import com.inov8.microbank.common.wrapper.inventorymodule.ShipmentBaseWrapperImpl;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;

/**
 * Controller class to upload Files.
 *
 * <p>
 * <a href="FileUploadFormController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Asad Hayat>
 */
public class ShipmentUploadFormController
    extends BaseFormController
{
	protected static Log				logger	= LogFactory.getLog(ShipmentUploadFormController.class);	
  private ProductUnitManager productUnitManager;

  public ShipmentUploadFormController()
  {
    setCommandName("shipmentUploadModel");
    setCommandClass(ShipmentUploadModel.class);

  }

  public ModelAndView processFormSubmission(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Object command,
                                            BindException errors) throws
      Exception
  {
    if (request.getParameter("cancel") != null)
    {
      return new ModelAndView(getCancelView());
    }

    return super.processFormSubmission(request, response, command, errors);
  }

  public ModelAndView onSubmit(HttpServletRequest request,
                               HttpServletResponse response, Object command,
                               BindException errors) throws Exception
  {
	  if(logger.isDebugEnabled())
	  {
		  logger.debug("Inside onSubmit of ShipmentUploadFormController");
	  }
    long quantity = 0;
    double outstandingCredit = 0;

    Double creditAmount = ServletRequestUtils.getDoubleParameter(request,
        "creditAmount");
    Long shipmentId = ServletRequestUtils.getLongParameter(request,
        "shipmentId");
    ShipmentModel shipmentModel = new ShipmentModel();
    shipmentModel.setQuantity(quantity);
    Long productId = ServletRequestUtils.getLongParameter(request,
        "productId");
   

    ShipmentUploadModel fileUpload = (ShipmentUploadModel) command;
    fileUpload.setShipmentId(shipmentId);
    if(logger.isDebugEnabled())
	  {
		  logger.debug("Going to read CSV file");
	  }
    if (fileUpload.getFile().length == 0)
    {
      Object[] args =
          new Object[]
          {
          getText("uploadEntryFile.file", request.getLocale())};
      errors.rejectValue("file", "errors.required", args, "File");

      return showForm(request, response, errors);
    }

    MultipartHttpServletRequest multipartRequest =
        (MultipartHttpServletRequest) request;
    CommonsMultipartFile file =
        (CommonsMultipartFile) multipartRequest.getFile("file");
    

    // the directory to upload to
    String uploadDir =
        getServletContext().getRealPath("/resources") + "/" +
        request.getRemoteUser() + "/";

    // Create the directory if it doesn't exist
    File dirPath = new File(uploadDir);

    if (!dirPath.exists())
    {
      dirPath.mkdirs();
    }

    //retrieve the file data
    InputStream stream = file.getInputStream();

    //write the file to the file specified
    OutputStream bos =
        new FileOutputStream(uploadDir + file.getOriginalFilename());
    int bytesRead = 0;
    byte[] buffer = new byte[8192];

    while ( (bytesRead = stream.read(buffer, 0, 8192)) != -1)
    {
      bos.write(buffer, 0, bytesRead);
    }

    bos.close();

    //close the stream
    stream.close();

    // place the data into the request for retrieval on next page
    request.setAttribute("friendlyName", fileUpload.getUserName());
    request.setAttribute("fileName", file.getOriginalFilename());
    request.setAttribute("contentType", file.getContentType());
    request.setAttribute("size", file.getSize() + " bytes");
//    request.setAttribute("location",
//                         dirPath.getAbsolutePath() + Constants.FILE_SEP +
//                         file.getOriginalFilename());

    String link =
        getServletContext().getRealPath("") + File.separator + "resources" +
        File.separator +
        request.getRemoteUser() + File.separator;

    request.setAttribute("link", link + file.getOriginalFilename());

    CSVReader reader = new CSVReader(new FileReader(link +
        file.getOriginalFilename()));
    String[] nextLine;

    ArrayList<ProductUnitModel> productUnitModelList = new ArrayList();
    ProductUnitModel productUnitModel = null;

    try{
    	
    if(logger.isDebugEnabled())
  	  {
  		  logger.debug("Getting data from file...");
  	  }
    	
    while ( (nextLine = reader.readNext()) != null)
    {
      productUnitModel = new ProductUnitModel();
      int i = 0;
      for(int j = 0; j<nextLine.length;j++)
      {
      if(CommonUtils.isInvalidCharacters(nextLine[j]))
      {
    	  throw new FrameworkCheckedException("InvalidFormat");
      }
      }

      if (fileUpload.getUserName())
      {
    	  if(nextLine[i].contains(" "))
    	  {
    		  throw new FrameworkCheckedException("UsernameIncorrect");
    	  }
        productUnitModel.setUserName(nextLine[i]);
        i++;
      }

      if (fileUpload.getPin())
      {
        productUnitModel.setPin(nextLine[i]);
        i++;
      }

      if (fileUpload.getSerialNo())
      {
        productUnitModel.setSerialNo(nextLine[i]);
        i++;
      }

      if (fileUpload.getAdditionalField1())
      {
        productUnitModel.setAdditionalField1(nextLine[i]);
        i++;
      }

      if (fileUpload.getAdditionalField2())
      {
        productUnitModel.setAdditionalField2(nextLine[i]);
        i++;
      }
      long theDate = new Date().getTime();
      productUnitModel.setShipmentId(fileUpload.getShipmentId());
      productUnitModel.setSold(false);
      productUnitModel.setActive(true);
      productUnitModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      productUnitModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());

      productUnitModel.setProductId(productId);
      ProductUnitModel tempProductUnitModel =new ProductUnitModel();
      productUnitModel.setUpdatedOn(new Date(theDate));
      productUnitModel.setCreatedOn(new Date(theDate));
      tempProductUnitModel.setProductId(productUnitModel.getProductId());
      tempProductUnitModel.setShipmentId(productUnitModel.getShipmentId());
      tempProductUnitModel.setSerialNo(productUnitModel.getSerialNo());
      tempProductUnitModel.setPin(productUnitModel.getPin());
      productUnitManager.isPriceUnitEmpty(productId);
      int recordCount = productUnitManager.isProductUnit(tempProductUnitModel);
      if (recordCount != 0)
      {
    	  throw new FrameworkCheckedException("Recordsalreadyexist");
    	  
      }
      
      productUnitModelList.add(productUnitModel);
      quantity++;
      
      
      i++;
      
      
    }
    if(logger.isDebugEnabled())
	  {
		  logger.debug("Going to update product shipment...");
	  }
    
    }
    catch( Exception e )
    {
    	
    	e.printStackTrace();
//    	productUnitModelList.clear();
    	
    	if( e.getMessage().equalsIgnoreCase("Recordsalreadyexist") )
		{
			super.saveMessage(request, "Records already exist.");
			return super.showForm(request, response, errors);				
		}
    	else if( e.getMessage().equalsIgnoreCase("Priceunitempty") )
		{
			super.saveMessage(request, "Unit Price for the product should be defined before adding a product unit.");
			return super.showForm(request, response, errors);				
		}
    	
    	else if( e.getMessage().equalsIgnoreCase("InvalidFormat") )
		{
			super.saveMessage(request, "The input file contains invalid characters");
			return super.showForm(request, response, errors);				
		}
    	else if( e.getMessage().equalsIgnoreCase("UsernameIncorrect") )
		{
			super.saveMessage(request, "User Name is incorrect");
			return super.showForm(request, response, errors);				
		}
    	else
    	{
    		super.saveMessage(request,"Invalid file format.");
    	
    	return super.showForm(request, response, errors);
    	}
    }

    //outstandingCredit = quantity * creditAmount;
    //shipmentModel.setQuantity(quantity);
    //shipmentModel.setOutstandingCredit(outstandingCredit);
    //shipmentModel.setShipmentId(shipmentId);

    reader.close();

    //BaseWrapper baseWrapper = new BaseWrapperImpl();
   // baseWrapper.setBasePersistableModel(shipmentModel);
    //this.productUnitManager.updateShipment(baseWrapper);

    ShipmentBaseWrapper shipmentBaseWrapper = new ShipmentBaseWrapperImpl();
    shipmentBaseWrapper.setProductUnitModelList(productUnitModelList);

    try
	{
		productUnitManager.updateShipmentreadfromcsvfile(shipmentBaseWrapper);
		
		this.saveMessage(request, "The file has been successfully uploaded.");
		
	}
	catch (FrameworkCheckedException e)
	{
		
		
		
		if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
	          e.getErrorCode())
	      {

	        super.saveMessage(request,
	                          "Records already exist.");
	        return super.showForm(request, response, errors);
	      }
		
	}

    return new ModelAndView(getSuccessView());
  }

  public void setProductUnitManager(ProductUnitManager productUnitManager)
  {
    this.productUnitManager = productUnitManager;
  }
}
