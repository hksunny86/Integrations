package com.inov8.integration.channel.jsdebitcard.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.enums.I8SBKeysOfCollectionEnum;
import com.inov8.integration.i8sb.vo.*;
import com.inov8.integration.util.CommonUtils;
import com.inov8.integration.util.*;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@XmlRootElement
public class DebitCardExportRequest extends Request {

    ByteArrayOutputStream customeByteStream = new ByteArrayOutputStream();
    ByteArrayOutputStream accountByteStream = new ByteArrayOutputStream();
    ByteArrayOutputStream debitCardByteStream = new ByteArrayOutputStream();
    ByteArrayOutputStream customerAccountByteStream = new ByteArrayOutputStream();

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

        List<CustomerVO> customerVOList = (List<CustomerVO>) i8SBSwitchControllerRequestVO.getList(I8SBKeysOfCollectionEnum.Customer.getValue());
        List<AccountVO> accountVOList = (List<AccountVO>) i8SBSwitchControllerRequestVO.getList(I8SBKeysOfCollectionEnum.Account.getValue());
        List<DebitCardVO> debitCardVoList = (List<DebitCardVO>) i8SBSwitchControllerRequestVO.getList(I8SBKeysOfCollectionEnum.DebitCard.getValue());
        List<CustomerAccountVO> customerAccountVOList = (List<CustomerAccountVO>) i8SBSwitchControllerRequestVO.getList(I8SBKeysOfCollectionEnum.CustomerAccount.getValue());

        if (customerVOList == null) {
            throw new I8SBValidationException("[FAILED] Customer record is mandatory");
        }
        if (accountVOList == null) {
            throw new I8SBValidationException("[FAILED] Account record is mandatory");
        }
        if (debitCardVoList == null) {
            throw new I8SBValidationException("[FAILED] DebitCard record is mandatory");
        }
        if (customerAccountVOList == null) {
            throw new I8SBValidationException("[FAILED] CustomerAccount record is mandatory");
        }

        // Validating Customer Mandatory Attributes:

        for (int i = 0; i < customerVOList.size(); i++) {
            CustomerVO customerVO = customerVOList.get(i);
            if (StringUtils.isBlank(customerVO.getRelationshipNo())) {
                throw new I8SBValidationException(" Mandatory param [relationshipNo] is missing at customer record No:" + (i + 1));
            } else if (StringUtils.isBlank(customerVO.getCustomerTypeCode())) {
                throw new I8SBValidationException("Mandatory param [customerTypeCode] is missing at customer record No:" + (i + 1));
            } else if (StringUtils.isBlank(customerVO.getBranchCode())) {
                throw new I8SBValidationException("Mandatory param [branchCode] is missing at customer record No:" + (i + 1));
            } else if (StringUtils.isBlank(customerVO.getSegmantCode())) {
                throw new I8SBValidationException("Mandatory param [segmantCode] is missing at customer record No:" + (i + 1));
            } else if (StringUtils.isBlank(customerVO.getCustomerStatusCode())) {
                throw new I8SBValidationException("Mandatory param [customerStatusCode] is missing at customer record No:" + (i + 1));
            } else if (StringUtils.isBlank(customerVO.getFullName())) {
                throw new I8SBValidationException("Mandatory param [customerFullName] is missing at customer record No:" + (i + 1));
            } else if (StringUtils.isBlank(customerVO.getGenderCode())) {
                throw new I8SBValidationException("Mandatory param [genderCode] is missing at customer record No:" + (i + 1));
            }else if(StringUtils.isBlank(customerVO.getNationalityCode())){
                throw new I8SBValidationException("Mandatory param [NationalityCode] is missing at customer record No:" + (i + 1));
            }
        }

        // validating account Mandatory Attributes

        for (int i = 0; i < accountVOList.size(); i++) {
            AccountVO accountVO = accountVOList.get(i);
            if (StringUtils.isBlank(accountVO.getAccountNo())) {
                throw new I8SBValidationException("Mandatory param [accountNo] is missing at account record No:" + (i + 1));

            } else if (StringUtils.isBlank(accountVO.getAccountTypeCode())) {
                throw new I8SBValidationException("Mandatory param [accountTypeCode] is missing at account record No:" + (i + 1));
            } else if (StringUtils.isBlank(accountVO.getAccountStatusCode())) {
                throw new I8SBValidationException("Mandatory param [accounStatusCode] is missing at account record No:" + (i + 1));
            } else if (StringUtils.isBlank(accountVO.getBranchCode())) {
                throw new I8SBValidationException("Mandatory param [branchCode] is missing at account record No:" + (i + 1));

            } else if (StringUtils.isBlank(accountVO.getCurrencyCode())) {
                throw new I8SBValidationException("Mandatory param [currencyCode] is missing at account record No:" + (i + 1));
            } else if (StringUtils.isBlank(accountVO.getAccountTitle())) {
                throw new I8SBValidationException("Mandatory param [accountTitle] is missing at account record No:" + (i + 1));
            }
//            else if (StringUtils.isBlank(accountVO.getIBAN())) {
//                throw new I8SBValidationException("Mandatory param [IBAN] is missing at account record No:" + (i + 1));
//            }
        }

        for (int i = 0; i < debitCardVoList.size(); i++) {
            DebitCardVO debitCardVO = debitCardVoList.get(i);
            if (StringUtils.isBlank(debitCardVO.getRelationShipNo())) {
                throw new I8SBValidationException("Mandatory param [relationshipNo] is missing at debitcard record No:" + (i + 1));
            } else if (StringUtils.isBlank(debitCardVO.getAccountNo())) {
                throw new I8SBValidationException("Mandatory param [accountNo] is missing at debitcard record No:" + (i + 1));
            } else if (StringUtils.isBlank(debitCardVO.getCardEmborsingName())) {
                throw new I8SBValidationException("Mandatory param [emborsingName] is missing at debitcard record No:" + (i + 1));
            } else if (StringUtils.isBlank(debitCardVO.getCardTypeCode())) {
                throw new I8SBValidationException("Mandatory param [cardTypeCode] is missing at debitcard record No:" + (i + 1));
            } else if (StringUtils.isBlank(debitCardVO.getCardProdCode())) {
                throw new I8SBValidationException("Mandatory param [cardProdCode] is missing at debitcard record No:" + (i + 1));
            } else if (StringUtils.isBlank(debitCardVO.getCardBranchCode())) {
                throw new I8SBValidationException("Mandatory param [cardBranchCode] is missing at debitcard record No:" + (i + 1));
            } else if (StringUtils.isBlank(debitCardVO.getPrimaryCNIC())) {
                throw new I8SBValidationException("Mandatory param [primaryCNIC] is missing at debitcard record No:" + (i + 1));
            } else if (debitCardVO.getIssuedDate() == null) {
                throw new I8SBValidationException("Mandatory param [issueDate] is missing at debitcard record No:" + (i + 1));
            } else if (StringUtils.isBlank(debitCardVO.getRequestType())) {

                throw new I8SBValidationException("Mandatory param [requestType] is missing at debitcard record No:" + (i + 1));
            }
        }

        // Validating CustomerAccount Mandatory Attributes

        for (int i = 0; i < customerAccountVOList.size(); i++) {
            CustomerAccountVO customerAccountVO = customerAccountVOList.get(i);
            if (StringUtils.isBlank(customerAccountVO.getRelationshipNo())) {
                throw new I8SBValidationException("Mandatory param [relationNo] is missing at customeraccount record No:" + (i + 1));
            } else if (StringUtils.isBlank(customerAccountVO.getAccountNo())) {
                throw new I8SBValidationException("Mandatory param [accountNo] is missing at customeraccount record No:" + (i + 1));
            }
        }
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

        List<CustomerVO> customerVOList = (List<CustomerVO>) i8SBSwitchControllerRequestVO.getList(I8SBKeysOfCollectionEnum.Customer.getValue());
        List<AccountVO> accountVOList = (List<AccountVO>) i8SBSwitchControllerRequestVO.getList(I8SBKeysOfCollectionEnum.Account.getValue());
        List<DebitCardVO> debitCardVoList = (List<DebitCardVO>) i8SBSwitchControllerRequestVO.getList(I8SBKeysOfCollectionEnum.DebitCard.getValue());
        List<CustomerAccountVO> customerAccountVOList = (List<CustomerAccountVO>) i8SBSwitchControllerRequestVO.getList(I8SBKeysOfCollectionEnum.CustomerAccount.getValue());

        prepareRecordForDebitFile(debitCardVoList);
        prepareRecordForCustomerAccountFile(customerAccountVOList);
        prepareRecordForCustomerFile(customerVOList);
        prepareRecordForAccountFile(accountVOList);

    }

    public void prepareRecordForDebitFile(List<DebitCardVO> debitCardVOList) {

        for (DebitCardVO debitCardVO : debitCardVOList) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(StringUtils.trimToEmpty(debitCardVO.getRelationShipNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(debitCardVO.getAccountNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(debitCardVO.getCardEmborsingName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(debitCardVO.getCardTypeCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(debitCardVO.getCardProdCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(debitCardVO.getCardBranchCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(debitCardVO.getPrimaryCNIC()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(debitCardVO.getIssuedDate()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(debitCardVO.getRequestType()));

            // Add stringbuilder to stream:
            try {
                debitCardByteStream.write(stringBuilder.toString().getBytes());
                debitCardByteStream.write(System.lineSeparator().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void prepareRecordForCustomerAccountFile(List<CustomerAccountVO> customerAccountVOList) {

        for (CustomerAccountVO customerAccountVO : customerAccountVOList) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(StringUtils.trimToEmpty(customerAccountVO.getRelationshipNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerAccountVO.getAccountNo()));

            try {
                customerAccountByteStream.write(stringBuilder.toString().getBytes());
                customerAccountByteStream.write(System.lineSeparator().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void prepareRecordForCustomerFile(List<CustomerVO> customerVOList) {

        for (CustomerVO customerVO : customerVOList) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(StringUtils.trimToEmpty(customerVO.getRelationshipNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getCustomerTypeCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getBranchCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getSegmantCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getCustomerStatusCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getFullName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getGenderCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getNationalityCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getLangCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getFullNameOther()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getFirstName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getMiddleName()))
                    //middlename
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getLastName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getSurName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getSuffix()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getTitleName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getCompanyName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getDepartmentName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getBusinessTitle()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getPrimaryEmail()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getSecondaryEmail()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getMotherMaidenName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getFatherName()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getPassportNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getCustomerCNIC()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getCustomerTaxNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getPlaceOfBirth()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(customerVO.getDateOfBirth())
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getQualification()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getProfession()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getReligonCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getMartialCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOfficeAddress1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOfficeAddress2()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOfficeCity()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOfficeCountry()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOfficeZipCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getHomeAddress1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getHomeAddress2()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getHomeCity()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getHomeCountry()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getHomeZipCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getTempAddress1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getTempAddress2()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getTempCity()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getTempCountry()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getTempZipCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOtherAddress1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOtherAddress2()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getCorrespondenceFlag()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOfficePhone1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOfficePhone2()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getOfficeFaxNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getResidencePhone1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getResidencePhone2()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getResidenceFaxNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getMobilePhone()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getLimitProfile()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(customerVO.getDeletedOn()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getReserverd1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getReserved2()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getReserved3()))//reserved3
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(customerVO.getReserverd4()));

            try {
                customeByteStream.write(stringBuilder.toString().getBytes());
                customeByteStream.write(System.lineSeparator().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void prepareRecordForAccountFile(List<AccountVO> accountVOList) {

        for (AccountVO accountVO : accountVOList) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(StringUtils.trimToEmpty(accountVO.getAccountNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getAccountTypeCode()))//89999
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getAccountStatusCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getBranchCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getCurrencyCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getAccountTitle()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(accountVO.getOdaLimitReviewDate()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(accountVO.getLisFinTranDate()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(accountVO.getInterestFromDate()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(accountVO.getInterestToDate()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(accountVO.getOpenedDate()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getLimitProfile1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(CommonUtils.trimObjectToEmpty(accountVO.getJoinType())))  // value to check
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getAccountTitleOther()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getOfficerCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(CommonUtils.trimObjectToEmpty(accountVO.getAvailableBalance()))) // value to check
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(CommonUtils.trimObjectToEmpty(accountVO.getLedgerBalance())))  // value to check
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(CommonUtils.trimObjectToEmpty(accountVO.getHoldAmount()))) // value to check
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getStatementFrequency()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(accountVO.getLastStatementDate()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(accountVO.getNextDateTime()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getIntroRelationShipNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getIntroAddress()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getIntroAccountNo()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getComments()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(accountVO.getClosedDate()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getClosedRemark()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(DateUtil.trimToEmpty(accountVO.getDeletedOn()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getJointDescription()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getAddress1()))//mailingadd
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getAddress2()))//mailingadd
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getCity()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getCountryCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getZipCode()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getT1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getT2()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getT3()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getT4()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getT5()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getT6()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getT7()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getT8()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getReserved1()))
                    .append(I8SBConstants.CAP_DELIMITER)
                    .append(StringUtils.trimToEmpty(accountVO.getIBAN()));
            try {
                accountByteStream.write(stringBuilder.toString().getBytes());
                accountByteStream.write(System.lineSeparator().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public ByteArrayOutputStream getCustomeByteStream() {
        return customeByteStream;
    }

    public void setCustomeByteStream(ByteArrayOutputStream customeByteStream) {
        this.customeByteStream = customeByteStream;
    }

    public ByteArrayOutputStream getAccountByteStream() {
        return accountByteStream;
    }

    public void setAccountByteStream(ByteArrayOutputStream accountByteStream) {
        this.accountByteStream = accountByteStream;
    }

    public ByteArrayOutputStream getDebitCardByteStream() {
        return debitCardByteStream;
    }

    public void setDebitCardByteStream(ByteArrayOutputStream debitCardByteStream) {
        this.debitCardByteStream = debitCardByteStream;
    }

    public ByteArrayOutputStream getCustomerAccountByteStream() {
        return customerAccountByteStream;
    }

    public void setCustomerAccountByteStream(ByteArrayOutputStream customerAccountByteStream) {
        this.customerAccountByteStream = customerAccountByteStream;
    }
}
