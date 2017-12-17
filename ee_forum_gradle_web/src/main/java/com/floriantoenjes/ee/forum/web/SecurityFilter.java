package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.EntityBean;
import com.floriantoenjes.ee.forum.ejb.MessageBean;
import com.floriantoenjes.ee.forum.ejb.PostBean;
import com.floriantoenjes.ee.forum.ejb.ThreadBean;
import com.floriantoenjes.ee.forum.ejb.model.*;
import com.floriantoenjes.ee.forum.ejb.model.Thread;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// ToDo: Perhaps declare filter priority in web.xml
@WebFilter(urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class SecurityFilter implements Filter {

    private User user;

    private String path;

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;

    @Inject
    private SignInController signInController;

    @EJB
    private MessageBean messageBean;

    @EJB
    private PostBean postBean;

    @EJB
    private ThreadBean threadBean;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        httpServletRequest = (HttpServletRequest) servletRequest;
        httpServletResponse = (HttpServletResponse) servletResponse;

        path = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
        user = signInController.getUser();

        /* Administrator has full access */
        if (user != null && user.hasRole("ADMIN")) {

            filterChain.doFilter(servletRequest, servletResponse);
            return;

        /* Only an administrator can create boards */
        } else if (path.matches("^\\/board(_form\\.xhtml)*")){

            sendForbidden(httpServletRequest, httpServletResponse);

        /* Restrict sign in and register pages for signed in users */
        } else if ((path.startsWith("/signin") || path.startsWith("/register")) && user != null) {

            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);

        /* Restrict views to signed in users */
        } else if ((
                path.startsWith("/thread_form") ||
                path.startsWith("/post_form") ||
                path.startsWith("/control-center") ||
                path.startsWith("/message")
        ) && user == null) {
            sendUnauthorized(httpServletRequest, httpServletResponse);
        }

        /* Check if user is author of the thread */
        filterAuthEntity(threadBean, "^/board/\\d+/thread/(\\d+)/edit/$");

        /* Check if user is author of the post or post is marked as deleted */
        filterAuthEntity(postBean, "^/board/\\d+/thread/\\d+/posts/(\\d+)/edit/$");

        /* Check if user is receiver of the message */
        filterAuthEntity(messageBean, "^/message/(\\d+)/$");


        filterChain.doFilter(servletRequest, servletResponse);
    }

    private <T extends AuthEntity> void filterAuthEntity(EntityBean<T> entityBean, String regex)
            throws IOException, ServletException {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            Long id = Long.parseLong(matcher.group(1));
            T entity = entityBean.find(id);

            if (user == null || !entity.isUserAuthorized(user)) {
                sendForbidden(httpServletRequest, httpServletResponse);
            }
        }
    }

    private void sendUnauthorized(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        httpServletResponse.setStatus(401);
        httpServletRequest.getRequestDispatcher("/unauthorized.xhtml")
                .forward(httpServletRequest, httpServletResponse);
    }

    private void sendForbidden(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        httpServletResponse.setStatus(403);
        httpServletRequest.getRequestDispatcher("/forbidden.xhtml")
                .forward(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
