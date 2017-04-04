//#############################################################################
//### HighnoonJLE: Controller
//#############################################################################
JackDanger.HighnoonJLE.Controller = function(parent) {
    this.parent = parent;
}

JackDanger.HighnoonJLE.Controller.prototype = {
    //Feldgröße einstellen (Tilegröße & Bodenhöhe)
    cellSize: 25,

    //FPS der Animationen
    fps: 10,

    //Anfangszustand des Spiels
    gameState: 0,

    gravitationUp: true,

    inAir: false,

    timer: 0
}

JackDanger.HighnoonJLE.Controller.prototype.controllRunning = function() {
    if(Pad.isDown(Pad.LEFT)) {
        this.parent.player.sprite.body.velocity.x = -300;
    } else if(Pad.isDown(Pad.RIGHT)) {
        this.parent.player.sprite.body.velocity.x = 300;
    } else {
        this.parent.player.sprite.body.velocity.x = 0;
    }

    if(!this.inAir) {
        if(Pad.isDown(Pad.UP)) {
            console.log("up");
            this.parent.player.sprite.body.velocity.y = -400;
            this.inAir = true;
        }
    } else {
        if(this.parent.player.sprite.body.velocity.y >= 0) {
            this.inAir = false;
            console.log("fällt");
        }
    }

    if(Pad.isDown(Pad.JUMP)) {
        if(this.timer < this.parent.time.now) {
            this.parent.room.toggleGravitation();
            this.timer = this.parent.time.now + 500;
        }
    }
}



JackDanger.HighnoonJLE.Controller.prototype.collisionRunning = function() {
    if(!this.inAir || (this.inAir && this.parent.player.sprite.body.velocity.y >= 0)) {
        this.parent.physics.arcade.collide(this.parent.player.sprite, this.parent.room.border);
    }
}