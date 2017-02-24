package com.icesoft.libgdx.airattack;

import com.badlogic.gdx.Game;
import com.icesoft.libgdx.airattack.screens.TestTiledMapScreen2;

public class AirAttack extends Game{
	@Override
	public void create() {
		setScreen(new TestTiledMapScreen2());
	}
}
