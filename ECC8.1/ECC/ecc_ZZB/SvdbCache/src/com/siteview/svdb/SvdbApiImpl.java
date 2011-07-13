package com.siteview.svdb;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siteview.svdb.dao.DaoFactory;
import com.siteview.svdb.dao.bean.ReportData;

public class SvdbApiImpl implements SvdbApi {
	private static final Log log = LogFactory.getLog(SvdbApiImpl.class);
	//private static Object url = ConfigReader.getConfig("CenterWebServiceUrl");
	//private static SvdbApiImplProxy proxy = null;
	//private static com.siteview.cxf.client.SvdbApiImplService service = null;

	@Override
	public void appendRecord(String id, String text) {
		try {
			String decodeId = URLDecoder.decode(id,"UTF-8");
//			System.out.println("decodeId : "+ decodeId);
			String decodeText = URLDecoder.decode(text,"UTF-8");
//			System.out.println("decodeText : "+ decodeText);
			Object url = ConfigReader.getConfig("CenterWebServiceUrl");
//			System.out.println("url : "+ url);
			if (url == null || "".equals(url)) {
				log.info("CenterAdress 没有设置");
			}else if (url instanceof String){
				if (((String) url).contains("localhost") || ((String) url).contains("127.0.0.1")){
					log.info("CenterAdress 不能指向本机");
				}else{
					try{
						boolean flag =  DaoFactory.getTelebackupDataDao().query(decodeId);
//						System.out.println("flag : "+ flag);
						if(decodeId!=null) decodeId = decodeId.trim();
//						System.out.println("falg.......>"+flag + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+decodeId);
						if(flag){
							//if (proxy==null) proxy = new SvdbApiImplProxy((String)url);
							//SvdbApiImplProxy proxy = new SvdbApiImplProxy((String)url);
							//proxy.appendRecord(id, text);
							//if (service==null)
							com.siteview.cxf.client.SvdbApiImplService service = new com.siteview.cxf.client.SvdbApiImplService(new URL((String)ConfigReader.getConfig("CenterWebServiceUrl")));
							service.getSvdbApiImpl().appendRecord(id, text);
						}
					}catch(Exception e){
						log.error(e);
					}
				}
			}
			String[] textArray = decodeText.split("#");
			ReportData data = new ReportData();
			data.setId(decodeId);
			data.setCreateTime(new Date());
			data.setName("MonitorData");
			for (String textKv : textArray){
				if (textKv.trim().length() == 0 ){
					continue;
				}
				String[] kv = textKv.split("=");
				if (kv.length != 2){
					continue;
				}
				String skey = kv[0].trim();
				String value = kv[1].trim();
				skey = skey.replaceAll("#", "");
				value = value.replaceAll("#", "");
				data.setValue(kv[0].trim(), kv[1].trim());
			}
//			System.out.println("data> decodeId :"+data.getId()+"data> monitorid :"+data.getMonitorid()+"data>name: "+data.getName()+"data > getValueKeys:"+data.getValueKeys()+"data> createtime :"+data.getCreateTime()+"######OVER###");
			if (data.getValueKeys().size()>0)
				DaoFactory.getReportDataDao().insert(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
