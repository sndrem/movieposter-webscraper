package no.uib.smo015.info310.host2016.scraper;

import no.uib.smo015.info310.host2016.Poster.Poster;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sindre on 12.10.2016.
 */
public class PosterScraper {
    private List<Poster> posters;
    private String baseUrl;

    public PosterScraper(String baseUrl, List<Poster> posters) {
        this.baseUrl = baseUrl;
        this.posters = posters;
    }

    public Document getDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Method for extracting all the movies from IMP Awards
     * Keep in mind that this method scrapes the site for every poster between 2016 and 1912, so it takes
     * some time.
     *
     * @param startUrl the start url - This should be http://www.impawards.com/gallery.html
     * @return a list of posters
     */
    public List<Poster> parseSite(String startUrl, boolean checkExistingUrls) {
        for (Year year : getYearUrls(startUrl)) {
            System.out.println("Henter data fra " + year.getYear());
            for (String url : getPaginationUrls(year)) {
                System.out.println("Pagination for year: " + year.getYear() + " - " + url);
                Document doc = getDocument(url);
                try {
                    this.posters.addAll(getPostersFromPage(year, doc, checkExistingUrls));
                } catch (IllegalStateException e) {
                    continue;
                }
            }
        }
        return this.posters;
    }

    public List<Poster> parseSite(String startUrl, int yearToDownload, boolean checkExistingUrls) {
        Year year = getYearUrls(startUrl).stream()
                .filter(x -> x.getYear() == yearToDownload)
                .findFirst()
                .orElse(null);
        if (year != null) {
            System.out.println("Henter data fra " + year.getYear());
            for (String url : getPaginationUrls(year)) {
                System.out.println("Pagination for year: " + year.getYear() + " - " + url);
                Document doc = getDocument(url);
                try {
                    this.posters.addAll(getPostersFromPage(year, doc, checkExistingUrls));
                } catch (IllegalStateException e) {
                    continue;
                }
            }
        }
        return this.posters;
    }

    /**
     * This method fetches a list of urls mapped to each year in the page for IMP Awards.
     *
     * @param url the url we want to extract data from
     * @return a list of Year-objects. The year object holds the year and the url for that year
     */
    public List<Year> getYearUrls(String url) {
        List<Year> yearUrls = new ArrayList<>();
        Document doc = getDocument(url);
        Elements urls = doc.select("table a");
        yearUrls = urls.stream()
                .map(tag -> new Year(Integer.parseInt(tag.text()), this.baseUrl + tag.attr("href")))
                .collect(Collectors.toList());
        return yearUrls;
    }

    /**
     * Method for fetching a list of pagination urls from the site
     *
     * @param year the url we want to fetch pagination for
     * @return a list of strings representing every pagination url
     */
    public List<String> getPaginationUrls(Year year) {
        List<String> paginationUrls = new ArrayList<>();
        Document doc = getDocument(year.getUrl());
        if (doc != null) {
            Element firstNavigation = doc.select("ul.pagination:first-child").get(0);
            Elements paginationLinks = firstNavigation.select("li a");
            paginationUrls = paginationLinks.stream()
                    .map(link -> this.baseUrl + "/" + year.getYear() + "/" + link.attr("href"))
                    .collect(Collectors.toList());

        } else {
            return null;
        }
        return paginationUrls;
    }

    /**
     * Method to parse a element containing data about a certain poster
     *
     * @param posterElement the element you want to extract data from
     * @return A poster object with data inserted
     */
    public Poster parsePosterData(Year year, Element posterElement, boolean checkExistingUrl) {
        StringBuilder largeUrl = new StringBuilder();
        String title, posterUrl, largePosterUrl, caption, impMovieUrl;

        title = posterElement.select("center a img").attr("title");
        impMovieUrl = this.baseUrl + year.getYear() + "/" + posterElement.select("center a").attr("href");
        Map<String, String> movieData = getMovieData(impMovieUrl);
        posterUrl = this.baseUrl + year.getYear() + "/" + posterElement.select("center a img").attr("src").replace("med_", "");
        largePosterUrl = posterUrl.replace("med_", "");
        largeUrl.append(largePosterUrl);
        largeUrl.insert(largePosterUrl.length() - 4, "_xlg");
        caption = posterElement.select("div.caption").text();
        Poster poster = null;
        if (checkExistingUrl) {

            try {
                if (urlExists(largeUrl.toString())) {
                    poster = new Poster(posterUrl, largeUrl.toString(), title, caption, year.getYear());
                } else {
                    poster = new Poster(posterUrl, null, title, caption, year.getYear());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            poster = new Poster(posterUrl, null, title, caption, year.getYear());
            poster.setDirector(movieData.get("director"));
            poster.setImdbLink(movieData.get("imdb"));
        }
        System.out.println("Henter: " + title + " med url: " + posterUrl);
        return poster;
    }

    /**
     * Method for returning movie data for each specific movie page
     *
     * @return a map with the correct data
     * @pararm url the url of the movie from IMP Awards
     */
    public Map<String, String> getMovieData(String url) {
        Map<String, String> movieData = null;
        Document doc = getDocument(url);
        if (doc != null) {
            movieData = new HashMap<>();
            Elements links = doc.select("div.rightsidesmallbordered");
            if (links.size() > 1) {
                String imdbLink = links.get(2).select("a:first-child").attr("href");
                System.out.println("IMDB: " + imdbLink);
                movieData.put("imdb", imdbLink);
            }
            Elements directorElement = doc.select("div.rightsidesmallbordered");
            if (directorElement.size() > 3) {

                // Fetch director if only director is present

                String director = directorElement.get(1).select("a:first-child").text();
                movieData.put("director", director);

                // Fetch director if starring is part of the director text
                try {
                    String directorWithStarred = directorElement.get(1).text();
                    System.out.println("DIRECTOR: " + directorWithStarred.substring(directorWithStarred.indexOf(":"), directorWithStarred.indexOf("starring")));
                    String onlyDirector = directorWithStarred.substring(directorWithStarred.indexOf(":") + 1, directorWithStarred.indexOf("starring")).trim();
                    movieData.put("director", onlyDirector);
                } catch (StringIndexOutOfBoundsException e) {

                }
            }
        }

        return movieData;
    }

    /**
     * Method for returning a list of posters for a given page
     *
     * @param doc the document you want to get posters from
     * @return a list of posters
     */
    public List<Poster> getPostersFromPage(Year year, Document doc, boolean checkExistingUrl) {
        List<Poster> posters = new ArrayList<>();
        if (doc != null) {
            Elements thumbnails = doc.select("div.constant_thumb");
            posters = thumbnails.stream()
                    .map(thumbnail -> parsePosterData(year, thumbnail, checkExistingUrl))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
        return posters;
    }

    /**
     * Method to check if a url is valid and exists
     *
     * @param url the url we are checking
     * @return true if the url exists, false otherwise
     */
    public boolean urlExists(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("HEAD");
        return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
    }
}
