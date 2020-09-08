package com.allocate;

import java.util.LinkedHashMap;
import java.util.Map;

import com.allocate.constants.IntanceAllocatorConstants;
/**
 * Utility class for Instance Allocator.
 * 
 * @author Santhoshkumar.DS
 *
 */
public class InstanceAllocatorUtil {

   /**
	* Enum class used to specify each server type available and its corresponding cpu counts.
	* 
	* @author Santhoshkumar.DS
	*
	*/
	public static enum ServerType {
		SERVERTYPE_LARGE(IntanceAllocatorConstants.LARGE , IntanceAllocatorConstants.LARGE_CPU_COUNT),
		SERVERTYPE_XLARGE(IntanceAllocatorConstants.XLARGE, IntanceAllocatorConstants.XLARGE_CPU_COUNT),
		SERVERTYPE_2XLARGE(IntanceAllocatorConstants.XLARGE_2, IntanceAllocatorConstants.XLARGE_2_CPU_COUNT),
		SERVERTYPE_4XLARGE(IntanceAllocatorConstants.XLARGE_4, IntanceAllocatorConstants.XLARGE_4_CPU_COUNT),
		SERVERTYPE_8XLARGE(IntanceAllocatorConstants.XLARGE_8, IntanceAllocatorConstants.XLARGE_8_CPU_COUT),
		SERVERTYPE_10XLARGE(IntanceAllocatorConstants.XLARGE_10, IntanceAllocatorConstants.XLARGE_10_CPU_COUNT);
		
		private String type;
		private int cpus;
		
		ServerType(String type, int cpus) {
			this.type = type;
			this.cpus = cpus;
		}
		
		public String getType() {
			return type;
		}

		public int getCpus() {
			return cpus;
		}
	}
	
	/**
	 * Method to load the server types and its cost per hour and map them to its corresponding regions.
	 * 
	 * @return map of regions and its server types and its per hour price.
	 */
	 public static Map<String, Map<ServerType, Float>> getServerCostDetails() {
		 Map<String, Map<ServerType, Float>> allRegionServerCostDetails = new LinkedHashMap<>();
		
		 Map<ServerType, Float> usEastServerCostDetails = new LinkedHashMap<>();
		 usEastServerCostDetails.put(ServerType.SERVERTYPE_LARGE, Float.valueOf("0.12"));
		 usEastServerCostDetails.put(ServerType.SERVERTYPE_XLARGE, Float.valueOf("0.23"));
		 usEastServerCostDetails.put(ServerType.SERVERTYPE_2XLARGE, Float.valueOf("0.45"));
		 usEastServerCostDetails.put(ServerType.SERVERTYPE_4XLARGE, Float.valueOf("0.774"));
		 usEastServerCostDetails.put(ServerType.SERVERTYPE_8XLARGE, Float.valueOf("1.4"));
		 usEastServerCostDetails.put(ServerType.SERVERTYPE_10XLARGE, Float.valueOf("2.82"));
		
		 Map<ServerType, Float> usWestServerCostDetails = new LinkedHashMap<>();
		 usWestServerCostDetails.put(ServerType.SERVERTYPE_LARGE, Float.valueOf("0.14"));
		 usWestServerCostDetails.put(ServerType.SERVERTYPE_2XLARGE, Float.valueOf("0.413"));
		 usWestServerCostDetails.put(ServerType.SERVERTYPE_4XLARGE, Float.valueOf("0.89"));
		 usWestServerCostDetails.put(ServerType.SERVERTYPE_8XLARGE, Float.valueOf("1.3"));
		 usWestServerCostDetails.put(ServerType.SERVERTYPE_10XLARGE, Float.valueOf("2.97"));
		
		 Map<ServerType, Float> asiaServerCostDetails = new LinkedHashMap<>();
		 asiaServerCostDetails.put(ServerType.SERVERTYPE_LARGE, Float.valueOf("0.11"));
		 asiaServerCostDetails.put(ServerType.SERVERTYPE_XLARGE, Float.valueOf("0.20"));
		 asiaServerCostDetails.put(ServerType.SERVERTYPE_4XLARGE, Float.valueOf("0.67"));
		 asiaServerCostDetails.put(ServerType.SERVERTYPE_8XLARGE, Float.valueOf("1.18"));
		
		 allRegionServerCostDetails.put(IntanceAllocatorConstants.REGION_US_EAST, usEastServerCostDetails);
		 allRegionServerCostDetails.put(IntanceAllocatorConstants.REGION_US_WEST, usWestServerCostDetails);
		 allRegionServerCostDetails.put(IntanceAllocatorConstants.REGION_US_ASIA, asiaServerCostDetails);
		
		 return allRegionServerCostDetails;
	 }
	
}
