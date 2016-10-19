package no.uib.smo015.info310.host2016.Poster;

/**
 * Created by Sindre on 12.10.2016.
 */
public class Poster {
    private String posterUrl, largePosterUrl, title, caption, director, imdbLink;
    private int year;

    public Poster(String posterUrl, String largePosterUrl, String title, String caption, int year) {
        this.posterUrl = posterUrl;
        this.largePosterUrl = largePosterUrl;
        this.title = title;
        this.caption = caption;
        this.year = year;
    }

    public Poster(String posterUrl, String title, String caption, int year) {
        this.posterUrl = posterUrl;
        this.title = title;
        this.year = year;
        this.caption = caption;
    }

    public Poster(String posterUrl, String title, int year) {
        this.posterUrl = posterUrl;
        this.title = title;
        this.year = year;
        this.caption = caption;
    }

    @Override
    public String toString() {
        return "Poster{" +
                "posterUrl='" + posterUrl + '\'' +
                ", largePosterUrl='" + largePosterUrl + '\'' +
                ", title='" + title + '\'' +
                ", caption='" + caption + '\'' +
                ", year=" + year +
                '}';
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLargePosterUrl() {
        return largePosterUrl;
    }

    public void setLargePosterUrl(String largePosterUrl) {
        this.largePosterUrl = largePosterUrl;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getImdbLink() {
        return imdbLink;
    }

    public void setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
    }
}
