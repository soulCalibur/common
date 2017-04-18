/**
 * File Name:MyDatasetUtilities.java
 * Package Name:com.test.jfree
 * Date:2017-4-9下午7:13:26
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.test.jfree;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.ArrayUtilities;

/**
 * ClassName:MyDatasetUtilities <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017-4-9 下午7:13:26 <br/>
 * @author   Administrator
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class MyDatasetUtilities {
	@SuppressWarnings("rawtypes")
    public static CategoryDataset createCategoryDataset(Comparable[] columnKeys, double[][] data) {
        if (columnKeys == null) {
            throw new IllegalArgumentException("Null 'columnKeys' argument.");
        }
        if (ArrayUtilities.hasDuplicateItems(columnKeys)) {
            throw new IllegalArgumentException(
                    "Duplicate items in 'columnKeys'.");
        }

        int columnCount = 0;
        for (int r = 0; r < data.length; ++r) {
            columnCount = Math.max(columnCount, data[r].length);
        }
        if (columnKeys.length != columnCount) {
            throw new IllegalArgumentException(
                    "The number of column keys does not match the number of columns in the data array.");
        }

        DefaultCategoryDataset result = new DefaultCategoryDataset();
            for (int c = 0; c < data[0].length; ++c) {
                Comparable columnKey = columnKeys[c];
                result.addValue(new Double(data[0][c]), columnKey, columnKey);
            }
        return result;
    }
}

