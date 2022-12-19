package com.inov8.integration.channel.jsdebitcard.service;

import com.inov8.integration.channel.jsdebitcard.request.DebitCardExportRequest;
import com.inov8.integration.channel.jsdebitcard.response.DebitCardExportResponse;
import com.inov8.integration.channel.jsdebitcard.response.DebitCardImportResponse;
import com.inov8.integration.channel.jsdebitcard.utill.PGPUtils;
//import com.inov8.integration.channel.kmbl.util.DateTime;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.vo.*;
import org.apache.commons.net.ftp.*;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JSDebitCardService {

    private String FTP_PORT_NUMBER = PropertyReader.getProperty("jsdebitcard.ftp.port");
    private String FTP_USER_NAME = PropertyReader.getProperty("jsdebitcard.ftp.userName");
    private String FTP_PASSWORD = PropertyReader.getProperty("jsdebitcard.ftp.password");
    private String FTP_TIME_OUT = PropertyReader.getProperty("jsdebitcard.ftp.timeout");
    private String FTP_SERVER_URL = PropertyReader.getProperty("jsdebitcard.ftp.server");
    private String FTP_WRITE_DIRECTORY = PropertyReader.getProperty("jsdebitcard.ftp.writedirectory");
    private String FTP_READ_DIRECTORY = PropertyReader.getProperty("jsdebitcard.ftp.readdirectory");
    private String FTP_CUSTOMER_FILE = PropertyReader.getProperty("jsdebitcard.ftp.customerfilename");
    private String FTP_ACCOUNT_FILE = PropertyReader.getProperty("jsdebitcard.ftp.accountfilename");
    private String FTP_DEBITCARD_FILE = PropertyReader.getProperty("jsdebitcard.ftp.debitcardfilename");
    private String FTP_CUSTOMER_ACCOUNT_FILE = PropertyReader.getProperty("jsdebitcard.ftp.customeraccountfilename");
    private String FTP_DEBITCARD_STATUS_PREFIX = PropertyReader.getProperty("jsdebitcard.ftp.debitcardstatus");
    private String FTP_DEBITCARD_CHARGES = PropertyReader.getProperty("jsdebitcard.ftp.debitcardcharges");
    private String LOCAL_FILE_PATH = PropertyReader.getProperty("jsdebitcard.ftp.localpath");
    private String PGP_KEY_PATH = "/com/inov8/integration/channel/jsdebitcard/resources/";
    private String PRIVATE_KEY_FILE = PGP_KEY_PATH + PropertyReader.getProperty("jsdebitcard.pgp.privatekeyfile");
    private String PUBLIC_KEY_FILE =  PGP_KEY_PATH + PropertyReader.getProperty("jsdebitcard.pgp.publickeyfile");
    private String PASS_PHRASE =  PropertyReader.getProperty("jsdebitcard.pgp.passphrase");
    private String FTP_CARDS_STATUS_READ_LENGTH =  PropertyReader.getProperty("jsdebitcard.ftp.debitcardstatus.readlength");
    private String FTP_CARDS_CHARGES_READ_LENGTH = PropertyReader.getProperty("jsdebitcard.ftp.debitcardcharges.readlength");
    private static Logger logger = LoggerFactory.getLogger(JSDebitCardService.class.getSimpleName());

    public DebitCardExportResponse exportCardsToFTP(DebitCardExportRequest debitCardExportRequest) {

        DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
        Date dateobj = new Date();
        DebitCardExportResponse debitCardExportResponse = new DebitCardExportResponse();
        ByteArrayOutputStream customerByteStream = new ByteArrayOutputStream();
        ByteArrayOutputStream accountByteStream = new ByteArrayOutputStream();
        ByteArrayOutputStream customerAccountByteStream = new ByteArrayOutputStream();
        ByteArrayOutputStream debitCardByteStream = new ByteArrayOutputStream();

        try {

            PGPPublicKey publicKey =  PGPUtils.readPublicKey( new FileInputStream(new ClassPathResource(PUBLIC_KEY_FILE).getFile()));
            PGPUtils.encryptStream(customerByteStream,publicKey,new ByteArrayInputStream(debitCardExportRequest.getCustomeByteStream().toByteArray()));
            PGPUtils.encryptStream(accountByteStream,publicKey,new ByteArrayInputStream(debitCardExportRequest.getAccountByteStream().toByteArray()));
            PGPUtils.encryptStream(customerAccountByteStream,publicKey,new ByteArrayInputStream(debitCardExportRequest.getCustomerAccountByteStream().toByteArray()));
            PGPUtils.encryptStream(debitCardByteStream,publicKey,new ByteArrayInputStream(debitCardExportRequest.getDebitCardByteStream().toByteArray()));

        } catch (IOException e) {
            logger.error("Exception", e);
            debitCardExportResponse.setCode(I8SBResponseCodeEnum.INTERNAL_ERROR.getValue());
            debitCardExportResponse.setDescription(e.getMessage());
        } catch (PGPException e) {
            logger.error("Exception", e);
            debitCardExportResponse.setCode(I8SBResponseCodeEnum.INTERNAL_ERROR.getValue());
            debitCardExportResponse.setDescription(e.getMessage());
        }

        int replyCode;
        String replyDescription;


        InputStream customerInputStream =  new ByteArrayInputStream(customerByteStream.toByteArray());
        InputStream accountInputStream = new ByteArrayInputStream(accountByteStream.toByteArray());
        InputStream debitCardInputStream = new ByteArrayInputStream(debitCardByteStream.toByteArray());
        InputStream customerAccountInputStream = new ByteArrayInputStream(customerAccountByteStream.toByteArray());

        InputStream[] inputStreams = new InputStream[]{customerInputStream, accountInputStream, debitCardInputStream, customerAccountInputStream};
        String[] fileNames = new String[]{FTP_CUSTOMER_FILE, FTP_ACCOUNT_FILE, FTP_DEBITCARD_FILE, FTP_CUSTOMER_ACCOUNT_FILE};

        try {
            FTPClient ftpClient = new FTPClient();
            int connectionTime = Integer.parseInt(FTP_TIME_OUT);
            ftpClient.setConnectTimeout(connectionTime);
            int port = Integer.parseInt(FTP_PORT_NUMBER);

            logger.info("Connecting with FTP server");

            ftpClient.connect(FTP_SERVER_URL, port);
            showServerReply(ftpClient);
            replyCode = ftpClient.getReplyCode();
            replyDescription = ftpClient.getReplyString();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.info("Could not connected with FTP server");
                debitCardExportResponse.setCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
                debitCardExportResponse.setDescription(replyDescription);

                return debitCardExportResponse;
            }
            boolean success = ftpClient.login(FTP_USER_NAME, FTP_PASSWORD);
            showServerReply(ftpClient);
            replyCode = ftpClient.getReplyCode();
            replyDescription = ftpClient.getReplyString();
            if (!success) {
                logger.info("Could not login to FTP server");
                debitCardExportResponse.setCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
                debitCardExportResponse.setDescription(replyDescription);
                return debitCardExportResponse;
            }

             if (FTP_WRITE_DIRECTORY != null && !FTP_WRITE_DIRECTORY.isEmpty()) {
                 ftpClient.changeWorkingDirectory(FTP_WRITE_DIRECTORY);
            }

            ftpClient.printWorkingDirectory();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            for (int i = 0; i < 4; i++) {
                String exportFileName = fileNames[i]+"."+df.format(dateobj)+".txt";
                success = ftpClient.storeFile(exportFileName, inputStreams[i]);
                if (success == false) {
                    showServerReply(ftpClient);
                    replyCode = ftpClient.getReplyCode();
                    replyDescription = ftpClient.getReplyString();

                    logger.info("Failed to create file on FTP server.");
                    debitCardExportResponse.setCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
                    debitCardExportResponse.setDescription(replyDescription);
                    return debitCardExportResponse;

                }
            }

            showServerReply(ftpClient);
            replyCode = ftpClient.getReplyCode();
            replyDescription = ftpClient.getReplyString();
            logger.debug("File created on ftp server");
            debitCardExportResponse.setCode(I8SBResponseCodeEnum.PROCESSED.getValue());
            debitCardExportResponse.setDescription("Files are created on server");
            //}

            // closing all streams:
            customerInputStream.close();
            accountInputStream.close();
            debitCardInputStream.close();
            customerInputStream.close();

            ftpClient.logout();
            ftpClient.disconnect();

            return debitCardExportResponse;
        } catch (Exception ex) {
            logger.info("Internal server error");
            debitCardExportResponse.setCode(I8SBResponseCodeEnum.INTERNAL_ERROR.getValue());
            debitCardExportResponse.setDescription("Exception on FTP server");
            logger.error("Exception", ex);
        }

        return debitCardExportResponse;

    }

    public DebitCardImportResponse importCardsFromFTP() {
        DebitCardImportResponse debitCardImportResponse = new DebitCardImportResponse();
        List<DebitCardCharges> debitCardChargesList = new ArrayList<>();
        List<DebitCardStatusVO> debitCardStatusList = new ArrayList<>();
        int replyCode;
        String replyDescription;
        String debitCardStatusFile = "";
        try {
            FTPClient ftpClient = new FTPClient();
            int connectionTime = Integer.parseInt(FTP_TIME_OUT);
            ftpClient.setConnectTimeout(connectionTime);
            int port = Integer.parseInt(FTP_PORT_NUMBER);

            logger.info("Connecting with FTP server");

            ftpClient.connect(FTP_SERVER_URL, port);
            showServerReply(ftpClient);
            replyCode = ftpClient.getReplyCode();
            replyDescription = ftpClient.getReplyString();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.debug("Could not connected with FTP server");
                debitCardImportResponse.setCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
                debitCardImportResponse.setDescription(replyDescription);
                return debitCardImportResponse;
            }
            boolean success = ftpClient.login(FTP_USER_NAME, FTP_PASSWORD);
            showServerReply(ftpClient);
            replyCode = ftpClient.getReplyCode();
            replyDescription = ftpClient.getReplyString();
            if (!success) {
                logger.debug("Could not login to the server");
                debitCardImportResponse.setCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
                debitCardImportResponse.setDescription(replyDescription);
                return debitCardImportResponse;
            }

            if (FTP_READ_DIRECTORY != null && !FTP_READ_DIRECTORY.isEmpty()) {
                ftpClient.changeWorkingDirectory(FTP_READ_DIRECTORY);
            }

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();


            FTPFileFilter filter = new FTPFileFilter() {

                @Override
                public boolean accept(FTPFile ftpFile) {
                    // TODO Auto-generated method stub
                    return (ftpFile.isFile() && ftpFile.getName().contains(FTP_DEBITCARD_STATUS_PREFIX));
                }

            };

            FTPFile[] result = ftpClient.listFiles(FTP_READ_DIRECTORY,filter);
            ftpClient.printWorkingDirectory();
            for(FTPFile file: result)
            {
                if(file.getName().contains(FTP_DEBITCARD_STATUS_PREFIX))
                    debitCardStatusFile =  file.getName();
            }

            if (result == null || result.length == 0) {
                logger.info("File is not present on FTP server");
                debitCardImportResponse.setCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
                debitCardImportResponse.setDescription("File is not present on FTP server");

            } else {

                InputStream debitCardStatusInputStream = ftpClient.retrieveFileStream(debitCardStatusFile);
                ByteArrayOutputStream debitCardStatusDecryptedStream = new ByteArrayOutputStream();
                PGPUtils.decryptFile(debitCardStatusInputStream,debitCardStatusDecryptedStream ,new FileInputStream(new ClassPathResource(PRIVATE_KEY_FILE).getFile()),PASS_PHRASE.toCharArray());
                ByteArrayInputStream debitCardStatusPlainStream = new ByteArrayInputStream(debitCardStatusDecryptedStream .toByteArray());
                BufferedReader debitCardStatusReader = new BufferedReader(new InputStreamReader(debitCardStatusPlainStream));
                String inputLine;
                while ((inputLine = debitCardStatusReader.readLine()) != null) {
                    DebitCardStatusVO debitCardStatus = new DebitCardStatusVO();
                    String[] splitedString = inputLine.split("\\^");
                    if(splitedString.length == Integer.parseInt(FTP_CARDS_STATUS_READ_LENGTH)) {
                        debitCardStatus.setPan(splitedString[0]);
                        debitCardStatus.setAccountNo(splitedString[1]);
                        debitCardStatus.setRelationshipNo(splitedString[2]);
                        debitCardStatus.setCreatedOn(splitedString[3]);
                        debitCardStatus.setStatus(splitedString[4]);
                        debitCardStatus.setFlag(splitedString[5]);
                        debitCardStatus.setUpdatedOn(splitedString[6]);
                        debitCardStatus.setCardTitle(splitedString[7]);

                        debitCardStatusList.add(debitCardStatus);
                    }
                    else
                    {
                        debitCardImportResponse.setCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
                        debitCardImportResponse.setDescription("Incorrect [DebitCard Status] file format");
                        return debitCardImportResponse;
                    }
                }
                if (!ftpClient.completePendingCommand()) {
                    debitCardImportResponse.setCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
                    debitCardImportResponse.setDescription("Failed to read the file");
                    return debitCardImportResponse;
                }

                // downloading the file:

                   FileOutputStream fos = new FileOutputStream(LOCAL_FILE_PATH+debitCardStatusFile);
                   ftpClient.retrieveFile(debitCardStatusFile, fos);

                   // deleting the file:
                    ftpClient.deleteFile(debitCardStatusFile);


                // Reading charges file
//                InputStream debitCardChargesInputStream = ftpClient.retrieveFileStream(FTP_DEBITCARD_CHARGES);
//                ByteArrayOutputStream debitCardChargesDecryptedStream = new ByteArrayOutputStream();
//                PGPUtils.decryptFile(debitCardChargesInputStream,debitCardChargesDecryptedStream ,new FileInputStream(PRIVATE_KEY_FILE),PASS_PHRASE.toCharArray());
//
//                ByteArrayInputStream debitCardChargesPlainStream = new ByteArrayInputStream(debitCardChargesDecryptedStream .toByteArray());
//                BufferedReader debitCardChargesReader = new BufferedReader(new InputStreamReader(debitCardChargesPlainStream));
//
//                while ((inputLine = debitCardChargesReader.readLine()) != null) {
//                    DebitCardCharges debitCardCharges = new DebitCardCharges();
//                    String[] splitedString = inputLine.split("\\^");
//                    if(splitedString.length == FTP_CARDS_CHARGES_READ_LENGTH) {
//                        debitCardCharges.setPan(splitedString[0]);
//                        debitCardCharges.setAccountNo(splitedString[1]);
//                        debitCardCharges.setRelationshipNo(splitedString[2]);
//                        debitCardCharges.setStatus(splitedString[3]);
//                        debitCardCharges.setFlag(splitedString[4]);
//
//                        debitCardChargesList.add(debitCardCharges);
//                    }
//                    else
//                    {
//                        debitCardImportResponse.setCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
//                        debitCardImportResponse.setDescription("Incorrect [DebitCard Charges] file format");
//                        return debitCardImportResponse;
//                    }
//                }

                // Downloading the files:
              //  debitCardImportResponse.setCardChargesList(debitCardChargesList);

                debitCardImportResponse.setCardStatusList(debitCardStatusList);
                debitCardImportResponse.setCode(I8SBResponseCodeEnum.PROCESSED.getValue());
                debitCardImportResponse.setDescription("Files read operation is successfull");
            }
        } catch (Exception ex) {
            logger.debug("Internal Server Error");
            logger.error("Exception", ex);
            debitCardImportResponse.setCode(I8SBResponseCodeEnum.INTERNAL_ERROR.getValue());
            debitCardImportResponse.setDescription("Files read operation is unsuccessful");
        }
        return debitCardImportResponse;
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                logger.info("SERVER: " + aReply);
            }
        }
    }

}