package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import DTO.UserDTO;

public class UserTest {
	
	UserDTO User = new UserDTO (1, "Mort123", "Morten", "Petersen", "111111-1111", 
									"wergergw33", "AA", Arrays.asList("Admin"), 1);

	@Test
	public final void testGetName() {
		String expected = "Morten";
		String actual = User.getName();

		assertEquals(expected, actual);
	}

	@Test
	public final void testGetRole() {
		List<String> expected = Arrays.asList("Admin");
		List<String> actual = User.getRole();

		assertEquals(expected, actual);
	}

	@Test
	public final void testGetActive() {
		int expected = 1;
		int actual = User.getActive();

		assertEquals(expected, actual);
	}

}
