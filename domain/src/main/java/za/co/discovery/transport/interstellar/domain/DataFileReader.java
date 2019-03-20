package za.co.discovery.transport.interstellar.domain;

import com.opencsv.CSVReader;
import lombok.extern.java.Log;
import za.co.discovery.transport.interstellar.service.api.exception.TransportSystemException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Log
public class DataFileReader {

	public static List<String[]> readDataFromFile(String fileName) {
		CSVReader reader = null;
		List<String[]> result = new ArrayList<String[]>();
		try {
			InputStream sourceFileStream = DataFileReader.class.getClassLoader().getResourceAsStream(fileName);
			reader = new CSVReader(new InputStreamReader(sourceFileStream));
			String[] header = reader.readNext();
			if (header == null) {
				throw new TransportSystemException("Failed to import data from file. File is empty");
			}

			String[] line;
			while ((line = reader.readNext()) != null) {
				result.add(line);
			}
		} catch (Exception p) {
			log.log(Level.SEVERE, p.getMessage(), p);
		} finally {
			try { if (reader != null) {reader.close();} } catch (IOException e) {e.printStackTrace();}
		}

		return result;
	}
}