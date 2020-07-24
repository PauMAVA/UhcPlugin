/*
 * UhcPlugin
 * Copyright (c) 2019  Pau Machetti Vallverdu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.PauMAVA.UhcPlugin.util;

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