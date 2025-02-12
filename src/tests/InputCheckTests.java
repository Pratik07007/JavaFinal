package tests;


import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;

import app.EmailValidiator;
import app.PasswordHash;

public class InputCheckTests {
	
	@Test
	public void checkValidEmail_success() {
		String validEmail = "s.dhimal006@gmail.com";
		assertTrue(EmailValidiator.isValidEmail(validEmail),"Failed: Invalid Email");
	}
	
	@Test
	public void checkValidEmail_failure() {
		String invalidEmail = "hainahaina";
		assertTrue(EmailValidiator.isValidEmail(invalidEmail),"Failed: Invalid Email");
	}
	
	@Test
	public void compareHashPassword_success() {
		String rawText = "qwy94y81nasFKnaslkfna";
		String hash =  PasswordHash.hashPassword(rawText);
		assertTrue(PasswordHash.verifyPassword(rawText, hash),"Couldnot compare check the hashing functionality ");
	}
	
	@Test
	public void compareHashPassword_failure() {
		String rawText = "safkjnaskfnaskfnjasbfjkbasjkfb";
		String hash =  PasswordHash.hashPassword(rawText);
		assertTrue(PasswordHash.verifyPassword("asfafasfas", hash),"Couldn't compare check the hashing functionality ");
	}

	
}
