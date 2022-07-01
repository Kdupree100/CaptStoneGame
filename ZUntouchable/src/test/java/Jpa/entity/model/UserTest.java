package Jpa.entity.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.ACsecurity.model.User;

import Jpa.entity.model.UserTest;

public class UserTest {
	@Test
    public void testgetmail() {
    User user = new User();
    user.setEmail("admin@gmail.com");

  //assertEquals("admin@gmail.com", user.getEmail());
    assertEquals("fhdjgr@yellowbook.com", user.getEmail());
}
}