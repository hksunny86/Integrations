package com.inov8.microbank.server.service.retailermodule;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public interface RetailerTransactionManager
{
    SearchBaseWrapper searchRetailerTransaction( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

    SearchBaseWrapper searchRetailerTransactionView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

    SearchBaseWrapper fetchRegionalRetailActivitySummary( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
}
