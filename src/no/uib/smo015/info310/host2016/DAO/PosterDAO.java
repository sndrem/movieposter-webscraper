package no.uib.smo015.info310.host2016.DAO;

import no.uib.smo015.info310.host2016.DAOInterface.DAOInterface;
import no.uib.smo015.info310.host2016.Poster.Poster;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Sindre on 12.10.2016.
 */
public class PosterDAO implements DAOInterface {

    private String insertPoster = "INSERT INTO Posters (name, caption, url, largePosterUrl, year) VALUES(?, ?, ?, ?, ?);";
    private PreparedStatement insertPosterStmt = null;

    private String getAllPosters = "SELECT name, caption, url, largePosterUrl, year FROM Posters";
    private PreparedStatement getAllPostersStmt;

    private String getAllPostersWithLimit = "SELECT name, caption, url, largePosterUrl, year FROM Posters LIMIT ?;";
    private PreparedStatement getAllPostersWithLimitStmt;

    private String getPostersByName = "SELECT name, url, year FROM Posters WHERE NAME = ?;";
    private PreparedStatement getPosterByNameStmt;

    private String getPostersByYear = "SELECT name, url, year FROM Posters WHERE year = ?;";
    private PreparedStatement getPosterByYearStmt;

    private String updateDirectorAndImdbLink = "UPDATE Posters SET director = ?, imdbLink = ? WHERE name = ?";
    private PreparedStatement updateDirectorAndImdbLinkStmt;

    private Connection connection;
    private static PosterDAO instance;

    private PosterDAO() {
        connect();
    }

    public static PosterDAO getInstance() {
        if(instance == null) {
            instance = new PosterDAO();
        }
        return instance;
    }

    @Override
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("MYSQL-driver present");
        } catch (Exception e) {
            System.err.println("cannot connect");
        }

        String host = "localhost";
        String dbName = "movieposters";
        int port = 3306;
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

        Properties userInfo = new Properties();
        userInfo.put("user", "root");
        userInfo.put("password", "");

        try {
            this.connection = DriverManager.getConnection(url,userInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPoster(Poster poster) {
        try {
            this.insertPosterStmt = this.connection.prepareStatement(this.insertPoster);
            this.insertPosterStmt.setString(1, poster.getTitle());
            this.insertPosterStmt.setString(2, poster.getCaption());
            this.insertPosterStmt.setString(3, poster.getPosterUrl());
            this.insertPosterStmt.setString(4, poster.getLargePosterUrl());
            this.insertPosterStmt.setString(5, Integer.toString(poster.getYear()));
            this.insertPosterStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Poster> getAllPosters() {
        List<Poster> posters = new ArrayList<>();
        try {
            this.getAllPostersStmt = this.connection.prepareStatement(this.getAllPosters);
            ResultSet result = this.getAllPostersStmt.executeQuery();
            while(result.next()) {
                Poster poster = new Poster(result.getString(3), result.getString(1), result.getString(2), Integer.parseInt(result.getString(5)));
                poster.setLargePosterUrl(result.getString(4));
                posters.add(poster);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posters;
    }

    @Override
    public List<Poster> getAllPosters(int limit) {
        List<Poster> posters = new ArrayList<>();
        try {
            this.getAllPostersWithLimitStmt = this.connection.prepareStatement(this.getAllPostersWithLimit);
            this.getAllPostersWithLimitStmt.setInt(1, limit);
            ResultSet result = this.getAllPostersWithLimitStmt.executeQuery();
            while(result.next()) {
                Poster poster = new Poster(result.getString(3), result.getString(1), result.getString(2), Integer.parseInt(result.getString(5)));
                poster.setLargePosterUrl(result.getString(4));
                posters.add(poster);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posters;
    }

    @Override
    public List<Poster> getPostersByYear(int year) {
        List<Poster> posters = new ArrayList<>();
        try {
            this.getPosterByYearStmt = this.connection.prepareStatement(this.getPostersByYear);
            this.getPosterByYearStmt.setInt(1, year);
            ResultSet result = this.getPosterByYearStmt.executeQuery();
            while(result.next()) {
                posters.add(new Poster(result.getString(2), result.getString(1), Integer.parseInt(result.getString(3))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posters;
    }

    @Override
    public List<Poster> getPostersByName(String name) {
        List<Poster> posters = new ArrayList<>();
        try {
            this.getPosterByNameStmt = this.connection.prepareStatement(this.getPostersByName);
            this.getPosterByNameStmt.setString(1, name);
            ResultSet result = this.getPosterByNameStmt.executeQuery();
            while(result.next()) {
                posters.add(new Poster(result.getString(2), result.getString(1), Integer.parseInt(result.getString(3))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posters;
    }

    @Override
    public void updatePosterWithDirectorAndImdbLink(Poster poster) {
        try {
            this.updateDirectorAndImdbLinkStmt = this.connection.prepareStatement(this.updateDirectorAndImdbLink);
            this.updateDirectorAndImdbLinkStmt.setString(1, poster.getDirector());
            this.updateDirectorAndImdbLinkStmt.setString(2, poster.getImdbLink());
            this.updateDirectorAndImdbLinkStmt.setString(3, poster.getCaption());
            this.updateDirectorAndImdbLinkStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
