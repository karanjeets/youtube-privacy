package edu.usc.csci599.util;

import edu.usc.csci599.fetch.Fetcher;
import edu.usc.csci599.model.Query;
import edu.usc.csci599.model.QueryResult;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
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
    public static QueryResult queryAndFetch(Query query, Integer topN) {
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

        HashMap<String,String> urlContent = new HashMap<>();
        WebDriver driver = null;
        boolean badRequest = false;
        try {
            driver = Fetcher.getSeleniumDriverInstance();
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
                    int resultSize = 0;
                    List<WebElement> results = driver.findElements(By.className("yt-uix-tile-link"));
                    resultSize = results.size();
                    System.out.println("Result Size: " + resultSize);
                    if(results.size() > 0) {
                        while (urlContent.size() < topN && resultSize > 0) {
                            for(WebElement element: results) {
                                String url = element.getAttribute("href");

                                //TODO: Filter URLs if required
                                System.out.println(element.getText() + " - " + url);

                                

                                // TODO: Extract content if required
                                urlContent.put(url, "");
                                if(urlContent.size() == topN) {
                                    break;
                                }
                            }
                            if(urlContent.size() < topN) {
                                // Page Scroll
                                List<WebElement> pageLinks = driver.findElements(By.className("vve-check"));
                                pageLinks.get(pageLinks.size() - 1).click();
                                Thread.sleep(3000);
                                // Filter new results
                                results = driver.findElements(By.className("yt-uix-tile-link"));
                                resultSize = results.size();
                                System.out.println("Moved to Next Page. Result Size: " + resultSize);
                            }
                        }
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                if(driver != null) {
                    try {
                        driver.close();
                        driver.quit();
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        System.out.println("Search Completed");

        QueryResult queryResult = new QueryResult(query, urlContent);
        return queryResult;
    }

    /**
     * Queries Commercial Search Engine (DuckDuckGo) and fetch top 10 URLs from the result set
     * @param query
     * @return HashMap of URL - Content
     */
    public static QueryResult queryAndFetch(Query query) {
        return queryAndFetch(query, 10);
    }


    public static void main(String[] args) {
        Query query = new Query("suit suit karda");
        queryAndFetch(query, 30);
        /*for(String url: queryAndFetch(query, 30).getUrlContent().keySet()) {
            System.out.println(url);
        }*/
    }
}
