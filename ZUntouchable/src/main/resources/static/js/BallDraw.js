function BallDraw(){
	this.ballDrawnNumber =[];
	this.ballDrawnLetter =[];
	
	
	this.DrawBall = () =>{
		let repeat = 0;
		let ballletter;
		let ballnumber= 1 =Math.floor(Math.Random()*75);
		for(var index = 0; index<75; index++){
			if(ballNumber == this.BallNumber[index] && ballletter == this.ballDrawnLetter(index)){
				repeat ++;
			}
			}
			if(repeat == 0){
				if(ballNumber < 15){
					this.ballDrawnNumber.push("0");
					this.ballDrawnNumber.push(ballNumber);
					}
					else if (ballNumber > 15 && ballNumber <= 30){
					this.ballDrawnNumber.push("0");
					this.ballDrawnNumber.push(ballNumber);
			}
			else if (ballNumber > 31 && ballNumber <= 45){
					this.ballDrawnNumber.push("0");
					this.ballDrawnNumber.push(ballNumber);
		}
		else if (ballNumber >= 46 && ballNumber <= 60){
					this.ballDrawnNumber.push("0");
					this.ballDrawnNumber.push(ballNumber);
		}
		else if (ballNumber >=61 && ballNumber <= 75){
					this.ballDrawnNumber.push("0");
					this.ballDrawnNumber.push(ballNumber);
	}
}
	else{
	ballNumber = 1 + Math.floor(Math.random() * 75);
	}
	repeat = 0;
}
}
