/**
 * 
 */
package com.siteview.ecc.tasks;

import org.apache.log4j.Logger;

/**
 * @author yuandong，task bean
 *
 */
public class Task {
	private final static Logger logger = Logger.getLogger(Task.class);

	String name;
	String type;//为1是绝对时间任务计划，2是时间段任务计划，3是相对时间任务计划，目前仅完成12
	String description;
	String start0;
	String start1;
	String start2;
	String start3;
	String start4;
	String start5;
	String start6;
	String allow0;
	String allow1;
	String allow2;
	String allow3;
	String allow4;
	String allow5;
	String allow6;
	String end0;
	String end1;
	String end2;
	String end3;
	String end4;
	String end5;
	String end6;
	
	public void display(){
		logger.info("该任务的详细如下：\n"+
	        "\n Type= "+type+
	            "\n sv_name= "+name+
	            "\n Description= "+description+
	            "\n end= "+end0+end1+end2+end3+end4+end5+end6+ 
	            "\n start1=" +start0+start1+start2+start3+start4+start5+start6+
	            "\n Allow= "+allow0+allow1+allow2+allow3+allow4+allow5+allow6);
	}
	
	
	public String getStart0() {
		return start0;
	}
	public void setStart0(String start0) {
		this.start0 = start0;
	}
	public String getStart1() {
		return start1;
	}
	public void setStart1(String start1) {
		this.start1 = start1;
	}
	public String getStart2() {
		return start2;
	}
	public void setStart2(String start2) {
		this.start2 = start2;
	}
	public String getStart3() {
		return start3;
	}
	public void setStart3(String start3) {
		this.start3 = start3;
	}
	public String getStart4() {
		return start4;
	}
	public void setStart4(String start4) {
		this.start4 = start4;
	}
	public String getStart5() {
		return start5;
	}
	public void setStart5(String start5) {
		this.start5 = start5;
	}
	public String getStart6() {
		return start6;
	}
	public void setStart6(String start6) {
		this.start6 = start6;
	}
	public String getAllow0() {
		return allow0;
	}
	public void setAllow0(String allow0) {
		this.allow0 = allow0;
	}
	public String getAllow1() {
		return allow1;
	}
	public void setAllow1(String allow1) {
		this.allow1 = allow1;
	}
	public String getAllow2() {
		return allow2;
	}
	public void setAllow2(String allow2) {
		this.allow2 = allow2;
	}
	public String getAllow3() {
		return allow3;
	}
	public void setAllow3(String allow3) {
		this.allow3 = allow3;
	}
	public String getAllow4() {
		return allow4;
	}
	public void setAllow4(String allow4) {
		this.allow4 = allow4;
	}
	public String getAllow5() {
		return allow5;
	}
	public void setAllow5(String allow5) {
		this.allow5 = allow5;
	}
	public String getAllow6() {
		return allow6;
	}
	public void setAllow6(String allow6) {
		this.allow6 = allow6;
	}
	public String getEnd0() {
		return end0;
	}
	public void setEnd0(String end0) {
		this.end0 = end0;
	}
	public String getEnd1() {
		return end1;
	}
	public void setEnd1(String end1) {
		this.end1 = end1;
	}
	public String getEnd2() {
		return end2;
	}
	public void setEnd2(String end2) {
		this.end2 = end2;
	}
	public String getEnd3() {
		return end3;
	}
	public void setEnd3(String end3) {
		this.end3 = end3;
	}
	public String getEnd4() {
		return end4;
	}
	public void setEnd4(String end4) {
		this.end4 = end4;
	}
	public String getEnd5() {
		return end5;
	}
	public void setEnd5(String end5) {
		this.end5 = end5;
	}
	public String getEnd6() {
		return end6;
	}
	public void setEnd6(String end6) {
		this.end6 = end6;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
