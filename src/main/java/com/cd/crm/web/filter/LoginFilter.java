package com.cd.crm.web.filter;

import com.cd.crm.settings.domian.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("过滤器");
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (uri.indexOf("login")!=-1) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        HttpServletResponse response=(HttpServletResponse)servletResponse;
        //重定向转发：request.getContextPath()：  /项目名称
        //重定向转发路径必须以/项目名开头
        response.sendRedirect(request.getContextPath()+"/login.jsp");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
