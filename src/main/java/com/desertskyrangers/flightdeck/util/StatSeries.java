package com.desertskyrangers.flightdeck.util;

import java.util.ArrayList;
import java.util.List;

public class StatSeries<T> {

	private final List<OwnedValue<T>> values;

	private double max = Double.MIN_NORMAL;

	private T maxValueOwner;

	private double avg;

	private double min = Double.MAX_VALUE;

	private T minValueOwner;

	private double sum;

	private double count;

	public StatSeries( List<OwnedValue<T>> values ) {
		this.values = new ArrayList<>( values );
		process();
	}

	public double getMax() {
		return max;
	}

	public T getMaxValueOwner() {
		return maxValueOwner;
	}

	public double getAvg() {
		return avg;
	}

	public double getMin() {
		return min;
	}

	public T getMinValueOwner() {
		return minValueOwner;
	}

	public double getSum() {
		return sum;
	}

	public double getCount() {
		return count;
	}

	private void process() {
		double sum = 0;
		for( OwnedValue<T> value : values ) {
			sum += value.value();
			if( value.value() < min ) {
				min = value.value();
				minValueOwner = value.owner();
			}
			if( value.value() > max ) {
				max = value.value();
				maxValueOwner = value.owner();
			}
		}
		this.avg = sum / values.size();
		this.sum = sum;
		this.count = values.size();
	}

}
