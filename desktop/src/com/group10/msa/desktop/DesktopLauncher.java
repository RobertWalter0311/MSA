package com.group10.msa.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.group10.msa.MAS;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MAS";
		cfg.width = 1800;
		cfg.height = 1080;

		new LwjglApplication(new MAS(), cfg);
	}
}