package me.PauMAVA.Util.Math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Range {
	
	private Integer minBound;
	private Integer maxBound;
	
	public Range() {}
	
	public Range(Integer value1, Integer value2) {
		setBounds(value1, value2);
	}
	
	public Integer getMin() {
		return this.minBound;
	}
	
	public Integer getMax() {
		return this.maxBound;
	} 
	
	public List<Integer> getBounds() {
		return Arrays.asList(this.minBound, this.maxBound);
	}
	
	public Integer size() {
		return this.maxBound - this.minBound;
	}
	
	public Boolean setMin(Integer minBound) {
		try {
			this.minBound = minBound;
			return true;
		} catch(Exception e) {
			return false;
		}		
	}
	
	public Boolean setMax(Integer maxBound) {
		try {
			this.maxBound = maxBound;
			return true;
		} catch(Exception e) {
			return false;
		}		
	}
	
	public Boolean setBounds(Integer value1, Integer value2) {
		try {
			if(value1 <= value2) {
				this.minBound = value1;
				this.maxBound = value2;
			} else {
				this.minBound = value2;
				this.maxBound = value1;
			}
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public Float getMiddleValue() {
		if(this.minBound == null || this.maxBound == null) {
			return null;
		}
		return (this.minBound.floatValue() + this.maxBound.floatValue()) / 2F;
	}
	
	public List<Integer> asList() {
		if(this.minBound == null || this.maxBound == null) {
			return new ArrayList<Integer>();
		}
		List<Integer> list = new ArrayList<Integer>();
		Integer iterator = this.minBound;
		while(minBound <= this.maxBound) {
			list.add(iterator);
			iterator++;
		}
		return list;
	}
	
	public Integer getRandomInteger() {
		if(this.minBound == null || this.maxBound == null) {
			return null;
		}
		return new Random().nextInt(this.maxBound - this.minBound) + this.minBound;
	}
	
}