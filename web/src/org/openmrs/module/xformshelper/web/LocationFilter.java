package org.openmrs.module.xformshelper.web;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet filter to return the location of syncing server
 * 
 * @author Samuel Mbugua
 *
 */
public final class LocationFilter implements Filter {

    @SuppressWarnings("unused")
	private FilterConfig filterConfig = null;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse hsr = null;
		hsr = (HttpServletResponse)response;
		hsr.setHeader("Location", "/moduleServlet/xformshelper/fileUpload");
		chain.doFilter(request, hsr);
    }
    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
    }

}


