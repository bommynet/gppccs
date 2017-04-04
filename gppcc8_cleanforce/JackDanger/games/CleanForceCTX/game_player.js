//#############################################################################
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
}