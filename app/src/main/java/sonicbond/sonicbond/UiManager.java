package sonicbond.sonicbond;

//import sonicbond.sonicbond.AlertManager;

public class UiManager {

    public String UserName;
    private void SetUserName(String value)
    {
        this.UserName = EncryptCredentials(value);
    }
    private String GetUserName()
    {
        return this.UserName;
    }
    public String Password;
    private void SetPassword(String value)
    {
        this.Password = EncryptCredentials(value);
    }
    private String GetPassword()
    {
        return this.Password;
    }
    private String EncryptCredentials(String value)
    {
        return value;
    }

    //AlertManager Alert;
    //private boolean InitializeAlertManager()  { return true;}

    //ServerManager Server;

    public UiManager() // String UserName, String PassWord)
    {
        SetUserName("default");
        SetPassword("default");
    }

    public boolean Init()
    {
        //Set up Networking Object
        //Server = new ServerManager();
        if (true) //ServerManager.LogIn(UserName, Password))
        {
            //Login and connected to server
            //Display log in formation
            //Set parameters to relay alert information automatically


        }
        else
        {
            //Server = null;
            //Unable to Connect
            //Start Listening app and set for non-networked mode

        }
        //Set up Listening Object
        //InitializeAlertManager();
        return true;
    }
    /*
    private boolean LogIn()
    {

        return true;
    }*/

    public void LogOut()
    {
        //Server = null;
        //Alert = null;
        UserName = null;
        Password = null;
    }

    public boolean UpdateSettings()
    {
        return true;
    }

    public void ExecuteAction()
    {
        String u = GetUserName();
        String p = GetPassword();

        //SetPageDef(u+p);
    }

}


