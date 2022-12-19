package com.inov8.microbank.server.service.workflow.controller;

import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

public interface WorkFlowController
{
   public WorkFlowWrapper workflowProcess(WorkFlowWrapper workFlowWrapper)throws Exception;
}
