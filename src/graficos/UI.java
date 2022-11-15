package graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import entities.Player;
import main.Game;

public class UI {
	
	public void tick() {

	}

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 23));
		g.drawString("Score: " + Game.score, 10, 25);
		
		g.setColor(Color.red);
		g.fillRect(Game.WIDTH * Game.SCALE - 170, 6, 150, 25);
		
		g.setColor(Color.green);
		g.fillRect(Game.WIDTH * Game.SCALE - 170, 6, (int) ((Game.life/100) * 150), 25);
	}
	
}
