package com.siteview.axis2;

import javax.jws.WebService;

@WebService(serviceName = "eccapi", endpointInterface = "com.siteview.axis2.InterfaceEccService")
public class EccService implements InterfaceEccService
{
	private static final com.siteview.eccservice.InterfaceEccService itf = new com.siteview.eccservice.EccService();
	@Override
	public RetMapInVector GetForestData(KeyValue[] inwhat) {
		return ConvertTools.convert(itf.GetForestData(ConvertTools.convert(inwhat)));
	}

	@Override
	public RetMapInMap GetUnivData(KeyValue[] inwhat) {
		return ConvertTools.convert(itf.GetUnivData(ConvertTools.convert(inwhat)));
	}

	@Override
	public RetMapInVector GetUnivData2(KeyValue[] inwhat) {
		return ConvertTools.convert(itf.GetUnivData2(ConvertTools.convert(inwhat)));
	}

	@Override
	public RetMapInMap SubmitUnivData(
			AnyType2AnyTypeMapEntry[] fmap, KeyValue[] inwhat) {
		return ConvertTools.convert(itf.SubmitUnivData(ConvertTools.convert(fmap),ConvertTools.convert(inwhat)));
	}

	@Override
	public RetMapInVector SubmitUnivData2(
			KeyValueArray[] invmap, KeyValue[] inwhat) {
		return ConvertTools.convert(itf.SubmitUnivData2(ConvertTools.convert(invmap),ConvertTools.convert(inwhat)));
	}

	@Override
	public RetMapInVector php_GetForestData(KeyValue[] inwhat) {
		return ConvertTools.convert(itf.php_GetForestData(ConvertTools.convertTo(inwhat)));
	}

	@Override
	public RetMapInVector php_GetUnivData2(KeyValue[] inwhat) {
		return ConvertTools.convert(itf.php_GetUnivData2(ConvertTools.convertTo(inwhat)));
	}

	@Override
	public RetMapInVector php_SubmitUnivData2(
			KeyValueArray[] inlist, KeyValue[] inwhat) {
		return ConvertTools.convert(itf.php_SubmitUnivData2(ConvertTools.convertTo(inlist),ConvertTools.convertTo(inwhat)));
	}

	@Override
	public String test1(String str) {
		return itf.test1(str);
	}

	@Override
	public KeyValue[] test2(AnyType2AnyTypeMapEntry[] fmap) {
		return ConvertTools.convert(itf.test2(ConvertTools.convert(fmap)));
	}

	@Override
	public KeyValue[] test3(KeyValue[] inwhat) {
		return ConvertTools.convertTo(itf.test3(ConvertTools.convertTo(inwhat)));
	}



}
