package com.siteview.cwmp;

import java.util.List;

import com.siteview.utils.ConfigInformation;

/**
 * 
 * 线程安全队列操作实现接口
 * @author hailong.yi
 *
 * @param <T>
 */
public interface QueueManager<T> {
	public final static int DEFAULT_SIZE = ConfigInformation.getAlertQueueMaxSize();
	
	/**
	 * 清除队列中的数据
	 * @throws Exception
	 */
	public void clear() throws Exception;
	
	/**
	 * 取得队列中对应索引号的数据
	 * @param index 0开始的索引号
	 * @return
	 * @throws Exception
	 */
	public T get(int index) throws Exception;
	
	/**
	 * 取得队列中的所有元素
	 * @return 所有元素
	 * @throws Exception
	 */
	public List<T> getAll() throws Exception;
	
	/**
	 * 取得队列的允许的数据的最大数量
	 * @return 队列的允许的数据的最大数量
	 * @throws Exception
	 */
	public int getMaxSize() throws Exception;
	
	/**
	 * 取得队列中beginindex开始的所有最上层的元素
	 * @param beginindex 开始索引号
	 * @return 元素列表
	 * @throws Exception
	 */
	public List<T> getNewRecords(int beginindex)throws Exception;
	
	/**
	 * 取得队列的数据的数量
	 * @return 队列的数据的数量
	 * @throws Exception
	 */
	public int getSize() throws Exception;

	/**
	 * 弹出队列中的最底层的一个元素
	 * @return 最底层的一个元素
	 * @throws Exception
	 */
	public T popup() throws Exception;

	/**
	 * 弹出队列中的最底层的num个元素
	 * @return 最底层的num个元素
	 * @throws Exception
	 */
	public List<T> popup(int num) throws Exception;
	
	/**
	 * 在最上层压入一个元素
	 * @param object 要压入的元素
	 * @throws Exception
	 */
	public void push(T object) throws Exception;
	
	/**
	 * 移除队列中指定索引号的元素
	 * @param index 0开始的索引号
	 * @throws Exception
	 */
	public void remove(int index)throws Exception;
	
	
	public T getOldest() throws Exception;
	
}
