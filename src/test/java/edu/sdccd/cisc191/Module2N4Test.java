package edu.sdccd.cisc191;

import edu.sdccd.cisc191.Jdbcstuff.JdbcUserRepository;
import edu.sdccd.cisc191.Jdbcstuff.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Module2N4Test {

    private Connection conn;
    private UserRepository repo;

    @BeforeEach
    void setup() throws SQLException {
        // In-memory H2 database (fresh for every test)
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        // Create required tables
        conn.createStatement().execute("""
            CREATE TABLE users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(100) NOT NULL
            );
        """);

        conn.createStatement().execute("""
            CREATE TABLE scores (
                id INT AUTO_INCREMENT PRIMARY KEY,
                score INT NOT NULL
            );
        """);

        repo = new JdbcUserRepository(conn);
    }

    @Test
    void testSaveAndLoadScores() throws SQLException {
        // Save scores
        repo.saveScore(15);
        repo.saveScore(30);
        repo.saveScore(45);

        // Load scores
        List<Integer> scores = repo.getAllScores();

        // Assertions
        assertEquals(3, scores.size());
        assertTrue(scores.contains(15));
        assertTrue(scores.contains(30));
        assertTrue(scores.contains(45));
    }
}