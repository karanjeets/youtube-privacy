package edu.usc.csci599.util;

import edu.usc.csci599.fetch.Fetcher;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;

/**
 * Created by ksingh on 2/16/17.
 */
public class YoutubePlayer {

    /**
     * Plays a Youtube video
     * @param url
     * @param start
     */
    public static void openVideo(String url, String start) {
        System.out.println("URL: " + url);

        HashMap<String,String> urlContent = new HashMap<>();
        WebDriver driver = null;
        boolean badRequest = false;
        try {
            driver = Fetcher.getSeleniumDriverInstance();
            driver.get(url + "&t=" + start);
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
                    // TODO: Add random function
                    Thread.sleep(5000);
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

        System.out.println("Video Completed");
    }


    /**
     * Plays a Youtube video
     * @param url
     */
    public static void openVideo(String url) {
        openVideo(url, "0");
    }


    public static void main(String[] args) {
        openVideo("https://www.youtube.com/watch?v=uQ763VvqiEM");
        /*for(String url: queryAndFetch(query, 30).getUrlContent().keySet()) {
            System.out.println(url);
        }*/
    }
}
