//#############################################################################
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
}