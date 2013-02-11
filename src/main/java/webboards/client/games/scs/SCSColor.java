package webboards.client.games.scs;

import webboards.client.display.Color;

public enum SCSColor {
	DELCARE(Color.MAGENTA), 
	SUCCESS(Color.RED), 
	PARTIAL_SUCCESS(Color.ORANGE),
	FAILURE(Color.GREY);

	private Color color;

	SCSColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
