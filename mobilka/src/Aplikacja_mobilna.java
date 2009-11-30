import javax.microedition.midlet.*;  
import javax.microedition.lcdui.*;  
import javax.microedition.io.*;  
import java.io.*;  

/**
 * Aplikacja_mobilna
 * @author Artur Majewski & Witold Olejniczak
 * @version 0.5
 */
public class Aplikacja_mobilna extends MIDlet
implements CommandListener, Runnable {

private Display display;
private Form loggerForm;
private Form addressForm;
private Form connectForm;
private Form displayForm;
private Form wyslijForm;
private TextField serverURL;
private StringItem messageLabel;
private StringItem errorLabel;
private StringItem wyslijLabel;
private TextField loginField;
private TextField passField;
private Command acceptLog;
private Command okCommand;
private Command exitCommand;
private Command backCommand;
private Command wyslijCommand;



/* (non-Javadoc)
 * @see javax.microedition.midlet.MIDlet#startApp()
 */
protected void startApp() throws MIDletStateChangeException {
if (display == null) {
initialize();
display.setCurrent(loggerForm);
}
}

/* (non-Javadoc)
 * @see javax.microedition.midlet.MIDlet#pauseApp()
 */
protected void pauseApp() {
}

/* (non-Javadoc)
 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
 */
protected void destroyApp(boolean unconditional)
    throws MIDletStateChangeException {
}

/* (non-Javadoc)
 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
 */
public void commandAction(Command cmd, Displayable d) {
if (cmd == okCommand) {
Thread t = new Thread(this);
t.start();
display.setCurrent(connectForm);
} else if (cmd == backCommand) {
display.setCurrent(addressForm);
} else if (cmd == exitCommand) {
try {
destroyApp(true);
} catch (MIDletStateChangeException ex) {
}
notifyDestroyed();
}else if(cmd == wyslijCommand){
	display.setCurrent(wyslijForm);
}
else if(cmd==acceptLog){
	display.setCurrent(addressForm);
}
}


/* (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
public void run() {
/* tu by³o badziewie 
od
	InputStream is = null;
do
}
} catch (IOException ex1) {
}
}
*/
	Polaczenie moje= new Polaczenie();
	moje.polacz(serverURL.getString(),display, addressForm, displayForm, messageLabel);
	//moje.slij(serverURL.getString(), "huhu", wyslijForm);
}

/**
 * Komentrarz metody initilize
 * 
 * @param 
 */
private void initialize() {
display = Display.getDisplay(this);

// Commands
exitCommand = new Command("Exit", Command.EXIT, 0);
okCommand = new Command("OK", Command.OK, 0);
backCommand = new Command("Back", Command.BACK, 0);
wyslijCommand=new Command("Wyslij",Command.OK,1);
acceptLog = new Command("Zatwierdz",Command.OK,2);
loginField = new TextField("Login:", "", 20, TextField.ANY);
passField = new TextField("Haslo:", "", 20, TextField.PASSWORD);

// The address form
addressForm = new Form("Blop");
serverURL = new TextField("debug - URL odbioru:", "", 256, TextField.ANY);
addressForm.append(serverURL);
addressForm.addCommand(okCommand);
addressForm.addCommand(exitCommand);
addressForm.addCommand(wyslijCommand);
addressForm.setCommandListener(this);

// The connect form
connectForm = new Form("Connecting");
messageLabel = new StringItem(null, "Connecting...\nPlease wait.");
connectForm.append(messageLabel);
connectForm.addCommand(backCommand);
connectForm.setCommandListener(this);

// The display form
displayForm = new Form("Server Reply");
messageLabel = new StringItem(null, null);
displayForm.append(messageLabel);
displayForm.addCommand(backCommand);
displayForm.setCommandListener(this);


//wysylacz form
wyslijForm = new Form("Wysylacz");
//odbierzForm.append(odbierzLabel);
wyslijForm.addCommand(backCommand);
//odbierzForm.addCommand(odbierzCommand);
wyslijForm.setCommandListener(this);


//logger form
loggerForm = new Form("Zaloguj");
//odbierzForm.append(odbierzLabel);
loggerForm.append(loginField);
loggerForm.append(passField);
loggerForm.addCommand(backCommand);
loggerForm.addCommand(acceptLog);
//odbierzForm.addCommand(odbierzCommand);
loggerForm.setCommandListener(this);


}
}