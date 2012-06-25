////////////////////////////////////////////////////////////////////////
//
// odsErrorCode.h
//
//		ODS �������ʹ�����Ϣ��
//
// by Wang Yong Gang, 1999-10-15
//
////////////////////////////////////////////////////////////////////////

#if !defined(_ODS_20_ERROR_CODE_H_)
#define _ODS_20_ERROR_CODE_H_


// ���������
#define ODS_ERR_BASE			6000

// ���������д�������Լ�������Ϣ�Ķ���
#define ODS_ERR_OK				0
#define ODS_MSG_OK				_T("�ɹ�")	

#define ODS_ERR_NET				( ODS_ERR_BASE + 1 )
#define ODS_MSG_NET				_T("����ͨѶ����")

#define ODS_ERR_DUPBATCH		( ODS_ERR_BASE + 2 )
#define ODS_MSG_DUPBATCH		_T("����Ϣ�ظ�")

#define ODS_ERR_NOSRV			( ODS_ERR_BASE + 3 )
#define ODS_MSG_NOSRV			_T("��ָ���ļ�������޷��ҵ��ĵ�����")

#define ODS_ERR_BADPARAM		( ODS_ERR_BASE + 4 )
#define ODS_MSG_BADPARAM		_T("�����Ƿ�")

#define ODS_ERR_CREATEDIR		( ODS_ERR_BASE + 6 )
#define ODS_MSG_CREATEDIR		_T("�޷�����Ŀ¼")

#define ODS_ERR_OVERWRITE		( ODS_ERR_BASE + 7 )
#define ODS_MSG_OVERWRITE		_T("Ŀ¼���ļ��Ѿ����ڣ��޷�����")

#define ODS_ERR_EMPTYDIR		( ODS_ERR_BASE + 8 )
#define ODS_MSG_EMPTYDIR		_T("Ŀ¼Ϊ��")

#define ODS_ERR_NAMEOVERFLOW	( ODS_ERR_BASE + 9 )
#define ODS_MSG_NAMEOVERFLOW	_T("Ŀ¼�����ļ���̫���������޷����")

#define ODS_ERR_COPYDIR			( ODS_ERR_BASE + 10 )
#define ODS_MSG_COPYDIR			_T("����Ŀ¼ʱ�ļ�ϵͳ��������")

#define ODS_ERR_REMOVEDIR		( ODS_ERR_BASE + 11 )
#define ODS_MSG_REMOVEDIR		_T("ɾ��Ŀ¼ʱ�ļ�ϵͳ��������")

#define ODS_ERR_MOVEDIR			( ODS_ERR_BASE + 12 )
#define ODS_MSG_MOVEDIR			_T("�ƶ�Ŀ¼ʱ�ļ�ϵͳ��������")

#define ODS_ERR_NODIR			( ODS_ERR_BASE + 13 )
#define ODS_MSG_NODIR			_T("ָ����Ŀ¼������")

#define ODS_ERR_NOFILE			( ODS_ERR_BASE + 14 )
#define ODS_MSG_NOFILE			_T("�ļ�������")

#define ODS_ERR_BROWSEDIR		( ODS_ERR_BASE + 15 )
#define ODS_MSG_BROWSEDIR		_T("���Ŀ¼ʱ�ļ�ϵͳ��������")

#define ODS_ERR_READFILE		( ODS_ERR_BASE + 16 )
#define ODS_MSG_READFILE		_T("���ļ�ʱ����")

#define ODS_ERR_WRITEFILE		( ODS_ERR_BASE + 17 )
#define ODS_MSG_WRITEFILE		_T("д�ļ�ʱ����")

#define ODS_ERR_OPENFILE		( ODS_ERR_BASE + 18 )
#define ODS_MSG_OPENFILE		_T("���ļ�ʱ����")

#define ODS_ERR_POSFILE			( ODS_ERR_BASE + 19 )
#define ODS_MSG_POSFILE			_T("�ƶ��ļ�ָ��ʧ��")

#define ODS_ERR_MALLOC			( ODS_ERR_BASE + 20 )
#define ODS_MSG_MALLOC			_T("�ڴ治�㣬�����ڴ����")

#define ODS_ERR_RECURSUBDIR		( ODS_ERR_BASE + 21 )
#define ODS_MSG_RECURSUBDIR		_T("Ҫ�鲢��Ŀ¼������Ŀ¼���޷���ɹ鲢")

#define ODS_ERR_LARGE			( ODS_ERR_BASE + 22 )
#define ODS_MSG_LARGE			_T("�ļ�̫�󣬲����޷����")

#define ODS_ERR_UNKNOWNPACK		( ODS_ERR_BASE + 23 )
#define ODS_MSG_UNKNOWNPACK		_T("��Ĺ鲢��ʽ�޷�ʶ��")

#define ODS_ERR_SETATTR			( ODS_ERR_BASE + 24 )
#define ODS_MSG_SETATTR			_T("�޷������ļ�����")

#define ODS_ERR_OPENSCM			( ODS_ERR_BASE + 25 )
#define ODS_MSG_OPENSCM			_T("�޷���ָ��������ϵķ��������")

#define ODS_ERR_OPENSERVICE		( ODS_ERR_BASE + 26 )
#define ODS_MSG_OPENSERVICE		_T("�޷���ָ���ķ���")

#define ODS_ERR_DELSERVICE		( ODS_ERR_BASE + 27 )
#define ODS_MSG_DELSERVICE		_T("�޷�ɾ��ָ���ķ���")

#define ODS_ERR_STOPSERVICE		( ODS_ERR_BASE + 28 )
#define ODS_MSG_STOPSERVICE		_T("�޷�ָֹͣ���ķ���")

#define ODS_ERR_REPORTSTATUS	( ODS_ERR_BASE + 29 )
#define ODS_MSG_REPORTSTATUS	_T("�޷������������������״̬")

#define ODS_ERR_INSTSERVICE		( ODS_ERR_BASE + 30 )
#define ODS_MSG_INSTSERVICE		_T("�޷���װָ���ķ���")

#define ODS_ERR_STARTSERVICE	( ODS_ERR_BASE + 31 )
#define ODS_MSG_STARTSERVICE	_T("�޷�����ָ���ķ���")

#define ODS_ERR_QUERYSERVICE	( ODS_ERR_BASE + 32 )
#define ODS_MSG_QUERYSERVICE	_T("�޷�ȡ��ָ�������״̬��Ϣ")

#define ODS_ERR_INITMFC			( ODS_ERR_BASE + 33 )
#define ODS_MSG_INITMFC			_T("�����޷�����:������ϵͳ�а�װ�� MFC ��İ汾̫��")

#define ODS_ERR_STARTSVCDISP	( ODS_ERR_BASE + 34 )
#define ODS_MSG_STARTSVCDISP	_T("�޷�������������������")

#define ODS_ERR_REGSVCHCTRL		( ODS_ERR_BASE + 35 )
#define ODS_MSG_REGSVCHCTRL		_T("�޷�ע��������Ŀ��ƺ���")

#define ODS_ERR_RPCUSEPROTOCOL	( ODS_ERR_BASE + 36 )
#define ODS_MSG_RPCUSEPROTOCOL	_T("�޷����� RPC ʹ�õ�Э��")

#define ODS_ERR_RPCREGIF		( ODS_ERR_BASE + 37 )
#define ODS_MSG_RPCREGIF		_T("�޷�ע�� RPC �ӿ�")

#define ODS_ERR_RPCLISTEN		( ODS_ERR_BASE + 38 )
#define ODS_MSG_RPCLISTEN		_T("�޷���ʼ RPC ����")

#define ODS_ERR_REGISTRY		( ODS_ERR_BASE + 39 )
#define ODS_MSG_REGISTRY		_T("����ע���ʧ��")

#define ODS_ERR_CREATEMUTEX		( ODS_ERR_BASE + 40 )
#define ODS_MSG_CREATEMUTEX		_T("�޷�����������")

#define ODS_ERR_MAXCONNECT		( ODS_ERR_BASE + 41 )
#define ODS_MSG_MAXCONNECT		_T("�ܿͻ������������ݿ����������ƣ��޷���Ӧ�ͻ�����������")

#define ODS_ERR_MAXDOCOPENED	( ODS_ERR_BASE + 42 )
#define ODS_MSG_MAXDOCOPENED	_T("��ͬһ�ͻ�ͬʱ�򿪵��ĵ������ƣ��޷��򿪸�����ĵ�")

#define ODS_ERR_LOSTCLIENT		( ODS_ERR_BASE + 43 )
#define ODS_MSG_LOSTCLIENT		_T("�ͻ������쳣�жϣ������ǿͻ����������쳣")

#define ODS_ERR_RPCBINDING		( ODS_ERR_BASE + 44 )
#define ODS_MSG_RPCBINDING		_T("�޷������ĵ�����:�޷���� RPC ��")

#define ODS_ERR_PING			( ODS_ERR_BASE + 45 )
#define ODS_MSG_PING			_T("�޷������ĵ�����:�������û������")

#define ODS_ERR_GETCOMPUTER		( ODS_ERR_BASE + 46 )
#define ODS_MSG_GETCOMPUTER		_T("�޷��õ��������")

#define ODS_ERR_DBCONNECT		( ODS_ERR_BASE + 47 )
#define ODS_MSG_DBCONNECT		_T("�޷��������ݿ�")

#define ODS_ERR_DATABASE		( ODS_ERR_BASE + 48 )
#define ODS_MSG_DATABASE		_T("���ݿ����ʧ��")

#define ODS_ERR_FILETRANS		( ODS_ERR_BASE + 49 )
#define ODS_MSG_FILETRANS		_T("�ļ�����ʱ���󣬷��ͺͽ��ܵ��ֽ�����ƥ��")

#define ODS_ERR_NOSPACE			( ODS_ERR_BASE + 50 )
#define ODS_MSG_NOSPACE			_T("�豸����û�п��ÿռ䣬�޷����ջ��ƶ��ĵ�")

#define ODS_ERR_NOCDGROUP		( ODS_ERR_BASE + 51 )
#define ODS_MSG_NOCDGROUP		_T("ָ���Ĺ����鲻����")

#define ODS_ERR_NOVOLUME		( ODS_ERR_BASE + 52 )
#define ODS_MSG_NOVOLUME		_T("ָ���ľ�����")

#define ODS_ERR_NODOCUMENT		( ODS_ERR_BASE + 53 )
#define ODS_MSG_NODOCUMENT		_T("ָ�����ĵ������ڣ���������������ʹ��")

#define ODS_ERR_NODEVICE		( ODS_ERR_BASE + 54 )
#define ODS_MSG_NODEVICE		_T("ָ�����豸������")

#define ODS_ERR_LOCATION		( ODS_ERR_BASE + 55 )
#define ODS_MSG_LOCATION		_T("��λ��Ϣ��ʽ����ȷ")

#define ODS_ERR_DIFFPACK		( ODS_ERR_BASE + 56 )
#define ODS_MSG_DIFFPACK		_T("Ҫ��Ĺ鲢��ʽ�;�ǰ�Ĺ鲢��ʽ����")

#define ODS_ERR_UNZIP			( ODS_ERR_BASE + 57 )
#define ODS_MSG_UNZIP			_T("�޷��� ZIP �ļ��н�ѹ����ָ�����ļ�")

#define ODS_ERR_TEMPFILE		( ODS_ERR_BASE + 58 )
#define ODS_MSG_TEMPFILE		_T("����ϵͳ�޷�������ʱ�ļ�")

#define ODS_ERR_DEVREADONLY		( ODS_ERR_BASE + 59 )
#define ODS_MSG_DEVREADONLY		_T("�ĵ�����ֻ������(�����)�ϣ��޷��޸�")

#define ODS_ERR_DOCREADONLY		( ODS_ERR_BASE + 60 )
#define ODS_MSG_DOCREADONLY		_T("�ĵ���ֻ����ʽ�򿪣��޷��޸�")

#define ODS_ERR_SINGLEBACK		( ODS_ERR_BASE + 61 )
#define ODS_MSG_SINGLEBACK		_T("�ĵ��в��ܴ��ڹ����ĸ���ҳ��")

#define ODS_ERR_FILENAMEDUP		( ODS_ERR_BASE + 62 )
#define ODS_MSG_FILENAMEDUP		_T("�ĵ��ڲ����������ļ�")

#define ODS_ERR_VOLMAKECD		( ODS_ERR_BASE + 63 )
#define ODS_MSG_VOLMAKECD		_T("�ĵ����ڵľ����ڿ��̣��ܾ�����")

#define ODS_ERR_DEVNOTREADONLY	( ODS_ERR_BASE + 64 )
#define ODS_MSG_DEVNOTREADONLY	_T("�ĵ����Ǵ���ֻ������(�����)�ϣ��޷�����������")

#define ODS_ERR_NEEDNAMEONCOPY	( ODS_ERR_BASE + 65 )
#define ODS_MSG_NEEDNAMEONCOPY	_T("��ͬһ�������и����ĵ�ʱ����������µ��ĵ�����")


#endif //_ODS_20_ERROR_CODE_H_