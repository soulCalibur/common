package com.ht.common.mode;

import java.util.Date;

public class FileBlock {

	public FileBlock() {
	}

	public FileBlock(String hashCode, Long fileSize, String fileName) {
		this.hashCode = hashCode;
		this.fileSize = fileSize;
		this.fileName = fileName;
		this.blockSize=0L;
		this.blockIndex=0;
		this.blockNumber=0;
		this.isFinished=0;
	}

	/**
	 * @param hashCode
	 * @param fileSize
	 * @param blockSize
	 * @param userId
	 * @param fileName
	 * @param returnUrl
	 */
	public FileBlock(String hashCode, Long fileSize, Long blockSize, String userId, String fileName, String returnUrl) {
		this.hashCode = hashCode;
		this.fileSize = fileSize;
		this.blockSize = blockSize;
		this.userId = userId;
		this.fileName = fileName;
		this.returnUrl = returnUrl;
		this.blockIndex=0;
		this.blockNumber=0;
		this.isFinished=0;
	}
	
	private String hashCode;

	private Long fileSize;

	private Long blockSize;

	private Short blockNumber;

	private Short blockIndex;

	private String userId;

	private String fileName;

	private String returnUrl;

	private Date createTime;

	private Date updateTime;

	private Short isFinished;

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(Long blockSize) {
		this.blockSize = blockSize;
	}

	public Short getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(Short blockNumber) {
		this.blockNumber = blockNumber;
	}

	public Short getBlockIndex() {
		return blockIndex;
	}

	public void setBlockIndex(Short blockIndex) {
		this.blockIndex = blockIndex;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}



	public Short getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Short isFinished) {
		this.isFinished = isFinished;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FileBlock [");
		sb.append("hashCode=").append(hashCode).append(",");
		sb.append("userId=").append(userId).append(",");
		sb.append("fileName=").append(fileName).append(",");
		sb.append("fileSize=").append(fileSize).append(",");
		sb.append("blockSize=").append(blockSize).append(",");
		sb.append("blockNumber=").append(blockNumber).append(",");
		sb.append("blockIndex=").append(blockIndex).append(",");
		sb.append("createTime=").append(createTime).append(",");
		sb.append("updateTime=").append(updateTime).append(",");
		sb.append("isFinished=").append(isFinished);
		sb.append("]");
		return sb.toString();
	}
}