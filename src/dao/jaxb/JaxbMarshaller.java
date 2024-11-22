package dao.jaxb;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.ProductList;

public class JaxbMarshaller {
	public boolean init(ProductList products) {
		boolean isExported = false;
		try {
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Marshaller marshaller = context.createMarshaller();
			// System.out.println("marshalling... ");
			Date myDate = new Date();
			String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(myDate);
			File file = new File("jaxb" + File.separator + "inventory_" + formatDate + ".xml");
			marshaller.marshal(products, file);
			isExported = true;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return isExported;
	}
}
