/*
* Copyright 2012 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.openehealth.pors.admin_frontend.guiengine.dialogdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;

public abstract class AbstractDataModel<T> extends SerializableDataModel {
	
	private static final long serialVersionUID = 8197035840249994633L;
	
	private Long currentPk;
	private List<Long> wrappedKeys = null;
	private Map<Long,T> wrappedData = new HashMap<Long, T>();
	private AbstractDataProvider<T> dataProvider;
	private Integer rowCount;
	
	public AbstractDataModel(AbstractDataProvider<T> dataProvider) {
		this.dataProvider = dataProvider;
	}

	@Override
	public void update() {
	}

	@Override
	public Object getRowKey() {
		return currentPk;
	}

	@Override
	public void setRowKey(Object key) {
		this.currentPk = (Long) key;
	}

	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {
		int firstRow = ((SequenceRange)range).getFirstRow();
        int numberOfRows = ((SequenceRange)range).getRows();
        wrappedKeys = new ArrayList<Long>();
        for (T item : dataProvider.getItemsByRange(new Integer(firstRow), numberOfRows)) {
            wrappedKeys.add((Long) dataProvider.getKey(item));
            wrappedData.put((Long) dataProvider.getKey(item), item);
            visitor.process(context, (Long) dataProvider.getKey(item), argument);
        }
	}

	@Override
	public int getRowCount() {
		rowCount = new Integer(dataProvider.getRowCount());
		return rowCount;
	}

	@Override
	public Object getRowData() {
		if (currentPk == null) {
			return null;
		} else {
			T ret = wrappedData.get(currentPk);
			if (ret == null) {
				ret = dataProvider.getItemByKey(currentPk);
				wrappedData.put(currentPk, ret);
			} 
			return ret;
		}
	}

	@Override
	public int getRowIndex() {
		return 0;
	}

	@Override
	public Object getWrappedData() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRowAvailable() {
		if (currentPk == null) {
			return false;
		} else {
			return dataProvider.getItemByKey(currentPk) != null;
		}
	}

	@Override
	public void setRowIndex(int arg0) {
	}

	@Override
	public void setWrappedData(Object arg0) {
		throw new UnsupportedOperationException();
	}
	
	public AbstractDataProvider<T> getDataProvider() {
		return this.dataProvider;
	}

}
