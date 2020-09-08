package com.allocate.model;
/**
 * POJO class for CPU allocation by server type. It contains fields to hold the server type and its corresponding allocated server count.
 * 
 * @author Santhoshkumar.DS
 *
 */
public class CPUAllocationByServerType {

	private String serverType;
	private int serverCount;
	
	public CPUAllocationByServerType() {}
	
	public CPUAllocationByServerType(String serverType, int serverCount) {
		this.serverType = serverType;
		this.serverCount = serverCount;
	}
	
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public int getServerCount() {
		return serverCount;
	}
	public void setServerCount(int serverCount) {
		this.serverCount = serverCount;
	}
}
