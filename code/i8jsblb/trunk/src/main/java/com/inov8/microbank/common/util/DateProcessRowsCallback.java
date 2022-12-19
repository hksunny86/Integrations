package com.inov8.microbank.common.util;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.extremecomponents.table.callback.ProcessRowsCallback;
import org.extremecomponents.table.core.TableModel;

/**
 * Overwrite Default ProcessRowsCallback to enable date filtering.
 *
 * @author Maqsood Shahzad
 */
public class DateProcessRowsCallback extends ProcessRowsCallback
{
	/** {@inheritDoc} **/
    public Collection filterRows(TableModel model, Collection rows)
        throws Exception
    {
        boolean filtered = model.getLimit().isFiltered();
        boolean cleared = model.getLimit().isCleared();

        if (!filtered || cleared)
        {
            return rows;
        }

        if (filtered)
        {
            Collection collection = new ArrayList();
            Predicate filterPredicate = new DateFilterPredicate(model);
            CollectionUtils.select(rows, filterPredicate, collection);

            return collection;
        }

        return rows;
    }
}