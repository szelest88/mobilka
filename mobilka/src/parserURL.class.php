<?php
include 'Statusy.class.php';
class parserURL{
	public $adresURL;
	public $metodaRest;
	public $kryteria = array();
	public $typWynikowy;
	public $zasoby = array();
	public $blad = false;

	public function pobierzURL(){

		$s = empty($_SERVER["HTTPS"]) ? ''
		: ($_SERVER["HTTPS"] == "on") ? "s"
		: "";
		$protocol = substr(strtolower($_SERVER["SERVER_PROTOCOL"]),
		0,strpos(strtolower($_SERVER["SERVER_PROTOCOL"]), "/")).$s;
		$port = ($_SERVER["SERVER_PORT"] == "80") ? ""
		: (":".$_SERVER["SERVER_PORT"]);
		return $protocol."://".$_SERVER['SERVER_NAME'].$port.$_SERVER['REQUEST_URI'];
	}

	public function parsujURL($adresURL){


		//var_dump($_POST);

		//echo "<br>\$adresURL:::  ".$adresURL;

		$sparsowany = parse_url($adresURL);   ///<-przerabia na tablice asocjacyjna
		//echo "<br><br> \$sparsowany::: ";
		//var_dump($sparsowany);

		$this->metodaRest =   strtolower($_SERVER['REQUEST_METHOD']);   //post|get|put|delete
		//echo "<br><br> \$metodaRest::: ".$this->metodaRest;

		$zapytanie = explode("=",parse_url($adresURL, PHP_URL_QUERY));
		//echo "<br><br> \$zapytanie::: ";
		//var_dump($zapytanie);


		//$parse['/scheme/host/path']."<br>";
		$sciezka = $sparsowany['path'];
		//$sciezka = ltrim($sciezka,"/new/API.php");
		$sciezka = ltrim($sciezka,"/~gkmio2009/API/API.php");
		//echo "<br><br> \$sciezka::: ";
		//var_dump($sciezka);

		$sciezkaTab = explode("/",$sciezka);
		//echo "<br><br> \$sciezkaTab::: ";
		//var_dump($sciezkaTab);

		$this->typWynikowy = $sparsowany['fragment'];
		//########################################################################################

		switch($this->metodaRest){
			//*****************************GETGETGETGETGETGETGETGETGETGETGETGETGET******************
			case'get':
				if(isset($sciezkaTab[0])){
					switch($sciezkaTab[0]){	//status | subscriptions | users

						//===================>
						case'status':		//<======================================GET/status
							//===================>

							//echo "<br>status<br>";
							if(isset($sciezkaTab[1])){
								switch($sciezkaTab[1]){	//all | liczba
									case 'all':
										//echo "<br>zwroc najnowsze<br>";
										if(isset($zapytanie[0]) && isset($zapytanie[1])){
											if($zapytanie[0]=="limit" && is_numeric($zapytanie[1])){
												//echo"<br>limit=$zapytanie[1]<br>";
												//POPRAWNE status/all?limit=7++++++++++++++++++++++++++++++++++++++++++++
											}else{
												echo "<br>BLAD<br>";
												$this->blad = true;
											}
										}else{
											echo "<br>BLAD<br>";
											$this->blad = true;
										}


										break;
									default:
										if(is_numeric($sciezkaTab[1])){
											//POPRAWNE status/5++++++++++++++++++++++++++++++++++++++++++
											//echo "<br>zwroc o id=".$sciezkaTab[1];
										}else{
											echo "<br>BLAD<br>";
											$this->blad = true;
										}
										break;
								}
							}else{
								echo "<br>BLAD<br>";
								$this->blad = true;
							}
							break;

							//================>
						case'users':	//<======================================GET/users
							//================>

							//echo "<br>users<br>";
							if(isset($sciezkaTab[1])){
								//$username = $sciezkaTab[1];	//users/*kazio*/status|subscriptions
								//echo"<br>dla $sciezkaTab[1]<br>";
								if(isset($sciezkaTab[2])){		//
									if($sciezkaTab[2]=="status"){
										//echo "<br>status<br>";
										if(isset($sciezkaTab[3])){
											if(is_numeric($sciezkaTab[3])){	//TYLKO liczba
												//echo "<br>zwroc nowsze od id=".$sciezkaTab[3];
												if(isset($sciezkaTab[4])){
													if($sciezkaTab[4]=="since"){ //TYLKO since
														//echo "<br>since<br>";
															
														if(isset($zapytanie[0]) && isset($zapytanie[1])){
															if($zapytanie[0]=="limit" && is_numeric($zapytanie[1])){
																//echo"<br>limit=$zapytanie[1]<br>";
																//POPRAWNE users/kazio/status/5/since?limit=6+++++
															}else{
																echo "<br>BLAD<br>";
																$this->blad = true;
															}
														}else{
															echo "<br>BLAD<br>";
															$this->blad = true;
														}
													}else{
														echo "<br>BLAD<br>";
														$this->blad = true;
													}
												}else{
													echo "<br>BLAD<br>";
													$this->blad = true;
												}
											}else{
												echo "<br>BLAD<br>";
												$this->blad = true;
											}
										}else{

											if(isset($zapytanie[0]) && isset($zapytanie[1])){
												if($zapytanie[0]=="limit" && is_numeric($zapytanie[1])){
													//echo"<br>limit=$zapytanie[1]<br>";
													//POPRAWNE users/kazio/status?limit=20++++++++++++++++++++++
												}else{
													echo "<br>BLAD<br>";
													$this->blad = true;
												}
											}else{
												echo "<br>BLAD<br>";
												$this->blad = true;
											}


										}
									}elseif($sciezkaTab[2]=="subscriptions"){
										//echo "<br>subskrybcje<br>";
										if(isset($sciezkaTab[3])){
											if($sciezkaTab[3]=="from"){
												//echo "<br>from<br>";
												//POPRAWNE users/kazio/subscriptions/from++++++++++++++++++
											}elseif($sciezkaTab[3]=="to"){
												//echo "<br>to<br>";
												//POPRAWNE users/kazio/subscriptions/to+++++++++++++++++++++
											}else{
												echo "<br>BLAD<br>";
												$this->blad = true;
											}
										}else{
											//POPRAWNE users/kazio/subscriptions+++++++++++++++++++++++++++++
										}

									}else{
										echo "<br>BLAD<br>";
										$this->blad = true;
									}
								}else{
									echo "<br>BLAD<br>";
									$this->blad = true;
								}
							}else{
								echo "<br>BLAD<br>";
								$this->blad = true;
							}
							break;

							//========================>
						case'subscriptions':	//<======================================GET/subscriptions
							//========================>

							//echo "<br>subscriptions dla aktualnego usera<br>";

							if(isset($sciezkaTab[1])){
								if($sciezkaTab[1]=="from"){
									//echo "<br>from<br>";
									
									//POPRAWNE subscriptions/from++++++++++++++++++
								}elseif($sciezkaTab[1]=="to"){
									//echo "<br>to<br>";
									//POPRAWNE subscriptions/to+++++++++++++++++++++
								}else{
									echo "<br>BLAD<br>";
									$this->blad = true;
								}
							}else{
								//echo "<br>wszystkie<br>";
								//POPRAWNE subscriptions+++++++++++++++++++++++++++++
							}

							break;
							//TODO - casy dla reszty zasobow
						default:
							echo "<br>BLAD<br>";
							$this->blad = true;
					}
				}
				else{
					echo "<br>BLAD<br>";
					$this->blad = true;
				}

				break;

				//***************************************************************************************
			case'put':
				//TODO - tworzenie SUBSKRBCJI
				break;
			case'post':
				if(isset($sciezkaTab[0])){
					if($sciezkaTab[0]=="status"){
						//echo "<br>stworz nowy status<br>";
						//var_dump($_POST);
						$nowyStatus = new Statusy();
						$nowyStatus->nadawca = $_POST['nadawca'];
						$nowyStatus->odbiorcy = $_POST['odbiorcy'];
						$nowyStatus->typ = $_POST['typ'];
						$nowyStatus->timestamp = $_POST['timestamp'];
						$nowyStatus->kanal = $_POST['kanal'];
						$nowyStatus->tresc = $_POST['tresc'];
						$this->zasoby[0] = $nowyStatus;
						var_dump($nowy_status);
						//TODO - JAKA STRUKTURA STATUSU W POSCIE??
					}else{
						echo "<br>BLAD<br>";
						$this->blad = true;
					}
				}else
				{
					echo "<br>BLAD<br>";
					$this->blad = true;
				}
				break;
			case'delete':
				//TODO - kasowanie
				break;


		}

	}
	public function getOperacja(){
		return $this->metodaRest;
	}
	public function getKryteria(){
		return $this->kryteria;
	}
	public function getTypWynikowy(){
		return $this->typWynikowy;
	}
	public function getZasoby(){
		return $this->zasoby;
	}
	public function getBlad(){
		return $this->blad;
	}
}
?>
