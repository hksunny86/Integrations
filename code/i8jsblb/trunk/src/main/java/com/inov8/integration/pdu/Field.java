/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.pdu;

import com.inov8.integration.enums.DataTypeEnum;

/**
 * 
 * <p>
 * <code>Field</code> class represent a single field of External System. It hold
 * a field's value, length and data type. Based on its data type it add padding
 * characters up to the length of field.
 * </p>
 * 
 */
public class Field {

	private String value;
	private Integer length;
	private DataTypeEnum type;

	public Field() {

	}

	public Field(String value, int length, DataTypeEnum type) {
		this.length = length;
		this.type = type;
		buildField(value, length, type);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (length != null && type != null) {
			buildField(value, length, type);
			return;
		}
		this.value = value;
	}

	public void setValue(String value, int length, DataTypeEnum type) {
		buildField(value, length, type);
	}

	/**
	 * <p>
	 * An utility method which takes care of building a field's appropriate
	 * value type, and padding
	 * </p>
	 * 
	 * @param value
	 * @param length
	 * @param type
	 */
	private void buildField(String value, int length, DataTypeEnum type) {
		if (value == null) {
			value = "";
		}
		if (value.length() > length) {
//			throw new IllegalArgumentException("Field Value exceeds field length.");
			return;
		}
		StringBuilder buffer = new StringBuilder();
		switch (type) {
		// A type padding char is space.
		case A: {
			buffer.append(value);
			for (int i = value.length(); i < length; i++) {
				buffer.append(" ");
			}
		}
			break;
		// N type padding char is 0.
		case N: {
			for (int i = value.length(); i < length; i++) {
				buffer.append("0");
			}
			buffer.append(value);
		}
			break;
		// AN type padding char is space.
		case AN: {
			buffer.append(value);
			for (int i = value.length(); i < length; i++) {
				buffer.append(" ");
			}
		}
			break;
		// ANS type padding char is space.
		case ANS: {
			int valueLength = buffer.toString().length();
			for (int i = valueLength; i < length; i++) {
				buffer.append(" ");
			}
			buffer.append(value);
		}
			break;
		// A type padding char is 0. It will have a prefix char, + or -
		case XN: {
			if (value.isEmpty()) {
				buffer.append("+");
			} else {
				char xChar = value.charAt(0);
				// if + or - already present, then ignore, but if value is 995
				// and have no prefix then add + char.
				if (xChar != '+' && xChar != '-') {
					buffer.append("+");
				}
			}

			int valueLength = buffer.toString().length() + value.length();
			for (int i = valueLength; i < length; i++) {
				buffer.append("0");
			}
			buffer.append(value);
		}
			break;
		// This will not be used in DC App, instead only in mock server.
		case XN_M: {
			if (value.isEmpty()) {
				buffer.append("-");
			} else {
				char xChar = value.charAt(0);
				if (xChar != '-') {
					buffer.append("-");
				}
			}

			int valueLength = buffer.toString().length() + value.length();
			for (int i = valueLength; i < length; i++) {
				buffer.append("0");
			}
			buffer.append(value);
		}
			break;
		default: {

		}
			break;
		}

		this.value = buffer.toString();
	}

	public byte[] getFieldBytes() {
		return this.value.getBytes();
	}

}
