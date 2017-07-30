package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.PostBean;
import com.floriantoenjes.ee.forum.ejb.model.Post;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class SearchController implements Serializable {

    private String query;
    private List<Post> results;

    private static final int RESULT_LENGTH = 250;

    @EJB
    private PostBean postBean;

    public void search() {
        if (query != null) {
            results = postBean.findByText(query.toLowerCase());
            results.forEach(result -> {
                if (result.getText().length() >= RESULT_LENGTH) {
                    result.setText(result.getText().substring(0, RESULT_LENGTH -1));
                }
            });
        }
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
}
