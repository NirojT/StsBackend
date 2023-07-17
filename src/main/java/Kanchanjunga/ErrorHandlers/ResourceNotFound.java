package Kanchanjunga.ErrorHandlers;

import java.util.UUID;

public class ResourceNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	String resourceName;
	String fieldName;
	UUID fieldValue;

	public ResourceNotFound(String resourceName, String fieldName, UUID fieldValue) {
		super(String.format("%s not found with %s :%s", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

}
