package com.allocate;

import java.util.Collections;
import java.util.List;

import com.allocate.model.CPUAllocation;

/**
 * 
 * InstanceAllocator specifies a single method 'getCosts' which is used to get the totalCost and servers allocated for each region based on the 3 parameters passed (i.e.) hours, price and cpus.
 * 
 * @author Santhoshkumar.DS
 *
 */
public class InstanceAllocator {

	/**
	 * This method is responsible for allocating servers by optimizing the costs.
	 * If user requested with minimum number of CPUs for H hours, then this method will allocate the required minimum cpu's for H hours and calculate the totalCost of each server type for H hours.
	 * If user requested with maximum price they can pay for H hours, then this method will allocated the required cpu's which totalCost won't cross the specified amount for those H hours.
	 * If user requested with all the parameters, require minimum of C cpu's and wouldn't want to pay more than P price for H hours, 
	 * then this method will allocate cpu's and make sure that the required cpu's are met with the given price limit.
	 * Once the cpu's are allocated, the list is then sorted in ascending order based on the totalCost of each region.
	 * 
	 * @param hours - number of hours the servers are required
	 * @param cpus - number of minimum cpus expected
	 * @param price - maximum price user can afford
	 * @return List of CPUAllocation if cpu's can be allocated with the given price/hours/cpu, else returns null
	 */
	public List<CPUAllocation> getCosts(int hours, int cpus, float price) {
		List<CPUAllocation> cpuAllocationDetails = InstanceAllocatorCalculation.getCalculatedCPUAllocation(hours, cpus, price);
		if (cpuAllocationDetails != null && cpuAllocationDetails.size() > 0) {
			Collections.sort(cpuAllocationDetails);
		}
		return cpuAllocationDetails;
	}
}
