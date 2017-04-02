package edu.usc.csci599.util;


import edu.usc.csci599.fetch.Fetcher;
import edu.usc.csci599.model.Query;
import edu.usc.csci599.model.QueryResult;
import edu.usc.csci599.model.Video;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksingh on 2/16/17.
 */
public class SearchUtil {

    private static Integer COUNTER = 0;

    /**
     * Queries Commercial Search Engine (DuckDuckGo) and fetch topN URLs from the result set
     * @param query
     * @param topN
     * @return HashMap of URL - Content
     */
    public static QueryResult queryAndFetch(WebDriver driver, Query query, Integer topN) {
        System.out.println("Query: " + query.getValue() + "; Region: " + query.getRegion());
        COUNTER++;
        if(COUNTER % 50 == 0) {
            System.out.println(COUNTER + " Queries Executed. Supposed to wait for 10 minutes but let's skip it...");
            /*
            try {
                System.out.println(COUNTER + " Queries Executed. Waiting for 10 minutes...");
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
        }

        ArrayList<Video> videoList = new ArrayList<Video>();

        //WebDriver driver = null;
        boolean badRequest = false;
        try {
            //driver = Fetcher.getSeleniumDriverInstance();
            driver.get("https://www.youtube.com/results?search_query=" + query.getValue());
        }
        catch(Exception e) {
            if(e instanceof TimeoutException) {
                System.out.println("Timeout Exception Raised. Processing whatever loaded so far...");
            }
            else {
                e.printStackTrace();
                badRequest = true;
            }
        }
        finally {
            try {
                if(!badRequest) {
                    int feedSize = 0;
                    
                    List<WebElement> feedList = driver.findElements(By.className("yt-lockup-dismissable"));

                    feedSize = feedList.size();
                    System.out.println("Result Size: " + feedSize);
                    if(feedList.size() > 0) {
                        while (videoList.size() < topN && feedSize > 0) {
                            for(WebElement element: feedList) {
                            
                            	String url= "";
                            	String title = "";
                            	String duration ="";
                            	try {
                                   url = element.findElement(By.className("yt-uix-tile-link")).getAttribute("href");
                                   title = element.findElement(By.className("yt-uix-tile-link")).getAttribute("title");
                                   
                                   if(!url.contains("channel") && !url.contains("user"))
                                   {
                                	   duration = element.findElement(By.className("video-time")).getText();
                                	   int durationVal = YoutubePlayer.convertTime(duration);
                                	   Video vid = new Video(durationVal,title,url);
                                	   videoList.add(vid);
                                   }
                            	}
                            	catch (Exception e)
                            	{
                            		continue;
                            	}
                                //TODO: Filter URLs if required
                                // TODO: Extract content if required
                                if(videoList.size() == topN) {
                                    break;
                                }
                            }
                            if(videoList.size() < topN) {
                                // Page Scroll
                                List<WebElement> pageLinks = driver.findElements(By.className("vve-check"));
                                pageLinks.get(pageLinks.size() - 1).click();
                                Thread.sleep(3000);
                                // Filter new results
                                feedList = driver.findElements(By.className("yt-lockup-dismissable"));
                                feedSize = feedList.size();
                                System.out.println("Moved to Next Page. Result Size: " + feedSize);
                            }
                        }
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            /*finally {
                if(driver != null) {
                    try {
                        driver.close();
                        driver.quit();
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }*/
        }

        System.out.println("Search Completed");

        QueryResult queryResult = new QueryResult(query, videoList);
        return queryResult;
    }

    /**
     * Queries Commercial Search Engine (DuckDuckGo) and fetch top 10 URLs from the result set
     * @param query
     * @return HashMap of URL - Content
     */
    public static QueryResult queryAndFetch(WebDriver driver, Query query) {
        return queryAndFetch(driver, query, 10);
    }


    public static void main(String[] args) {
        WebDriver driver = Fetcher.getSeleniumDriverInstance();
        Query query = new Query("bb ki vines");
        //queryAndFetch(query, 30);
        try {
            for (Video video : queryAndFetch(driver, query, 30).getVideos()) {
                System.out.println(video.getUrl());
            }
        } finally {
            Fetcher.closeSeleniumDriverInstance(driver);
        }
    }
}
