package com.dt.autumn.api.xmlProcessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class JaxbImpl<T> {
    private static volatile JaxbImpl _instance;

    public JaxbImpl() {
    }

    public static JaxbImpl getInstance() {
        if (_instance == null) {
            synchronized (JaxbImpl.class) {
                if (_instance == null) {
                    _instance = new JaxbImpl();
                }
            }
        }
        return _instance;
    }


    public <T> T fromXML(String xml,
                         Class<T> clazz) throws JAXBException {
        try {
            InputStream mockresponsestream=new ByteArrayInputStream(xml.getBytes());
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            T t=(T) jaxbUnmarshaller.unmarshal(mockresponsestream);
            return t;
        }  catch (JAXBException ioe) {
          throw ioe;
        }

    }
}
