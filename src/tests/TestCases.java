package tests;

import static org.junit.Assert.*;
import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import backend.TelecomClient;

public class TestCases {

	static String password = "password";
  static String username = RandomStringUtils.randomAlphabetic(6);
	int respMsgSubType; 
	
	 @Before
   public void setUp() {
			 try {
					TelecomClient.connectToServer();
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	      System.out.println("@Before - Connect to server");
   }
	 
	 /*Create User: The user creation command has four possible responses in the Sub-Message Type:
		 a. User Creation Success = 0
		 b. User Already Exists = 1
		 	i. This will be returned if the username already exists in the system
		 c. User Already Logged In = 2
		 	i. In order to create a user on the system, no one can be logged in. It is assumed you want to create an account and do not have one already.
		 d. Badly Formatted Create User Request = 3
		 	i. This will occur if you do not have 2 fields (username and password), separated by a Ò,Ó when you as the server to create a user.*/
	 @Test
	 //Created new user <username>
	 public void testCreateUser() { 
		 try {
			respMsgSubType = TelecomClient.createUser(username, password);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 assertEquals(0, respMsgSubType);
	 }
	 
	 @Test
	 //Failed to create user <username>. User probably already exists
	 public void testCreateUserExists() { 
		 try {
			respMsgSubType = TelecomClient.createUser(username, password);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 assertEquals(1, respMsgSubType);
	 }
	 
	 /*@Test
	 public void testCreateUserLoggedIn() { 
		 if(loggedIn = true){
			 try {
				 respMsgSubType = TelecomClient.createUser(username, password);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 assertEquals(2, respMsgSubType);
		 }
	 }*/
	
	 @Test
	 //Badly formatted user create message
	 public void testCreateUserBadlyFormatted() { 
		 try {
			 String payload = username + ":" + password;
			 respMsgSubType = TelecomClient.readWriteSocket(5, 0, payload.getBytes().length, payload);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 assertEquals(3, respMsgSubType);
	 }
	 
	 /*Login: The login operation has four possible responses:
		 a. Login Ok = 0
		 b. User already logged in = 1
		 c. Bad credentials = 2
		 	i. This will be returned if the supplied username and password combination was not able to be found in the database. It is however a correctly formatted message
		 d. Badly formatted message = 3
		 	i. Returned if expected fields are missing (such as no username/password supplied)*/
	 
	 /*Create Store: The operation for creating a user store has three possible return values
		 a. Store Created Successfully = 0 - every time the user logs in a store is created
		 b. Store already exists = 1
		 c. Not Logged In = 2
		 	i. This will be returned if you try to create a store before logging in first.*/
	 
	 @Test
	 //Login (Result = Login successful) and create user store (Result = User's data store created)
	 public void testLoginCreateStore() { 
		 try {
			respMsgSubType = TelecomClient.loginUser(username, password);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 assertEquals(0, respMsgSubType);
	 }
	 
	 @Test
	 /* Tests logged in (Result = Already logged in) does not login
	  * and create store (Result = Must login first to create a user store)
	  */
	 public void testLoggedInStoreExists() { 
			 try {
				respMsgSubType = TelecomClient.loginUser(username, password);
			 }
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 assertEquals(1, respMsgSubType);
	 }
	 
	 @Test
	 /* Tests log in bad credentials (Result = Failed to login, bad credentials) 
	  * and create user store (Result = Must login to create a user store) 
	  */
	 public void testLoginBadCredentialsCreateUserStore() { 
		 try {
			 String newuser = RandomStringUtils.randomAlphabetic(3);
			 respMsgSubType = TelecomClient.loginUser(newuser, password);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 assertEquals(2, respMsgSubType);
	 }
	 
	 @Test
	 public void testLoginBadlyFormatted() { 
		 try {
			 String payload = ":" + password;
			 respMsgSubType = TelecomClient.readWriteSocket(3, 0, payload.getBytes().length, payload);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 assertEquals(3, respMsgSubType);
	 }
	 
	 //Exit Request: The exit request will have no response, and will simply disconnect the connection immediately.
	 @Test
	 public void testExit(){
				try {
					respMsgSubType = TelecomClient.readWriteSocket(0, 0, 1, " ");
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				assertEquals(0, respMsgSubType);
	 }
	 
	 //Echo request
	 @Test
	 public void testEcho(){
		try {
			String payload = "hello world";
			respMsgSubType = TelecomClient.readWriteSocket(2, 0, payload.getBytes().length, payload);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(0, respMsgSubType);
	 }

}
