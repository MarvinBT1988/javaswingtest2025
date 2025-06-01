package esfe.persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import esfe.dominio.User;

import java.util.ArrayList;
import java.util.Random;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class UserDAOTest {
    private  UserDAO userDAO;
    @BeforeEach
    void setUp(){
        userDAO = new UserDAO();
    }
    private User create(User user) throws SQLException{
        User res = userDAO.create(user);
        assertNotEquals(res,null);
        assertEquals(user.getName(), res.getName());
        assertEquals(user.getEmail(), res.getEmail());
        assertEquals(user.getStatus(), res.getStatus());
        return res;
    }
    private void update(User user) throws SQLException{
        user.setName(user.getName()+"_u");
        user.setEmail("u" + user.getEmail());
        user.setStatus((byte)1);
        boolean res = userDAO.update(user);
        assertTrue(res);
        getById(user);
    }
    private void getById(User user) throws SQLException {
        User res = userDAO.getById(user.getId());
        assertEquals(user.getId(), res.getId());
        assertEquals(user.getName(), res.getName());
        assertEquals(user.getEmail(), res.getEmail());
        assertEquals(user.getStatus(), res.getStatus());
    }

    private void search(User user) throws SQLException {
        ArrayList<User> users = userDAO.search(user.getName());
        boolean find = false;

        for (User userItem : users) {
            if (userItem.getName().contains(user.getName())) {
                find = true;
            }
            else{
                find = false;
                break;
            }
        }

        assertTrue(find);
    }

    private void delete(User user) throws SQLException{
        boolean res = userDAO.delete(user);
        assertTrue(res);
        User res2 = userDAO.getById(user.getId());
        assertNull(res2);
    }

    private void autenticate(User user) throws SQLException {
        User res = userDAO.authenticate(user);
        assertNotEquals(res,null);
        assertEquals(res.getEmail(), user.getEmail());
        assertEquals(res.getStatus(), 1);
    }

    private void autenticacionFails(User user) throws SQLException {
        User res = userDAO.authenticate(user);
        assertNull(res);
    }
    private void updatePassword(User user) throws SQLException{
        boolean res = userDAO.updatePassword(user);
        assertTrue(res);
        autenticate(user);
    }
    @Test
    void testUserDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String strEmail = "test" + num + "@example.com";
        User user = new User(0, "Test User", "password", strEmail, (byte) 2);
        User testUser = create(user);
        update(testUser);
        search(testUser);
        testUser.setPasswordHash(user.getPasswordHash());
        autenticate(testUser);
        testUser.setPasswordHash("12345");
        autenticacionFails(testUser);
        testUser.setPasswordHash("12345");
        updatePassword(testUser);
        delete(testUser);
    }
    @Test
    void createUser() throws SQLException {
        User user = new User(0, "admin", "12345", "admin@gmail.com", (byte) 1);
        User res = userDAO.create(user);
        assertNotEquals(res,null);
    }
}