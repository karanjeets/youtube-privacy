package edu.usc.csci599.explore;

import edu.usc.csci599.fetch.Fetcher;

import edu.usc.csci599.model.Query;
import edu.usc.csci599.model.QueryResult;
import edu.usc.csci599.model.Video;
import edu.usc.csci599.util.Constants;
import edu.usc.csci599.util.FileUtil;
import edu.usc.csci599.util.SearchUtil;
import edu.usc.csci599.util.YoutubePlayer;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Set;
import java.util.Collections;
import java.util.List;
/**
 * Created by ksingh on 2/16/17.
 */
public class Explorer {

    // Initialize Variables & Configuration
    private void init() {
        System.setProperty("webdriver.firefox.bin", Constants.FFX_BIN_PATH);
    }

    public static void main(String[] args) throws IOException{

    	// WE will explore everything.
		WebDriver driver = Fetcher.getSeleniumDriverInstance();
    	
    	List<String> list = FileUtil.readLinesInList(Explorer.class.getClassLoader()
				.getResourceAsStream("data/a-search-queries.txt"));

    	Collections.shuffle(list);
    	
    	
		try {
			for (String element : list) {
				Query query = new Query(element);
				QueryResult response = SearchUtil.queryAndFetch(driver, query, 10);
				for (Video video : response.getVideos()) {
					YoutubePlayer.playVideo(driver, video);
				}
			}
		} finally {
			Fetcher.closeSeleniumDriverInstance(driver);
		}

    }
}
