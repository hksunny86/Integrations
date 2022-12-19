package com.inov8.microbank.common.util;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Apr 12, 2013 2:54:13 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public enum StatusEnum
{
    SUCCESS
    {
        @Override
        public String toString()
        {
            return STATUS_SUCCESS;
        }
    },
    FAILURE
    {
        @Override
        public String toString()
        {
            return STATUS_FAILURE;
        }
    };

    public static StatusEnum[] getValues()
    {
        return values();
    }

    private static final String STATUS_SUCCESS = "Success";
    private static final String STATUS_FAILURE = "Failure";

}
