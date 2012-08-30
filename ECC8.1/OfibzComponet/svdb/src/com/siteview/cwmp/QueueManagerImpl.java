package com.siteview.cwmp;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javolution.util.FastList;

/**
 * 
 * 线程安全队列操作实现类
 * @author hailong.yi
 *
 * @param <T>
 */
public class QueueManagerImpl<T> implements QueueManager<T> {

	public static void main(String[] args) throws Exception
	{
		final QueueManager<Integer> testQueue = new QueueManagerImpl<Integer>(10000);
		Thread thread = new Thread(){
			Integer i = 0;
			public void run(){
				while(true){
					try {
						testQueue.push(i);
						i++;
						//sleep(10);
					} catch (Exception e) {
					}
				}
			}
		};
		Thread thread2 = new Thread(){
			Integer i = 0;
			public void run(){
				while(true){
					try {
						testQueue.push(i);
						i++;
						//sleep(10);
					} catch (Exception e) {
					}
				}
			}
		};
		thread.start();
		thread2.start();
		while(true){
			long curTime = System.currentTimeMillis();
			System.out.println(testQueue.popup(10000));
			curTime = System.currentTimeMillis() - curTime;
			System.out.println(curTime);
			Thread.sleep(1000);
		}
		/*
		for (Integer i = 0 ; i<10 ; i++)
			testQueue.push(i);
		
		//testQueue.clear();
		for (Integer i = 0 ; i<10 ; i++)
			System.out.println(testQueue.get(i));
		System.out.println("----");
		for (Object obj : testQueue.getAll())
			System.out.println(obj);
		*/
	}
	private List<T> queue = new CopyOnWriteArrayList<T>();
	
	private int size = DEFAULT_SIZE;
	public QueueManagerImpl(){
		this(DEFAULT_SIZE);
	}
	
	public QueueManagerImpl(int size){
		this.setMaxSize(size);
	}

	@Override
	public void clear() throws Exception {
		this.queue.clear();
	}

	@Override
	public T get(int index) throws Exception {
		return this.queue.get(index);
	}

	@Override
	public List<T> getAll() throws Exception {
		return this.queue;
	}

	@Override
	public int getMaxSize() throws Exception {
		return this.size;
	}

	@Override
	public List<T> getNewRecords(int beginindex) throws Exception {
		List<T> retlist = new FastList<T>();
		for(int index = (this.queue.size()-1) ; index>=beginindex ; index--){
			retlist.add(this.queue.get(index));
		}
		return retlist;
	}

	@Override
	public int getSize() throws Exception {
		return this.queue.size();
	}

	@Override
	public T popup() throws Exception {
		if (this.queue.isEmpty()) return null;
		T object = this.queue.get(0);
		this.queue.remove(0);
		return object;
	}
	
	@Override
	public List<T> popup(int num) throws Exception {
		List<T> retlist = new FastList<T>();
		for (int index = 0 ; index<num ; index ++ ){
			T object = popup();
			if (object == null) break;
			retlist.add(object);
		} 
		return retlist;
	}
	@Override
	public void push(T object) throws Exception {
		if (this.queue.size() >= this.getMaxSize()) throw new Exception("0004:队列已经满了");
		this.queue.add(object);
	}
	@Override
	public void remove(int index) throws Exception {
		this.queue.remove(index);
	}
	private void setMaxSize(int size) {
		this.size = size;
	}

	@Override
	public T getOldest() throws Exception {
		if (this.queue.isEmpty()) return null;
		return this.queue.get(0);
	}

}
