package com.desertskyrangers.flightdeck;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter( autoApply = true )
public class AppColorConverter implements AttributeConverter<AppColor, String> {

	@Override
	public String convertToDatabaseColumn( AppColor color ) {
		if( color == null ) return null;
		return AppColor.toWeb( color );
	}

	@Override
	public AppColor convertToEntityAttribute( String data ) {
		if( data == null ) return null;
		return AppColor.fromWeb( data );
	}

}