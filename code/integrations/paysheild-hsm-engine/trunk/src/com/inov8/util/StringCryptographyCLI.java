package com.inov8.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

public class StringCryptographyCLI {

	private static String format = "%-20s%s%n";

	private static StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

	public static void main(String[] args) {
		// Set Cryptic Password
		encryptor.setPassword("682ede816988e58fb6d057d9d85605e0");

		System.out.println("Meezan String Encryptor/Decryptor");

		int chosenOption = -1;

		while (true) {
			try {
				System.out.println();
				System.out.println("Choose from following options.");
				System.out.println("1. String Encryption");
				System.out.println("2. String Decryption");
				System.out.println("3. Exit");
				System.out.println();

				String input = readLine("Enter valid Option: ");
				if (input != null && input.length() > 0) {
					try {
						Integer selectedOption = Integer.valueOf(input);
						chosenOption = selectedOption.intValue();
						switch (chosenOption) {
						case 1:
							encStriing();
							break;
						case 2:
							decStriing();
							break;
						case 3:
							exit();
							break;
						default:
							System.out.println("Invalid option.");
							break;
						}
					} catch (NumberFormatException nfe) {
						System.out.println("Invalid option selected.");
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void encStriing() {

		try {
			String input = readLine("Please enter value to encrypt: ");
			System.out.println();
			if (input != null) {
				if (input.equalsIgnoreCase("q")) {
					System.out.print("Exiting Encryptor....");
					exit();
				} else {
					encStriing(encryptor, input);
					System.out.println();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void decStriing() {
		try {
			String input = readLine("Please enter value to decrypt: ");
			System.out.println();
			if (input != null) {
				if (input.equalsIgnoreCase("q")) {
					System.out.print("Exiting Decryptor....");
					exit();
				} else {
					decStriing(encryptor, input);
					System.out.println();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void exit() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		}).start();
	}

	private static String readLine(String format, Object... args) throws IOException {
		if (System.console() != null) {
			return System.console().readLine(format, args);
		}
		System.out.print(String.format(format, args));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader.readLine();
	}

	public static void encStriing(StandardPBEStringEncryptor encryptor, String value) {

		System.out.printf(format, "Input Value:", value);
		String encValue = encryptor.encrypt(value);
		System.out.printf(format, "Encrypted Value:", encValue);

	}

	public static void decStriing(StandardPBEStringEncryptor encryptor, String value) {
		String decValue = "";
		System.out.printf(format, "Input Value:", value);
		try {
			decValue = encryptor.decrypt(value);
		} catch (Exception ex) {
			if (ex instanceof EncryptionOperationNotPossibleException) {
				System.out.printf(format, "Decrypted Value:", "ERROR: Decryption can not be done on this value.");
				return;
			}
		}
		System.out.printf(format, "Decrypted Value:", decValue);

	}

}
