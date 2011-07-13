package com.siteview.ecc.system.impl;

import java.io.File;
import java.lang.management.ManagementFactory;

import com.siteview.ecc.system.Diagnosis;
import com.sun.management.OperatingSystemMXBean;

public class HardwareDiagnosisImpl extends Diagnosis {

	@Override
	public void execute() throws Exception {
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();  
		
		// 总的物理内存   
		long totalPhysicalMemorySize = osmxb.getTotalPhysicalMemorySize();
		// 剩余的物理内存    
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize(); 
        // 已使用的物理内存    
        long usedPhysicalMemorySize = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize());

		getResultList().add("CPU类型:" + osmxb.getArch());
		getResultList().add("处理器数目:" + osmxb.getAvailableProcessors());
		getResultList().add("总物理内存:" + totalPhysicalMemorySize / 1024 / 1024 + "M");
		getResultList().add("空闲内存:" + freePhysicalMemorySize / 1024 / 1024 + "M");
		getResultList().add("已使用的物理内存:" + usedPhysicalMemorySize / 1024 / 1024 + "M");
		File file = new File("/");
		if("".equals(file.getName())){
			getResultList().add("程序运行所在驱动器  容量:" + file.getTotalSpace()/1024/1024/1024 + "G");
			getResultList().add("程序运行所在驱动器  可用空间:" + file.getUsableSpace()/1024/1024/1024 + "G");		

		}else{
			getResultList().add("程序运行所在驱动器(" + file.getName() + ")容量:" + file.getTotalSpace()/1024/1024/1024 + "G");
			getResultList().add("程序运行所在驱动器(" + file.getName() + ")可用空间:" + file.getUsableSpace()/1024/1024/1024 + "G");		

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
			throw new Exception("总的物理内存推荐至少2G!");
		if ((freePhysicalMemorySize / 1024 / 1024) < 200)
			throw new Exception("空闲内存不足了!小于200M");
		if ((file.getUsableSpace() / 1024 / 1024) < 500)
			throw new Exception("程序运行所在驱动器(" + file.getName() + ")可用空间不足了!小于500M");
        
	}

	@Override
	public String getDescription() {
		return "系统硬件诊断与分析功能.诊断是否符合系统运行环境要求.";
	}

	@Override
	public String getName() {
		return "硬件诊断";
	}

}
