package pl.krolka.jsf.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Scanner;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class AdminController implements Serializable {

	private static final long serialVersionUID = 1L;

	private int maxFileSizeInMB;
	private String extentionName;
	private int timeToRemoveInMin;
	private String pathToFile;

	public int getMaxFileSizeInMB() {
		return maxFileSizeInMB = Integer.parseInt(readDateFromFile(1));
	}

	public void setMaxFileSizeInMB(int maxFileSizeInMB) {
		this.maxFileSizeInMB = maxFileSizeInMB;
	}

	public String getExtentionName() {
		return extentionName = readDateFromFile(2);
	}

	public void setExtentionName(String extentionName) {
		this.extentionName = extentionName;
	}

	public int getTimeToRemoveInMin() {
		return timeToRemoveInMin = Integer.parseInt(readDateFromFile(3));
	}

	public void setTimeToRemoveInMin(int timeToRemoveInMin) {
		this.timeToRemoveInMin = timeToRemoveInMin;
	}

	public String getPathToFile() {
		return pathToFile = readDateFromFile(4);
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	public AdminController() {

	}

	public String readDateFromFile(int lineNuber) {

		File propertiesFile = new File(
				"C:\\Users\\Admin\\eclipse-workspace\\WeTransferClone\\src\\main\\resources\\properties.txt");
		Scanner sc;
		int i = 0;
		String result = new String();

		try {
			sc = new Scanner(propertiesFile);
			while (sc.hasNextLine()) {
				i++;
				if (i == lineNuber) {
					result = sc.next();
				} else {
					sc.nextLine();
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return result;

	}

	public void writeDataToFile() {
		File fout = new File(
				"C:\\Users\\Admin\\eclipse-workspace\\WeTransferClone\\src\\main\\resources\\properties.txt");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fout);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for (int i = 0; i < 3; i++) {

				bw.write(maxFileSizeInMB + "");
				bw.newLine();
				bw.write(extentionName);
				bw.newLine();
				bw.write(timeToRemoveInMin + "");
				bw.newLine();
				bw.write(pathToFile);

				bw.newLine();
				bw.newLine();
				bw.newLine();
				bw.newLine();
				bw.newLine();
				bw.newLine();
				bw.newLine();
				bw.newLine();
				bw.newLine();

				bw.write("	*****************************************\r\n"
						+ "	|                                       |\r\n"
						+ "	|           PROPERTIES FILE             |\r\n"
						+ "	|              NOT EDIT                 |\r\n"
						+ "	|                                       |\r\n"
						+ "	|     line 1 : Max file size in MB      |\r\n"
						+ "	|     line 2 : extension of file        |\r\n"
						+ "	|     line 3 : time to remove in min.   |\r\n"
						+ "	|     line 3 : path to file             |\r\n"
						+ "	|                                       |\r\n"
						+ "	|                                       |\r\n"
						+ "	|    WARNING!!! NOT USE WHITE SPACE     |\r\n"
						+ "	|                                       |\r\n"
						+ "	*****************************************");
				bw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
