package com.allocate.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.allocate.InstanceAllocator;
import com.allocate.constants.IntanceAllocatorConstants;
import com.allocate.model.CPUAllocation;
import com.allocate.model.CPUAllocationByServerType;

/**
 * Test class for CPU allocation module.
 * 
 * @author Santhoshkumar.DS
 *
 */
public class InstanceAllocatorTest {

	private InstanceAllocator instanceAllocator;
	
	@Before
	public void setUp() {
		instanceAllocator = new InstanceAllocator(); 
	}
	
	@Test
	public void testGetCostsWithCpuAndHours() {
		int cpus = 115;
		int hours = 24;
		List<CPUAllocation> cpuAllocations = instanceAllocator.getCosts(hours, cpus, 0);
		assertNotNull(cpuAllocations);
		for(CPUAllocation cpuAllocation : cpuAllocations) {
			int totalCpus = getCpusCount(cpuAllocation.getServers());
			assertTrue(totalCpus <= cpus);
		}
	}
	
	@Test
	public void testGetCostsWithHoursAndPrice() {
		int hours = 8;
		float price = 29;
		List<CPUAllocation> cpuAllocations = instanceAllocator.getCosts(hours, 0, price);
		assertNotNull(cpuAllocations);
		for(CPUAllocation cpuAllocation : cpuAllocations) {
			float totalCost = getTotalCost(cpuAllocation.getTotalCost());
			assertTrue(totalCost <= price);
		}
	}
	
	@Test
	public void testGetCostsWithHoursPriceAndCpus() {
		int cpus = 214;
		int hours = 7;
		float price = 95;
		List<CPUAllocation> cpuAllocations = instanceAllocator.getCosts(hours, cpus, price);
		assertNull(cpuAllocations);
		
		price=250;
		cpuAllocations = instanceAllocator.getCosts(hours, cpus, price);
		assertNotNull(cpuAllocations);
		
		for(CPUAllocation cpuAllocation : cpuAllocations) {
			int totalCpus = getCpusCount(cpuAllocation.getServers());
			assertTrue(totalCpus >= cpus);
		}
		
		for(CPUAllocation cpuAllocation : cpuAllocations) {
			float totalCost = getTotalCost(cpuAllocation.getTotalCost());
			assertTrue(totalCost <= price);
		}
		
	}
	
	private int getCpusCount(List<CPUAllocationByServerType> cpuAllocationByServerTypes) {
		int totalCpus = 0;
		for(CPUAllocationByServerType cpuAllocationByServerType : cpuAllocationByServerTypes) {
			String serverType = cpuAllocationByServerType.getServerType();
			if (IntanceAllocatorConstants.LARGE.equals(serverType)) {
				totalCpus += (IntanceAllocatorConstants.LARGE_CPU_COUNT * cpuAllocationByServerType.getServerCount());
			} else if (IntanceAllocatorConstants.XLARGE.equals(serverType)) {
				totalCpus += (IntanceAllocatorConstants.XLARGE_CPU_COUNT * cpuAllocationByServerType.getServerCount());
			} else if (IntanceAllocatorConstants.XLARGE_2.equals(serverType)) {
				totalCpus += (IntanceAllocatorConstants.XLARGE_2_CPU_COUNT * cpuAllocationByServerType.getServerCount());
			} else if (IntanceAllocatorConstants.XLARGE_4.equals(serverType)) {
				totalCpus += (IntanceAllocatorConstants.XLARGE_4_CPU_COUNT * cpuAllocationByServerType.getServerCount());
			} else if (IntanceAllocatorConstants.XLARGE_8.equals(serverType)) {
				totalCpus += (IntanceAllocatorConstants.XLARGE_8_CPU_COUT * cpuAllocationByServerType.getServerCount());
			} else if (IntanceAllocatorConstants.XLARGE_10.equals(serverType)) {
				totalCpus += (IntanceAllocatorConstants.XLARGE_10_CPU_COUNT * cpuAllocationByServerType.getServerCount());
			}
		}
		return totalCpus;
	}
	
	private float getTotalCost(String totalCostStr) {
		float totalCost = 0.0f;
		if (totalCostStr != null && !IntanceAllocatorConstants.BLANK_STRING.equals(totalCostStr)) {
			totalCostStr = totalCostStr.substring(1);
			totalCost = Float.valueOf(totalCostStr);
		}
		return totalCost;
	}

}
