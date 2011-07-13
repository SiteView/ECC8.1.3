package com.siteview.ecc.util;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbSession;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.siteview.base.data.UserEdit;
import com.siteview.ecc.email.IniFilePack;

public class LdapAuth
{
	private final static Logger logger = Logger.getLogger(LdapAuth.class);
	public static void main(String[] args) throws Exception
	{
	}
	
	/**
	 * ldap 认证
	 * @param providerUrl 类似于：ldap://localhost:389/
	 * @param securityPrincipal 类似于：cn=Manager,o=tcl,c=cn
	 * @param password 是对应前一个参数的密码
	 * @return 当返回 true 时，ldap 认证成功
	 */
	public static boolean tryAuth(String providerUrl, String securityPrincipal, String password) throws Exception
	{
		if(providerUrl==null || securityPrincipal==null || password==null)
			throw new Exception("输入参数有一个为  null !  ");
			
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, providerUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS, password);
		
		DirContext ctx = null;
		try
		{
			ctx = new InitialDirContext(env);
		} catch (javax.naming.AuthenticationException e)
		{
			e.printStackTrace();
			throw new Exception(" LDAP认证失败(通常是密码错误等原因)! ");
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception(" LDAP认证出错(通常是ldap服务器不可达等原因)! ");
		}finally{
			if (ctx != null)
			{
				try
				{
					ctx.close();
				} catch (NamingException e){}
			}
		}
		return true;
	}
	public static void loginNTLM(String host,String domain,String username,String passwd)throws Exception{
		UniAddress mydomaincontroller = UniAddress.getByName( host);
		NtlmPasswordAuthentication mycreds = new NtlmPasswordAuthentication( domain, username, passwd );
		SmbSession.logon( mydomaincontroller, mycreds );
	}
	
	public static boolean isLoginNTLM(HttpServletRequest request,HttpServletResponse response,String username) throws Exception{
	 	String auth = request.getHeader("Authorization");
//	 	if (setNTLM(response,auth)) return false;
		String loginname = getUserNamebyAuthorization(auth);
		if (loginname==null) return false;
		if (loginname.equals(username)) return true;
		return false;
	}
	public static String getUserNamebyAuthorization(String auth) throws Exception{
		if (auth == null) return null; 
		if (auth.startsWith("NTLM ") == false) return null; 
        byte[] msg = new BASE64Decoder().decodeBuffer(auth.substring(5));
        if (msg[8] != 3) return null; 
        int off = 30;
        int length,offset;
        length = msg[off+17]*256 + msg[off+16];   
        offset = msg[off+19]*256 + msg[off+18];   
        String remoteHost = new String(msg, offset, length,"UTF-16LE");   
      
        length = msg[off+1]*256 + msg[off];   
        offset = msg[off+3]*256 + msg[off+2];   
        String domain = new String(msg, offset, length,"UTF-16LE");   
      
        length = msg[off+9]*256 + msg[off+8];   
        offset = msg[off+11]*256 + msg[off+10];   
        String username =new String(msg, offset, length,"UTF-16LE");   
        return username;
	}
	private static boolean setNTLM(HttpServletResponse response,String auth)throws Exception{
		if (auth == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setHeader("WWW-Authenticate", "NTLM");
			return true; 
		}
		if (auth.startsWith("NTLM ") == false) return false; 
        byte[] msg = new BASE64Decoder().decodeBuffer(auth.substring(5));
        if (msg[8] != 1) return false; 
        byte z = 0;
        byte[] msg1 =
            {(byte)'N', (byte)'T', (byte)'L', (byte)'M', (byte)'S',
            (byte)'S', (byte)'P', z,
            (byte)2, z, z, z, z, z, z, z,
            (byte)40, z, z, z, (byte)1, (byte)130, z, z,
            z, (byte)2, (byte)2, (byte)2, z, z, z, z, // 
            z, z, z, z, z, z, z, z};
        // 
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "NTLM " 
           + new sun.misc.BASE64Encoder().encodeBuffer(msg1).trim());
        return true; 
	}
	public static void addLdapUser(String providerUrl, String securityPrincipal, String password, IniFilePack ini, String section) throws Exception
	{
		if( ini==null || section==null )
			throw new Exception(" 输入参数 IniFilePack ini 为  null , 或  String section 为 null !  ");
		if( !tryAuth(providerUrl,securityPrincipal,password) )
			throw new Exception(" LDAP认证返回 false! ");
		ini.setKeyValue(section, "LDAPProviderUrl", providerUrl);
		ini.setKeyValue(section, "LDAPSecurityPrincipal", securityPrincipal);
		//ini.setKeyValue(section, "Password", "1R1LRJJNGReIGQJGJNJNKIe7cJ4IK1I4zNRIQ1IzIcGQRzOL");//why
	}

	public static void addLdapUser(String providerUrl, String securityPrincipal, String password, UserEdit userEdit) throws Exception
	{
		if( userEdit==null )
			throw new Exception(" 输入参数 userEdit 为  null !  ");
		if( !tryAuth(providerUrl,securityPrincipal,password) )
			throw new Exception(" LDAP认证返回 false! ");
		userEdit.setLDAPProviderUrl(providerUrl);
		userEdit.setLDAPSecurityPrincipal(securityPrincipal);
	}
	
	//add by kai.zhang 
	/**
	 * 增加用户时 不进行密码验证
	 */
	public static void addLdapUser_onlyAdd(String providerUrl, String securityPrincipal, IniFilePack ini, String section) throws Exception
	{
		if( ini==null || section==null )
			throw new Exception(" 输入参数 IniFilePack ini 为  null , 或  String section 为 null !  ");
		if( !tryAuth_onlyAdd(providerUrl,securityPrincipal) )
			throw new Exception(" LDAP认证返回 false! ");
		ini.setKeyValue(section, "LDAPProviderUrl", providerUrl);
		ini.setKeyValue(section, "LDAPSecurityPrincipal", securityPrincipal);
		//ini.setKeyValue(section, "Password", "1R1LRJJNGReIGQJGJNJNKIe7cJ4IK1I4zNRIQ1IzIcGQRzOL");//why
	}
	
	//add by kai.zhang 
	/**
	 * 增加用户时 不进行密码验证
	 * ldap 认证
	 * @param providerUrl 类似于：ldap://localhost:389/
	 * @param securityPrincipal 类似于：cn=Manager,o=tcl,c=cn
	 * @return 当返回 true 时，ldap 认证成功
	 */
	public static boolean tryAuth_onlyAdd(String providerUrl, String securityPrincipal ) throws Exception
	{
		if(providerUrl==null || securityPrincipal==null )
			throw new Exception("输入参数有一个为  null !  ");
			
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, providerUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
//		env.put(Context.SECURITY_CREDENTIALS, password);
		
		DirContext ctx = null;
		Exception error_exception= null;
		try
		{
			ctx = new InitialDirContext(env);
		} catch (javax.naming.AuthenticationException e)
		{
			error_exception= new Exception(" LDAP认证失败(通常是密码错误等原因)! ");
			e.printStackTrace();
		} catch (Exception e)
		{
			error_exception= new Exception(" LDAP认证出错(通常是ldap服务器不可达等原因)! ");
			e.printStackTrace();
		}
		if (ctx != null)
		{
			try
			{
				ctx.close();
			} catch (NamingException e)
			{
			}
		}
		if(error_exception!=null)
			throw error_exception ;
		return true;
	}
	
}



