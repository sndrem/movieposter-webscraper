package no.uib.smo015.info310.host2016.scraper;

/**
 * Created by Sindre on 12.10.2016.
 */
public class Year {

    private int year;
    private String url;

    public Year(int year, String url) {
        this.year = year;
        this.url = url;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
