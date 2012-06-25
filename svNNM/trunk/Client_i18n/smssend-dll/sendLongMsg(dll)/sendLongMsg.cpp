// sendLongMsg.cpp : ���� DLL Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include <atlstr.h>
#include "Gsm.h"

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
    return TRUE;
}

//just long message
extern "C" __declspec(dllexport) 
bool sendLongMessage(char *portName, char *strRecvPhone , char* content)
{
	SM_param pSm;
	pSm.strText		= content;
	pSm.strNumber	= strRecvPhone;
	pSm.ucValidTime	= 0; //��Чʱ��5����

	CGsm gsm;
	gsm.SetPortName(portName);
	gsm.m_comm.SetRate("9600");

	//CString strPortName = gsm.m_comm.GetPortName();
	//if( gsm.m_comm.OpenComm(strPortName.GetBuffer(strPortName.GetLength()), CBR_9600, NOPARITY, 8, ONESTOPBIT) == FALSE )
	//{
	//	printf("���ڳ�ʼ��ʧ��");
	//	return FALSE;
	//}

	int   iSectionCount = gsm.GetMsgSectionCount(content);
	for(int i=1; i<=iSectionCount; i++ )
	{
		BOOL b = gsm.SendLongMsg( &pSm, (BYTE)i, (BYTE)iSectionCount );
		if( b == false )
		{
			printf("����ʧ��");
			return false;
		}
		else
		{
			printf("���ͳɹ�");
		}
	} 
	return true;
}

extern "C" __declspec(dllexport) 
bool TestSendLongMessage()
{
	CString content = "���� ����������������������������������������������������������������������pNetTraffic(portName, phoneNumber,content)����������������������������������������;";
	////m_smsPort.SendMsg("15073150641",content,70);

	SM_param pSm;
	pSm.strText		= content;
	pSm.strNumber	= "13170484066";
	pSm.ucValidTime	= 0; //��Чʱ��5����

	CGsm gsm;
	gsm.SetPortName("COM1");

	const TCHAR *port = _T("COM1");
	if( gsm.m_comm.OpenComm(port, CBR_9600,NOPARITY,8, ONESTOPBIT) == FALSE )
	{
		printf("���ڳ�ʼ��ʧ��");
		return FALSE;
	}

	int   iSectionCount = gsm.GetMsgSectionCount(content);

	for(int i=1; i<=iSectionCount; i++ )
	{
		BOOL b = gsm.SendLongMsg( &pSm, (BYTE)i, (BYTE)iSectionCount );
		if( b == FALSE )
		{
			printf("����ʧ��");
			gsm.m_comm.CloseComm();
			return FALSE;
		}
		else
		{
			printf("���ͳɹ�");
		}
	} 
	gsm.m_comm.CloseComm();
	return TRUE;
}

