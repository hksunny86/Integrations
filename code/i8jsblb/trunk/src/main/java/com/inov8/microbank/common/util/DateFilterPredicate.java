package com.inov8.microbank.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.util.ExtremeUtils;

/**
 * Filter Predicate implementation which enable date comparison.
 *
 * @author Maqsood Shahzad
 */
public class DateFilterPredicate implements Predicate
{
    /** less than or equal. usage: <= 18-12-1997 */
    public static final String LESS_THAN_OR_EQUAL = "<=";

    /** greater than or equal. usage: >= 18-12-1997 */
    public static final String GREATER_THAN_OR_EQUAL = ">=";

    /** date between. usage: <> 18-12-1997 19-12-1997 */
    public static final String BETWEEN = "<>";

    /** date not equal. ussage: != 18-12-2004 */
    public static final String NOT_EQUAL = "!=";

    /** delimiters */
    public static final String DELIM = "\\s";
    
    private final Log logger = LogFactory.getLog(this.getClass());
    private static final String asterisk = "*";
    private static final String emptyString = "";
    private TableModel model;

    /**
     * Creates a new DateFilterPredicate object.
     *
     * @param model table model
     */
    public DateFilterPredicate(TableModel model)
    {
    	this.model = model;
    }

    /**
     * Use the filter parameters to filter out the table.
     */
    public boolean evaluate(Object bean)
    {
        boolean match = false;

        try
        {
            Iterator iter = model.getColumnHandler().getColumns().iterator();

            while (iter.hasNext())
            {
                Column column = (Column) iter.next();
                String alias = column.getAlias();
                String filterValue = model.getLimit().getFilterSet()
                                          .getFilterValue(alias);

                if (StringUtils.isEmpty(filterValue))
                {
                    continue;
                }

                String property = column.getProperty();
                Object value = PropertyUtils.getProperty(bean, property);

                if (value == null)
                {
                    continue;
                }

                if (column.isDate())
                {
                    Locale locale = model.getLocale();
                    value = ExtremeUtils.formatDate(column.getParse(),
                            column.getFormat(), value, locale);
                }
                else if (column.isCurrency())
                {
                    Locale locale = model.getLocale();
                    value = ExtremeUtils.formatNumber(column.getFormat(),
                            value, locale);
                }

                if (!isSearchMatch(value, filterValue, column.isDate(),
                            column.getFormat(), model.getLocale()))
                {
                    match = false; // as soon as fail just short circuit

                    break;
                }

                match = true;
            }
        }
        catch (Exception e)
        {
            logger.error("FilterPredicate.evaluate() had problems", e);
        }

        return match;
    }

    private boolean isSearchMatch(Object value, String search, boolean isDate,
        String format, Locale locale)
    {
        String valueStr = value.toString().toLowerCase().trim();
        search = search.toLowerCase().trim();

        if (search.startsWith(asterisk) &&
                valueStr.endsWith(StringUtils.replace(search, asterisk,
                        emptyString)))
        {
            return true;
        }
        else if (search.endsWith(asterisk) &&
                valueStr.startsWith(StringUtils.replace(search, asterisk,
                        emptyString)))
        {
            return true;
        }
        else if (isDate)
        {
            DateFormat dateFormat = new SimpleDateFormat(format, locale);

            Date dateToCompare = null;
            Date dateToCompare2 = null;

            try
            {
                Date dateValue = dateFormat.parse(value.toString());

                String[] result = search.split(DELIM);

                String operator = result[0];

                if (LESS_THAN_OR_EQUAL.equals(operator))
                {
                    dateToCompare = dateFormat.parse(result[1]);

                    return dateValue.getTime() <= dateToCompare.getTime();
                }
                else if(GREATER_THAN_OR_EQUAL.equals(operator))
                {
                    dateToCompare = dateFormat.parse(result[1]);

                    return dateValue.getTime() >= dateToCompare.getTime();
                }
                else if (BETWEEN.equals(operator))
                {
                    dateToCompare = dateFormat.parse(result[1]);
                    dateToCompare2 = dateFormat.parse(result[2]);

                    return (dateValue.getTime() >= dateToCompare.getTime()) &&
                    (dateValue.getTime() <= dateToCompare2.getTime());
                }
                else if (NOT_EQUAL.equals(operator))
                {
                    dateToCompare = dateFormat.parse(result[1]);

                    return dateValue.getTime() != dateToCompare.getTime();
                }
                else
                {
                    return StringUtils.contains(valueStr, search);
                }
            }
            catch (Exception e)
            {
                logger.error(
                    "The parse was incorrectly defined for date String [" +
                    search + "].");

                // date comparions failed. Campare it as normal string.
                return StringUtils.contains(valueStr, search);
            }
        }
        else if (StringUtils.contains(valueStr, search))
        {
            return true;
        }

        return false;
    }
}