package edu.sdccd.cisc191.Jdbcstuff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository  implements UserRepository {
    private final Connection conn;

    public JdbcUserRepository(Connection conn) throws SQLException {
        this.conn = conn;


    String createScoresTable = """
            CREATE TABLE IF NOT EXISTS scores (
                id INT AUTO_INCREMENT PRIMARY KEY,
                score INT NOT NULL
            );
        """;

    PreparedStatement stmt = conn.prepareStatement(createScoresTable);
        stmt.executeUpdate();
}
        @Override
    public List<Integer> getAllScores() throws SQLException {

        List<Integer> scores = new ArrayList<>();

        String sql = "SELECT score FROM scores"; // or whatever your table is called
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            scores.add(rs.getInt("score"));
        }

        return scores;
    }


    @Override
    public void saveScore(int score) throws SQLException {
        String sql = "INSERT INTO scores (score) VALUES (?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, score);
        stmt.executeUpdate();
    }

    @Override
    public void createAccount(String username, String password) {
    // this will end up inserting into the login
        String sql ="INSERT INTO users (username, password) VALUES (?,?)";
        try(PreparedStatement pstate = conn.prepareStatement(sql)){
            // first in index
            pstate.setString(1,username);
            //second in index
            pstate.setString(2,password);
            pstate.executeUpdate();
        } catch(Exception e){
            throw new RuntimeException("not work", e);
        }
    }

    @Override
    public User findUserByUsername(String login) {
        String sql = "SELECT login, password FROM users WHERE login = ?";
        try(PreparedStatement pstate = conn.prepareStatement(sql)){
            pstate.setString(1,login);
            ResultSet rs = pstate.executeQuery();

            if(rs.next()){
                return new User(
                        rs.getString("login"),
                        rs.getString("passwordHash"));



            }

            else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("must be ",e);
        }

    }

    @Override
    public boolean usernameExists(String login) {
        String sql = "SELECT 1 FROM users WHERE  username = ?";
        try(PreparedStatement pstate = conn.prepareStatement(sql)){
            pstate.setString(1,login);
            ResultSet rs = pstate.executeQuery();
            if(rs.next()){
                return true;
            }
        }
        catch(Exception e){
            throw new RuntimeException("username taken", e);
        }

    return false;
    }

    @Override
    public boolean checkLogin(String login, String passwordHash) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        try(PreparedStatement st = conn.prepareStatement(sql)){
            st.setString(1, login);
            st.setString(2,passwordHash);
            ResultSet rset = st.executeQuery();
            rset.next();
            return rset.getInt(1) == 1;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
