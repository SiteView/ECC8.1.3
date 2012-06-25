package com.siteview.ecc.report.beans;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class TendencyCheckDataBean {
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String name;
	
	private String type;
	
	private String max;
	
	private String min;
	
	private String average;
	
	private String latest;
	
	private boolean color;
	
	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public TendencyCheckDataBean(){}
	
	public TendencyCheckDataBean(String name, String type, String max,
			String min, String average, String latest, String max_when) {
		super();
		this.name = name;
		this.type = type;
		this.max = max;
		this.min = min;
		this.average = average;
		this.latest = latest;
		this.max_when = max_when;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getLatest() {
		return latest;
	}

	public void setLatest(String latest) {
		this.latest = latest;
	}

	public String getMax_when() {
		return max_when;
	}

	public void setMax_when(String max_when) {
		this.max_when = max_when;
	}

	private String max_when;
	public String getStart_when() {
		return start_when;
	}

	public void setStart_when(String start_when) {
		this.start_when = start_when;
	}

	public String getEnd_when() {
		return end_when;
	}

	public void setEnd_when(String end_when) {
		this.end_when = end_when;
	}

	private String start_when;
	private String end_when;
	public String getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
	}

	private String errorValue;
}
