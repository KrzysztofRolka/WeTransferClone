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
	private List<String> listOfFIles;
	private String path = "C:\\Users\\Admin\\eclipse-workspace\\WeTransferClone\\src\\main\\resources\\Files\\";
	AdminController adminController;

	public List<String> getListOfFIles() {
		return listOfFIles;
	}

	public void setListOfFIles(List<String> listOfFIles) {
		this.listOfFIles = listOfFIles;
	}

	public String getPath() {
		adminController = new AdminController();
		return path = adminController.getPathToFile();
	}

	public void setPath(String path) {
		this.path = path;
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
		// Example test.zip -> 123233435_test.zip
		String extention = getFilename(file).substring(getFilename(file).length() - 4);
		Calendar calendar = Calendar.getInstance();
		String fileName = calendar.getTimeInMillis() + "_"
				+ getFilename(file).substring(0, getFilename(file).length() - 4) + extention;

		file.write(path + fileName);

		listOfFIles = loadFilesToArray();

		return "faces/success";

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

		checkDirExist();

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
					result.add(f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\") + 1));
				}
			}

		}

	}

	public void refresh() throws IOException {

		path = adminController.getPathToFile();
		checkDirExist();
		listOfFIles = loadFilesToArray();

		// Refresh page
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public void admin() throws IOException {

		listOfFIles = loadFilesToArray();

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect("sec/admin_page.xhtml");

	}

	public void checkDirExist() {
		File tmpDir = new File(path);
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
	}

	public void delete(String fileName) throws IOException {

		File fileToRemove = new File(path + fileName);
		fileToRemove.delete();

		refresh();
	}

	public void deleteAfterTime() throws IOException {

		System.out.println("Delete After Time");

		if (!listOfFIles.isEmpty()) {

			Calendar calendar = Calendar.getInstance();

			for (String file : listOfFIles) {
				long fileTime = Long.parseLong((file.substring(0, 13)));
				long currentTime = calendar.getTimeInMillis();

				if ((currentTime - fileTime) > (adminController.getTimeToRemoveInMin() * 60000)) {
					delete(file);

				}
			}
		}
	}
}
