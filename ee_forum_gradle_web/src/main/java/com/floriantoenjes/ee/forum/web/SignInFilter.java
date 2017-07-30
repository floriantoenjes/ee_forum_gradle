package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.PostBean;
import com.floriantoenjes.ee.forum.ejb.ThreadBean;
import com.floriantoenjes.ee.forum.ejb.model.Post;
import com.floriantoenjes.ee.forum.ejb.model.Thread;
import com.floriantoenjes.ee.forum.ejb.model.User;

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
public class SignInFilter implements Filter {

    @Inject
    private SignInController signInController;

    @EJB
    private PostBean postBean;

    @EJB
    private ThreadBean threadBean;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String path = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());

        Pattern threadPattern = Pattern.compile("^/board/\\d+/thread/(\\d+)/edit$");
        Matcher threadMatcher = threadPattern.matcher(path);

        Pattern postPattern = Pattern.compile("^/board/\\d+/thread/\\d+/posts/(\\d+)$");
        Matcher postMatcher = postPattern.matcher(path);

        User user = signInController.getUser();

        if (user != null && user.hasRole("ADMIN")) {

            filterChain.doFilter(servletRequest, servletResponse);
            return;

        /* Restrict sign in and register pages to signed in user */
        } else if ((path.startsWith("/signin") || path.startsWith("/register")) && user != null) {

            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);

        } else if ((path.startsWith("/thread_form") || path.startsWith("/post_form") || path.startsWith("/control-center")) && user == null) {

            httpServletResponse.setStatus(401);
            servletRequest.getRequestDispatcher("/unauthorized.xhtml").forward(servletRequest, servletResponse);

        /* Check if user is author of the thread */
        } else if (threadMatcher.find()) {

            Long threadId = Long.parseLong(threadMatcher.group(1));
            Thread thread = threadBean.find(threadId);

            if (user == null || !signInController.getUser().equals(thread.getAuthor())) {
                httpServletResponse.setStatus(403);
                servletRequest.getRequestDispatcher("/forbidden.xhtml").forward(servletRequest, servletResponse);
            }

        /* Check if user is author of the post */
        } else if (postMatcher.find()) {

            Long postId = Long.parseLong(postMatcher.group(1));
            Post post = postBean.find(postId);

            if (user == null || !signInController.getUser().equals(post.getAuthor())) {
                httpServletResponse.setStatus(403);
                servletRequest.getRequestDispatcher("/forbidden.xhtml").forward(servletRequest, servletResponse);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
