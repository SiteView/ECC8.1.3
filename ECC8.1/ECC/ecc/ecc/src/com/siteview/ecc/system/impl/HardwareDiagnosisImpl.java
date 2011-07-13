package com.siteview.ecc.system.impl;

import java.io.File;
import java.lang.management.ManagementFactory;

import com.siteview.ecc.system.Diagnosis;
import com.sun.management.OperatingSystemMXBean;

public class HardwareDiagnosisImpl extends Diagnosis {

	@Override
	public void execute() throws Exception {
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();  
		
		// �ܵ������ڴ�   
		long totalPhysicalMemorySize = osmxb.getTotalPhysicalMemorySize();
		// ʣ��������ڴ�    
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize(); 
        // ��ʹ�õ������ڴ�    
        long usedPhysicalMemorySize = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize());

		getResultList().add("CPU����:" + osmxb.getArch());
		getResultList().add("��������Ŀ:" + osmxb.getAvailableProcessors());
		getResultList().add("�������ڴ�:" + totalPhysicalMemorySize / 1024 / 1024 + "M");
		getResultList().add("�����ڴ�:" + freePhysicalMemorySize / 1024 / 1024 + "M");
		getResultList().add("��ʹ�õ������ڴ�:" + usedPhysicalMemorySize / 1024 / 1024 + "M");
		File file = new File("/");
		if("".equals(file.getName())){
			getResultList().add("������������������  ����:" + file.getTotalSpace()/1024/1024/1024 + "G");
			getResultList().add("������������������  ���ÿռ�:" + file.getUsableSpace()/1024/1024/1024 + "G");		

		}else{
			getResultList().add("������������������(" + file.getName() + ")����:" + file.getTotalSpace()/1024/1024/1024 + "G");
			getResultList().add("������������������(" + file.getName() + ")���ÿռ�:" + file.getUsableSpace()/1024/1024/1024 + "G");		

		}
	
		
		/*
		System.out.println("osm.getArch() "+osmxb.getArch());   
        System.out.println("osm.getAvailableProcessors() "+osmxb.getAvailableProcessors());   
        System.out.println("osm.getCommittedVirtualMemorySize() "+osmxb.getCommittedVirtualMemorySize());   
        System.out.println("osm.getName() "+osmxb.getName());   
        System.out.println("osm.getProcessCpuTime() "+osmxb.getProcessCpuTime());   
        System.out.println("osm.getVersion() "+osmxb.getVersion());
        
        MemoryMXBean mm=(MemoryMXBean)ManagementFactory.getMemoryMXBean();   
        System.out.println("getHeapMemoryUsage "+mm.getHeapMemoryUsage());   
        System.out.println("getNonHeapMemoryUsage "+mm.getNonHeapMemoryUsage());   

        ThreadMXBean tm=(ThreadMXBean)ManagementFactory.getThreadMXBean(); 
		System.out.println("getThreadCount "+tm.getThreadCount());   
        System.out.println("getPeakThreadCount "+tm.getPeakThreadCount());   
        System.out.println("getCurrentThreadCpuTime "+tm.getCurrentThreadCpuTime());   
        System.out.println("getDaemonThreadCount "+tm.getDaemonThreadCount());   
        System.out.println("getCurrentThreadUserTime "+tm.getCurrentThreadUserTime());   

        RuntimeMXBean rmb=(RuntimeMXBean)ManagementFactory.getRuntimeMXBean();   
        System.out.println("getClassPath "+rmb.getClassPath());   
        System.out.println("getLibraryPath "+rmb.getLibraryPath());   
        System.out.println("getVmVersion "+rmb.getVmVersion());  
        */
		
		
		if ((totalPhysicalMemorySize / 1024 / 1024) < 2000)
			throw new Exception("�ܵ������ڴ��Ƽ�����2G!");
		if ((freePhysicalMemorySize / 1024 / 1024) < 200)
			throw new Exception("�����ڴ治����!С��200M");
		if ((file.getUsableSpace() / 1024 / 1024) < 500)
			throw new Exception("������������������(" + file.getName() + ")���ÿռ䲻����!С��500M");
        
	}

	@Override
	public String getDescription() {
		return "ϵͳӲ��������������.����Ƿ����ϵͳ���л���Ҫ��.";
	}

	@Override
	public String getName() {
		return "Ӳ�����";
	}

}
