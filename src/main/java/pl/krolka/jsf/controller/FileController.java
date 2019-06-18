package pl.krolka.jsf.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

@Named
@SessionScoped
public class FileController implements Serializable {

	private static final long serialVersionUID = 1L;
	private Part file;	 	
	private static String path = "C:\\Users\\Admin\\eclipse-workspace\\WeTransferClone\\src\\main\\resources\\Files\\";
	private List<String> listOfFIles;

	public List<String> getListOfFIles() {
		return listOfFIles;
	}

	public void setListOfFIles(List<String> listOfFIles) {
		this.listOfFIles = listOfFIles;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		FileController.path = path; 
		
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public FileController() {

	}

	@PostConstruct
	public void onSetup() {
		listOfFIles = loadFilesToArray();
	}

	public String upload() throws IOException {

		// Add TimeInMilis to file name.
		// Example test.zip -> test_123233435.zip
		String extention = getFilename(file).substring(getFilename(file).length() - 4);
		Calendar calendar = Calendar.getInstance();
		String fileName = getFilename(file).substring(0, getFilename(file).length() - 4) + "_"
				+ calendar.getTimeInMillis() + extention;

		file.write(path + fileName);

		listOfFIles = loadFilesToArray();

		return "success";
	}

	
	private static String getFilename(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
				return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE
																													// fix.
			}
		}
		return null;
	}

	public List<String> loadFilesToArray() {

		File folder = new File(path);

		List<String> result = new ArrayList<>();

		search(".*\\.*", folder, result);

		return result;

	}

	public static void search(final String pattern, final File folder, List<String> result) {
		for (final File f : folder.listFiles()) {

			if (f.isDirectory()) {
				search(pattern, f, result);
			}

			if (f.isFile()) {
				if (f.getName().matches(pattern)) {
					result.add(f.getAbsolutePath());
				}
			}

		}

	}

	public void refresh() throws IOException {
		
		listOfFIles = loadFilesToArray();
		
		// Refresh page
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public String admin() {
		
		listOfFIles = loadFilesToArray();
		return "admin_page";
	}
}
