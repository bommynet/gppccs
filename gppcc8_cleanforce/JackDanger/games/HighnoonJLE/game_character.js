//#############################################################################
//### HighnoonJLE: Steuerbarer Charakter (Spieler/KI)
//#############################################################################
JackDanger.HighnoonJLE.Character = function(parent) {
	this.parent = parent;
}

JackDanger.HighnoonJLE.Character.prototype = {
	/**
	 * Initialisiert alle wichtigen Daten des Sprite-Objekts.
	 */
	init: function(x, y, ax, ay, gravity, worldBound) {
		//Sprite selbst anlegen (noch kein Bild)
		this.sprite = this.parent.add.sprite(x, y, this.parent.ATLAS, "laufpfeil_up_0");
		this.sprite.width = this.parent.ctrl.cellSize;
		this.sprite.height = 2 * this.parent.ctrl.cellSize;
		this.sprite.anchor.set(ax, ay);

		//Physik aktivieren
		this.parent.physics.enable(this.sprite, Phaser.Physics.ARCADE);
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
}


//### Funktionen: Aktionen
//#############################################################################
JackDanger.HighnoonJLE.Character.prototype.actionGoLeft = function() {
	console.log("links");
}

JackDanger.HighnoonJLE.Character.prototype.actionGoRight = function() {
	console.log("rechts");	
}

JackDanger.HighnoonJLE.Character.prototype.actionJump = function() {
	console.log("sprung");
}

JackDanger.HighnoonJLE.Character.prototype.actionShoot = function() {
	console.log("schuss");
}

JackDanger.HighnoonJLE.Character.prototype.actionSpecial1 = function() {
	console.log("spezial 1");
}

JackDanger.HighnoonJLE.Character.prototype.actionSpecial2 = function() {
	console.log("spezial 2");
}