package com.inov8.verifly.common.util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Errors implements Serializable
{
  private List errorsList = new ArrayList();
  public Errors()
  {
  }
  public void addErrorObject(Error errorObj)
  {
    this.errorsList.add(errorObj);
  }
  public List getErrorList()
  {
    return this.errorsList;
  }
}
