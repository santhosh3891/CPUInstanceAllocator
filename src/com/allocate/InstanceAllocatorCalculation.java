package com.allocate;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.allocate.InstanceAllocatorUtil.ServerType;
import com.allocate.constants.IntanceAllocatorConstants;
import com.allocate.exception.CPUAllocationException;
import com.allocate.model.CPUAllocation;
import com.allocate.model.CPUAllocationByServerType;

/**
 * InstanceAllocatorCalculation is responsible for calculating the cost and allocating the cpu's for each region based on user's varying inputs.
 * 
 * @author Santhoshkumar.DS
 *
 */
public class InstanceAllocatorCalculation {
	
	/**
	 * Returns List of allocated cpu's and its total cost per region. 
	 * This method will return null if expected cpu counts cannot be allocated with the given cpus/hours/price.
	 * It retrieves the map of all regions cost per hour for each server type, and based on the combination of inputs passed, it calls the corresponding api.
	 * 
	 * @param hours - number of hours the servers are required
	 * @param cpus - number of minimum cpus expected
	 * @param price - maximum price user can afford
	 * @return - List of CPUAllocation if cpu's can be allocated with the given price/hours/cpu, else returns null
	 */
	public static List<CPUAllocation> getCalculatedCPUAllocation(int hours, int cpus, float price) {
		List<CPUAllocation> cpuAllocationDetails = new ArrayList<>();
		Map<String, Map<ServerType, Float>> allRegionServerCostDetails =  InstanceAllocatorUtil.getServerCostDetails();
		if (allRegionServerCostDetails != null) {
			Iterator<Entry<String, Map<ServerType, Float>>> allRegionServerCostDetailsItr =  allRegionServerCostDetails.entrySet().iterator();
			while (allRegionServerCostDetailsItr.hasNext()) {
				Entry<String, Map<ServerType, Float>> allRegionServerCostDetailsEntry =  allRegionServerCostDetailsItr.next();
				String region = allRegionServerCostDetailsEntry.getKey();
				Map<ServerType, Float> serverCostDetailsMap = allRegionServerCostDetailsEntry.getValue();
				CPUAllocation cpuAllocation =  null;
				if (hours !=0 && cpus != 0 && price == 0) {
					cpuAllocation = getHoursAndCpuBasedAllocation(hours, cpus, region, serverCostDetailsMap);
				} else if (hours != 0 && cpus == 0 && price != 0) {
					cpuAllocation = getHoursAndPriceBasedAllocation(hours, price, region, serverCostDetailsMap);
				} else if (hours != 0 && cpus != 0 && price != 0) {
					try {
						cpuAllocation = getHoursPriceAndCpuBasedAllocation(cpus, hours, price, region, serverCostDetailsMap);
					} catch (CPUAllocationException cpuAllocationException) {
						System.out.println(cpuAllocationException);
					}
				}
				if (cpuAllocation != null)
					cpuAllocationDetails.add(cpuAllocation);
			}
		}
		return cpuAllocationDetails.size() > 0 ? cpuAllocationDetails : null;
	}
	
	/**
	 * Allocates the minimum CPU's as requested in the input and calculates the total cost based on the hours required.
	 * 
	 * @param hours - number of hours the servers are required
	 * @param cpus - number of minimum cpus expected
	 * @param region - Region from the cloud
	 * @param serverCostDetailsMap - server types and its cost per hour details
	 * @return - CPU allocation object with totalCost and allocated server counts.
	 */
	private static CPUAllocation getHoursAndCpuBasedAllocation(int hours, int cpus, String region, Map<ServerType, Float> serverCostDetailsMap) {
		 int totalCpus = 0;
		 Float totalCost = 0.0f;
		 boolean isPriceLimitReached = false;
		 Map<String, Integer> cpuAllocationByServerTypeMap = new LinkedHashMap<>();
		 while (totalCpus < cpus) {
			 Iterator<Entry<ServerType, Float>> serverCostDetailsMapItr = serverCostDetailsMap.entrySet().iterator();
			 while (serverCostDetailsMapItr.hasNext()) {
				 Entry<ServerType, Float> serverCostDetailsMapEntry = serverCostDetailsMapItr.next();
				 ServerType serverType = serverCostDetailsMapEntry.getKey();
				 Float perHourPrice = serverCostDetailsMapEntry.getValue();
				 
				 int serverCpus = serverType.getCpus();
				 int temp = totalCpus + serverCpus;
				 if (temp > cpus && (temp - cpus <= IntanceAllocatorConstants.getHighestCpuCount())) {
					continue;
				 } else if (temp > cpus) {
					 isPriceLimitReached = true;
					 break;
				 } else {
					 totalCpus += serverCpus;
					 Float serverHourPrice = hours * perHourPrice;
					 totalCost += serverHourPrice;
					 updateCpuAllocationByServerTypeMap(cpuAllocationByServerTypeMap, serverType.getType());
				 }
			 }
			 if (isPriceLimitReached) 
				 break;
		 }
		 return getCPUAllocationFromMap(cpuAllocationByServerTypeMap, region, totalCost);
	}
	
	/**
	 * Allocates max number of cpu's within the price limit passed and calculates the toal cost based on the hours required.
	 * 
	 * @param hours - number of hours the servers are required
	 * @param price - maximum price user can afford
	 * @param region - Region from the cloud
	 * @param serverCostDetailsMap - server types and its cost per hour details
	 * @return - CPU allocation object with totalCost and allocated server counts.
	 */
	private static CPUAllocation getHoursAndPriceBasedAllocation(int hours, float price, String region, Map<ServerType, Float> serverCostDetailsMap) {
		 Float totalCost = 0.0f;
		 boolean isPriceLimitReached = false;
		 Map<String, Integer> cpuAllocationByServerTypeMap = new LinkedHashMap<>();
		 Float largeServerHourPrice = hours * serverCostDetailsMap.get(ServerType.SERVERTYPE_LARGE);
		 while (totalCost < price) {
			 Iterator<Entry<ServerType, Float>> serverCostDetailsMapItr = serverCostDetailsMap.entrySet().iterator();
			 while (serverCostDetailsMapItr.hasNext()) {
				 Entry<ServerType, Float> serverCostDetailsMapEntry = serverCostDetailsMapItr.next();
				 ServerType serverType = serverCostDetailsMapEntry.getKey();
				 Float perHourPrice = serverCostDetailsMapEntry.getValue();
				 
				 Float serverHourPrice = hours * perHourPrice;
				 Float temp = totalCost + serverHourPrice;
				 if ((temp > price) && (temp - price >= largeServerHourPrice)) {
					 continue;
				 } else if (temp > price) {
					 isPriceLimitReached = true;
					 break;
				 } else {
					 totalCost += serverHourPrice;
					 updateCpuAllocationByServerTypeMap(cpuAllocationByServerTypeMap, serverType.getType());
				 }
			 }
			 if (isPriceLimitReached) 
				 break;
		 }
		return getCPUAllocationFromMap(cpuAllocationByServerTypeMap, region, totalCost);
	}
	
	/**
	 * Returns CPUAllocation object after allocates based on the cpus, hours and price. 
	 * 
	 * @param cpus - number of minimum cpus expected
	 * @param hours - number of hours the servers are required
	 * @param price - maximum price user can afford
	 * @param region - Region from the cloud
	 * @param serverCostDetailsMap - server types and its cost per hour details
	 * @return - CPU allocation object with totalCost and allocated server counts.
	 * @throws CPUAllocationException - this exception will be thrown when the expected cpus cannot be allocated with the given cpus/hours/price
	 */
	private static CPUAllocation getHoursPriceAndCpuBasedAllocation(int cpus, int hours, float price, String region, Map<ServerType, Float> serverCostDetailsMap) throws CPUAllocationException {
		 Float totalCost = 0.0f;
		 int totalCpus = 0;
		 boolean isPriceLimitReached = false;
		 Map<String, Integer> cpuAllocationByServerTypeMap = new LinkedHashMap<>();
		 
		 Float largeServerHourPrice = serverCostDetailsMap.get(ServerType.SERVERTYPE_LARGE);
		 if ((cpus * hours * largeServerHourPrice) > price ) {
			 String errorMsg = "Expected CPU count "+ cpus +" cannot be allocated with the given price $"+ price +" in the region: " + region + " for given hours: " + hours;
			 throw new CPUAllocationException(errorMsg);
 		 }
		 
		 while (totalCpus < cpus && totalCost < price) {
			 Iterator<Entry<ServerType, Float>> serverCostDetailsMapItr = serverCostDetailsMap.entrySet().iterator();
			 while (serverCostDetailsMapItr.hasNext()) {
				 Entry<ServerType, Float> serverCostDetailsMapEntry = serverCostDetailsMapItr.next();
				 ServerType serverType = serverCostDetailsMapEntry.getKey();
				 Float perHourPrice = serverCostDetailsMapEntry.getValue();
				 
				 Float serverHourPrice = hours * perHourPrice;
				 int serverCpus = serverType.getCpus();
				 int tempCpus = totalCpus + serverCpus;
				 Float tempPrice = totalCost + serverHourPrice;
				 if ((tempPrice > price) && (totalCpus < cpus)) {
					 serverCostDetailsMapItr.remove();
					 continue;
				 } else if((tempPrice > price) && (tempCpus > cpus)) {
					 isPriceLimitReached = true;
					 break;
				 }
				 else {
					 totalCost += serverHourPrice;
					 totalCpus += serverCpus;
					 updateCpuAllocationByServerTypeMap(cpuAllocationByServerTypeMap, serverType.getType());
				 }
			 }
			 if (isPriceLimitReached) 
				 break;
		 }
		 return getCPUAllocationFromMap(cpuAllocationByServerTypeMap, region, totalCost);
	}
	
	/**
	 * Method to put the server type in the map and increment the counter if its already exist.
	 * 
	 * @param cpuAllocationByServerTypeMap
	 * @param serverType
	 */
	private static void updateCpuAllocationByServerTypeMap(Map<String, Integer> cpuAllocationByServerTypeMap, String serverType) {
		if (cpuAllocationByServerTypeMap != null) {
			if (cpuAllocationByServerTypeMap.containsKey(serverType)) {
				 cpuAllocationByServerTypeMap.put(serverType, cpuAllocationByServerTypeMap.get(serverType)+1);
			 } else {
				 cpuAllocationByServerTypeMap.put(serverType, 1);
			 }
		}
	}
	
	/**
	 * Method to create the CPUAllocation object with the end result parameters passed.
	 * 
	 * @param cpuAllocationByServerTypeMap
	 * @param region
	 * @param totalCost
	 * @return new CPUAllocation object.
	 */
	private static CPUAllocation getCPUAllocationFromMap(Map<String, Integer> cpuAllocationByServerTypeMap, String region, Float totalCost) {
		 CPUAllocation cpuAllocation = new CPUAllocation();
		 if (cpuAllocationByServerTypeMap != null) {
			 cpuAllocation.setRegion(region);
			 String totalCostStr = IntanceAllocatorConstants.BLANK_STRING;
			 if (totalCost != null) {
				 DecimalFormat decimalFormat = new DecimalFormat(IntanceAllocatorConstants.DECIMAL_FORMAT_2_DIGITS);
				 decimalFormat.setRoundingMode(RoundingMode.UP);
				 totalCostStr = IntanceAllocatorConstants.DOLLAR;
				 totalCostStr = totalCostStr.concat(decimalFormat.format(totalCost));
			 }
			 cpuAllocation.setTotalCost(totalCostStr);
			 
			 List<CPUAllocationByServerType> cpuAllocationByServerTypeList = new ArrayList<>();
			 Iterator<Entry<String, Integer>> cpuAllocationByServerTypeItr = cpuAllocationByServerTypeMap.entrySet().iterator();
			 while(cpuAllocationByServerTypeItr.hasNext()) {
				 Entry<String, Integer> cpuAllocationByServerTypeEntry = cpuAllocationByServerTypeItr.next();
				 String serverType = cpuAllocationByServerTypeEntry.getKey();
				 int serversCount = cpuAllocationByServerTypeEntry.getValue();
				 CPUAllocationByServerType cpuAllocationByServerType = new CPUAllocationByServerType(serverType, serversCount);
				 cpuAllocationByServerTypeList.add(cpuAllocationByServerType);
			 }
			 cpuAllocation.setServers(cpuAllocationByServerTypeList);
		 }
		 return cpuAllocation;
	 }
}
