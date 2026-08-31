package com.eventsphere.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomConfig {


	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT) // only MATCHING prop names
				                                       // between src n dest will be transferred , during the mapping
				.setPropertyCondition(Conditions.isNotNull());// only non-null properties will be transferred from src
		// --> dest , during the mapping
		return modelMapper;
	}
}
