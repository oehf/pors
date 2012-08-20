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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.openehealth.pors.admin_frontend.guiengine.util.Fields;

import org.openehealth.pors.admin_client.ImportResultDTO;

public class ImportBean {
	
	private File uploadedFile;
	
	private List<SelectItem> referenceFields;
	
	private List<ImportWrapper> fields;
	private ImportResultDTO result;
	
	private boolean statusUpdate;
	
	public ImportBean(List<SelectItem> referenceFields) {
		this.referenceFields = referenceFields;
		this.statusUpdate = false;
	}
	
	
	public void updateAvailableFields() {
		for (ImportWrapper field : fields) {
			if (field.getSelectedField() != null && !field.getSelectedField().getValue().equals(Fields.NO_SELECTION)) {
				
				for (SelectItem item : referenceFields) {
					if (item.getValue().equals(field.getSelectedField().getValue())) {
						item.setDisabled(true);
					}
				}
			}
		}
		
		for (ImportWrapper field : fields) {
			List<SelectItem> available = new ArrayList<SelectItem>();
			if (field.getSelectedField() != null
					&& field.getSelectedField().getValue() != null
					&& !field.getSelectedField().getValue()
							.equals(Fields.NO_SELECTION)) {
				available.add(new SelectItem(field.getSelectedField()
						.getValue()));
			}
			for (SelectItem item : referenceFields) {
				if (!item.isDisabled()) {
					available.add(new SelectItem(item.getValue()));
				}
			}
			field.setAvailable(available);	
		}
		for (SelectItem item : referenceFields) {
			item.setDisabled(false);
		}
	}
	
	
	
	public void initializeFields(List<String> fields1) {
		fields = new ArrayList<ImportWrapper>();
		for (String string : fields1) {
			ImportWrapper wrapper = new ImportWrapper();
			wrapper.setUserField(new SelectItem(string));
			wrapper.setSelectedField(new SelectItem());
			wrapper.setAvailable(referenceFields);
			fields.add(wrapper);
		}
	}

	public List<SelectItem> getReferenceFields() {
		return referenceFields;
	}

	public void setReferenceFields(List<SelectItem> referencerFields) {
		this.referenceFields = referencerFields;
	}

	public File getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(File uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public List<ImportWrapper> getFields() {
		return fields;
	}

	public void setFields(List<ImportWrapper> fields) {
		this.fields = fields;
	}


	public ImportResultDTO getResult() {
		return result;
	}


	public void setResult(ImportResultDTO result) {
		this.result = result;
		statusUpdate = true;
		if ((result.getErrorMessage() != null && result.getErrorMessage().length() > 0) ||
				result.getProcessed() == (result.getAddEntries() + result.getUpdateEntries())) {
			if (result.getProcessed() != 0) {
				statusUpdate = false;
			}
		}
	}


	public boolean isStatusUpdate() {
		return statusUpdate;
	}


	public void setStatusUpdate(boolean statusUpdate) {
		this.statusUpdate = statusUpdate;
	}


}
