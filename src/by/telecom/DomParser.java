package by.telecom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomParser implements Parser {
	private DocumentBuilder documentBuilder;

	public DomParser() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); // создали фабрику строителей, сложный и грамосткий процесс (по реже выполняйте это действие)
		// f.setValidating(false); // не делать проверку валидации
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();// создали конкретного строителя документа ;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> read(String fileName) {
		List<User> userList = new ArrayList<User>();
		try {
			File xmlFile = new File(fileName);
			Document document = documentBuilder.parse(xmlFile);
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					User user = new User();
					user.setId(Long.parseLong(element.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue()));
					user.setName(element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue());
					user.setAddress(element.getElementsByTagName("address").item(0).getChildNodes().item(0).getNodeValue());
					userList.add(user);
				}
			}

		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public void write(String filePath, List<User> userList) {
		Document document = documentBuilder.newDocument();
		Element rootNode = document.createElement("users");

		for (User user : userList) {
			Element userNode = document.createElement("user");
			rootNode.appendChild(userNode);
			Element idNode = document.createElement("id");
			idNode.appendChild(document.createTextNode(Long.toString(user.getId())));
			Element nameNode = document.createElement("name");
			nameNode.appendChild(document.createTextNode(user.getName()));
			Element addressNode = document.createElement("address");
			addressNode.appendChild(document.createTextNode(user.getAddress()));
			userNode.appendChild(idNode);
			userNode.appendChild(nameNode);
			userNode.appendChild(addressNode);
		}
		document.appendChild(rootNode);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		DOMSource source = new DOMSource(document);
		File file = new File(filePath);
		StreamResult result = new StreamResult(file);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}

}
