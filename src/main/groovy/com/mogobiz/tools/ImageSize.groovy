/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.tools;

public enum ImageSize {
	ICON(32,32), SMALL(240,240)
	
	public int height() {
		return this.height
	}
	public int width() {
		return this.width
	}
	private final int width;
	private final int height
	ImageSize (int width, int height) {
		this.width = width
		this.height = height
	}
}
