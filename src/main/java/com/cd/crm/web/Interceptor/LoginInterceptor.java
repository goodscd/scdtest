package com.cd.crm.web.Interceptor;

import com.cd.crm.settings.domian.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 拦截器，防止用户恶意登录
 */
public class LoginInterceptor implements HandlerInterceptor {
    /**
     *预处理方法
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器");
        String uri = request.getRequestURI();
        HttpSession session = null;
        session=request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (uri.indexOf("login")!=-1) {
            return true;
        }
        if ((session != null && user.getLoginAct()!=null)) {
            return  true;
        }
        response.sendRedirect(request.getContextPath()+"login.jsp");
        return false;
    }
}
