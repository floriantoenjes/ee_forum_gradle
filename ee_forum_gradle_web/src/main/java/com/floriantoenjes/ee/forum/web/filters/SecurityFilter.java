package com.floriantoenjes.ee.forum.web.filters;

import com.floriantoenjes.ee.forum.ejb.EntityBean;
import com.floriantoenjes.ee.forum.ejb.MessageBean;
import com.floriantoenjes.ee.forum.ejb.PostBean;
import com.floriantoenjes.ee.forum.ejb.ThreadBean;
import com.floriantoenjes.ee.forum.ejb.model.AuthEntity;
import com.floriantoenjes.ee.forum.ejb.model.User;
import com.floriantoenjes.ee.forum.web.SignInController;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@WebFilter(urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class SecurityFilter implements Filter {

    private final String[] pathsForAdmins = {
            "/board_form"
    };

    private final String[] pathsForGuests = {
            "/signin",
            "/register"
    };

    private final String[] pathsForUsers = {
            "/thread_form",
            "/post_form",
            "/control-center",
            "/message"
    };

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
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        httpServletRequest = (HttpServletRequest) servletRequest;
        httpServletResponse = (HttpServletResponse) servletResponse;

        path = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
        user = signInController.getUser();

        if (!isUserAdmin()) {
            restrictViews(servletRequest, servletResponse);
            filterAuthEntities();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isUserAdmin() {
        return user != null && user.hasRole("ADMIN");
    }

    private void restrictViews(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IOException, ServletException {

        if (isViewRestricted(pathsForAdmins)) {
            sendForbidden(httpServletRequest, httpServletResponse);

        } else if (isViewRestricted(pathsForGuests) && user != null) {
            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);

        } else if (isViewRestricted(pathsForUsers) && user == null) {
            sendUnauthorized(httpServletRequest, httpServletResponse);
        }
    }

    private boolean isViewRestricted(String[] paths) {
        return Arrays.stream(paths).anyMatch(path::startsWith);
    }

    private void filterAuthEntities() throws IOException, ServletException {

        /* Check if user is author of the thread */
        filterAuthEntity(threadBean, "^/board/\\d+/thread/(\\d+)/edit/$");

        /* Check if user is author of the post or post is marked as deleted */
        filterAuthEntity(postBean, "^/board/\\d+/thread/\\d+/posts/(\\d+)/edit/$");

        /* Check if user is receiver of the message */
        filterAuthEntity(messageBean, "^/message/(\\d+)/$");

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
