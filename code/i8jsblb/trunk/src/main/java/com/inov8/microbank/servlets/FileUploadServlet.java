package com.inov8.microbank.servlets;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL_ONE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL_THREE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_MSG_ID;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESGS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_ERROR;
import static com.inov8.microbank.common.util.XMLConstants.TAG_ERRORS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MSG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


//IOStream
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


import javax.servlet.annotation.MultipartConfig;
//Servlet3.0 specific annotations
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Part;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.MessageUtil;

/**
 * @author Soofiafa
 * 
 */

@WebServlet
@MultipartConfig(maxFileSize = 400000L)
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private PrintWriter out;

	protected final Log logger = LogFactory.getLog(FileUploadServlet.class);

	public FileUploadServlet() {
		super();
		if (logger.isDebugEnabled()) {
			logger.debug("Start/End of FileUploadServlet(): FileUploadServlet Initialized & Instantiated");
		}
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("Start of FileUploadServlet.service()");
		}
		out = response.getWriter();

		fileUploadWithDesiredFilePathAndName(request);

		out.println(toXML());

		if (logger.isDebugEnabled()) {
			logger.debug("End of FileUploadServlet.service()");
		}
	}

	/*
	 * Following method allows us to place the uploaded file in a desired
	 * Location on the server along with the desired fileName
	 */
	public void fileUploadWithDesiredFilePathAndName(HttpServletRequest request)
			throws IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("Start of fileUploadWithDesiredFilePathAndName()");
		}

		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			for (Part part : request.getParts()) {
				inputStream = request.getPart(part.getName()).getInputStream();
				int i = inputStream.available();
				byte[] b = new byte[i];
				inputStream.read(b);
				logger.info("FileUploadServlet.service(): " + "Length : "
						+ b.length);

				String fileName = "";
				String partHeader = part.getHeader("content-disposition");
				logger.info("FileUploadServlet.service(): " + "Part Header = "
						+ partHeader);
				logger.info("FileUploadServlet.service(): "
						+ "part.getHeader(\"content-disposition\") = "
						+ part.getHeader("content-disposition"));

				for (String temp : part.getHeader("content-disposition").split(
						";")) {
					if (temp.trim().startsWith("filename")) {
						fileName = temp.substring(temp.indexOf('=') + 1).trim()
								.replace("\"", "");
					}
				}

				ServletContext context = getServletContext();
				String uploadDir = context.getRealPath("/images/upload_dir");
				logger.info("FileUploadServlet.service(): "
						+ "File will be Uploaded at: " + uploadDir + "/"
						+ fileName);
				outputStream = new FileOutputStream(uploadDir + "/" + fileName);
				outputStream.write(b);
				inputStream.close();
			}
		} catch (Exception e) {
			out.println(toErrorXML());
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("End of fileUploadWithDesiredFilePathAndName()");
		}
	}

	private String toXML() {
		if (logger.isDebugEnabled()) {
			logger.debug("Start of FileUploadServlet.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_MSG)
				.append(TAG_SYMBOL_SPACE).append(ATTR_MSG_ID)
				.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE).append("-1")
				.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE);
		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_MESGS)
				.append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN)
				.append(TAG_MESG).append(TAG_SYMBOL_SPACE).append(ATTR_LEVEL)
				.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
				.append(ATTR_LEVEL_ONE).append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE);
		strBuilder.append(MessageUtil
				.getMessage("fileUploadServlet.fileUploadSuccessful"));
		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
				.append(TAG_MESG).append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
				.append(TAG_MESGS).append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN_SLASH).append(TAG_MSG)
				.append(TAG_SYMBOL_CLOSE);

		if (logger.isDebugEnabled()) {
			logger.debug("End of FileUploadServlet.toXML()");
		}
		return strBuilder.toString();
	}

	private String toErrorXML() {
		if (logger.isDebugEnabled()) {
			logger.debug("Start of FileUploadServlet.toErrorXML()");
		}
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_MSG)
				.append(TAG_SYMBOL_SPACE).append(ATTR_MSG_ID)
				.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE).append("-1")
				.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE);
		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_ERRORS)
				.append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN)
				.append(TAG_ERROR).append(TAG_SYMBOL_SPACE).append(ATTR_CODE)
				.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
				.append(ErrorCodes.FILE_UPLOAD_ERROR).append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_SPACE).append(ATTR_LEVEL)
				.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
				.append(ATTR_LEVEL_THREE).append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_CLOSE);
		strBuilder.append(MessageUtil
				.getMessage("fileUploadServlet.fileUploadUnSuccessful"));
		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
				.append(TAG_ERROR).append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
				.append(TAG_ERRORS).append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN_SLASH).append(TAG_MSG)
				.append(TAG_SYMBOL_CLOSE);

		if (logger.isDebugEnabled()) {
			logger.debug("End of FileUploadServlet.toErrorXML()");
		}
		return strBuilder.toString();
	}
}