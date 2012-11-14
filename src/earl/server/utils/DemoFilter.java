package earl.server.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DemoFilter implements Filter {

	@Override
	public void destroy() {
	}

	public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
		String requestURI = req.getRequestURI();
		System.out.println(requestURI);
		if("/demo/index.jsp".equals(requestURI) || "/demo/".equals(requestURI)) {
			chain.doFilter(req, resp);
		}else{
			String redir = requestURI.replace("/demo/", "/bastogne/");
			req.getRequestDispatcher(redir).forward(req, resp);
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		doFilter((HttpServletRequest) req, (HttpServletResponse) resp, chain);
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {

	}

}
