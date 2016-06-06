package by.telecom;

import java.util.List;

public interface Parser {
	public List<User> read(String filePath);

	public void write(String filePath, List<User> userList);
}
