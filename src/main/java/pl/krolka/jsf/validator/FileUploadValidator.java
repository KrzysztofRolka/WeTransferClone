package pl.krolka.jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

import pl.krolka.jsf.controller.*;

@FacesValidator(value = "fileUploadValidator")
public class FileUploadValidator implements Validator<Object> {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		AdminController adminController = new AdminController();
		
		int properSizeInMB = adminController.getMaxFileSizeInMB();
		String properExtention = adminController.getExtentionName();

		Part file = (Part) value;
		FacesMessage message = null;

		try {

			if (file == null || file.getSize() <= 0 || file.getContentType().isEmpty()) {
				message = new FacesMessage("Select a valid file");
			}

			else if (!file.getSubmittedFileName().endsWith(properExtention)) {
				message = new FacesMessage("Select " + properExtention + " file");
			}

			else if (file.getSize() > (1000000 * properSizeInMB))
				message = new FacesMessage(
						"File size too big. File size allowed  is less than or equal to " + properSizeInMB + " MB.");

			if (message != null && !message.getDetail().isEmpty()) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}

		} catch (Exception ex) {
			throw new ValidatorException(new FacesMessage(ex.getMessage()));
		}

	}

}
