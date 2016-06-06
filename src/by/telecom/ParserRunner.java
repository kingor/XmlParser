package by.telecom;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ParserRunner {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException {
		Parser domParser = new DomParser();
		List<User> userList = domParser.read("D:/users.xml");
		System.out.println(userList);
		domParser.write("d:/user2.xml", userList);
	}

}
