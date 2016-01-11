package com.structuredproducts.sevices;

import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 08.01.2016.
 */
public class AbstractCSVService {

    private static final Logger log = Logger.getLogger(AbstractCSVService.class);

    protected <T> List<T> getListFromFile(Class<T> clazz, final String FILE_NAME, final CellProcessor[] PROCESSORS) {
        ICsvBeanReader beanReader = null;
        List<T> list = new ArrayList<>();
        try {
            String file = this.getClass().getClassLoader().getResource(FILE_NAME).getFile();
            beanReader = new CsvBeanReader(new InputStreamReader(new FileInputStream(file), "UTF8"), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            final String[] header = beanReader.getHeader(true);

            T data;
            while( (data = beanReader.read(clazz, header, PROCESSORS)) != null ) {
                list.add(data);
            }

        } catch (FileNotFoundException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        } finally {
            if( beanReader != null ) {
                try {
                    beanReader.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }

        return list;
    }

}
