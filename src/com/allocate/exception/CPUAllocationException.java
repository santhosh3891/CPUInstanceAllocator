package com.allocate.exception;

/**
 * Class to handle any CPU allocation user defined exception. 
 * 
 * @author Santhoshkumar.DS
 *
 */
public class CPUAllocationException extends Exception {

	private static final long serialVersionUID = -8986122556214132370L;

	private String errorMessage;
	
	public CPUAllocationException() {}
	
	public CPUAllocationException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String toString() {
		return "CPUAllocationException occurred: " + this.errorMessage;
	}
}
