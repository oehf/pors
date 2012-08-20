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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.richfaces.model.DataProvider;

import org.openehealth.pors.admin_client.IPorsAdministratorService;
import org.openehealth.pors.admin_client.UserDTO;

public abstract class AbstractDataProvider<T> implements DataProvider<T>{

	private static final long serialVersionUID = -6144863367633565142L;
	
	protected IPorsAdministratorService connector;
	protected UserDTO user;
	protected Logger logger;
	protected boolean searchMode;
	private List<T> searchResult;
	private Map<Long, T> entryMap = new HashMap<Long, T>();
	
	public AbstractDataProvider(IPorsAdministratorService connector, UserDTO user) {
		this.connector = connector;
		this.user = user;
		initLogger();
		this.searchMode = false;
	}
	
	protected abstract void initLogger();
	
	public int getRowCount() {
		if (searchMode) {
			return searchResult.size();
		}
		return getWSRowCount();
	}
	
	protected abstract int getWSRowCount();

	public List<T> getItemsByRange(int firstRow, int numberOfRows) {
		List<T> items = null;
		if (searchMode) {
			items = getSearchResultRange(firstRow, numberOfRows);
		} else {
			items = getWSItemsByRange(firstRow, numberOfRows);
		}
		entryMap.clear();
		if (items != null) {
			for (T t : items) {
				entryMap.put((Long) getKey(t), t);
			}
		} else {
			items = new ArrayList<T>();
		}
		return items;
	}
	
	protected abstract List<T> getWSItemsByRange(int firstRow, int numberOfRows);

	public T getItemByKey(Object key) {
		return entryMap.get(key);
	}
	
	public abstract Object getKey(T item);
	
	public void setSearchResult(List<T> result) {
		if (result == null) {
			logger.info("setSearchResult called with null");
		} else {
			logger.info("setSearchResult called, size: " + result.size());
		}
		this.searchResult = result;
		entryMap.clear();
		for (T t : result) {
			entryMap.put((Long) getKey(t), t);
		}
	}
	
	private List<T> getSearchResultRange(int firstRow, int numberOfRows) {
		logger.info("getSearchResultRange called, firstRow: " + firstRow + ", numberOfRows: " + numberOfRows);
		List<T> selection = new ArrayList<T>();
		int lastRow = firstRow + numberOfRows;
		if (lastRow > searchResult.size()) {
			lastRow = searchResult.size();
		}
		logger.info("getSearchResultRange called: firstRow = " + firstRow + ", lastRow = " + lastRow);
		for (int i = firstRow; i < lastRow; i++) {
			selection.add(searchResult.get(i));
		}
		return selection;
	}
	
	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}

}
