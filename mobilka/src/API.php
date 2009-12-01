<?php
include 'parserURL.class.php';
include 'KonwerterXML.class.php';
include 'KonwerterJSON.class.php';
//include 'IOperacja.interface.php';

class APImain{
	public $typOperacji;
	public $kryteria = array();
	public $typWynikowy;
	public $zasoby = array();

	public $blad = false;

	function __construct(){
		$parserURL = new parserURL();
		$parserURL->parsujURL($parserURL->pobierzURL());
		$this->typOperacji=$parserURL->getOperacja();
		$this->typWynikowy=$parserURL->getTypWynikowy();
		$this->kryteria=$parserURL->getKryteria();
		$this->zasoby=$parserURL->getZasoby();

		if(!$parserURL->getBLad()){
			switch($this->typOperacji){

				case 'get':

					/*TODO polaczenie z baza
					 * otrzymanie zasobów lub komunikatu z bazy
					 * jesli get spr czy xml czy json
					 * uruchomienie konwertera
					 * jesli put, post, delete - komunikat
					 */
					$dummyStatus = new Statusy();
					$dummyStatus->id = 77;
					$dummyStatus->nadawca = "dummy";
					$dummyStatus->odbiorcy = "dummy";
					$dummyStatus->typ = "dummy";
					$dummyStatus->timestamp = "dummy";
					$dummyStatus->kanal = "dummy";
					$dummyStatus->tresc = $parserURL->pobierzURL();
					$this->zasoby[0] = $dummyStatus;

						//echo "<br>$this->typWynikowy<br>";
					if($this->typWynikowy=="XML"){
						//header('Cache-Control: no-cache, must-revalidate');
						//header('Expires: Mon, 26 Jul 1997 05:00:00 GMT');
						//header('Content-type: application/xml');
						print "Content-type: text/html\n\n";
						$konwerter = new KonwerterXML();
						$konwerter->konwertujDane($this->zasoby);
						
					}elseif($this->typWynikowy=="JSON"){
						
						$konwerter = new KonwerterJSON();
						$konwerter->konwertujDane($this->zasoby);
					}
						
						

					break;

				case 'put':
					//komunikat
					break;

				case 'post':
					//wysy³anie do bazy
					//komunikat
					break;

				case 'delete':
					//komunikat
					break;
			}

		}

	}
}


$main = new APImain();

?>

