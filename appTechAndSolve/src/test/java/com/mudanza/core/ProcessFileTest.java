package com.mudanza.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mudanza.configuration.StorageProperties;
import com.mudanza.exception.PreconditionException;
import com.mudanza.utils.Constants;



public class ProcessFileTest {
	
	ProcessFile processFile = null;
	
	@Before
	public void setUp() {
		processFile = new ProcessFile(new StorageProperties());
	}
	
	@Test
	public void testWorkDaysNullOrEmpty() {
		try{
			processFile.calculateTripsByEachDay(new ArrayList<Integer>());
			Assert.fail();
		}catch(PreconditionException pe){
			Assert.assertTrue(pe.getMessage().contains(Constants.LISTNULLOREMPTY));
		}
	}
	
	@Test
	public void testWorkDaysNotValidLessThanOne() {
		try{
			List<Integer> list =  Collections.singletonList(0);
			processFile.calculateTripsByEachDay(list);
			Assert.fail();
		}catch(PreconditionException pe){
			Assert.assertTrue(pe.getMessage().contains(Constants.WORKEDAYSNOTVALID));
		}
	}
	
	@Test
	public void testWorkDaysNotValidMoreThanFiveHundred() {
		try{
			List<Integer> list =  Collections.singletonList(501);
			processFile.calculateTripsByEachDay(list);
			Assert.fail();
		}catch(PreconditionException pe){
			Assert.assertTrue(pe.getMessage().contains(Constants.WORKEDAYSNOTVALID));
		}
	}
	
	
	
	@Test 
	public void testElementsQuantityLessThanOne(){
		try{
			List<Integer> list = new ArrayList<>(Arrays.asList(1, 0, 1));
			processFile.calculateTripsByEachDay(list);
			Assert.fail();
		}catch(PreconditionException pe){
			Assert.assertTrue(pe.getMessage().contains(Constants.ELEMENTSOUTOFPARAMETERS));
		}
	}
	
	@Test 
	public void testElementsQuantityMoreThanOneHundred(){
		try{
			List<Integer> list = new ArrayList<>(Arrays.asList(1, 101, 1));
			processFile.calculateTripsByEachDay(list);
			Assert.fail();
		}catch(PreconditionException pe){
			Assert.assertTrue(pe.getMessage().contains(Constants.ELEMENTSOUTOFPARAMETERS));
		}
	}

	
	@Test 
	public void testWeightElementLessThanOne(){
		try{
			List<Integer> list = new ArrayList<>(Arrays.asList(1, 1, 0));
			processFile.calculateTripsByEachDay(list);
			Assert.fail();
		}catch(PreconditionException pe){
			Assert.assertTrue(pe.getMessage().contains(Constants.WEIGHTSOUTOFPARAMETERS));
		}
	}
	
	@Test 
	public void testWeightElementMoreThanOneHundred(){
		try{
			List<Integer> list = new ArrayList<>(Arrays.asList(1, 1, 101));
			processFile.calculateTripsByEachDay(list);
			Assert.fail();
		}catch(PreconditionException pe){
			Assert.assertTrue(pe.getMessage().contains(Constants.WEIGHTSOUTOFPARAMETERS));
		}
	}
	
	@Test 
	public void testValidateOneTrips(){
		List<Integer> lData = new ArrayList<>(Arrays.asList(3,20,20,20));
		List<Integer> lTrips = processFile.splitWeightsByDay(lData);
		Assert.assertTrue(lTrips.get(0) == 1);
	}
	
	@Test 
	public void testValidateTwoTrips(){
		List<Integer> lData = new ArrayList<>(Arrays.asList(3,51,20,1));
		List<Integer> lTrips = processFile.splitWeightsByDay(lData);
		Assert.assertTrue(lTrips.get(0) == 2);
	}
	
	@Test 
	public void testValidateFourTrips(){
		List<Integer> lData = new ArrayList<>(Arrays.asList(6,9,19,29,39,49,59));
		List<Integer> lTrips = processFile.splitWeightsByDay(lData);
		Assert.assertTrue(lTrips.get(0) == 4);
	}
	
	
	@Test 
	public void testValidateEightTrips(){
		List<Integer> lData = new ArrayList<>(Arrays.asList(10,32,56,76,8,44,60,47,85,71,91));
		List<Integer> lTrips = processFile.splitWeightsByDay(lData);
		Assert.assertTrue(lTrips.get(0) == 8);
	}
	
}
