/*
 * RANY!
 * Chcia³o Ci siê otwieraæ ten plik?
 * 				Wyrazy najserdeczniejszego zdziwienia,
 * 				Artur M.
 */

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

private TextField coWyslac;
private Command zatwierdzWyslij;
Polaczenie moje;
boolean wysylanie=false;


/* (non-Javadoc)
 * @see javax.microedition.midlet.MIDlet#startApp()
 */
protected void startApp() throws MIDletStateChangeException {
if (display == null) {
initialize();
//display.setCurrent(loggerForm);
display.setCurrent(addressForm);

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
Thread t;
Thread t2;
if (cmd == okCommand) {
t = new Thread(this);
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
else if(cmd==zatwierdzWyslij){
if(!wysylanie)wysylanie=true;
//???
{
	/*
	moje= new Polaczenie();
	try{
	moje.slij("http://starzaki.eu.org/~gkmio2009/API/API.php/status/", display, "dummy", wyslijForm, "dummy");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	System.out.println("Wysylanie ok");
	*/
	//experiment
	new Thread(new Runnable()
	{
	public void run()
	{
	try{
		moje=new Polaczenie();
		moje.slij("http://starzaki.eu.org/~gkmio2009/API/API.php/status/", display, "dummy", wyslijForm, "dummy");
	} catch(IOException e) {}
	}
	}).start();
	//eoexperiment
	wysylanie=false;
			}
//???
}
}


/* (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
public void run() {
/* tu by³o
od
	InputStream is = null;
do
}
} catch (IOException ex1) {
}
}
*/
	if(!wysylanie){
	moje= new Polaczenie();
	//moje.polacz(serverURL.getString(),display, addressForm, displayForm, messageLabel); //to nawet dzia³a! Dziwne!
	
		moje.polacz("http://starzaki.eu.org/~gkmio2009/API/API.php/status/all?typ=XML&limit=10",display, addressForm, displayForm, messageLabel); //to nawet dzia³a! Dziwne!
	
	}else
	{

	}
		
}

/**
 * Komentarz metody initilize
 * 
 * @param 
 */
private void initialize() {
display = Display.getDisplay(this);

// Commands
exitCommand = new Command("Exit", Command.EXIT, 0);
okCommand = new Command("Pobierz", Command.OK, 0);
backCommand = new Command("Back", Command.BACK, 0);
wyslijCommand=new Command("Wyslij",Command.OK,1);
acceptLog = new Command("Zatwierdz",Command.OK,2);
loginField = new TextField("Login:", "", 20, TextField.ANY);
passField = new TextField("Haslo:", "", 20, TextField.PASSWORD);
coWyslac=new TextField("Zawartosc statusu","",20,TextField.ANY);
zatwierdzWyslij = new Command ("Wysylaj",Command.OK,3);
// The address form
addressForm = new Form("Blop");
serverURL = new TextField("debug - URL odbioru:", "", 256, TextField.ANY);
//addressForm.append(serverURL);
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
displayForm = new Form("Ostatni status");
messageLabel = new StringItem(null, null);
displayForm.append(messageLabel);
displayForm.addCommand(backCommand);
displayForm.setCommandListener(this);


//wysylacz form
wyslijForm = new Form("Wysylacz");
//odbierzForm.append(odbierzLabel);
wyslijForm.append(coWyslac);
wyslijForm.addCommand(backCommand);
wyslijForm.addCommand(zatwierdzWyslij);
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