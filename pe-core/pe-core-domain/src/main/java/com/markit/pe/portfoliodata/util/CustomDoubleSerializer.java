/**
 * 
 */
package com.markit.pe.portfoliodata.util;

import java.io.IOException;
import java.text.DecimalFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author mahesh.agarwal
 *
 */
public class CustomDoubleSerializer extends JsonSerializer<Double>{

	@Override
	public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		if (null == value) {
            //write the word 'null' if there's no value available
            jgen.writeNull();
        } else {
            final String pattern = "###.#####";
            //final String pattern = "###,###,##0.00";
            final DecimalFormat myFormatter = new DecimalFormat(pattern);
            final String output = myFormatter.format(value);
            jgen.writeNumber(output);
        }
	}

}
