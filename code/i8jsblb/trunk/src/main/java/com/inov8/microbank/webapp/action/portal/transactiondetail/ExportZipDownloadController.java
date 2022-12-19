package com.inov8.microbank.webapp.action.portal.transactiondetail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.ExportInfoModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;

public class ExportZipDownloadController extends BaseSearchController{
	
	public ExportZipDownloadController() {
		super.setFilterSearchCommandClass(ExportInfoModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object model, HttpServletRequest request,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		
		
		String exportDir = MessageUtil.getMessage("export.directory");
	    String exportDirPath = request.getServletContext().getRealPath(exportDir);
		String customizedExportPath = exportDirPath+File.separator+UserUtils.getCurrentUser().getUsername();
	    
		///Check if Customized Directory Exists 
		Path path = Paths.get(customizedExportPath);
		boolean pathExists =Files.exists(path,new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
		if(!pathExists)
			Files.createDirectory(path);
		/// End Check if Customized Directory Exists 
		
		File folder = new File(customizedExportPath);
		
		File[] fileArray = folder.listFiles();		
		Arrays.sort(fileArray, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
		List<File> filesList = Arrays.asList(fileArray);
		
		List<String> fileNames = new ArrayList<>();
		List<String> downloadLinks = new ArrayList<>();
		
		
		for (File f : filesList) {
			fileNames.add(f.getName());
			downloadLinks.add(request.getContextPath()+"/"+exportDir+"/"+UserUtils.getCurrentUser().getUsername()+"/"+f.getName());
		}
		
		Map<String,Object> modelMap = new HashMap<>();
	
		modelMap.put("files", fileNames);
		modelMap.put("downloadLinks", downloadLinks);
		modelMap.put("totalFiles", fileNames.size());

		
		pagingHelperModel.setTotalRecordsCount(fileNames.size());
		
		return new  ModelAndView(getSearchView(),modelMap);
	}

}
