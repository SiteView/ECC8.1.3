/**
 * 
 */
package com.siteview.ecc.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.ecc.tasks.Task;
import com.siteview.jsvapi.Jsvapi;

/**
 * @author yuandong封装任务计划的相关操作目前仅是时间段和绝对任务计划 不了解相对任务计划与此数据结构是否相符
 */

public class TaskPack {

	private Map<String, Map<String, String>> m_fmap = new HashMap<String, Map<String, String>>();
	private Task task;

	public void createTask() {
		// Helper.XfireCreateKeyValue("dowhat","GetTask"),
		// Helper.XfireCreateKeyValue("id",name)
	}

	public Task findByName(String name) throws Exception {
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetTask");
		ndata.put("id", name);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		// display();
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
		m_fmap = ret.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");
		Task t = new Task();
		Map<String, String> m = m_fmap.get("property");
		if (m.get("Type").equals("1")) {
			t.setName(m.get("sv_name"));
			t.setDescription(m.get("Description"));
			t.setType(m.get("Type"));
			t.setStart0(m.get("start0"));
			t.setStart1(m.get("start1"));
			t.setStart2(m.get("start2"));
			t.setStart3(m.get("start3"));
			t.setStart4(m.get("start4"));
			t.setStart5(m.get("start5"));
			t.setStart6(m.get("start6"));
			t.setAllow0(m.get("Allow0"));
			t.setAllow1(m.get("Allow1"));
			t.setAllow2(m.get("Allow2"));
			t.setAllow3(m.get("Allow3"));
			t.setAllow4(m.get("Allow4"));
			t.setAllow5(m.get("Allow5"));
			t.setAllow6(m.get("Allow6"));
		} else if (m.get("Type").equals("2")) {
			t.setName(m.get("sv_name"));
			t.setDescription(m.get("Description"));
			t.setType(m.get("Type"));
			t.setStart0(m.get("start0"));
			t.setStart1(m.get("start1"));
			t.setStart2(m.get("start2"));
			t.setStart3(m.get("start3"));
			t.setStart4(m.get("start4"));
			t.setStart5(m.get("start5"));
			t.setStart6(m.get("start6"));
			t.setAllow0(m.get("Allow0"));
			t.setAllow1(m.get("Allow1"));
			t.setAllow2(m.get("Allow2"));
			t.setAllow3(m.get("Allow3"));
			t.setAllow4(m.get("Allow4"));
			t.setAllow5(m.get("Allow5"));
			t.setAllow6(m.get("Allow6"));
			t.setEnd0(m.get("end0"));
			t.setEnd1(m.get("end1"));
			t.setEnd2(m.get("end2"));
			t.setEnd3(m.get("end3"));
			t.setEnd4(m.get("end4"));
			t.setEnd5(m.get("end5"));
			t.setEnd6(m.get("end6"));
		} else if (m.get("Type").equals("3")){
			t.setName(m.get("sv_name"));
			t.setDescription(m.get("Description"));
			t.setType(m.get("Type"));
			t.setStart0(m.get("start0"));
			t.setStart1(m.get("start1"));
			t.setStart2(m.get("start2"));
			t.setStart3(m.get("start3"));
			t.setStart4(m.get("start4"));
			t.setStart5(m.get("start5"));
			t.setStart6(m.get("start6"));
		}
		return t;
	}
	
	public Task[] findTaskAbsloute()//获取绝对任务计划
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetAllTask");
		RetMapInMap retm = ManageSvapi.GetUnivData(ndata);
		m_fmap = retm.getFmap();
		if (m_fmap.containsKey("return")){
			m_fmap.remove("return");
		}
		Set<String> set = m_fmap.keySet();
		ArrayList<String> keylist = new ArrayList<String>();
		ArrayList <Task>tasklist = new ArrayList<Task>();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			if(key == null){
				continue;
			}
			if("".equals(key)){
				continue;
			}
			Map<String, String> value = m_fmap.get(key);
			if (value.get("Type").equals("1")) 
			{
				keylist.add(key);//绝对时间任务key
			}
		}
		Collections.sort(keylist);

		for(String key:keylist){
			Map<String, String> value = m_fmap.get(key);
			Task task = new Task();
			task.setName(key);
			task.setDescription(value.get("Description"));
			task.setType(value.get("Type"));
			task.setStart0(value.get("start0"));
			task.setStart1(value.get("start1"));
			task.setStart2(value.get("start2"));
			task.setStart3(value.get("start3"));
			task.setStart4(value.get("start4"));
			task.setStart5(value.get("start5"));
			task.setStart6(value.get("start6"));
			task.setAllow0(value.get("Allow0"));
			task.setAllow1(value.get("Allow1"));
			task.setAllow2(value.get("Allow2"));
			task.setAllow3(value.get("Allow3"));
			task.setAllow4(value.get("Allow4"));
			task.setAllow5(value.get("Allow5"));
			task.setAllow6(value.get("Allow6"));
			tasklist.add(task);
		}

		Task[] task = new Task[keylist.size()];
		for(int i=0;i<tasklist.size();i++){
			task[i] = tasklist.get(i);
		}
		return task;	
	}
	
	public Task[] findTaskPeriod(){//相对时间任务计划
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetAllTask");
		RetMapInMap retm = ManageSvapi.GetUnivData(ndata);
		m_fmap = retm.getFmap();
		if (m_fmap.containsKey("return")){
			m_fmap.remove("return");
		}
		Set<String> set = m_fmap.keySet();
		ArrayList<String> keylist = new ArrayList<String>();
		ArrayList <Task>tasklist = new ArrayList<Task>();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			if(key == null){
				continue;
			}
			if("".equals(key)){
				continue;
			}
			Map<String, String> value = m_fmap.get(key);
			if (value.get("Type").equals("2")) 
			{
				keylist.add(key);
			}
		}
		Collections.sort(keylist);

		for(String key:keylist){
			Map<String, String> value = m_fmap.get(key);
			Task task = new Task();
			task.setName(key);
			task.setDescription(value.get("Description"));
			task.setType(value.get("Type"));
			task.setStart0(value.get("start0"));
			task.setStart1(value.get("start1"));
			task.setStart2(value.get("start2"));
			task.setStart3(value.get("start3"));
			task.setStart4(value.get("start4"));
			task.setStart5(value.get("start5"));
			task.setStart6(value.get("start6"));
			task.setAllow0(value.get("Allow0"));
			task.setAllow1(value.get("Allow1"));
			task.setAllow2(value.get("Allow2"));
			task.setAllow3(value.get("Allow3"));
			task.setAllow4(value.get("Allow4"));
			task.setAllow5(value.get("Allow5"));
			task.setAllow6(value.get("Allow6"));
			task.setEnd0(value.get("end0"));
			task.setEnd1(value.get("end1"));
			task.setEnd2(value.get("end2"));
			task.setEnd3(value.get("end3"));
			task.setEnd4(value.get("end4"));
			task.setEnd5(value.get("end5"));
			task.setEnd6(value.get("end6"));
			tasklist.add(task);
		}

		Task[] task = new Task[keylist.size()];
		for(int i=0;i<tasklist.size();i++){
			task[i] = tasklist.get(i);
		}
		return task;
	}

	public Task[] findTaskRelative(){//绝对时间任务计划
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetAllTask");
		RetMapInMap retm = ManageSvapi.GetUnivData(ndata);
		m_fmap = retm.getFmap();
		if (m_fmap.containsKey("return")){
			m_fmap.remove("return");
		}
		Set<String> set = m_fmap.keySet();
		ArrayList<String> keylist = new ArrayList<String>();
		ArrayList <Task>tasklist = new ArrayList<Task>();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			if(key == null){
				continue;
			}
			if("".equals(key)){
				continue;
			}
			Map<String, String> value = m_fmap.get(key);
			if (value.get("Type").equals("3")) 
			{
				keylist.add(key);
			}
		}
		Collections.sort(keylist);

		for(String key:keylist){
			Map<String, String> value = m_fmap.get(key);
			Task task = new Task();
			task.setName(key);
			task.setDescription(value.get("Description"));
			task.setType(value.get("Type"));
			task.setStart0(value.get("start0"));
			task.setStart1(value.get("start1"));
			task.setStart2(value.get("start2"));
			task.setStart3(value.get("start3"));
			task.setStart4(value.get("start4"));
			task.setStart5(value.get("start5"));
			task.setStart6(value.get("start6"));
			tasklist.add(task);
		}

		Task[] task = new Task[keylist.size()];
		for(int i=0;i<tasklist.size();i++){
			task[i] = tasklist.get(i);
		}
		return task;
	}

/*
 * 根据类型 获取任务计划  // 类型“1”和“2” 3表示绝对任务计划和时间段任务计划和相对任务计划
 * */
	public Task[] findAllByType(String type) {
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetAllTask");
		RetMapInMap retm = ManageSvapi.GetUnivData(ndata);
		m_fmap = retm.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");

		Set<String> set = m_fmap.keySet();
		if("1".equals(type)){
			ArrayList <Task>tasklist = new ArrayList<Task>();
			for (Iterator<String> it = set.iterator(); it.hasNext();) {
				String key = it.next();
				Map<String, String> value = m_fmap.get(key);
				if (value.get("Type").equals("1")) 
				{
					Task task = new Task();
					task.setName(key);
					task.setDescription(value.get("Description"));
					task.setType(value.get("Type"));
					task.setStart0(value.get("start0"));
					task.setStart1(value.get("start1"));
					task.setStart2(value.get("start2"));
					task.setStart3(value.get("start3"));
					task.setStart4(value.get("start4"));
					task.setStart5(value.get("start5"));
					task.setStart6(value.get("start6"));
					task.setAllow0(value.get("Allow0"));
					task.setAllow1(value.get("Allow1"));
					task.setAllow2(value.get("Allow2"));
					task.setAllow3(value.get("Allow3"));
					task.setAllow4(value.get("Allow4"));
					task.setAllow5(value.get("Allow5"));
					task.setAllow6(value.get("Allow6"));
					tasklist.add(task);
				}
			}
			Task[] task1 = new Task[tasklist.size()];
			for(int i=0;i<tasklist.size();i++){
				task1[i] = tasklist.get(i);
			}
			return task1;
			
		}else if("2".equals(type)){
			ArrayList <Task>tasklist = new ArrayList<Task>();
			for (Iterator<String> it = set.iterator(); it.hasNext();) {
				String key = it.next();
				Map<String, String> value = m_fmap.get(key);
				if (value.get("Type").equals("2")) 
				{
					Task task = new Task();
					task.setName(key);
					task.setDescription(value.get("Description"));
					task.setType(value.get("Type"));
					task.setStart0(value.get("start0"));
					task.setStart1(value.get("start1"));
					task.setStart2(value.get("start2"));
					task.setStart3(value.get("start3"));
					task.setStart4(value.get("start4"));
					task.setStart5(value.get("start5"));
					task.setStart6(value.get("start6"));
					task.setAllow0(value.get("Allow0"));
					task.setAllow1(value.get("Allow1"));
					task.setAllow2(value.get("Allow2"));
					task.setAllow3(value.get("Allow3"));
					task.setAllow4(value.get("Allow4"));
					task.setAllow5(value.get("Allow5"));
					task.setAllow6(value.get("Allow6"));
					task.setEnd0(value.get("end0"));
					task.setEnd1(value.get("end1"));
					task.setEnd2(value.get("end2"));
					task.setEnd3(value.get("end3"));
					task.setEnd4(value.get("end4"));
					task.setEnd5(value.get("end5"));
					task.setEnd6(value.get("end6"));
					tasklist.add(task);
				}
			}
			Task[] task2 = new Task[tasklist.size()];
			for(int i=0;i<tasklist.size();i++){
				task2[i] = tasklist.get(i);
			}
			return task2;
		}else if("3".equals(type)){
			ArrayList <Task>tasklist = new ArrayList<Task>();
			for (Iterator<String> it = set.iterator(); it.hasNext();) {
				String key = it.next();
				Map<String, String> value = m_fmap.get(key);
				if (value.get("Type").equals("3")) 
				{
					Task task = new Task();
					task.setName(key);
					task.setDescription(value.get("Description"));
					task.setType(value.get("Type"));
					task.setStart0(value.get("start0"));
					task.setStart1(value.get("start1"));
					task.setStart2(value.get("start2"));
					task.setStart3(value.get("start3"));
					task.setStart4(value.get("start4"));
					task.setStart5(value.get("start5"));
					task.setStart6(value.get("start6"));
					tasklist.add(task);
				}
			}
			Task[] task3 = new Task[tasklist.size()];
			for(int i=0;i<tasklist.size();i++){
				task3[i] = tasklist.get(i);
			}
			return task3;
		}else
		{
			return null;
		}
	}
	/*
	 * 获取所有的任务计划
	 * */
	public Task[] findAllTasks() {
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetAllTask");
		RetMapInMap retm = ManageSvapi.GetUnivData(ndata);
		m_fmap = retm.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");

		Set<String> set = m_fmap.keySet();
		ArrayList <Task>tasklist = new ArrayList<Task>();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			Map<String, String> value = m_fmap.get(key);
			if (value.get("Type").equals("1")) 
			{
				Task task = new Task();
				task.setName(key);
				task.setDescription(value.get("Description"));
				task.setType(value.get("Type"));
				task.setStart0(value.get("start0"));
				task.setStart1(value.get("start1"));
				task.setStart2(value.get("start2"));
				task.setStart3(value.get("start3"));
				task.setStart4(value.get("start4"));
				task.setStart5(value.get("start5"));
				task.setStart6(value.get("start6"));
				task.setAllow0(value.get("Allow0"));
				task.setAllow1(value.get("Allow1"));
				task.setAllow2(value.get("Allow2"));
				task.setAllow3(value.get("Allow3"));
				task.setAllow4(value.get("Allow4"));
				task.setAllow5(value.get("Allow5"));
				task.setAllow6(value.get("Allow6"));
				tasklist.add(task);
			}
			if (value.get("Type").equals("2")) 
			{
				Task task = new Task();
				task.setName(key);
				task.setDescription(value.get("Description"));
				task.setType(value.get("Type"));
				task.setStart0(value.get("start0"));
				task.setStart1(value.get("start1"));
				task.setStart2(value.get("start2"));
				task.setStart3(value.get("start3"));
				task.setStart4(value.get("start4"));
				task.setStart5(value.get("start5"));
				task.setStart6(value.get("start6"));
				task.setAllow0(value.get("Allow0"));
				task.setAllow1(value.get("Allow1"));
				task.setAllow2(value.get("Allow2"));
				task.setAllow3(value.get("Allow3"));
				task.setAllow4(value.get("Allow4"));
				task.setAllow5(value.get("Allow5"));
				task.setAllow6(value.get("Allow6"));
				task.setEnd0(value.get("end0"));
				task.setEnd1(value.get("end1"));
				task.setEnd2(value.get("end2"));
				task.setEnd3(value.get("end3"));
				task.setEnd4(value.get("end4"));
				task.setEnd5(value.get("end5"));
				task.setEnd6(value.get("end6"));
				tasklist.add(task);
			}
			if (value.get("Type").equals("3")) 
			{
				Task task = new Task();
				task.setName(key);
				task.setDescription(value.get("Description"));
				task.setType(value.get("Type"));
				task.setStart0(value.get("start0"));
				task.setStart1(value.get("start1"));
				task.setStart2(value.get("start2"));
				task.setStart3(value.get("start3"));
				task.setStart4(value.get("start4"));
				task.setStart5(value.get("start5"));
				task.setStart6(value.get("start6"));
				tasklist.add(task);
			}
		}
		Task[] task = new Task[tasklist.size()];
		for(int i=0;i<tasklist.size();i++){
			task[i] = tasklist.get(i);
		}
		return task;
	}
/*	
	public Task[] findAllByType(String type) {
		//通过类型得到task[]
		// 类型“1”和“2” 3表示绝对任务计划和时间段任务计划和相对任务计划
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetAllTask");
		RetMapInMap retm = ManageSvapi.GetUnivData(ndata);
		m_fmap = retm.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");
		Task[] t = new Task[m_fmap.size()];
		for (int i = 0; i < t.length; i++) {
			t[i] = new Task();
		}
		Set<String> set = m_fmap.keySet();
		int i = 0;
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			Map<String, String> value = m_fmap.get(key);
//			if (value.get("Type").equals(type) || type == null) 
			if (value.get("Type").equals("1")) 
			{
				//t[i].setName(value.get("sv_name"));//为配置默认数据//修改
				t[i].setName(key);
				t[i].setDescription(value.get("Description"));
				t[i].setType(value.get("Type"));
				if (value.get("Type").equals("1")){
				t[i].setStart0(value.get("start0"));
				t[i].setStart1(value.get("start1"));
				t[i].setStart2(value.get("start2"));
				t[i].setStart3(value.get("start3"));
				t[i].setStart4(value.get("start4"));
				t[i].setStart5(value.get("start5"));
				t[i].setStart6(value.get("start6"));
				t[i].setAllow0(value.get("Allow0"));
				t[i].setAllow1(value.get("Allow1"));
				t[i].setAllow2(value.get("Allow2"));
				t[i].setAllow3(value.get("Allow3"));
				t[i].setAllow4(value.get("Allow4"));
				t[i].setAllow5(value.get("Allow5"));
				t[i].setAllow6(value.get("Allow6"));
			}
			if (value.get("Type").equals("2")) 
			{
				t[i].setStart0(value.get("start0"));
				t[i].setStart1(value.get("start1"));
				t[i].setStart2(value.get("start2"));
				t[i].setStart3(value.get("start3"));
				t[i].setStart4(value.get("start4"));
				t[i].setStart5(value.get("start5"));
				t[i].setStart6(value.get("start6"));
				t[i].setAllow0(value.get("Allow0"));
				t[i].setAllow1(value.get("Allow1"));
				t[i].setAllow2(value.get("Allow2"));
				t[i].setAllow3(value.get("Allow3"));
				t[i].setAllow4(value.get("Allow4"));
				t[i].setAllow5(value.get("Allow5"));
				t[i].setAllow6(value.get("Allow6"));
				t[i].setEnd0(value.get("end0"));
				t[i].setEnd1(value.get("end1"));
				t[i].setEnd2(value.get("end2"));
				t[i].setEnd3(value.get("end3"));
				t[i].setEnd4(value.get("end4"));
				t[i].setEnd5(value.get("end5"));
				t[i].setEnd6(value.get("end6"));
			}
			if (value.get("Type").equals("3"))
			{
				t[i].setStart0(value.get("start0"));
				t[i].setStart1(value.get("start1"));
				t[i].setStart2(value.get("start2"));
				t[i].setStart3(value.get("start3"));
				t[i].setStart4(value.get("start4"));
				t[i].setStart5(value.get("start5"));
				t[i].setStart6(value.get("start6"));
			}
			i++;
		}
			// index.add((value.get("sv_monitortype")));

		}
		return t;
	}*/
	public void deleteTaskByName(String name) throws Exception {
		// Helper.XfireCreateKeyValue("dowhat","DeleteTask"),
		// Helper.XfireCreateKeyValue("id",taskName)
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "DeleteTask");
		ndata.put("id", name);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
	}

	public void createTask(Task task) throws Exception {
		// Helper.XfireCreateKeyValue("dowhat","CreateTask"),
		// Helper.XfireCreateKeyValue("id",task.Name)
		// 创建一个新的task；update会添加数据进去
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "CreateTask");
		ndata.put("id", task.getName());
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
		this.Update(task);
	}

	public void Update(Task task) throws Exception {
		// Helper.XfireCreateKeyValue("dowhat","SubmitTask"),
		// Helper.XfireCreateKeyValue("del_supplement","true")
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "SubmitTask");
		ndata.put("del_supplement", "true");
		Map<String, Map<String, String>> rm = new HashMap<String, Map<String, String>>();
		Map<String, String> m = new HashMap<String, String>();
		m.put("sv_name", task.getName());
		m.put("Description", task.getDescription());
		m.put("Type", task.getType());
		if (task.getType().equals("1")) {//绝对
			m.put("start0", task.getStart0());
			m.put("start1", task.getStart1());
			m.put("start2", task.getStart2());
			m.put("start3", task.getStart3());
			m.put("start4", task.getStart4());
			m.put("start5", task.getStart5());
			m.put("start6", task.getStart6());
			m.put("Allow0", task.getAllow0());
			m.put("Allow1", task.getAllow1());
			m.put("Allow2", task.getAllow2());
			m.put("Allow3", task.getAllow3());
			m.put("Allow4", task.getAllow4());
			m.put("Allow5", task.getAllow5());
			m.put("Allow6", task.getAllow6());
		}
		if (task.getType().equals("2")) {//时间段
			m.put("start0", task.getStart0());
			m.put("start1", task.getStart1());
			m.put("start2", task.getStart2());
			m.put("start3", task.getStart3());
			m.put("start4", task.getStart4());
			m.put("start5", task.getStart5());
			m.put("start6", task.getStart6());
			m.put("Allow0", task.getAllow0());
			m.put("Allow1", task.getAllow1());
			m.put("Allow2", task.getAllow2());
			m.put("Allow3", task.getAllow3());
			m.put("Allow4", task.getAllow4());
			m.put("Allow5", task.getAllow5());
			m.put("Allow6", task.getAllow6());
			m.put("end0", task.getEnd0());
			m.put("end1", task.getEnd1());
			m.put("end2", task.getEnd2());
			m.put("end3", task.getEnd3());
			m.put("end4", task.getEnd4());
			m.put("end5", task.getEnd5());
			m.put("end6", task.getEnd6());
		}
		if (task.getType().equals("3")){//相对
			m.put("start0", task.getStart0());
			m.put("start1", task.getStart1());
			m.put("start2", task.getStart2());
			m.put("start3", task.getStart3());
			m.put("start4", task.getStart4());
			m.put("start5", task.getStart5());
			m.put("start6", task.getStart6());
		}
		Map<String, String> re = new HashMap<String, String>();
		re.put("id", task.getName());
		re.put("return", "true");
		rm.put("return", re);
		rm.put("property", m);
		// 公用数据提交
		RetMapInMap ret = ManageSvapi.SubmitUnivData(rm, ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to updata task :" + ret.getEstr());

		m_fmap = null;
	}

	public void displayFmap() {
		Jsvapi.getInstance().DisplayUtilMapInMap(m_fmap);
	}
}
