package com.allocate.constants;

/**
 * Re-usable constants for CPUAllocator module.
 * 
 * @author Santhoshkumar.DS
 *
 */
public interface IntanceAllocatorConstants {

	public static final String BLANK_STRING ="";
	
	public static final String REGION_US_EAST = "us-east";
	public static final String REGION_US_WEST = "us-west";
	public static final String REGION_US_ASIA = "asia";
	
	public static final String LARGE = "large";
	public static final String XLARGE = "xlarge";
	public static final String XLARGE_2 = "2xlarge";
	public static final String XLARGE_4 = "4xlarge";
	public static final String XLARGE_8 = "8xlarge";
	public static final String XLARGE_10 = "10xlarge";
	
	public static final int LARGE_CPU_COUNT = 1;
	public static final int XLARGE_CPU_COUNT = 2;
	public static final int XLARGE_2_CPU_COUNT = 4;
	public static final int XLARGE_4_CPU_COUNT = 8;
	public static final int XLARGE_8_CPU_COUT = 16;
	public static final int XLARGE_10_CPU_COUNT = 32;
	
	public static final String DECIMAL_FORMAT_2_DIGITS = "#.##";
	public static final String DOLLAR ="$";
	
	static int getHighestCpuCount() {
		return XLARGE_10_CPU_COUNT;
	}
}
