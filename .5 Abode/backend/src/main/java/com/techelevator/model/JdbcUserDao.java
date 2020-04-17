package com.techelevator.model;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.DataSource;

import com.techelevator.authentication.PasswordHasher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private PasswordHasher passwordHasher;

    /**
     * Create a new user dao with the supplied data source and the password hasher
     * that will salt and hash all the passwords for users.
     *
     * @param dataSource an SQL data source
     * @param passwordHasher an object to salt and hash passwords
     */
    @Autowired
    public JdbcUserDao(DataSource dataSource, PasswordHasher passwordHasher) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.passwordHasher = passwordHasher;
    }

    /**
     * Save a new user to the database. The password that is passed in will be
     * salted and hashed before being saved. The original password is never
     * stored in the system. We will never have any idea what it is!
     *
     * @param email the user name to give the new user
     * @param password the user's password
     * @param role the user's role
     * @return the new user
     */
    @Override
    public User saveUser(String email, String firstName, String lastName, String phoneNumber, String password, String role) {
        byte[] salt = passwordHasher.generateRandomSalt();
        String hashedPassword = passwordHasher.computeHash(password, salt);
        String saltString = new String(Base64.getEncoder().encode(salt));
        long newId = jdbcTemplate.queryForObject(
                "INSERT INTO users(email, first_name, last_name, phone_number, password, salt, role) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING user_id", Long.class,
                email, firstName, lastName, phoneNumber, hashedPassword, saltString, role);

        User newUser = new User();
        newUser.setId(newId);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setRole(role);

        return newUser;
    }

    @Override
    public void changePassword(User user, String newPassword) {
        byte[] salt = passwordHasher.generateRandomSalt();
        String hashedPassword = passwordHasher.computeHash(newPassword, salt);
        String saltString = new String(Base64.getEncoder().encode(salt));

        jdbcTemplate.update("UPDATE users SET password=?, salt=? WHERE id=?", hashedPassword, saltString, user.getId());
    }

    /**
     * Look for a user with the given email and password. Since we don't
     * know the password, we will have to get the user's salt from the database,
     * hash the password, and compare that against the hash in the database.
     *
     * @param email the user name of the user we are checking
     * @param password the password of the user we are checking
     * @return true if the user is found and their password matches
     */
    @Override
    public User getValidUserWithPassword(String email, String password) {
        String sqlSearchForUser = "SELECT * FROM users WHERE UPPER(email) = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchForUser, email.toUpperCase());
        if (results.next()) {
            String storedSalt = results.getString("salt");
            String storedPassword = results.getString("password");
            String hashedPassword = passwordHasher.computeHash(password, Base64.getDecoder().decode(storedSalt));
            if (storedPassword.equals(hashedPassword)) {
                return mapResultToUser(results);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Get all of the users from the database.
     * @return a List of user objects
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String sqlSelectAllUsers = "SELECT user_id, email, first_name, last_name, phone_number, role FROM users";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectAllUsers);

        while (results.next()) {
            User user = mapResultToUser(results);
            users.add(user);
        }

        return users;
    }

    /**
     * Get all of the users with role="renter" from the database.
     * @return a List of user objects
     */
    @Override
    public List<User> getAllRenters() {
        String sql = "SELECT user_id, email, first_name, last_name, phone_number, role FROM users WHERE role=?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "renter");
        
        List<User> renters = new ArrayList<User>();
        while (results.next()) {
            User user = mapResultToUser(results);
            renters.add(user);
        }
        return renters;
    }


    private User mapResultToUser(SqlRowSet results) {
        User user = new User();
        user.setId(results.getLong("user_id"));
        user.setEmail(results.getString("email"));
        user.setFirstName(results.getString("first_name"));
        user.setLastName(results.getString("last_name"));
        user.setPhoneNumber(results.getString("phone_number"));
        user.setRole(results.getString("role"));
    
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        String sqlSelectUserByEmail = "SELECT user_id, email, first_name, last_name, phone_number, role FROM users WHERE email = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectUserByEmail, email);

        if (results.next()) {
            return mapResultToUser(results);
        } else {
            return null;
        }
    }

    // @Override
    // public List<User> getUserByProperty(int propId) {
    //     String sqlUserAndProp = "SELECT first_name FROM user INNER JOIN property ON "
    // }

    @Override
    public List<User> getAllMaintenancUsers(){
        List<User> allMaintenancUsers = new ArrayList<>();
        String sql = "SELECT user_id, email, first_name, last_name, phone_number, role FROM users WHERE role = 'maintenance';";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        
        while(results.next()){
            User user = mapResultToUser(results);
            user = numberOfRequestsPerEmployee(user);
            allMaintenancUsers.add(user);
        }
        return allMaintenancUsers;
    }

    @Override
    public User numberOfRequestsPerEmployee(User user){
        String sql = "SELECT COUNT(assigned_user_id) AS number FROM maintenance_request WHERE assigned_user_id = ? GROUP BY assigned_user_id;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user.getId());
        if(results.next()){
            user.setNumberOfAssignedRequests(results.getInt("number"));
        }
        return user;
    }
}
