package com.siteview.cwmp;

import java.util.List;

import com.siteview.utils.ConfigInformation;

/**
 * 
 * �̰߳�ȫ���в���ʵ�ֽӿ�
 * @author hailong.yi
 *
 * @param <T>
 */
public interface QueueManager<T> {
	public final static int DEFAULT_SIZE = ConfigInformation.getAlertQueueMaxSize();
	
	/**
	 * ��������е�����
	 * @throws Exception
	 */
	public void clear() throws Exception;
	
	/**
	 * ȡ�ö����ж�Ӧ�����ŵ�����
	 * @param index 0��ʼ��������
	 * @return
	 * @throws Exception
	 */
	public T get(int index) throws Exception;
	
	/**
	 * ȡ�ö����е�����Ԫ��
	 * @return ����Ԫ��
	 * @throws Exception
	 */
	public List<T> getAll() throws Exception;
	
	/**
	 * ȡ�ö��е���������ݵ��������
	 * @return ���е���������ݵ��������
	 * @throws Exception
	 */
	public int getMaxSize() throws Exception;
	
	/**
	 * ȡ�ö�����beginindex��ʼ���������ϲ��Ԫ��
	 * @param beginindex ��ʼ������
	 * @return Ԫ���б�
	 * @throws Exception
	 */
	public List<T> getNewRecords(int beginindex)throws Exception;
	
	/**
	 * ȡ�ö��е����ݵ�����
	 * @return ���е����ݵ�����
	 * @throws Exception
	 */
	public int getSize() throws Exception;

	/**
	 * ���������е���ײ��һ��Ԫ��
	 * @return ��ײ��һ��Ԫ��
	 * @throws Exception
	 */
	public T popup() throws Exception;

	/**
	 * ���������е���ײ��num��Ԫ��
	 * @return ��ײ��num��Ԫ��
	 * @throws Exception
	 */
	public List<T> popup(int num) throws Exception;
	
	/**
	 * �����ϲ�ѹ��һ��Ԫ��
	 * @param object Ҫѹ���Ԫ��
	 * @throws Exception
	 */
	public void push(T object) throws Exception;
	
	/**
	 * �Ƴ�������ָ�������ŵ�Ԫ��
	 * @param index 0��ʼ��������
	 * @throws Exception
	 */
	public void remove(int index)throws Exception;
	
	
	public T getOldest() throws Exception;
	
}
