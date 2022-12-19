package com.inov8.microbank.webapp.action.lescobillcollection;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.LescoCollectionModel;
import com.inov8.microbank.server.service.lescomodule.LescoCollectionManager;

public class LescoFileDownloadController implements Controller {

	private LescoCollectionManager lescoCollectionManager;

	

	public LescoFileDownloadController() {

	}


	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Long id = ServletRequestUtils.getLongParameter(request, "id");
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		LescoCollectionModel lescoCollectionModel = new LescoCollectionModel();
		lescoCollectionModel.setLescoCollectionId(id);

			baseWrapper.setBasePersistableModel(lescoCollectionModel);
			baseWrapper = this.lescoCollectionManager.loadLescoCollection(baseWrapper);
					
			lescoCollectionModel = (LescoCollectionModel) baseWrapper.getBasePersistableModel();
			String file = lescoCollectionModel.getFileData();
			

			/*SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

			String filename = "";
			if("0".equals(actionName)){
				filename = "AccountManagementLog_" + sf.format(new Date()) + ".csv";
			}else if("1".equals(actionName)){ 
				filename = "PINChangeLog_" + sf.format(new Date()) + ".csv";
			}else{
				filename = "TransactionStatusLog_" + sf.format(new Date()) + ".csv";
			}
			*/
			
			response.setContentType("text/lbc; charset=ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ lescoCollectionModel.getFileName()+".lbc" + "\"");

			PrintWriter p = response.getWriter();
			p.write(file);

//			System.out.println(buffer.toString());
			return null;
		
	}

	public void setLescoCollectionManager(LescoCollectionManager lescoCollectionManager)
	{
		this.lescoCollectionManager = lescoCollectionManager;
	}

}
