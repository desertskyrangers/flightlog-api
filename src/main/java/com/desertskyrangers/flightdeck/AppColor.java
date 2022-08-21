package com.desertskyrangers.flightdeck;

public class AppColor {

	/* Gray scale */
	public static final AppColor WHITE = AppColor.of( 255, 255, 255 );

	public static final AppColor LIGHT_GRAY = AppColor.of( 192, 192, 192 );

	public static final AppColor GRAY = AppColor.of( 128, 128, 128 );

	public static final AppColor DARK_GRAY = AppColor.of( 64, 64, 64 );

	public static final AppColor BLACK = AppColor.of( 0, 0, 0 );

	/* Common colors */
	public static final AppColor RED = AppColor.of( 255, 0, 0 );

	public static final AppColor ORANGE = AppColor.of( 255, 128, 0 );

	public static final AppColor YELLOW = AppColor.of( 255, 255, 0 );

	public static final AppColor GREEN = AppColor.of( 0, 255, 0 );

	public static final AppColor BLUE = AppColor.of( 0, 0, 255 );

	public static final AppColor PURPLE = AppColor.of( 128, 0, 256 );

	public static final AppColor MAGENTA = AppColor.of( 255, 0, 255 );

	public static final AppColor CYAN = AppColor.of( 0, 255, 255 );

	/**
	 * The color values are stored in ARGB order.
	 */
	private final int value;

	public AppColor( int r, int g, int b ) {
		this( r, g, b, 255 );
	}

	public AppColor( int r, int g, int b, int a ) {
		this( ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF)) );
	}

	public AppColor( float r, float g, float b ) {
		this( (int)(r * 255 + 0.5), (int)(g * 255 + 0.5), (int)(b * 255 + 0.5), 255 );
	}

	public AppColor( float r, float g, float b, float a ) {
		this( (int)(r * 255 + 0.5), (int)(g * 255 + 0.5), (int)(b * 255 + 0.5), (int)(a * 255 + 0.5) );
	}

	public AppColor( int value ) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public int getRed() {
		return (value >> 16) & 0xFF;
	}

	/**
	 * Returns the green component in the range 0-255 in the default sRGBA
	 * space.
	 *
	 * @return the green component.
	 */
	public int getGreen() {
		return (value >> 8) & 0xFF;
	}

	/**
	 * Returns the blue component in the range 0-255 in the default sRGBA
	 * space.
	 *
	 * @return the blue component.
	 */
	public int getBlue() {
		return (value) & 0xFF;
	}

	/**
	 * Returns the alpha component in the range 0-255 in the default sRGBA
	 * space.
	 *
	 * @return the alpha component.
	 */
	public int getAlpha() {
		return (value >> 24) & 0xff;
	}

	public int hashCode() {
		return value;
	}

	public boolean equals( Object object ) {
		return object instanceof AppColor && ((AppColor)object).value == this.value;
	}

	public String toString() {
		return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + ",a=" + getAlpha() + "]";
	}

	public String toWeb() {
		return AppColor.toWeb( this );
	}

	public static AppColor of( int r, int g, int b ) {
		return new AppColor( r, g, b );
	}

	public static AppColor of( int r, int g, int b, int a ) {
		return new AppColor( r, g, b, a );
	}

	public static AppColor of( float r, float g, float b ) {
		return new AppColor( r, g, b );
	}

	public static AppColor of( float r, float g, float b, float a ) {
		return new AppColor( r, g, b, a );
	}

	public static AppColor fromHex( String hex ) throws NumberFormatException {
		String s = hex;
		if( s.startsWith( "#" ) ) s = s.substring( 1 );
		if( s.startsWith( "0x" ) ) s = s.substring( 2 );

		// Hex colors are in the form #aarrggbb
		int i = Integer.parseUnsignedInt( s, 16 );
		return new AppColor( (i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF, (i >> 24) & 0xFF );
	}

	public static String toHex( AppColor color ) throws NumberFormatException {
		// Hex colors are in the form #aarrggbb
		return "0x".concat( String.format( "%08x", color.value ) );
	}

	public static AppColor fromWeb( String web ) throws NumberFormatException {
		String s = web;
		if( s.startsWith( "#" ) ) s = s.substring( 1 );
		if( s.startsWith( "0x" ) ) s = s.substring( 2 );

		// Web colors are in the form #rrggbbaa
		int i = Integer.parseUnsignedInt( s, 16 );
		return new AppColor( (i >> 24) & 0xFF, (i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF );
	}

	public static String toWeb( AppColor color ) throws NumberFormatException {
		// Web colors are in the form #rrggbbaa
		return "#".concat( String.format( "%08x", color.value << 8 | (color.value >> 24) & 0xFF ) );
	}

	public static int hsbToRgb( float hue, float saturation, float brightness ) {
		return hsbToRgb( hue, saturation, brightness, 1.0f );
	}

	public static int hsbToRgb( float hue, float saturation, float brightness, float alpha ) {
		int r = 0;
		int g = 0;
		int b = 0;
		int a = (int)(alpha * 255.0f + 0.5f);

		if( saturation == 0 ) {
			r = g = b = (int)(brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float)Math.floor( hue )) * 6.0f;
			float f = h - (float)java.lang.Math.floor( h );
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch( (int)h ) {
				case 0 -> {
					r = (int)(brightness * 255.0f + 0.5f);
					g = (int)(t * 255.0f + 0.5f);
					b = (int)(p * 255.0f + 0.5f);
				}
				case 1 -> {
					r = (int)(q * 255.0f + 0.5f);
					g = (int)(brightness * 255.0f + 0.5f);
					b = (int)(p * 255.0f + 0.5f);
				}
				case 2 -> {
					r = (int)(p * 255.0f + 0.5f);
					g = (int)(brightness * 255.0f + 0.5f);
					b = (int)(t * 255.0f + 0.5f);
				}
				case 3 -> {
					r = (int)(p * 255.0f + 0.5f);
					g = (int)(q * 255.0f + 0.5f);
					b = (int)(brightness * 255.0f + 0.5f);
				}
				case 4 -> {
					r = (int)(t * 255.0f + 0.5f);
					g = (int)(p * 255.0f + 0.5f);
					b = (int)(brightness * 255.0f + 0.5f);
				}
				case 5 -> {
					r = (int)(brightness * 255.0f + 0.5f);
					g = (int)(p * 255.0f + 0.5f);
					b = (int)(q * 255.0f + 0.5f);
				}
			}
		}

		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static float[] rgbToHsb( int r, int g, int b ) {
		return rgbToHsb( r, g, b, 255 );
	}

	public static float[] rgbToHsb( int r, int g, int b, int a ) {
		return rgbToHsb( r, g, b, a, null );
	}

	public static float[] rgbToHsb( int r, int g, int b, float[] hsbaValues ) {
		return rgbToHsb( r, g, b, 255, hsbaValues );
	}

	public static float[] rgbToHsb( int r, int g, int b, int a, float[] hsbaValues ) {
		float hue;
		float saturation;
		float brightness;
		float alpha = ((float)a) / 255.0f;

		if( hsbaValues == null ) hsbaValues = new float[ 4 ];

		int max = Math.max( r, g );
		if( b > max ) max = b;
		int min = Math.min( r, g );
		if( b < min ) min = b;

		brightness = ((float)max) / 255.0f;
		if( max != 0 ) {saturation = ((float)(max - min)) / ((float)max);} else saturation = 0;
		if( saturation == 0 ) {hue = 0;} else {
			float cRed = ((float)(max - r)) / ((float)(max - min));
			float cGreen = ((float)(max - g)) / ((float)(max - min));
			float cBlue = ((float)(max - b)) / ((float)(max - min));
			if( r == max ) {hue = cBlue - cGreen;} else if( g == max ) {hue = 2.0f + cRed - cBlue;} else hue = 4.0f + cGreen - cRed;
			hue = hue / 6.0f;
			if( hue < 0 ) hue = hue + 1.0f;
		}

		hsbaValues[ 0 ] = hue;
		hsbaValues[ 1 ] = saturation;
		hsbaValues[ 2 ] = brightness;
		hsbaValues[ 3 ] = alpha;

		return hsbaValues;
	}

}
