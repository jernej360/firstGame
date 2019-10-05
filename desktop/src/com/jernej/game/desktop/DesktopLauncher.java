package com.jernej.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jernej.game.MyGdxGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Space rescue mission";
		config.width = 1024;
		config.height = 480;
		config.forceExit=false; //Do I need it https://gamedev.stackexchange.com/questions/109047/how-to-close-an-app-correctly-on-desktop

		new LwjglApplication(new MyGdxGame(), config);
	}
}
