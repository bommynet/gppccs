//#############################################################################
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
}