package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.PostBean;
import com.floriantoenjes.ee.forum.ejb.model.Post;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Named
@RequestScoped
public class SearchController implements Serializable {

    private String query;
    private List<Post> results;

    private static final int RESULT_LENGTH = 250;
    private static final String ELLIPSIS = "...";
    private static final int PAGE_SIZE = 5;

    private int currentPage;
    private List<Integer> pages = new ArrayList<>();

    @EJB
    private PostBean postBean;

    public void search() {
        if (query != null) {
            results = postBean.findByText(query.toLowerCase(), currentPage, PAGE_SIZE);
            long totalPosts = postBean.getTotalPostsByText(query.toLowerCase());
            Pattern queryPattern = Pattern.compile(String.format("(.{0,%d}%s.{0,%d})",
                    RESULT_LENGTH / 2, query, RESULT_LENGTH / 2), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            for (Post result : results) {
                Matcher queryMatcher = queryPattern.matcher(result.getText());
                if (queryMatcher.find()) {
                    String resultText = queryMatcher.group(0);

                    if (queryMatcher.start() != 0) {
                        resultText = ELLIPSIS + resultText;
                    }

                    if (!queryMatcher.hitEnd()) {
                        resultText = resultText + ELLIPSIS;
                    }
                    result.setText(resultText);
                }
            }
            pages.clear();
            IntStream.range(0, (int) Math.ceil(totalPosts / (double) PAGE_SIZE)).forEach(pages::add);
        }
    }

    public int getResultPage(Long postNumber) {
        return (int) Math.ceil((postNumber) / (double) PAGE_SIZE) - 1;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Post> getResults() {
        return results;
    }

    public void setResults(List<Post> results) {
        this.results = results;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }
}
