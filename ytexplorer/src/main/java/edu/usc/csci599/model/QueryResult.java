package edu.usc.csci599.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ksingh on 2/19/17.
 */
public class QueryResult {

    private Query query;
    private List<Video> videos;
    private Double score;
    private HashMap<String, Double> urlScore;
    private HashMap<String, String> urlContent;

    public QueryResult(Query query, HashMap<String, String> urlContent) {
        this.query = query;
        this.urlContent = urlContent;
    }

    public QueryResult(Query query, List<Video> videos) {
        this.query = query;
        this.videos = videos;
    }

    public Query getQuery() { return query; }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public HashMap<String, Double> getUrlScore() {
        return urlScore;
    }

    public void setUrlScore(HashMap<String, Double> urlScore) {
        this.urlScore = urlScore;
    }

    public HashMap<String, String> getUrlContent() {
        return urlContent;
    }

    public void setUrlContent(HashMap<String, String> urlContent) {
        this.urlContent = urlContent;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
