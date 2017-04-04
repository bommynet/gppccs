//#############################################################################
//### HighnoonJLE: Room
//#############################################################################
JackDanger.HighnoonJLE.Room = function(parent) {
    this.parent = parent;
}

JackDanger.HighnoonJLE.Room.prototype = {
	
}

/**
 * Initialisiert alle wichtigen Elemente.
 */
JackDanger.HighnoonJLE.Room.prototype.init = function() {
	this.border = this.parent.add.group();
	this.floor  = this.parent.add.group();

	this.parent.stage.backgroundColor = "#4488AA";
}

/**
 * Bereitet ein neues Lebel vor.
 * @param levelId Nummer des vorzubereitenden Levels
 */
JackDanger.HighnoonJLE.Room.prototype.prep = function(levelId) {
	this.border.removeAll(true);
	this.floor.removeAll(true);

	var maxX = this.parent.game.width / this.parent.ctrl.cellSize;
	var maxY = this.parent.game.height / this.parent.ctrl.cellSize;

	//oberer und unterer Rand
	for(var i=0; i < maxX; i++) {
		this.addWallTile(i*this.parent.ctrl.cellSize, 0, this.border);
		this.addWallTile(i*this.parent.ctrl.cellSize, (maxY-1) * this.parent.ctrl.cellSize, this.border);
		//this.addWallTile(i*this.parent.ctrl.cellSize, (maxY-2) * this.parent.ctrl.cellSize, this.border);
	}

	//linker und rechter Rand
	for(var i=1; i < maxY-1; i++) {
		this.addWallTile(0, i*this.parent.ctrl.cellSize, this.border);
		this.addWallTile((maxX-1) * this.parent.ctrl.cellSize, i*this.parent.ctrl.cellSize, this.border);
	}

	//Reihe oben
	for(var i=4; i < maxX-4; i++) {
		if(i <= 13 || i >= maxX-14) {
			this.addWallTile(i*this.parent.ctrl.cellSize, 4*this.parent.ctrl.cellSize, this.floor);
		}
	}

	//Reihe oben
	for(var i=1; i < maxX-1; i++) {
		if(i <= 6 || i >= maxX-7) {
			this.addWallTile(i*this.parent.ctrl.cellSize, 8*this.parent.ctrl.cellSize, this.floor);
		} else if(i >= 9 && i <= maxX-10) {
			this.addWallTile(i*this.parent.ctrl.cellSize, 9*this.parent.ctrl.cellSize, this.floor);
		}
	}

	//Reihe unten
	for(var i=4; i < maxX-4; i++) {
		if(i <= 13 || i >= maxX-14) {
			this.addWallTile(i*this.parent.ctrl.cellSize, 13*this.parent.ctrl.cellSize, this.floor);
		}
	}
}

/**
 * Erstellt ein animiertes Wandteil an gewünschter Stelle und fügt es einer
 * Gruppe hinzu.
 * @param x horizontale Position
 * @param y vertikale Position
 * @param group Wandteil hinzufügen zu dieser Gruppe
 */
JackDanger.HighnoonJLE.Room.prototype.addWallTile = function(x, y, group) {
	var wallUp = this.parent.add.sprite(x, y, this.parent.ATLAS, "laufpfeil_up_0", group);
	wallUp.scale.set(this.parent.ctrl.cellSize / wallUp.height);
	wallUp.anchor.set(0,0);
	wallUp.animations.add("rotateUp", Phaser.Animation.generateFrameNames("laufpfeil_up_", 0, 4), 10, true);
	wallUp.animations.add("rotateDown", Phaser.Animation.generateFrameNames("laufpfeil_down_", 0, 4), 10, true);
	wallUp.animations.play("rotateUp");

	if(group == this.border) {
		//Physik bestimmen
		game.physics.enable(wallUp, Phaser.Physics.ARCADE);
		wallUp.body.bounce.y = 0;
		wallUp.body.collideWorldBounds = false;
		wallUp.body.immovable = true;
		wallUp.body.allowGravity = false;
	}
}

/**
 * Wechselt die Gravitation des Raumes.
 */
JackDanger.HighnoonJLE.Room.prototype.toggleGravitation = function() {
	//Animation umdrehen
	var aniSet = "rotateUp";
	if(this.parent.ctrl.gravitationUp) {aniSet = "rotateDown";}
	for(var i=0; i<this.border.length; i++) {
		this.border.children[i].animations.play(aniSet);
	}
	for(var i=0; i<this.floor.length; i++) {
		this.floor.children[i].animations.play(aniSet);
	}

	this.parent.ctrl.gravitationUp = !this.parent.ctrl.gravitationUp;
}