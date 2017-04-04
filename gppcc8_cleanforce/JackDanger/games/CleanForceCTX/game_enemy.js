//#############################################################################
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
}