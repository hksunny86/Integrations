package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.model.Level3AccountModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.Level2AccountModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Ajax Controller to delete the temporary files</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Abu Turab
 * @version 1.0
 */
public class MfsAccountDetailsAjxController extends AjaxController
 {

	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		Level3AccountModel level3AccountModel = (Level3AccountModel) session.getAttribute("accountModelLevel3");
		Level2AccountModel level2AccountModel = (Level2AccountModel) session.getAttribute("accountModelLevel2");
		
		
		StringBuffer buffer = new StringBuffer();
		String[] filePaths = new String[29];
		//getting parameters from request
		Long appUserId = null;
		String appUserIdStr = null;
		
		if(level3AccountModel != null){
			//session.removeAttribute("accountModelLevel3");
			appUserId = level3AccountModel.getAppUserId();
		}else if(level2AccountModel != null){
			appUserId = level2AccountModel.getAppUserId();
			
		}else{
			appUserIdStr = ServletRequestUtils.getStringParameter(request,"appUserId");
			if(null!=appUserIdStr && appUserIdStr!="" )
				appUserId = new Long(appUserIdStr);
		}
		
		String actionAuthorizationId = ServletRequestUtils.getStringParameter(request,"actionAuthorizationId");
		
		filePaths[0] = getServletContext().getRealPath("images")+"/upload_dir/"+ appUserId + ".gif";
		filePaths[1] = getServletContext().getRealPath("images")+"/upload_dir/fatcaFormPic_"+ appUserId + ".gif";
		filePaths[2] = getServletContext().getRealPath("images")+"/upload_dir/idBackPic_"+ appUserId + ".gif";
		filePaths[3] = getServletContext().getRealPath("images")+"/upload_dir/idFrontPic_"+ appUserId + ".gif";
		filePaths[4] = getServletContext().getRealPath("images")+"/upload_dir/signPic_"+ appUserId + ".gif";
		filePaths[5] = getServletContext().getRealPath("images")+"/upload_dir/tncPic_"+ appUserId + ".gif";
		
		
		filePaths[6] = getServletContext().getRealPath("images")+"/upload_dir/fatcaFormPic2_"+ appUserId + ".gif";
		filePaths[7] = getServletContext().getRealPath("images")+"/upload_dir/idBackPic2_"+ appUserId + ".gif";
		filePaths[8] = getServletContext().getRealPath("images")+"/upload_dir/idFrontPic2_"+ appUserId + ".gif";
		filePaths[9] = getServletContext().getRealPath("images")+"/upload_dir/signPic2_"+ appUserId + ".gif";
		filePaths[10] = getServletContext().getRealPath("images")+"/upload_dir/tncPic2_"+ appUserId + ".gif";
		
		filePaths[11] = getServletContext().getRealPath("images")+"/upload_dir/fatcaFormPic1_"+ appUserId + ".gif";
		filePaths[12] = getServletContext().getRealPath("images")+"/upload_dir/idBackPic1_"+ appUserId + ".gif";
		filePaths[13] = getServletContext().getRealPath("images")+"/upload_dir/idFrontPic1_"+ appUserId + ".gif";
		filePaths[14] = getServletContext().getRealPath("images")+"/upload_dir/signPic1_"+ appUserId + ".gif";
		filePaths[15] = getServletContext().getRealPath("images")+"/upload_dir/tncPic1_"+ appUserId + ".gif";
		
		filePaths[16] = getServletContext().getRealPath("images")+"/upload_dir/cnicBackPic_"+ appUserId + ".gif";
		filePaths[17] = getServletContext().getRealPath("images")+"/upload_dir/cnicFrontPic_"+ appUserId + ".gif";
		filePaths[18] = getServletContext().getRealPath("images")+"/upload_dir/customerPic_"+ appUserId + ".gif";
		//Added for Action Authorization Images
		filePaths[19] = getServletContext().getRealPath("images")+"/upload_dir/level1FormPic_"+appUserId+".gif";
		
		filePaths[20] = getServletContext().getRealPath("images")+"/upload_dir/authorization/fatcaFormPic_"+actionAuthorizationId+".gif";		  	
		filePaths[21] = getServletContext().getRealPath("images")+"/upload_dir/authorization/tncPic_"+actionAuthorizationId+".gif";
		filePaths[22] = getServletContext().getRealPath("images")+"/upload_dir/authorization/signPic_"+actionAuthorizationId+".gif";
		filePaths[23] = getServletContext().getRealPath("images")+"/upload_dir/authorization/idFrontPic_"+actionAuthorizationId+".gif";
		filePaths[24] = getServletContext().getRealPath("images")+"/upload_dir/authorization/idBackPic_"+actionAuthorizationId+".gif";
		filePaths[25] = getServletContext().getRealPath("images")+"/upload_dir/authorization/level1FormPic_"+actionAuthorizationId+".gif";
		
		filePaths[26] = getServletContext().getRealPath("images")+"/upload_dir/authorization/cnicBackPic_"+actionAuthorizationId+".gif";
		filePaths[27] = getServletContext().getRealPath("images")+"/upload_dir/authorization/cnicFrontPic_"+actionAuthorizationId+".gif";
		filePaths[28] = getServletContext().getRealPath("images")+"/upload_dir/authorization/customerPic_"+actionAuthorizationId+".gif";
		//End Added for Action Authorization Images
		
		
		CommonUtils.deleteFiles(filePaths);
		buffer.append("files deleted successfully");
		
	return buffer.toString();
	}

}
