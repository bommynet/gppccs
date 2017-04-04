//#############################################################################
//### Clean Force: Kernobjekt
//#############################################################################
JackDanger.CleanForceCTX = function() { };

JackDanger.CleanForceCTX.prototype.init = function() {
    logInfo("init Game");
    addLoadingScreen(this, false);//nicht anfassen
}

JackDanger.CleanForceCTX.prototype.preload = function() {
	this.load.path = 'games/' + currentGameData.id + '/assets/';//nicht anfassen
	
    //füge hie rein was du alles laden musst.
    this.load.atlas("CleanForceCTX");
    this.load.image("lx0", "laderaum.png");

    this.load.audio("bullet_blue", "bullet_blue.wav");
    this.load.audio("bullet_end", "bullet_end.wav");
    this.load.audio("splash_0", "splash_0.wav");
    this.load.audio("splash_1", "splash_1.wav");
    this.load.audio("sponge", "sponge.wav");
    this.load.audio("music", "lollo.mp3");
}

//wird nach dem laden gestartet
JackDanger.CleanForceCTX.prototype.create = function() {
    Pad.init();//nicht anfassen
}

JackDanger.CleanForceCTX.prototype.mycreate = function() {
    //### eigene Objekte erstellen ####
    //#################################
    //Spielumgebung
    this.ctrl = new JackDanger.CleanForceCTX.Controller(this);
    //Spieler
    this.player = new JackDanger.CleanForceCTX.Player(this);
    //Eimer
    this.bucket = new JackDanger.CleanForceCTX.Bucket(this);
    //Dreck
    this.dirt = new JackDanger.CleanForceCTX.Dirt(this);
    //GUI
    this.gui = new JackDanger.CleanForceCTX.GUI(this);

    //Enemy Test
    this.enemies = new JackDanger.CleanForceCTX.Enemies(this);

    //Physik: Arcade Physik einschalten
    game.physics.startSystem(Phaser.Physics.ARCADE);
	game.physics.arcade.gravity.y = 1000;

	var sfxVol = 0.1;
	var musicVol = 0.5;

	this.sound = {
		//bullet_blue: this.add.sound(key, volume, loop , connect)
		//bullet_blue: this.add.sound(key,      1, false,    true),
		bullet_blue: this.add.sound("bullet_blue", sfxVol),
		bullet_end:  this.add.sound("bullet_end", sfxVol),
		splash_0:    this.add.sound("splash_0", sfxVol),
		splash_1:    this.add.sound("splash_1", sfxVol),
		sponge:      this.add.sound("sponge", sfxVol),
		music:       this.add.audio("music", musicVol, true)
	};
}

//wird jeden Frame aufgerufen
JackDanger.CleanForceCTX.prototype.update = function() {
    var dt = this.time.physicsElapsedMS * 0.001;

    switch(this.ctrl.gameState) {
    	case this.ctrl.INIT:
    		this.initEverything();
    		this.ctrl.gameState = this.ctrl.PREP;
    		break;

    	case this.ctrl.PREP:
            //Level vorbereiten
    		this.prepLevel(this.ctrl.level);
            //Starttimer einstellen
            
            //Starttimer starten
    		this.ctrl.gameState = this.ctrl.RDY;
    		break;

    	case this.ctrl.RDY:
    		this.ctrl.gameState = this.ctrl.RUN;
    		break;

    	case this.ctrl.RUN:
    		this.updateStateRunning(dt);
    		//this.ctrl.gameState = this.ctrl.END;
    		break;

    	case this.ctrl.END:
    		if(this.ctrl.level < this.ctrl.levels.length-1) {
    			this.ctrl.level++;
    			this.ctrl.gameState = this.ctrl.PREP;
    		} else {
    			onVictory();
    		}
    		break;
    }
}

JackDanger.CleanForceCTX.prototype.updateStateRunning = function(dt) {
	//### Kollisionen #################
    //#################################
    //Spieler mit Boden
    game.physics.arcade.collide(this.player.sprite, this.ctrl.groundBody);
    //Eimer mit Boden
    game.physics.arcade.collide(this.bucket.sprite, this.ctrl.groundBody);
    //Spieler mit Katzen
    //game.physics.arcade.overlap(this.player.sprite, this.ctrl.cats, this.ctrl.letsStumble, null, this.ctrl);
    //Spieler mit Gegnergeschossen
    game.physics.arcade.overlap(this.player.sprite, this.enemies.bullets, this.ctrl.letsStumble, null, this.ctrl);
    //Eimer mit Spielfeldrand
    this.bucket.collidesWithWorld();

    //Schwamm mit Dreck
    game.physics.arcade.overlap(this.dirt.dirt, this.bucket.sponges,
    	this.ctrl.hitDirtBySponge, null, this.ctrl);

    //Fische mit Boden
    game.physics.arcade.collide(this.ctrl.groundBodyBonus, this.ctrl.fish);
    //Katzen mit Boden
    game.physics.arcade.collide(this.ctrl.groundBodyBonus, this.ctrl.cats);
    //Katzen mit Fischen
    game.physics.arcade.overlap(this.ctrl.cats, this.ctrl.fish, this.ctrl.catEatsFish, null, this.ctrl);

    //Gegnergeschosse mit Boden
    game.physics.arcade.collide(this.ctrl.groundBody, this.enemies.bullets, function(ground,bullet){bullet.kill();})
    //Gegnergeschosse mit Eimer
    game.physics.arcade.collide(this.bucket.sprite, this.enemies.bullets, this.collideWithBullet, null, this);
    //Gegner mit Eimer
    this.enemies.stomped(this.bucket.sprite);
    //Gegnergeschosse mit Spielerschild
    game.physics.arcade.overlap(this.player.shield, this.enemies.bullets, function(shield,bullet){bullet.kill();});
    //Spielergeschosse mit Gegner
    game.physics.arcade.overlap(this.enemies.enemySprites, this.player.bullets, this.enemies.gotHit, null, this.enemies);
    //Spielergeschosse mit Dreck
    game.physics.arcade.overlap(this.dirt.dirt, this.player.bullets, this.dirt.hitByBullet, null, this.dirt);

    //### Spielziel ###################
    //#################################
    this.ctrl.targetArchieved(); //-> gewonnen! || nächstes Level
    this.ctrl.missionFailed();    //-> verloren :(
    //#################################

    //### Steuerung ###################
    //#################################
    this.player.controller(dt);
    this.bucket.move();
    this.bucket.fireSponge();
    this.enemies.update(dt);
    //#################################
}

JackDanger.CleanForceCTX.prototype.render = function() {
	if(this.ctrl == undefined || this.ctrl.gameState == this.ctrl.INIT ||
		this.ctrl.gameState == this.ctrl.PREP) {
		return;
	}

   	if(!true) {
   		game.debug.body(this.bucket.sprite);
		//game.debug.bodyInfo(this.bucket.sprite);
		//game.debug.body(this.ctrl.groundBody);
		game.debug.body(this.player.shield);
        game.debug.body(this.player.sprite);
	}
}




JackDanger.CleanForceCTX.prototype.collideWithBullet = function(target, bullet) {
    this.bucket.gotHit(target, bullet);
    this.gui.updateHealth();
}




JackDanger.CleanForceCTX.prototype.initEverything = function() {
	//Welt erstellen
	// * Hintergrund
	this.ctrl.initBackground();
	// * Untergrund
	this.ctrl.initGround();
	// * Dreckbrocken
	this.dirt.initDirt();
	//Spieler
	this.player.init();
	//Katzen und Fische
	this.ctrl.initCatsNFish();
	//Eimer
	this.bucket.init();
	//GUI
	this.gui.init();
    //Gegner
    this.enemies.init();

    //Musik an
    this.sound.music.play();
}


JackDanger.CleanForceCTX.prototype.prepLevel = function(levelId) {
	//Welt einstellen
	// * lade Hintergund des Levels
	this.ctrl.prepBackground(levelId);
	// * Untergrund anpassen: Breite = Hintergrundbildbreite
	this.ctrl.prepGround(this.world.width);
	// * Dreckbrocken setzen
	this.dirt.prepDirt(levelId);
	//Spieler platzieren
	this.player.prep(levelId);
	//Katzen und Fische einstellen
	this.ctrl.prepCatsNFish(levelId);
	//Eimer einstellen
	this.bucket.prep(levelId);
	//GUI erneuern
	this.gui.prep();
    //Gegner
    this.enemies.prep();

	//Z-Ordnung anpassen
	this.ctrl.background.sendToBack();
	this.player.sprite.bringToTop();
    this.player.shield.bringToTop();
}


//Das Einfügedingendsteil fürs Framework
addMyGame("CleanForceCTX", 
          "Clean Force", 
          "Bommy", 
          "Verteidige den Waschautomaten", 
          "Laufen", //Steuerkreuz
          "Schild", //Jump button belegung
          "Schiessen", //Shoot button belegung
          JackDanger.CleanForceCTX);