package com.dt.autumn.api.xmlProcessor;

/*-
 * #%L
 * autumn-api
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
