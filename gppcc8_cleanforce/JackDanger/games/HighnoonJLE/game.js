//#############################################################################
//### HighnoonJLE: Kernobjekt
//#############################################################################
JackDanger.HighnoonJLE = function() {

}

//globale Variablen
JackDanger.HighnoonJLE.prototype = {
    ATLAS: "HighnoonJLE",

    INIT: 0, //Status -> initialisiere Spielobjekte das erste Mal
    PREP: 1, //Status -> bereite Level vor (Einstellen der Objekte)
    RDY:  2,  //Status -> Level startklar
    RUN:  3,  //Status -> Spiel läuft aktuell
    END:  4,  //Status -> Spielrunde ist vorbei
    OVER: 5   //Status -> Spiel ist vorbei
}

JackDanger.HighnoonJLE.prototype.init = function() {
    logInfo("init Game");
    addLoadingScreen(this);//nicht anfassen
}

JackDanger.HighnoonJLE.prototype.preload = function() {
	this.load.path = 'games/' + currentGameData.id + '/assets/';//nicht anfassen
	
    //füge hie rein was du alles laden musst.
    this.load.atlas("HighnoonJLE");
}

//wird nach dem laden gestartet
JackDanger.HighnoonJLE.prototype.create = function() {
    Pad.init();//nicht anfassen
    removeLoadingScreen();//nicht anfassen

    //### eigene Objekte erstellen ####
    //#################################
    //Controller erstellen
    this.ctrl = new JackDanger.HighnoonJLE.Controller(this);
    //Wände aufbauen
    this.room = new JackDanger.HighnoonJLE.Room(this);
    this.room.init();
    this.room.prep(0);
    //Spieler erstellen
    this.player = new JackDanger.HighnoonJLE.Character(this);
    this.player.init(2*this.ctrl.cellSize, this.game.height - 2*this.ctrl.cellSize, 0.5, 0.5, true, true);
    console.log("1");
    this.player.addAnimationLoop("laufpfeil_up",3,10);
    console.log("2");
    this.player.runAnimation("laufpfeil_up");
    console.log("3");

    //Physik: Arcade Physik einschalten
    game.physics.startSystem(Phaser.Physics.ARCADE);
	game.physics.arcade.gravity.y = 650;
}

//wird jeden Frame aufgerufen
JackDanger.HighnoonJLE.prototype.update = function() {
    var dt = this.time.physicsElapsedMS * 0.001;

    this.ctrl.controllRunning(dt);

    this.ctrl.collisionRunning();

    //interne Zusatände: INIT, PREP, RDY, RUN, END
    /*switch(this.logic.gameState) {
    	case this.logic.INIT:
    		this.initEverything();
    		this.logic.gameState = this.logic.PREP;
    		break;

    	case this.logic.PREP:
    		this.prepLevel(this.logic.level);
    		this.logic.gameState = this.logic.RDY;
    		break;

    	case this.logic.RDY:
    		this.logic.gameState = this.logic.RUN;
    		break;

    	case this.logic.RUN:
    		this.updateStateRunning(dt);
    		//this.logic.gameState = this.logic.END;
    		break;

    	case this.logic.END:
    		if(this.logic.level < this.logic.levels.length) {
    			this.logic.level++;
    			this.logic.gameState = this.logic.PREP;
    		} else {
    			onVictory();
    		}
    		break;
    }*/
}

JackDanger.HighnoonJLE.prototype.render = function() {
	if(!true) {
   		//game.debug.body(this.player.sprite);
		//game.debug.bodyInfo(this.player.sprite);
	}
}


//#############################################################################
//### Das Einfügedingendsteil fürs Framework
//#############################################################################
//hier musst du deine Eintragungen vornhemen.
addMyGame("HighnoonJLE", "Highnoon", "Bommy", "Es ist Zeit für ein Duell-l-l-l...!", JackDanger.HighnoonJLE);

//Pfeiltasten: Bewegen
//Shoot: Schwamm werfen
//Jump: Springen