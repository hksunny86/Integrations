package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.debitcard.vo.DebitCardReversalVO;

public interface ReversalAdviceSender {

    void send(Object o) throws FrameworkCheckedException;
}
