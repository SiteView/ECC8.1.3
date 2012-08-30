package com.siteview.cxf;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

@WebService(name = "eccapi", targetNamespace = "http://com.siteview.cxf")
public class EccServiceImpl implements EccService {
	private static final com.siteview.eccservice.InterfaceEccService itf = new com.siteview.eccservice.EccService();
	
	@Override
	public RetMapInVector GetForestData(Map<String, String> inwhat) {
		return ConvertTools.convert(itf.GetForestData(inwhat));
	}

	@Override
	public RetMapInMap GetUnivData(Map<String, String> inwhat) {
		return ConvertTools.convert(itf.GetUnivData(inwhat));
	}

	@Override
	public RetMapInVector GetUnivData2(Map<String, String> inwhat) {
		return ConvertTools.convert(itf.GetUnivData2(inwhat));
	}

	@Override
	public RetMapInMap SubmitUnivData(Map<String, Map<String, String>> fmap,
			Map<String, String> inwhat) {
		return ConvertTools.convert(itf.SubmitUnivData(fmap,inwhat));
	}

	@Override
	public RetMapInVector SubmitUnivData2(List<Map<String, String>> invmap,
			Map<String, String> inwhat) {
		return ConvertTools.convert(itf.SubmitUnivData2(ConvertTools.convert(invmap),inwhat));
	}

	@Override
	public RetMapInVector php_GetForestData(Map<String, String> inwhat) {
		return ConvertTools.convert(itf.php_GetForestData(ConvertTools.convert(inwhat)));
	}

	@Override
	public RetMapInVector php_GetUnivData2(Map<String, String> inwhat) {
		return ConvertTools.convert(itf.php_GetUnivData2(ConvertTools.convert(inwhat)));
	}

	@Override
	public RetMapInVector php_SubmitUnivData2(List<Map<String, String>> inlist,
			Map<String, String> inwhat) {
		return ConvertTools.convert(itf.php_SubmitUnivData2(ConvertTools.convertTo(ConvertTools.convert(inlist)),ConvertTools.convert(inwhat)));
	}

	@Override
	public String test1(String str) {
		return itf.test1(str);
	}

	@Override
	public Map<String, String> test2(Map<String, Map<String, String>> fmap) {
		return itf.test2(fmap);
	}

	@Override
	public Map<String, String> test3(Map<String, String> inwhat) {
		return ConvertTools.convert(itf.test3(ConvertTools.convert(inwhat)));
	}

}
