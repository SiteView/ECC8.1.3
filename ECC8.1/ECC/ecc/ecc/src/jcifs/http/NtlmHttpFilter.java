package jcifs.http;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jcifs.Config;
import jcifs.UniAddress;
import jcifs.smb.NtlmChallenge;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbSession;
import jcifs.util.Base64;
import jcifs.util.LogStream;

import com.siteview.base.manage.Manager;
import com.siteview.ecc.alert.util.BaseTools;

/**
 * This servlet Filter can be used to negotiate password hashes with
 * MSIE clients using NTLM SSP. This is similar to <tt>Authentication:
 * BASIC</tt> but weakly encrypted and without requiring the user to re-supply
 * authentication credentials.
 * <p>
 * Read <a href="../../../ntlmhttpauth.html">jCIFS NTLM HTTP Authentication and the Network Explorer Servlet</a> for complete details.
 */

public class NtlmHttpFilter implements Filter {

    private static LogStream log = LogStream.getInstance();

    private String defaultDomain;
    private String domainController;
    private boolean loadBalance;
    private boolean enableBasic;
    private boolean insecureBasic;
    private String realm;

    public void init( FilterConfig filterConfig ) throws ServletException {

        /* Set jcifs properties we know we want; soTimeout and cachePolicy to 30min.
         */
        Config.setProperty( "jcifs.smb.client.soTimeout", "1800000" );
        Config.setProperty( "jcifs.netbios.cachePolicy", "1200" );
        /* The Filter can only work with NTLMv1 as it uses a man-in-the-middle
         * techinque that NTLMv2 specifically thwarts. A real NTLM Filter would
         * need to do a NETLOGON RPC that JCIFS will likely never implement
         * because it requires a lot of extra crypto not used by CIFS.
         */
        Config.setProperty( "jcifs.smb.lmCompatibility", "0" );
        Config.setProperty( "jcifs.smb.client.useExtendedSecurity", "false" );

        Enumeration<?> e = filterConfig.getInitParameterNames();
        while( e.hasMoreElements() ) {
        	String name = (String)e.nextElement();
            if( name.startsWith( "jcifs." )) {
                Config.setProperty( name, filterConfig.getInitParameter( name ));
            }
        }
        defaultDomain = Config.getProperty("jcifs.smb.client.domain");
        domainController = Config.getProperty( "jcifs.http.domainController" );
        if( domainController == null ) {
            domainController = defaultDomain;
            loadBalance = Config.getBoolean( "jcifs.http.loadBalance", true );
        }
        enableBasic = Boolean.valueOf(
                Config.getProperty("jcifs.http.enableBasic")).booleanValue();
        insecureBasic = Boolean.valueOf(
                Config.getProperty("jcifs.http.insecureBasic")).booleanValue();
        realm = Config.getProperty("jcifs.http.basicRealm");
        if (realm == null) realm = "jCIFS";

        int level;
        if(( level = Config.getInt( "jcifs.util.loglevel", -1 )) != -1 ) {
            LogStream.setLevel( level );
        }
        if( LogStream.level > 2 ) {
            try {
                Config.store( log, "JCIFS PROPERTIES" );
            } catch( IOException ioe ) {
            }
        }
    }

    public void destroy() {
    }

    /**
     * This method simply calls <tt>negotiate( req, resp, false )</tt>
     * and then <tt>chain.doFilter</tt>. You can override and call
     * negotiate manually to achive a variety of different behavior.
     */
    public void doFilter( ServletRequest request,
                ServletResponse response,
                FilterChain chain ) throws IOException, ServletException {
    	if ("NTLM".equalsIgnoreCase(BaseTools.getConfigString("auth.type")) == false) {
    		chain.doFilter(request, response);
    		return;
    	}
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;

        String path = req.getServletPath();
    	if (path != null && path.startsWith("/eccservices")){
    		chain.doFilter(request, response);
    		return;
    	}
    	
        NtlmPasswordAuthentication ntlm = null;
        if ((ntlm = negotiate( req, resp, false )) == null) {
            return;
        }
        chain.doFilter( new NtlmHttpServletRequest( req, ntlm ), response );
    }

    /**
     * Negotiate password hashes with MSIE clients using NTLM SSP
     * @param req The servlet request
     * @param resp The servlet response
     * @param skipAuthentication If true the negotiation is only done if it is
     * initiated by the client (MSIE post requests after successful NTLM SSP
     * authentication). If false and the user has not been authenticated yet
     * the client will be forced to send an authentication (server sends
     * HttpServletResponse.SC_UNAUTHORIZED).
     * @return True if the negotiation is complete, otherwise false
     */
    protected NtlmPasswordAuthentication negotiate( HttpServletRequest req,
                HttpServletResponse resp,
                boolean skipAuthentication ) throws IOException, ServletException {
        NtlmPasswordAuthentication ntlm = (NtlmPasswordAuthentication)req.getSession().getAttribute( "NtlmHttpAuth");
        if (ntlm != null) return ntlm;

        UniAddress dc;
        String msg;
        msg = req.getHeader( "Authorization" );
        boolean offerBasic = enableBasic && (insecureBasic || req.isSecure());

        if( msg != null && (msg.startsWith( "NTLM " ) ||
                    (offerBasic && msg.startsWith("Basic ")))) {
            if (msg.startsWith("NTLM ")) {
                HttpSession ssn = req.getSession();
                byte[] challenge;

                if( loadBalance ) {
                    NtlmChallenge chal = (NtlmChallenge)ssn.getAttribute( "NtlmHttpChal" );
                    if( chal == null ) {
                        chal = SmbSession.getChallengeForDomain();
                        ssn.setAttribute( "NtlmHttpChal", chal );
                    }
                    dc = chal.dc;
                    challenge = chal.challenge;
                } else {
                    dc = UniAddress.getByName( domainController, true );
                    challenge = SmbSession.getChallenge( dc );
                }

                if(( ntlm = NtlmSsp.authenticate( req, resp, challenge )) == null ) {
                    return null;
                }
                /* negotiation complete, remove the challenge object */
                ssn.removeAttribute( "NtlmHttpChal" );
            } else {
                String auth = new String(Base64.decode(msg.substring(6)),
                        "US-ASCII");
                int index = auth.indexOf(':');
                String user = (index != -1) ? auth.substring(0, index) : auth;
                String password = (index != -1) ? auth.substring(index + 1) :
                        "";
                index = user.indexOf('\\');
                if (index == -1) index = user.indexOf('/');
                String domain = (index != -1) ? user.substring(0, index) :
                        defaultDomain;
                user = (index != -1) ? user.substring(index + 1) : user;
                ntlm = new NtlmPasswordAuthentication(domain, user, password);
                dc = UniAddress.getByName( domainController, true );
            }
            try {

                SmbSession.logon( dc, ntlm );
				req.getSession().setAttribute("usersessionid", Manager.createView_zhongZuBu(ntlm.getUsername()));
                
                if( LogStream.level > 2 ) {
                    log.println( "NtlmHttpFilter: " + ntlm +
                            " successfully authenticated against " + dc );
                }
            } catch( Exception e ) {
                if (e instanceof SmbAuthException){
                	SmbAuthException sae = (SmbAuthException)e;
                    if( LogStream.level > 1 ) {
                        log.println( "NtlmHttpFilter: " + ntlm.getName() +
                                ": 0x" + jcifs.util.Hexdump.toHexString( sae.getNtStatus(), 8 ) +
                                ": " + sae );
                    }
                    if( sae.getNtStatus() == SmbAuthException.NT_STATUS_ACCESS_VIOLATION ) {
                        /* Server challenge no longer valid for
                         * externally supplied password hashes.
                         */
                        HttpSession ssn = req.getSession(false);
                        if (ssn != null) {
                            ssn.removeAttribute( "NtlmHttpAuth" );
                        }
                    }
                }else{
                    if( LogStream.level > 1 ) {
                        log.println( "NtlmHttpFilter: " + ntlm.getName() + ": " + e );
                    }
                }
                this.unSuccessAuth(req, resp, offerBasic);
                return null;
            }
            req.getSession().setAttribute( "NtlmHttpAuth", ntlm );
        } else {
            if (!skipAuthentication) {
                HttpSession ssn = req.getSession(false);
                if (ssn == null || (ntlm = (NtlmPasswordAuthentication)
                            ssn.getAttribute("NtlmHttpAuth")) == null) {
                    this.unSuccessAuth(req, resp, offerBasic);
                    return null;
                }
            }
        }

        return ntlm;
    }
    
    private void unSuccessAuth(HttpServletRequest req, HttpServletResponse resp,boolean offerBasic) throws IOException
    {
        resp.setHeader( "WWW-Authenticate", "NTLM" );
        if (offerBasic) {
            resp.addHeader( "WWW-Authenticate", "Basic realm=\"" +
                    realm + "\"");
        }
        resp.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
        resp.setContentLength(0);
        resp.flushBuffer();
    }

    // Added by cgross to work with weblogic 6.1.
    public void setFilterConfig( FilterConfig f ) {
        try {
            init( f );
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
    public FilterConfig getFilterConfig() {
        return null;
    }
}
