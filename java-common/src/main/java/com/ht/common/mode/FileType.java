package com.ht.common.mode;

import java.io.Serializable;

public class FileType implements Serializable {
	private static final long serialVersionUID = 5310029130020874753L;

	private String fileCode;

    private String fileType;

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}