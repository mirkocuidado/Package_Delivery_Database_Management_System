package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.UserOperations;

public class UserOperationsTest {
  private GeneralOperations generalOperations;
  
  private UserOperations userOperations;
  
  private TestHandler testHandler;
  
  @Before
  public void setUp() throws Exception {
    this.testHandler = TestHandler.getInstance();
    Assert.assertNotNull(this.testHandler);
    this.userOperations = this.testHandler.getUserOperations();
    Assert.assertNotNull(this.userOperations);
    this.generalOperations = this.testHandler.getGeneralOperations();
    Assert.assertNotNull(this.generalOperations);
    this.generalOperations.eraseAll();
  }
  
  @After
  public void tearDown() throws Exception {}
  
  @Test
  public void insertUser() {
    String username = "crno.dete";
    String firstName = "Svetislav";
    String lastName = "Kisprdilov";
    String password = "sisatovac123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
  }
  
  @Test
  public void declareAdmin() {
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    this.userOperations.insertUser(username, firstName, lastName, password);
    Assert.assertEquals(0L, this.userOperations.declareAdmin(username));
  }
  
  @Test
  public void declareAdmin_NoSuchUser() {
    Assert.assertEquals(2L, this.userOperations.declareAdmin("Nana"));
  }
  
  @Test
  public void declareAdmin_AlreadyAdmin() {
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    this.userOperations.insertUser(username, firstName, lastName, password);
    this.userOperations.declareAdmin(username);
    Assert.assertEquals(1L, this.userOperations.declareAdmin(username));
  }
  
  @Test
  public void getSentPackages_userExisting() {
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    this.userOperations.insertUser(username, firstName, lastName, password);
    Assert.assertEquals(new Integer(0), this.userOperations.getSentPackages(new String[] { username }));
  }
  
  @Test
  public void getSentPackages_userNotExisting() {
    String username = "rope";
    Assert.assertNull(this.userOperations.getSentPackages(new String[] { username }));
  }
  
  @Test
  public void deleteUsers() {
    String username1 = "rope";
    String firstName1 = "Pero";
    String lastName1 = "Simic";
    String password1 = "tralalalala123";
    this.userOperations.insertUser(username1, firstName1, lastName1, password1);
    String username2 = "rope_2";
    String firstName2 = "Pero";
    String lastName2 = "Simic";
    String password2 = "tralalalala321";
    this.userOperations.insertUser(username2, firstName2, lastName2, password2);
    Assert.assertEquals(2L, this.userOperations.deleteUsers(new String[] { username1, username2 }));
  }
  
  @Test
  public void getAllUsers() {
    String username1 = "rope";
    String firstName1 = "Pero";
    String lastName1 = "Simic";
    String password1 = "tralalalala221";
    this.userOperations.insertUser(username1, firstName1, lastName1, password1);
    String username2 = "rope_2";
    String firstName2 = "Pero";
    String lastName2 = "Simic";
    String password2 = "tralalalala222";
    this.userOperations.insertUser(username2, firstName2, lastName2, password2);
    Assert.assertEquals(2L, this.userOperations.getAllUsers().size());
    Assert.assertTrue(this.userOperations.getAllUsers().contains(username1));
    Assert.assertTrue(this.userOperations.getAllUsers().contains(username2));
  }
}
