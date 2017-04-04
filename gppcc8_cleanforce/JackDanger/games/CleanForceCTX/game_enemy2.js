//#############################################################################
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
}