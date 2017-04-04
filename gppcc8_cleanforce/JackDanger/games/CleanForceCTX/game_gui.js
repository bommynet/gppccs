//#############################################################################
//### Clean Force: GUI
//#############################################################################
JackDanger.CleanForceCTX.GUI = function(parent) {
	this.parent = parent;
}

JackDanger.CleanForceCTX.GUI.prototype.init = function() {
	this.score = 0;	//aktuelle Punktzahl des Spielers
	this.scoreStep = 10; //+Punkte je zerstörtem Stein

	//bars
	this.barWidth = 124;
	this.health = this.parent.add.group();
	this.barBucket = this.parent.add.sprite(54, this.parent.game.height - 5,
	                                       "CleanForceCTX", "panel_bar_0", this.panels);
	this.barBucket.anchor.set(0.0, 1.0);
	this.barBucket.width = this.barWidth;
	this.barShield = this.parent.add.sprite(this.parent.game.width/2 - 41, this.parent.game.height - 5,
	                                       "CleanForceCTX", "panel_bar_0", this.panels);
	this.barShield.anchor.set(0.0, 1.0);
	this.barShield.width = this.barWidth;

	//panels
	this.panels = this.parent.add.group();
	var panBucket = this.parent.add.sprite(5, this.parent.game.height - 5,
	                                       "CleanForceCTX", "panel_bucket_0", this.panels);
	panBucket.anchor.set(0.0, 1.0);
	var panShield = this.parent.add.sprite(this.parent.game.width/2 - 90, this.parent.game.height - 5,
	                                       "CleanForceCTX", "panel_shield_0", this.panels);
	panShield.anchor.set(0.0, 1.0);

	//text
	this.text   = this.parent.add.bitmapText(0, 0, "white", "000%");
	this.text.anchor.set(1, 1);

	this.fontArial32BC = { font: "bold 15px Arial", fill: "#fff", boundsAlignH: "center", boundsAlignV: "middle" };
}

JackDanger.CleanForceCTX.GUI.prototype.prep = function() {
	this.health.removeAll(true);

	this.barBucket.width = this.barWidth;
	this.barShield.width = this.barWidth;

	this.text.x = this.parent.game.width - 5;
	this.text.y = this.parent.game.height - 5;
	this.text.fontSize = 45;
	this.updateRemainingDirt();
}


JackDanger.CleanForceCTX.GUI.prototype.updateHealth = function() {
	var bucketHealth = this.parent.bucket.health;
	if(bucketHealth < 0) {
		bucketHealth = 0;
	}

	this.barBucket.width = this.barWidth * (bucketHealth / this.parent.bucket.healthMax);
}

JackDanger.CleanForceCTX.GUI.prototype.updateShield = function() {
	var shieldCur = this.parent.player.shieldCur;
	if(shieldCur < 0) {
		shieldCur = 0;
	}

	this.barShield.width = this.barWidth * (shieldCur / this.parent.player.shieldMax);
}

JackDanger.CleanForceCTX.GUI.prototype.updateRemainingDirt = function() {
	var remain = Math.round((1-this.parent.dirt.getRemainsPercent()) * 100);
	var target = Math.round((1-this.parent.ctrl.targetPercent()) * 100);
	this.text.setText( remain + "% / " + target + "%" );
}