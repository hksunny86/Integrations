package com.inov8.integration.pdu;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <code>BasePDU</code> Base/Parent Packet Data Unit for all message type that
 * interact from DC to External System.
 * 
 */
public abstract class BasePDU {
	// This list will hold all the field of specific request, header + body
	// fields.
	protected List<Field> fields = new ArrayList<Field>();
	// This byte array will have total bytes of specific pdu, bytes of header +
	// body
	protected byte[] pduBytes = null;

	/**
	 * <p>
	 * A convenient method which will extract bytes from fields list, by
	 * traversing the list. It will append the bytes into the byte array into
	 * pduBytes array.
	 * </p>
	 * 
	 * <p>
	 * Note: This method is mandatory to call right after you populated any
	 * request pdu & before sending to external system. If this is not called,
	 * and pdu sent, bytes may be in wrong position.
	 * </p>
	 */
	public void assemblePDU() {

		int pduLength = 0;
		for (Field field : fields) {
			int fieldLength = field.getFieldBytes().length;
			pduLength += fieldLength;
		}

		pduBytes = null;
		addLengthBytes(pduLength);
		for (Field field : fields) {
			pduBytes = appendBytes(field.getFieldBytes(), pduBytes);
		}
	}

	public byte[] getBytes(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

	public byte[] getBytes(byte value) {
		byte[] byteArray = new byte[1];
		byteArray[0] = value;
		return byteArray;
	}

	/**
	 * A method to append bytes into array.
	 * 
	 * @param bytes
	 * @param appendToBytes
	 * @return
	 */
	public byte[] appendBytes(byte[] bytes, byte[] appendToBytes) {
		if (appendToBytes == null) {
			appendToBytes = new byte[0];
		}
		int length = bytes.length;
		byte[] newBuffer = new byte[appendToBytes.length + length];
		System.arraycopy(appendToBytes, 0, newBuffer, 0, appendToBytes.length);
		System.arraycopy(bytes, 0, newBuffer, appendToBytes.length, length);
		appendToBytes = newBuffer;
		return appendToBytes;
	}

	/**
	 * <p>
	 * This is specifically according to phoenix protocol which set the request
	 * packet size into the header.
	 * </p>
	 * 
	 * @param pduLength
	 */
	public void addLengthBytes(int pduLength) {
		// Adding first two bytes as length
		byte[] lengthArray = new byte[2];
		int firstByte = pduLength / 256;
		int secondByte = pduLength % 256;
		lengthArray[0] = (byte) firstByte;
		lengthArray[1] = (byte) secondByte;
		pduBytes = appendBytes(lengthArray, pduBytes);
	}

	/**
	 * Just a rough method to print PDU's total length in bytes.
	 */
	public void printPduLength() {
	}

	/**
	 * An utility method which 
	 * @param bytes
	 * @return
	 */
	protected String hexDump(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(Character.forDigit((b >> 4) & 0x0f, 16));
			builder.append(Character.forDigit(b & 0x0f, 16));
		}
		return builder.toString();
	}

	public byte[] getPduBytes() {
		return pduBytes;
	}

}
