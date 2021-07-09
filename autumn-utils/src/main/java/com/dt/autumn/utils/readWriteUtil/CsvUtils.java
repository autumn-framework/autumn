package com.dt.autumn.utils.readWriteUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CsvUtils {

    private static volatile CsvUtils instance;

    public static CsvUtils getInstance() {
        if (instance == null) {
            synchronized (CsvUtils.class) {
                if (instance == null) {
                    instance = new CsvUtils();
                }
            }
        }
        return instance;
    }


   public synchronized List<String[]> readCSV(String fileName){
       CSVReader csvSource;
       List<String[]> allLines;
       File file = new File(getClass().getClassLoader().getResource(fileName).getFile());

       try {
           csvSource = new CSVReader(new FileReader(file));
           allLines = csvSource.readAll();
           csvSource.close();
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       return allLines;
   }


    public synchronized void createAndWriteCSV(LinkedList<String[]> allLines, String filePath) {
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(filePath));
            CSVWriter csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                  //  CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            csvWriter.writeAll(allLines);
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void createAndWriteCSV(List<HashMap> updatedData, String filePath) {
        LinkedList<String[]> allLines = new LinkedList<>();

        try {
            int loopCounter = 0;
            for (HashMap data : updatedData) {
                String[] values = new String[data.size()];
                String[] header = new String[data.size()];
                int counter = 0;
                for (Object key : data.keySet()) {
                    if (loopCounter == 0) {
                        header[counter] = key.toString();
                    }
                    values[counter] = data.get(key).toString();
                    counter++;
                }
                if(loopCounter==0){
                    allLines.add(header);
                }
                allLines.add(values);
                loopCounter++;
            }
            createAndWriteCSV(allLines, filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}