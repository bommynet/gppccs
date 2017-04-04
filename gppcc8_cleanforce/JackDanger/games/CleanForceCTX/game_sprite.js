//#############################################################################
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
}