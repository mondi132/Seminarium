package calendr.util;

import calendr.data.CalendarEvent;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringWriter;


public class XMLConverter {
    public XMLConverter() {}

    public String serializeToXML(CalendarEvent calendarEvent) {
        String xmlContent = null;
        try
        {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(CalendarEvent.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Print XML String to Console
            StringWriter sw = new StringWriter();

            //Write XML to StringWriter
            jaxbMarshaller.marshal(calendarEvent, sw);

            //Verify XML Content
            xmlContent = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlContent;
    }

    public CalendarEvent parseToObject(String filePath) {
        File xmlFile = new File(filePath);
        CalendarEvent calendarEvent = null;
        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(CalendarEvent.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            calendarEvent = (CalendarEvent) jaxbUnmarshaller.unmarshal(xmlFile);

        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }

        return calendarEvent;
    }
}
