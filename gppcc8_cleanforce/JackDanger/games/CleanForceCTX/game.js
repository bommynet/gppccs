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
          JackDanger.CleanForceCTX);//#############################################################################
//### Clean Force: Sprite
//#############################################################################
JackDanger.CleanForceCTX.Sprite = function() { }
JackDanger.CleanForceCTX.Sprite.prototype = {
	/**
	 * Initialisiert alle wichtigen Daten des Sprite-Objekts.
	 * @param game        Referenz zum Spiel
	 * @param bounds      Phaser.Rectangle für Position und Breite des Sprites
	 * @param anchor      Phaser.Point als Position des Drehpunktes
	 * @param gravity     Soll das Sprite durch Gravitation beeinflusst werden?
	 * @param worldBound  Soll das Sprite in der Welt gehalten werden?
	 */
	init: function(game, bounds, anchor, gravity, worldBound) {
		//Referenzen speichern
		this.game = game;
		
		//Sprite selbst anlegen (noch kein Bild)
		this.sprite = this.game.add.sprite(bounds.x, bounds.y);
		this.sprite.width = bounds.width;
		this.sprite.height = bounds.height;
		this.sprite.anchor.set(anchor.x, anchor.y);

		//Physik aktivieren
		this.game.physics.enable(this.sprite, Phaser.Physics.ARCADE);
		this.sprite.body.bounce.y = 0;
		this.sprite.body.allowGravity = gravity;
		this.sprite.body.collideWorldBounds = worldBound;

		//Referenz auf den Physik-Körper anlegen
		this.body = this.sprite.body;
	},

	/**
	 * Einfügen einer neuen Animation.
	 * @param name   Name der Grafik
	 * @param count  Anzahl der Frames
	 * @param fps    FpS der Animation
	 */
	addAnimationLoop: function(name, count, fps) {
		//min 1 Frame müssen wir wohl haben
		count = count || 1;
		//alle Animationen einfügen (0 bis count-1)
		var frames = [];
		for(var i=0; i<count; i++) {
			frames.push(name + "_" + i);
		};
		//und ab damit ins Sprite
		this.sprite.animations.add(name, frames, fps, true, false);
	},

	/**
	 * Starte eine bestimmte Animation.
	 * @param name  Name der zu startenden Animation
	 */
	runAnimation: function(name) {
		this.sprite.animations.play(name);
	},
}//#############################################################################
//### Clean Force: Player
//#############################################################################
JackDanger.CleanForceCTX.Player = function(parent) {
	//Referenz speichern
	this.parent = parent;
}

//Spieler als Sprite anlegen
JackDanger.CleanForceCTX.Player.prototype = new JackDanger.CleanForceCTX.Sprite();

//Spieler initialisieren
JackDanger.CleanForceCTX.Player.prototype.init = function() {
	//Sprite einstellen
	var positionY = this.parent.game.height - this.parent.ctrl.floorHeight;
	this.sprite = this.parent.add.sprite(this.parent.game.width / 2, positionY, "CleanForceCTX", "jack_stand_left_0");
	this.sprite.scale.set(1.5);
	this.sprite.anchor.set(0.5, 1);

	this.shield = this.parent.add.sprite(this.parent.game.width / 2, positionY, "CleanForceCTX", "schild_schild_0");
	this.shield.scale.set(1.5);
	this.shield.anchor.set(0.5, 1);
	this.shield.animations.add("schild", ["schild_schild_0", "schild_schild_1"], 10, true, false);

	this.lastDirection = "left";

	//Physik bestimmen
	game.physics.enable(this.sprite, Phaser.Physics.ARCADE);
	this.sprite.body.bounce.y = 0;
	this.sprite.body.collideWorldBounds = true;
	this.sprite.body.mass = 2;
	this.sprite.body.width /= 2;
	this.sprite.body.height /= 1.6;

	game.physics.enable(this.shield, Phaser.Physics.ARCADE);
	this.shield.body.bounce.y = 0;
	this.shield.body.collideWorldBounds = false;
	this.shield.body.immovable = true;
	this.shield.body.allowGravity = false;

	//Sprite Animation initialisieren
	this.initAnimations();
	this.runAnimation("jack_stand_left");

	//Geschosse erstellen
	this.bulletDelay = 100;
	this.bullets = this.parent.add.group();
	this.bullets.enableBody = true;
	this.bullets.physicsBodyType = Phaser.Physics.ARCADE;

	//Schildenergie
	this.shieldMax = 5000;
	this.shieldCur = this.shieldMax;
	this.shieldLos = 1000;
	this.shieldReg = 500;

	//Kamera einstellen
	game.camera.follow(this.sprite);
	game.camera.deadzone = new Phaser.Rectangle(300, 0, 200, 450);
}

//Spieler für Level vorbereiten
JackDanger.CleanForceCTX.Player.prototype.prep = function(levelId) {
	this.sprite.alive = true;
	this.sprite.x = this.parent.world.width / 2;
	this.sprite.y = this.parent.game.height - 2*this.parent.ctrl.floorHeight;
	this.speed = this.parent.game.width / 2;
	this.isInAir = false;
	this.stumbled = false;
	this.stumbledTimer = 0;
	this.stumbledDelay = 1000; //Dauer für Benommenheit

	this.shield.kill();

	//Gruppe leeren
	this.bullets.removeAll(true);

	//wichtige Werte setzen
	this.bulletTime = 0;

	//Gruppe neu aufbauen
	this.bullets.createMultiple(30, "CleanForceCTX", "bullet_blue_0");
    this.bullets.setAll('anchor.x',         0.5);
    this.bullets.setAll('anchor.y',         1.0);
    
    this.bullets.setAll('outOfBoundsKill',  true);
    this.bullets.setAll('checkWorldBounds', true);
    this.bullets.setAll('allowGravity',     true);
    this.bullets.forEach(function(bullet){
    	bullet.animations.add("fire", ["bullet_blue_0", "bullet_blue_1", "bullet_blue_2", "bullet_blue_3"], 10, true, false);
    }, this, true);
}

//initialisiere alle möglichen Animationen für Jack
JackDanger.CleanForceCTX.Player.prototype.initAnimations = function() {
	this.addAnimationLoop("jack_stand_left", 2, 10);
	this.addAnimationLoop("jack_stand_right", 2, 10);
	this.addAnimationLoop("jack_run_left", 4, 10);
	this.addAnimationLoop("jack_run_right", 4, 10);
	this.addAnimationLoop("jack_jump_left", 2, 10);
	this.addAnimationLoop("jack_jump_right", 2, 10);
	this.addAnimationLoop("jack_stumble_left", 2, 10);
	this.addAnimationLoop("jack_stumble_right", 2, 10);
};

//Spieler Steuerung
JackDanger.CleanForceCTX.Player.prototype.controller = function(dt) {
	//tote Spieler laufen nicht und schießen iss auch nich!
	if(!this.sprite.alive) return;

	//resette Bewegung
	this.sprite.body.velocity.x = 0;

	//Aktion: Schild - vor "stolpern", da Schild auch (de-)aktivierbar
	//wenn Jack auf die Nautze fällt.
	if(Pad.isDown(Pad.JUMP) && this.shieldCur > 0) {
		if(!this.shield.alive) {
			this.shield.reset(this.sprite.x, this.sprite.y);
			this.shield.animations.play("schild");
		} else {
			this.shield.x = this.sprite.x;
			this.shield.y = this.sprite.y;
		}
		this.shieldCur -= this.shieldLos * dt;
		if(this.shieldCur < 0) {
			this.shieldCur = 0;
		}
		this.parent.gui.updateShield();
	} else {
		if(this.shield.alive) {
			this.shield.kill();
		}
		if(!Pad.isDown(Pad.JUMP)) {
			this.shieldCur += this.shieldReg * dt;
			if(this.shieldCur > this.shieldMax) {
				this.shieldCur = this.shieldMax;
			}
			this.parent.gui.updateShield();
		}
	}

	//gestolperte Spieler laufen nicht!
	if(this.stumbled) {
		//animamimiere die Sternchen um den Kopf
		this.sprite.animations.play("jack_stumble_" + this.lastDirection);
		//Stolperzeit abgelaufen?
		if(game.time.now > this.stumbledTimer) {
			this.stumbled = false;
		} else {
			return;
		}
	}

	//Bewegung: links <-> rechts
	if(Pad.isDown(Pad.LEFT)) {
			this.sprite.body.velocity.x = -this.speed;
		if(this.sprite.body.touching.down) {
			this.sprite.animations.play("jack_run_left");
		} else {
			this.sprite.animations.play("jack_jump_left");
		}
		this.lastDirection = "left";
	} else if(Pad.isDown(Pad.RIGHT)) {
			this.sprite.body.velocity.x = this.speed;
		if(this.sprite.body.touching.down) {
			this.sprite.animations.play("jack_run_right");
		} else {
			this.sprite.animations.play("jack_jump_right");
		}
		this.lastDirection = "right";
	} else {
		if(this.sprite.body.touching.down) {
			this.sprite.animations.play("jack_stand_" + this.lastDirection);
		} else {
			this.sprite.animations.play("jack_jump_" + this.lastDirection);
		}
	}

	//Bewegung: springen
	// if(Pad.isDown(Pad.UP) && this.sprite.body.touching.down) {
	// 	if(!this.isInAir) {
	// 		this.isInAir = true;
	// 		this.sprite.body.velocity.y = -400;
	// 	}
	// } else if(!Pad.isDown(Pad.UP)) {
	// 	if(this.isInAir) {
	// 		this.isInAir = false;
	// 	}
	// }

	//Aktion: Schießen, aber nur wenns Schild nicht an is
	if(Pad.isDown(Pad.SHOOT) && !Pad.isDown(Pad.JUMP)) {
		this.fireBullet();
	}
};

JackDanger.CleanForceCTX.Player.prototype.fireBullet = function() {
	//verzögerung eines Schuss abgelaufen?
    if (game.time.now > this.bulletTime) {
        //erstes freies Element aus dem Pool holen
        bullet = this.bullets.getFirstExists(false);

        if (bullet) {
            //vom Spieler aus abschießen
            bullet.reset(this.sprite.x, this.sprite.y - this.sprite.height/2);
            bullet.animations.play("fire");
            bullet.body.allowGravity = false;
            bullet.body.velocity.y = -800;
            //Sound abspielen
            this.parent.sound.bullet_blue.play();
            //neue Verzögerungszeit setzen
            this.bulletTime = game.time.now + this.bulletDelay;
        }
    }
}

JackDanger.CleanForceCTX.Player.prototype.kill = function() {
	this.sprite.kill();
};

JackDanger.CleanForceCTX.Player.prototype.stumble = function() {
	this.stumbled = true;
	this.stumbledTimer = game.time.now + this.stumbledDelay;
}//#############################################################################
//### Clean Force: Enemies
//#############################################################################
JackDanger.CleanForceCTX.Enemies = function(parent) {
	//Referenz speichern
	this.parent = parent;
}

JackDanger.CleanForceCTX.Enemies.prototype = {
	//Flags für Status-Kreislauf
	SETUP:   0,
	NORMAL:  1,
	WARN:    2,
	WARNANI: 3,
	SPAWN:   4
}

//##
JackDanger.CleanForceCTX.Enemies.prototype.init = function() {
	this.enemies = [];
	this.enemySprites = this.parent.add.group();
	this.bullets = this.parent.add.group();
	this.bullets.enableBody = true;
	this.bullets.physicsBodyType = Phaser.Physics.ARCADE;
}

JackDanger.CleanForceCTX.Enemies.prototype.prep = function() {
	//alle Gegner und Geschosse killen
	for(var i=0; i < this.enemies.length; i++) {
		if(this.enemies[i].sprite != undefined) {
			this.enemies[i].sprite.kill();
		}
		if(this.enemies[i].shield != undefined) {
			this.enemies[i].shield.kill();
		}
	}
	this.enemySprites.removeAll(true);
	this.enemies.length = 0; //Array: Inhalt löschen
	this.bullets.removeAll(true); //Group: Inhalt löschen

	//Gruppe neu aufbauen
	this.bullets.createMultiple(200, "CleanForceCTX", "bullet_red_0");
    this.bullets.setAll('anchor.x',         0.5);
    this.bullets.setAll('anchor.y',         1.0);
    this.bullets.setAll('outOfBoundsKill',  true);
    this.bullets.setAll('checkWorldBounds', true);
    this.bullets.setAll('allowGravity',     false);

    //Flags auffrischen
    this.warnTimer = 0;
    this.boolWarn = false;
    this.timer = game.time.now + 5000;

    this.flags = {
    	left:   [0,  2,  1,  2,  2],
    	middle: [0,  0,  0,  0,  0],
    	right:  [1,  0,  1,  1,  2],
    	timer:  [3, 20, 20, 20, 30]
    };
    this.currentFlags = 0;
    this.state = 0;

    //warn Sprite erstellen
    this.warnLeft = this.parent.add.sprite(0, 0, "CleanForceCTX", "warning_left_0");
    this.warnLeft.anchor.set(0, 0.5);
    var frames = ["warning_left_0", "warning_left_1", "warning_left_2", "warning_left_3"];
	this.warnLeft.animations.add("warning", frames, 10, true, false);
	this.warnLeft.kill();

	this.warnRight = this.parent.add.sprite(0, 0, "CleanForceCTX", "warning_right_0");
    this.warnRight.anchor.set(1, 0.5);
    frames = ["warning_right_0", "warning_right_1", "warning_right_2", "warning_right_3"];
	this.warnRight.animations.add("warning", frames, 10, true, false);
	this.warnRight.kill();
}


//###
JackDanger.CleanForceCTX.Enemies.prototype.warn = function(index) {
	this.timer = game.time.now + 3000;
	
	if(this.flags.left[index] > 0) {
		this.warnLeft.reset(10, 100);
		this.warnLeft.animations.play("warning");
	} 

	if(this.flags.right[index] > 0) {
		this.warnRight.reset(this.parent.game.width-10, 100);
		this.warnRight.animations.play("warning");
	}
}

//###
JackDanger.CleanForceCTX.Enemies.prototype.spawn = function(index) {
	var newEnemy = null;

	//neue Gegner erstellen: links
	if(this.flags.left[index] > 0) {
		//Gegnertyp auswählen
		switch(this.flags.left[index]) {
			case 1:
				newEnemy = new JackDanger.CleanForceCTX.Enemy(this.parent);
				break;
			default:
				newEnemy = new JackDanger.CleanForceCTX.Enemy2(this.parent);
				break;
		}

		//Gegner initialisieren und spawnen
		newEnemy.init();
		newEnemy.spawn(0, 100, 200, this.bullets);
		this.enemies.push(newEnemy);
		this.enemySprites.add(newEnemy.sprite);
	}
	
	//neue Gegner erstellen: links
	if(this.flags.right[index] > 0) {
		//Gegnertyp auswählen
		switch(this.flags.right[index]) {
			case 1:
				newEnemy = new JackDanger.CleanForceCTX.Enemy(this.parent);
				break;
			default:
				newEnemy = new JackDanger.CleanForceCTX.Enemy2(this.parent);
				break;
		}

		//Gegner initialisieren und spawnen
		newEnemy.init();
		newEnemy.spawn(game.width, 100, 200, this.bullets);
		this.enemies.push(newEnemy);
		this.enemySprites.add(newEnemy.sprite);
	}

	//Warnsymbole weg
	this.warnLeft.kill();
	this.warnRight.kill();
}


//##
JackDanger.CleanForceCTX.Enemies.prototype.update = function(dt) {
	var now = game.time.now;

	switch(this.state) {
		case this.SETUP:
			this.timer = game.time.now + this.flags.timer[this.currentFlags]*1000;
			this.state = this.NORMAL;
			break;

		case this.WARN:
			this.warn(this.currentFlags);
			this.state = this.WARNANI;
			break;

		case this.WARNANI:
			if(this.timer <= now) {
				this.state = this.SPAWN;
			}
			break;

		case this.SPAWN:
			this.spawn(this.currentFlags);
			if(this.currentFlags < this.flags.timer.length-1) {
				this.currentFlags++;
			}
			this.state = this.SETUP;
			break;

		default:
			//Timer abgelaufen?
			if(this.timer <= now || this.enemies.length <= 0) {
				this.state = this.WARN;
			}
			break;
	}

	//Bewegungen und ggf. feuern
	for(var i=0; i<this.enemies.length; i++) {
		this.enemies[i].move(dt);
		this.enemies[i].fire();
	}
	// var now = game.time.now;

	// //Bewegungen und ggf. feuern
	// for(var i=0; i<this.enemies.length; i++) {
	// 	this.enemies[i].move(dt);
	// 	this.enemies[i].fire();
	// }

	// //neuen Gegner bewarnen
	// if(this.boolWarn) {
	// 	if(this.warnTimer >= now) {
	// 		return;
	// 	} else {
	// 		this.spawn(this.nextType, this.nextX, this.nextY);
	// 		this.boolWarn = false;
	// 	}
	// } else {
	// 	if(this.enemies.length > 0) {
	// 		//if(this.enemies.length >= 2) { 
	// 			return;
	// 		//}
	// 	}
	// 	//Gegner-Warner einfügen
	// 	if(this.timer <= now) {
	// 		var x = game.rnd.integerInRange(0,1) * game.width;
	// 		var type = game.rnd.integerInRange(0,1);
	// 		this.warn(type, x, 100);
	// 		this.boolWarn = true;
	// 	}
	// }
}


//##
JackDanger.CleanForceCTX.Enemies.prototype.gotHit = function(enemy, bullet) {
	//treffer
	bullet.kill();

	//Gegner mit Schild werden nicht getroffen...
	if(enemy.name == "shield") {
		return;
	}

	enemy.health--;
	if(enemy.health > 0) {
		return;
	}

	//versenkt
	var locArray = [];
	for(var i=0; i < this.enemies.length; i++) {
		if(!(this.enemies[i].sprite === enemy)) {
			locArray.push(this.enemies[i]);
		} else {
			if(this.enemies[i].shield != undefined) {
				this.enemies[i].shield.kill();
			}
		}
	}
	this.enemySprites.removeChild(enemy);
	this.enemies = locArray;
	enemy.kill();
}


JackDanger.CleanForceCTX.Enemies.prototype.stomped = function(bucket) {
	for(var i=0; i<this.enemies.length; i++) {
		game.physics.arcade.overlap(this.enemies[i].sprite, bucket, function(){
			this.parent.bucket.gotHit2();
		}, null, this.parent.bucket);
	}
	this.parent.gui.updateHealth();
}//#############################################################################
//### Clean Force: Enemy
//#############################################################################
JackDanger.CleanForceCTX.Enemy = function(parent) {
	//Referenz speichern
	this.parent = parent;
}

//Spieler als Sprite anlegen
JackDanger.CleanForceCTX.Enemy.prototype = new JackDanger.CleanForceCTX.Sprite();

//Spieler initialisieren
JackDanger.CleanForceCTX.Enemy.prototype.init = function() {
	//Sprite einstellen
	var positionY = this.parent.game.height - this.parent.ctrl.floorHeight;
	this.sprite = this.parent.add.sprite(this.parent.game.width / 2, positionY, "CleanForceCTX", "monster1_left_0");
	this.sprite.scale.set(1);
	this.sprite.anchor.set(0.5, 1);

	game.physics.enable(this.sprite, Phaser.Physics.ARCADE);
	this.sprite.body.bounce.y = 0;
	this.sprite.body.collideWorldBounds = true;
	this.sprite.body.allowGravity = false;

	//Geschosse erstellen
	this.bulletDelay = 500;
	this.bullets = this.parent.add.group();
	this.bullets.enableBody = true;
	this.bullets.physicsBodyType = Phaser.Physics.ARCADE;

	//Sprite Animation initialisieren
	this.initAnimations();
	this.runAnimation("monster1_left");
}

//initialisiere alle möglichen Animationen für Jack
JackDanger.CleanForceCTX.Enemy.prototype.initAnimations = function() {
	this.addAnimationLoop("monster1_left", 1, 10);
	this.addAnimationLoop("monster1_right", 1, 10);
};

//Spieler für Level vorbereiten
JackDanger.CleanForceCTX.Enemy.prototype.spawn = function(x, y, speed, bulletGroup) {
	this.sprite.alive = true;
	this.sprite.x = x;
	this.sprite.y = y;
	this.baseY = y;
	this.speed = speed;
	this.sprite.health = 10;

	//Gruppe leeren
	this.bullets = bulletGroup;

	//wichtige Werte setzen
	this.bulletTime = 0;
}

JackDanger.CleanForceCTX.Enemy.prototype.move = function(dt) {
	if(this.sprite.body.blocked.left || this.sprite.body.blocked.right) {
		this.speed *= -1;
		if(this.speed < 0) {
			this.sprite.animations.play("monster1_left");
		} else {
			this.sprite.animations.play("monster1_right");
		}
	}

	this.sprite.x = this.sprite.x + (this.speed * dt);
	this.sprite.y = this.baseY + 10*Math.cos((game.time.now/2) * dt);
}

JackDanger.CleanForceCTX.Enemy.prototype.fire = function() {
	//verzögerung eines Schuss abgelaufen?
    if (game.time.now > this.bulletTime) {
        //erstes freies Element aus dem Pool holen
        bullet = this.bullets.getFirstExists(false);

        if (bullet) {
            //vom Spieler aus abschießen
            bullet.reset(this.sprite.x, this.sprite.y);
            bullet.body.allowGravity = false;
            bullet.body.velocity.x = 0;
            bullet.body.velocity.y = 200;
            //Sound abspielen
            this.parent.sound.bullet_end.play();
            //neue Verzögerungszeit setzen
            this.bulletTime = game.time.now + this.bulletDelay;
        }
    }
}//#############################################################################
//### Clean Force: Enemy
//#############################################################################
JackDanger.CleanForceCTX.Enemy2 = function(parent) {
	//Referenz speichern
	this.parent = parent;
	this.specialActive = false;
	this.specialDelay = 5000;
	this.specialDelayFire = 1000;
	this.specialTimer = game.time.now + this.specialDelay;
}

//Spieler als Sprite anlegen
JackDanger.CleanForceCTX.Enemy2.prototype = new JackDanger.CleanForceCTX.Enemy();

//Spieler initialisieren
JackDanger.CleanForceCTX.Enemy2.prototype.init = function() {
	//Sprite einstellen
	var positionY = this.parent.game.height - this.parent.ctrl.floorHeight;
	this.sprite = this.parent.add.sprite(this.parent.game.width / 2, positionY, "CleanForceCTX", "monster1_left_0");
	this.sprite.scale.set(1);
	this.sprite.anchor.set(0.5, 1);
	this.sprite.name = "shield";

	this.shield = this.parent.add.sprite(this.parent.game.width / 2, positionY, "CleanForceCTX", "schild_schild_0");
	this.shield.scale.set(1);
	this.shield.anchor.set(0.5, 0.75);

	game.physics.enable(this.sprite, Phaser.Physics.ARCADE);
	this.sprite.body.bounce.y = 0;
	this.sprite.body.collideWorldBounds = true;
	this.sprite.body.allowGravity = false;

	//Geschosse erstellen
	this.bulletDelay = 500;
	this.bullets = this.parent.add.group();
	this.bullets.enableBody = true;
	this.bullets.physicsBodyType = Phaser.Physics.ARCADE;

	//Sprite Animation initialisieren
	this.initAnimations();
	this.runAnimation("monster1_left");
	this.shield.animations.play("shield");
}

//initialisiere alle möglichen Animationen für Jack
JackDanger.CleanForceCTX.Enemy2.prototype.initAnimations = function() {
	this.addAnimationLoop("monster1_left", 1, 10);
	this.addAnimationLoop("monster1_right", 1, 10);
	this.addAnimationLoop("monster1_left_special", 3, 10);
	this.addAnimationLoop("monster1_right_special", 3, 10);
	this.shield.animations.add("schild", ["schild_schild_0", "schild_schild_1"], 10, true, false);
};

//Spieler für Level vorbereiten
JackDanger.CleanForceCTX.Enemy2.prototype.spawn = function(x, y, speed, bulletGroup) {
	this.sprite.alive = true;
	this.sprite.x = x;
	this.sprite.y = y;
	this.baseY = y;
	this.speed = speed;
	this.sprite.health = 5;

	//Gruppe leeren
	this.bullets = bulletGroup;

	//wichtige Werte setzen
	this.bulletTime = 0;
}

JackDanger.CleanForceCTX.Enemy2.prototype.move = function(dt) {
	if(!this.sprite.alive) {
		this.shield.kill();
	}

	//nicht bewegen bei Spezial-Aktion
	if(this.specialActive) return;

	if(this.sprite.body.blocked.left || this.sprite.body.blocked.right) {
		this.speed *= -1;
		if(this.speed < 0) {
			this.sprite.animations.play("monster1_left");
		} else {
			this.sprite.animations.play("monster1_right");
		}
	}

	this.sprite.x = this.sprite.x + (this.speed/2 * dt);

	if(!this.shield.alive) {
		this.shield.reset(this.sprite.x, this.sprite.y);
		this.shield.animations.play("shield");
		this.sprite.name = "shield";
	}
	this.shield.x = this.sprite.x;
	this.shield.y = this.sprite.y;

	//Special starten?
	if(game.time.now > this.specialTimer) {
		this.specialTimer = game.time.now + this.specialDelayFire;
		this.specialActive = true;
		this.shield.kill();
		this.sprite.name = "noshield";
		if(this.speed < 0) {
			this.sprite.animations.play("monster1_left_special");
		} else {
			this.sprite.animations.play("monster1_right_special");
		}
	}
}

JackDanger.CleanForceCTX.Enemy2.prototype.fire = function() {
	//dieser Gegner feuert nur, wenn der Special läuft
	if(!this.specialActive) return;

	//feuern?
	if(game.time.now > this.specialTimer) {
		//einen Haufen Bumms aktivieren
		for(var i=0; i < 16; i++) {
			bullet = this.bullets.getFirstExists(false);

	        if (bullet) {
	            //vom Gegner aus abschießen
	            bullet.reset(this.sprite.x, this.sprite.y);
	            bullet.body.allowGravity = false;
	            bullet.body.velocity.x = Math.sin(i * Math.PI/8) * 200;//veloX[i];
	            bullet.body.velocity.y = Math.cos(i * Math.PI/8) * 200;//veloY[i];
	        }
		}

		for(var i=0; i < 16; i++) {
			bullet = this.bullets.getFirstExists(false);

	        if (bullet) {
	            //vom Gegner aus abschießen
	            bullet.reset(this.sprite.x, this.sprite.y);
	            bullet.body.allowGravity = false;
	            bullet.body.velocity.x = Math.sin(i * Math.PI/8) * 250;//veloX[i];
	            bullet.body.velocity.y = Math.cos(i * Math.PI/8) * 250;//veloY[i];
	        }
		}

        //Sound abspielen
        this.parent.sound.bullet_end.play();

		//Standard wieder herstellen und neuer Timer
		this.specialTimer = game.time.now + this.specialDelay;
		this.specialActive = false;
		if(this.speed < 0) {
			this.sprite.animations.play("monster1_left");
		} else {
			this.sprite.animations.play("monster1_right");
		}
	}
}//#############################################################################
//### Clean Force: Eimer & Schwämme
//#############################################################################
JackDanger.CleanForceCTX.Bucket = function(parent) {
	this.parent = parent;
}

//Eimer als Sprite anlegen
JackDanger.CleanForceCTX.Bucket.prototype = new JackDanger.CleanForceCTX.Sprite();

JackDanger.CleanForceCTX.Bucket.prototype.init = function() {
	this.movespeed = 20;
	this.spongeDelay = 500;
	this.sponges = this.parent.add.group();
	this.sponges.enableBody = true;
	this.sponges.physicsBodyType = Phaser.Physics.ARCADE;

	//Sprite einstellen
	var positionY = this.parent.game.height - this.parent.ctrl.floorHeight;
	this.sprite = this.parent.add.sprite(this.parent.game.width / 2, positionY, "CleanForceCTX", "bucket_run_0");
	this.sprite.scale.set(1);
	this.sprite.anchor.set(0.5, 1);

	//Physik bestimmen
	game.physics.enable(this.sprite, Phaser.Physics.ARCADE);
	this.sprite.body.bounce.y = 0;
	this.sprite.body.collideWorldBounds = true;
	this.sprite.body.width -= 4;
	this.sprite.body.height *= .70;
	//this.sprite.body.setSize(1,1,0,0);

	//Sprite Animation initialisieren
	this.initAnimations();
	this.runAnimation("bucket_run");

	//Kamera einstellen
	//game.camera.follow(this.sprite);
	//game.camera.deadzone = new Phaser.Rectangle(300, 0, 200, 450);
}

JackDanger.CleanForceCTX.Bucket.prototype.prep = function(levelId) {
	this.health = 5;
	this.healthMax = 5;

	//Gruppe leeren
	this.sponges.removeAll(true);

	//wichtige Werte setzen
	this.spongeTime = 0;
	this.hitTime = 0;

	//Gruppe neu aufbauen
	this.sponges.createMultiple(30, "CleanForceCTX", "others_sponge_0");
    this.sponges.setAll('anchor.x',         0.5);
    this.sponges.setAll('anchor.y',         1.0);
    this.sponges.setAll('width',            this.parent.ctrl.cellSize / 2);
    this.sponges.setAll('height',           this.parent.ctrl.cellSize / 4);
    this.sponges.setAll('body.width',       this.parent.ctrl.cellSize / 2);
    this.sponges.setAll('body.height',      this.parent.ctrl.cellSize / 4);
    this.sponges.setAll('outOfBoundsKill',  true);
    this.sponges.setAll('checkWorldBounds', true);
    this.sponges.setAll('allowGravity',     true);
}

JackDanger.CleanForceCTX.Bucket.prototype.move = function() {
	this.sprite.body.velocity.x = this.movespeed;
}

JackDanger.CleanForceCTX.Bucket.prototype.toggleDirection = function() {
	this.movespeed *= -1;
}


//initialisiere alle möglichen Animationen für Jack
JackDanger.CleanForceCTX.Bucket.prototype.initAnimations = function() {
	this.addAnimationLoop("bucket_run", 2, 10);
};


JackDanger.CleanForceCTX.Bucket.prototype.fireSponge = function() {
	//verzögerung eines Schuss abgelaufen?
    if (game.time.now > this.spongeTime) {
        //erstes freies Element aus dem Pool holen
        sponge = this.sponges.getFirstExists(false);

        if (sponge) {
            //vom Spieler aus abschießen
            sponge.reset(this.sprite.x, this.sprite.y - this.sprite.height);
            sponge.body.allowGravity = false;
            sponge.body.velocity.y = -400;
            sponge.body.velocity.x = game.rnd.integerInRange(-40,40);
            sponge.body.angularVelocity = 360;
            //Sound abspielen
            this.parent.sound.sponge.play();
            //neue Verzögerungszeit setzen
            this.spongeTime = game.time.now + this.spongeDelay;
        }
    }
}

JackDanger.CleanForceCTX.Bucket.prototype.collidesWithWorld = function() {
	if(this.sprite.body.blocked.left || this.sprite.body.blocked.right) {
		this.toggleDirection();
	}
}

JackDanger.CleanForceCTX.Bucket.prototype.gotHit = function(bucket, bullet) {
	//Dingens killen
	bullet.kill();
	//verzögerung für Treffer abgelaufen?
    if (game.time.now > this.hitTime) {
    	this.health--;
        this.hitTime = game.time.now + 2000;
    }
}

JackDanger.CleanForceCTX.Bucket.prototype.gotHit2 = function() {
	if(this.parent.player.shield.alive) {
		return;
	}

	//verzögerung für Treffer abgelaufen?
    if (game.time.now > this.hitTime) {
    	this.health--;
        this.hitTime = game.time.now + 2000;
    }
}//#############################################################################
//### Clean Force: Game
//#############################################################################
JackDanger.CleanForceCTX.Controller = function(parent) {
	this.parent = parent;
}

//Variablen für Spielstatus
JackDanger.CleanForceCTX.Controller.prototype = {
	INIT: 0, //Status -> initialisiere Spielobjekte das erste Mal
	PREP: 1, //Status -> bereite Level vor (Einstellen der Objekte)
	RDY:  2,  //Status -> Level startklar
	RUN:  3,  //Status -> Spiel läuft aktuell
	END:  4,  //Status -> Spielrunde ist vorbei
	OVER: 5   //Status -> Spiel ist vorbei
}

JackDanger.CleanForceCTX.Controller.prototype.levels = [
	//[Reihe1, Reihe2, Reihe3, Reihe4, #CatsNFish, fish%, cat%, ziel%, bgId]
	//[  0   ,   1   ,   2   ,   3   ,     4     ,   5  ,  6  ,   7  ,  8  ]
	//[ 10,   0,   0,   0,  5, 0.1, 1.0, 0.0,  0]
	//[ 50,  50,  40,   0,  1, 0.1, 0.5, 0.25,  0],
	[ 80,  80,  70,  60,  2, 0.2, 0.5, 0.05, 0]
];

JackDanger.CleanForceCTX.Controller.prototype.level = 0;

//Feldgröße einstellen (Tilegröße & Bodenhöhe)
JackDanger.CleanForceCTX.Controller.prototype.cellSize = 36;
JackDanger.CleanForceCTX.Controller.prototype.floorHeight = 70;

//FPS der Animationen
JackDanger.CleanForceCTX.Controller.prototype.fps = 10;

//Anfangszustand des Spiels
JackDanger.CleanForceCTX.Controller.prototype.gameState = 0;

//Punktzahl aktuell
JackDanger.CleanForceCTX.Controller.prototype.score = 0;


//### Funktionen: Background
//#############################################################################
//erstelle Standard-Hintergrund
JackDanger.CleanForceCTX.Controller.prototype.initBackground = function() {
	this.bggroup = this.parent.add.group();
}

//bereite Levelhintergrund vor
JackDanger.CleanForceCTX.Controller.prototype.prepBackground = function(levelId) {
	//bestimme Bild aus levelId
	var imgKey = "lx" + this.levels[levelId][8];
	//ersetze Hintergrundbild
	var width = game.cache.getImage(imgKey).width;
	var height = game.cache.getImage(imgKey).height;
	this.bggroup.removeAll(true);
	this.background = game.add.sprite(0, 0, imgKey, this.bggroup);
	this.parent.world.setBounds(0, 0, width, height);
}


//### Funktionen: Ground
//#############################################################################
//erstelle Boden-Objekt mit Standardwerten
JackDanger.CleanForceCTX.Controller.prototype.initGround = function() {
	//minimal einen Bildschirm mit Boden ausfüllen
	this.groundBody = this.parent.add.sprite(0, this.parent.world.height - this.floorHeight, null);

	//Physik für den Boden aktivieren
	game.physics.enable(this.groundBody, Phaser.Physics.ARCADE);
	this.groundBody.body.immovable = true;
	this.groundBody.body.allowGravity = false;

	//Katzen + Fische haben nen eigenen Boden, um im Hintergrund zu sein
	this.groundBodyBonus = this.parent.add.sprite(0, this.parent.world.height - this.floorHeight - 10, null);

	//Physik für den Boden aktivieren
	game.physics.enable(this.groundBodyBonus, Phaser.Physics.ARCADE);
	this.groundBodyBonus.body.immovable = true;
	this.groundBodyBonus.body.allowGravity = false;
}

//bereite Boden-Objekt für Level vor
JackDanger.CleanForceCTX.Controller.prototype.prepGround = function(width) {
	this.groundBody.body.setSize(width, this.floorHeight);
	this.groundBodyBonus.body.setSize(width, this.floorHeight + 10);
}


//### Funktionen: Fische & Katzen
//#############################################################################
//erstelle Katzen & Fische als Vorratsobjekte
JackDanger.CleanForceCTX.Controller.prototype.initCatsNFish = function() {
	//Fische
	this.fish = this.parent.add.group();
	this.fish.enableBody = true;
	this.fish.physicsBodyType = Phaser.Physics.ARCADE;

	//Katzen
	this.cats = this.parent.add.group();
	this.cats.enableBody = true;
	this.cats.physicsBodyType = Phaser.Physics.ARCADE;

	//Wasserplatschdingers für die Schwämme
	this.splash = this.parent.add.group();

	this.catLetsPlayerStumbleChance = 1;
	this.fishSpawnChance = 1;

	this.stumbleTimer = 0;
	this.stumbleDelay = 0;
}

//bereite Katzen & Fische für das Level vor
JackDanger.CleanForceCTX.Controller.prototype.prepCatsNFish = function(levelId) {
	//alte Elemente löschen
	this.fish.removeAll(true);
	this.cats.removeAll(true);
	this.splash.removeAll(true);
	//neue Elemente je nach Level anlegen
	this.createMultiple(this.fish, this.levels[levelId][4], "others_fish_0");
	this.createMultiple(this.cats, this.levels[levelId][4], "cat_run_left_0");
	this.createMultiple(this.splash, this.levels[levelId][4], "others_fish_0", false); ///
	//Wahrscheinlichkeit, dass ein Fisch aus zerstörten Dreckbrocken fällt
	this.fishSpawnChance = this.levels[levelId][5];
	//Wahrscheinlichkeit, dass der Spieler bei Berührung über Katzen stolpert
	this.catLetsPlayerStumbleChance = this.levels[levelId][6];

	//Animationen für Katzen einlesen
	for(var i=0; i<this.cats.length; i++) {
		var cat = this.cats.children[i];

		cat.scale.set(0.7);

		//Laufanimation anlegen
    	var framename = "cat_run_left_";
		cat.animations.add("run_left", [framename+"0", framename+"1", framename+"2"], 10, true, false);
		framename = "cat_run_right_";
		cat.animations.add("run_right", [framename+"0", framename+"1", framename+"2"], 10, true, false);
		//Futteranimation anlegen
		framename = "cat_eat_left_";
		cat.animations.add("eat_left", [framename+"0", framename+"1"], 5, true, false);
		framename = "cat_eat_right_";
		cat.animations.add("eat_right", [framename+"0", framename+"1"], 5, true, false);
	}

	//Fische verkleinern (sollten nicht so groß wie eine Katze sein :>)
	for(var i=0; i<this.fish.length; i++) {
		this.fish.children[i].scale.set(0.5);
	}

	this.stumbleTimer = 0;
	this.stumbleDelay = 500;
}


//### Aktionen
//#############################################################################
//ein Dreckbrocken wurde von einem Schwamm getroffen
JackDanger.CleanForceCTX.Controller.prototype.hitDirtBySponge = function(dirt, sponge) {
	//"Leben" des Dreckbrockens updaten...
    dirt.health--;
	//...und ihn ggf. zerstören
	if(dirt.health < 1) {
		//Fish'n'Chips...äh...Fische und Katzen schmeißen
		this.probeFishCreation(dirt.x, dirt.y);

		//animation!
		var splash = this.parent.add.sprite(dirt.x + dirt.width/2, dirt.y - dirt.height/2,
		                                    "CleanForceCTX", "splash_normal_0");
		var frames = ["splash_normal_0", "splash_normal_1", "splash_normal_2", "splash_normal_3"];
		var anim = splash.animations.add("splash", frames, 15, false, false);
		anim.onComplete.add(function(sprite){sprite.kill();}, this);
		splash.animations.play("splash");
		//Sound abspielen
		if(game.rnd.integerInRange(0,1) == 0) {
			this.parent.sound.splash_0.play();
		} else {
			this.parent.sound.splash_1.play();
		}
		//Dreck weg
		dirt.kill();
		this.parent.dirt.destructed++;
		this.parent.gui.updateRemainingDirt();
	}
	//Und der Schwamm ist auch hinüber!
	//Platschanimation an Stelle des Schwamms
	//Schwamm löschen
	sponge.kill();
}

//zu gegebener Wahrscheinlichkeit einen Fisch fallen lassen
JackDanger.CleanForceCTX.Controller.prototype.probeFishCreation = function(x, y) {
	//wird die Chance ein Fisch zu erstellen eingehalten?
	if(game.rnd.frac() > this.fishSpawnChance) return;

	//erstes freies Element aus dem Pool holen
    fish = this.fish.getFirstExists(false);
    cat = this.cats.getFirstExists(false);

    if (fish && cat) {
    	fish.health = 1;
        fish.reset(x, y);
        fish.body.allowGravity = true;
        fish.body.velocity.x = 0;
        fish.body.velocity.y = -100;

        this.throwInCat(cat);
    }
}

//neue Katze aufs Feld werfen
JackDanger.CleanForceCTX.Controller.prototype.throwInCat = function(cat) {
	var x = this.cellSize;
	var y = this.parent.world.height - this.floorHeight;
	var velo = 100;

	//zufällig von links oder rechts kommend
	var goesTo = "right";
	if(game.rnd.integerInRange(0, 1) == 1) {
		goesTo = "left";
		x = this.parent.world.width - this.cellSize;
		velo *= -1;
	}

    if (cat) {
    	//korrekte Animation starten
    	cat.play("run_" + goesTo);
		//rest festlegen
    	cat.health = 0;
    	cat.reset(x, y);
        cat.body.allowGravity = true;
        cat.body.velocity.x = velo;
        cat.body.velocity.y = -100;
    }
}

//Katze trifft auf Fisch und frisst diesen auf
JackDanger.CleanForceCTX.Controller.prototype.catEatsFish = function(cat, fish) {
	//MAUZ! -> wartet -> und weiter...
	fish.kill();
}

//irgendwas mach den Spieler benommen
JackDanger.CleanForceCTX.Controller.prototype.letsStumble = function(player, hitBy) {
	//ist der Spieler bereits gestolpert?
	if(this.parent.player.stumbled) return;
	//ist die Zeit reif zum Stolpern?
	if (game.time.now > this.stumbleTimer) {
        this.parent.player.stumble();
        this.stumbleTimer = game.time.now + this.stumbleDelay;
    }
}

//wurde das Spielziel erreicht?
JackDanger.CleanForceCTX.Controller.prototype.targetArchieved = function() {
	if(this.parent.dirt.getRemainsPercent() <= this.targetPercent()) {
		this.gameState = this.END;
		this.parent.sound.music.stop();
	}
}

//hat der Spieler verloren?
JackDanger.CleanForceCTX.Controller.prototype.missionFailed = function() {
	if(this.parent.bucket.health <= 0) {
		onLose();
		this.parent.sound.music.stop();
	}
}


//### Helfer
//#############################################################################
//Erstelle eine Gruppe von Sprites auf Vorrat.
JackDanger.CleanForceCTX.Controller.prototype.createMultiple = function(group, count, image, bGravity) {
	if(bGravity == undefined) {
		bGravity = true;
	}
    group.createMultiple(count, "CleanForceCTX", image);
    group.setAll('anchor.x',         0.5);
    group.setAll('anchor.y',         1.0);
    group.setAll('outOfBoundsKill',  true);
    group.setAll('checkWorldBounds', true);
    group.setAll('allowGravity',     bGravity);
}

JackDanger.CleanForceCTX.Controller.prototype.targetPercent = function() {
	return this.levels[this.level][7];
}//#############################################################################
//### Clean Force: Dirt
//#############################################################################
JackDanger.CleanForceCTX.Dirt = function(parent) {
	this.parent = parent;
	this.cellsPerRow = 50;
	this.cellSize = this.parent.world.width / this.cellsPerRow;
	console.log("this.cellsPerRow: " + this.cellsPerRow);
	console.log("this.cellSize   : " + this.cellSize);
}

//Anzahl zerstörter Brocken
JackDanger.CleanForceCTX.Dirt.prototype.destructed = 0; 

//Anzahl Brocken insgesamt
JackDanger.CleanForceCTX.Dirt.prototype.remainMax = 10;


//### Funktionen: Dirt
//#############################################################################
//erstelle Dreck-Objekt
JackDanger.CleanForceCTX.Dirt.prototype.initDirt = function() {
	this.dirt = this.parent.add.group();
}

//bereite Levelhintergrund vor
JackDanger.CleanForceCTX.Dirt.prototype.prepDirt = function(levelId) {
	//alte Elemente löschen
	this.dirt.removeAll(true);
	//Dreckreihen berechnen
	var levelInfo = this.parent.ctrl.levels[levelId];
	for(var i=0; i<4; i++) {
		var freescale = (100 - levelInfo[i]) / (2 * 100);
		var freespace = Math.floor(freescale * this.cellsPerRow);
		for(var x=freespace; x < this.cellsPerRow-freespace; x++) {
			this.createDirtElement(x*this.cellSize, i*this.cellSize, 1, this.cellSize, this.cellSize);
		}
	}

	this.destructed = 0;
	this.remainMax  = this.dirt.length;
}

JackDanger.CleanForceCTX.Dirt.prototype.createDirtElement = function(x, y, health, width, height) {
	//zufälliges Dreckimage wählen
	var imgNr = "dirt_" + game.rnd.integerInRange(0, 2);

	//Sprite einstellen
	var local = this.parent.add.sprite(x, y, "CleanForceCTX", imgNr, this.dirt);
	local.width = width;
	local.height = height;
	local.anchor.set(0, 0);

	//Physik bestimmen
	game.physics.enable(local, Phaser.Physics.ARCADE);
	local.body.allowGravity = false;
	local.body.immovable = true;

	//der Dreck braucht Leben!
	local.health = health;
}

//Wieviel % des Drecks klebt noch am Schiff?
JackDanger.CleanForceCTX.Dirt.prototype.getRemainsPercent = function() {
	return (this.remainMax - this.destructed) / this.remainMax;
}

JackDanger.CleanForceCTX.Dirt.prototype.hitByBullet = function(dirt, bullet) {
	var aniEnd = this.parent.add.sprite(bullet.x, dirt.y + dirt.height,
	                                    "CleanForceCTX", "bullet_end_blue_0");
	aniEnd.anchor.set(0.5,0);
	var frames = ["bullet_end_blue_0", "bullet_end_blue_1", "bullet_end_blue_2", "bullet_end_blue_3"];
	var anim = aniEnd.animations.add("end", frames, 15, false, false);
	anim.onComplete.add(function(sprite){sprite.kill();}, this);
	aniEnd.animations.play("end");
	bullet.kill();
}//#############################################################################
//### Clean Force: GUI
//#############################################################################
JackDanger.CleanForceCTX.GUI = function(parent) {
	this.parent = parent;
}

JackDanger.CleanForceCTX.GUI.prototype.init = function() {
	this.score = 0;	//aktuelle Punktzahl des Spielers
	this.scoreStep = 10; //+Punkte je zerstörtem Stein

	//bars
	this.barWidth = 124;
	this.health = this.parent.add.group();
	this.barBucket = this.parent.add.sprite(54, this.parent.game.height - 5,
	                                       "CleanForceCTX", "panel_bar_0", this.panels);
	this.barBucket.anchor.set(0.0, 1.0);
	this.barBucket.width = this.barWidth;
	this.barShield = this.parent.add.sprite(this.parent.game.width/2 - 41, this.parent.game.height - 5,
	                                       "CleanForceCTX", "panel_bar_0", this.panels);
	this.barShield.anchor.set(0.0, 1.0);
	this.barShield.width = this.barWidth;

	//panels
	this.panels = this.parent.add.group();
	var panBucket = this.parent.add.sprite(5, this.parent.game.height - 5,
	                                       "CleanForceCTX", "panel_bucket_0", this.panels);
	panBucket.anchor.set(0.0, 1.0);
	var panShield = this.parent.add.sprite(this.parent.game.width/2 - 90, this.parent.game.height - 5,
	                                       "CleanForceCTX", "panel_shield_0", this.panels);
	panShield.anchor.set(0.0, 1.0);

	//text
	this.text   = this.parent.add.bitmapText(0, 0, "white", "000%");
	this.text.anchor.set(1, 1);

	this.fontArial32BC = { font: "bold 15px Arial", fill: "#fff", boundsAlignH: "center", boundsAlignV: "middle" };
}

JackDanger.CleanForceCTX.GUI.prototype.prep = function() {
	this.health.removeAll(true);

	this.barBucket.width = this.barWidth;
	this.barShield.width = this.barWidth;

	this.text.x = this.parent.game.width - 5;
	this.text.y = this.parent.game.height - 5;
	this.text.fontSize = 45;
	this.updateRemainingDirt();
}


JackDanger.CleanForceCTX.GUI.prototype.updateHealth = function() {
	var bucketHealth = this.parent.bucket.health;
	if(bucketHealth < 0) {
		bucketHealth = 0;
	}

	this.barBucket.width = this.barWidth * (bucketHealth / this.parent.bucket.healthMax);
}

JackDanger.CleanForceCTX.GUI.prototype.updateShield = function() {
	var shieldCur = this.parent.player.shieldCur;
	if(shieldCur < 0) {
		shieldCur = 0;
	}

	this.barShield.width = this.barWidth * (shieldCur / this.parent.player.shieldMax);
}

JackDanger.CleanForceCTX.GUI.prototype.updateRemainingDirt = function() {
	var remain = Math.round((1-this.parent.dirt.getRemainsPercent()) * 100);
	var target = Math.round((1-this.parent.ctrl.targetPercent()) * 100);
	this.text.setText( remain + "% / " + target + "%" );
}