package sonicbond.sonicbond;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * These unit tests are designed to check the functionality of the main UiManager object
 *
 * Author: Roger Herzfeldt
 * Date: 3/31/2018
 */
public class UiManagerUnitTest {
    UiManager ui;


    ///Tests for username default settings
    ///to be used in later configuration for server connections
    @Test
    public void UserNameSet() {
        ui = new UiManager();
        String userName = "defaultuser";
        assertEquals(userName, ui.UserName);
     }

    ///Tests for username default settings
    ///to be used in later configuration for server connections
    public void PassWordSet() {
        ui = new UiManager();

        String passWord = "defaultuser";
        assertEquals(passWord, ui.Password);
    }

    ///Tests for  UiManager successful initialize of AlertManager and ServerManager
    public void UiInitSuccessfully(){
        ui = new UiManager();
        assertEquals(true, ui.Init());
    }

}