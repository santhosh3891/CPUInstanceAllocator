package com.allocate.model;

import java.util.List;
/**
 * POJO class for CPU allocation. It contains keys as region, totalCost and list of Server types.
 * 
 * @author Santhoshkumar.DS
 *
 */
public class CPUAllocation  {
	
	private String region;
	private String totalCost;
	private List<CPUAllocationByServerType> servers;
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}
	public List<CPUAllocationByServerType> getServers() {
		return servers;
	}
	public void setServers(List<CPUAllocationByServerType> servers) {
		this.servers = servers;
	}
}
