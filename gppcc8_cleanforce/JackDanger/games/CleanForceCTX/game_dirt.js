//#############################################################################
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
}