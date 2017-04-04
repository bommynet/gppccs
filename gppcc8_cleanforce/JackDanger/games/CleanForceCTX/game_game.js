//#############################################################################
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
}