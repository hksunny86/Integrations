package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;

public interface IvrAuthenticationRequestQueue {

	void sentAuthenticationRequest(IvrRequestDTO dto) throws FrameworkCheckedException;
}