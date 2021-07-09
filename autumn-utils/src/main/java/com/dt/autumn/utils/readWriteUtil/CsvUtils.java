package com.dt.autumn.utils.readWriteUtil;

/*-
 * #%L
 * autumn-utils
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
