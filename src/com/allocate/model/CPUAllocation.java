package com.allocate.model;

import java.util.List;
/**
 * POJO class for CPU allocation. It contains keys as region, totalCost and list of Server types.
 * 
 * @author Santhoshkumar.DS
 *
 */
public class CPUAllocation implements Comparable<CPUAllocation> {
	
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
	@Override
	public int compareTo(CPUAllocation cpuAllocation) {
		Float price1 = Float.valueOf(cpuAllocation.getTotalCost().substring(1));
		Float price2 = Float.valueOf(this.getTotalCost().substring(1));
		if(price1 == price2)
			return 0;
		else
			return price1 > price2 ? -1 : 1;
	}
}
