package dao.jaxb;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.ProductList;

public class JaxbUnMarshaller {

	public ProductList init() {
		// read from xml to java object
		ProductList products = null;
		try {
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			// System.out.println("unmarshalling...");
			products = (ProductList) unmarshaller.unmarshal(new File("jaxb" + File.separator + "inputInventory.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		if (products == null)
			System.out.println("Error unmarshalling");

		return products;
	}
}
