package com.inov8.ivr.task.input;

import io.task.exception.BaseException;
import io.task.util.StringUtil;
import io.task.util.TaskConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asteriskjava.fastagi.AgiException;

import com.inov8.ivr.script.JavaAgiScript;
import com.inov8.util.IvrConstant;
import com.inov8.util.IvrUtil;

/**
 * <pre>
 * 
 * @author Mobasher
 * 
 * </pre>
 */
public final class MultiCharInputTask extends InputTask
{
	private static String logClass = MultiCharInputTask.class.getName();

	private ArrayList<String>						promptSoundFiles;
	private String										noResponseSoundFile;
	private String										invalidLengthSoundFile;
	private String										invalidOptionSoundFile;
	private String										unknownErrorSoundFile;
	private HashMap<Character, String>	exitOnFirstMap;

	private String										exitPattern;
	private String										acceptablePattern;

	private HashMap<Integer, Integer>			validLengthMap;
	private ArrayList<Integer>						validLengthList;
	private int											firstKeyTimeout;
	private int											interKeyTimeout;
	private int											lastKeyTimeout;

	private String										inputBufferKey;
	private boolean									isMenu;
	private String							defaultNextTask, validLengthNextTask, noResponseNextTask;
	private int											noResponse;
	private String soundFileBasePath = "/var/lib/asterisk/sounds/";
	private String langId = "ur";
	private String decimalSoundFilePath ="numeric/decimal";
	private Map<String, NumberParser> numberParserMap;
	public static final String DEFAULT_NUMBER_PARSER = "defaultNumberParser";


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 *	Constructor
	 *
	 * </pre>
	 * 
	 * @throws AgiException
	 */

	public MultiCharInputTask()
	{
		exitOnFirstMap = new HashMap<Character, String>();
		exitPattern = "#";
		validLengthMap = new HashMap<Integer, Integer>();
		isMenu = false;
		validLengthList = new ArrayList<Integer>();
		promptSoundFiles = new ArrayList<String>(5);
		totalAttempts = 3;
		firstKeyTimeout = 100;
		interKeyTimeout = 5000;
		lastKeyTimeout = 2000;
		inputBufferKey = "inputBufferKey";
		noResponse = 3;
		noResponseSoundFile = "";
		invalidLengthSoundFile = "";
		invalidOptionSoundFile = "";
		unknownErrorSoundFile = "";
		acceptablePattern = "0123456789*#";
		defaultNextTask = "";
		numberParserMap = new HashMap<String, NumberParser>();
		numberParserMap.put(DEFAULT_NUMBER_PARSER, new NumberParserImpl());
	}


	public void setDefaultNextTask(String defaultNextTask)
	{
		this.defaultNextTask = defaultNextTask;
	}


	public void setValidLengthNextTask(String validLengthNextTask)
	{
		this.validLengthNextTask = validLengthNextTask;
	}


	public void setNoResponseNextTask(String noResponseNextTask)
	{
		this.noResponseNextTask = noResponseNextTask;
	}


	public void setNoResponse(int noResponse)
	{
		this.noResponse = noResponse;
	}


	private void setIsMenu(boolean isMenu)
	{
		this.isMenu = isMenu;
	}


	public void setInputBufferKey(String inputBufferKey)
	{
		this.inputBufferKey = inputBufferKey;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param promptSoundFiles - the promptSoundFiles to set
	 * </pre>
	 */
	public void setPromptSoundFiles(int index, String promptSoundFile)
	{
		while (index > this.promptSoundFiles.size()) {
			this.promptSoundFiles.add("");
		}
		this.promptSoundFiles.add(index, promptSoundFile);
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param noResponseSoundFile - the noResponseSoundFile to set
	 * </pre>
	 */
	public void setNoResponseSoundFile(String noResponseSoundFile)
	{
		if (noResponseSoundFile == null || noResponseSoundFile.equalsIgnoreCase("null")) {
			noResponseSoundFile = "";
		}
		this.noResponseSoundFile = noResponseSoundFile;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param invalidLengthSoundFile - the invalidLengthSoundFile to set
	 * </pre>
	 */
	public void setInvalidLengthSoundFile(String invalidLengthSoundFile)
	{
		if (invalidLengthSoundFile == null || invalidLengthSoundFile.equalsIgnoreCase("null")) {
			invalidLengthSoundFile = "";
		}
		this.invalidLengthSoundFile = invalidLengthSoundFile;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param invalidOptionSoundFile - the invalidOptionSoundFile to set
	 * </pre>
	 */
	public void setInvalidOptionSoundFile(String invalidOptionSoundFile)
	{
		if (invalidOptionSoundFile == null || invalidOptionSoundFile.equalsIgnoreCase("null")) {
			invalidOptionSoundFile = "";
		}
		this.invalidOptionSoundFile = invalidOptionSoundFile;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param unknownErrorSoundFile - the unknownErrorSoundFile to set
	 * </pre>
	 */
	public void setUnknownErrorSoundFile(String unknownErrorSoundFile)
	{
		if (unknownErrorSoundFile == null || unknownErrorSoundFile.equalsIgnoreCase("null")) {
			unknownErrorSoundFile = "";
		}
		this.unknownErrorSoundFile = unknownErrorSoundFile;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param exitOnFirstPattern - the exitOnFirstPattern to set
	 * </pre>
	 */
	public void setExitOnFirst(char exitOnFirst, String nextTask)
	{
		this.exitOnFirstMap.put(exitOnFirst, nextTask);
		setIsMenu(true);
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param exitOnPattern - the exitOnPattern to set
	 * </pre>
	 */
	public void setExitPattern(String exitPattern)
	{
		this.exitPattern = exitPattern;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param acceptablePattern - the acceptablePattern to set
	 * </pre>
	 */
	public void setAcceptablePattern(String acceptablePattern)
	{
		if (acceptablePattern == null || acceptablePattern.equalsIgnoreCase("null")) {
			acceptablePattern = "0123456789*#";
		}
		this.acceptablePattern = acceptablePattern;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param validLengthMap - the validLengthMap to set
	 * </pre>
	 */
	public void setValidLengthMap(int validLength)
	{
		int i = 0;
//		int validLength = Integer.parseInt(strValidLength);
		for (; i < validLengthList.size(); i++) {
			if (validLengthList.get(i) > validLength) {
				break;
			}
		}
		validLengthList.add(i, validLength);
		this.validLengthMap.put(validLength, validLength);
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param firstKeyTimeout - the firstKeyTimeout to set
	 * </pre>
	 */
	public void setFirstKeyTimeout(int firstKeyTimeout)
	{
		this.firstKeyTimeout = firstKeyTimeout;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param interKeyTimeout - the interKeyTimeout to set
	 * </pre>
	 */
	public void setInterKeyTimeout(int interKeyTimeout)
	{
		this.interKeyTimeout = interKeyTimeout;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @param lastKeyTimeout - the lastKeyTimeout to set
	 * </pre>
	 */
	public void setLastKeyTimeout(int lastKeyTimeout)
	{
		this.lastKeyTimeout = lastKeyTimeout;
	}


	/**
	 * <pre>
	 * 
	 * @author - Mobasher 
	 *
	 * @throws AgiException
	 */
	@Override
	public void process(Map<String, Object> dataMap)
	{
		JavaAgiScript parentAgi = null;
		
		String nextTask = defaultNextTask;
		StringBuilder inputBuffer = new StringBuilder();

		try {
			parentAgi = (JavaAgiScript) dataMap.get(IvrConstant.JAVA_AGI_SCRIPT);
			int noResponseCount = 0;
			char cKey = '\0';
			boolean isFirstChar = true;
			boolean exit = false;
			String [] soundFiles = promptSoundFiles.toArray(new String[0]);
			int currentAttempt = 0;

			dataMap.remove(IvrConstant.USER_INPUT);
			langId = (String) (dataMap.containsKey(IvrConstant.LANG_ID) ? dataMap.get(IvrConstant.LANG_ID) : langId);

			while (true) {
				cKey = '\0';
				if (isFirstChar) {
					if (nextTask == null || currentAttempt++ >= this.totalAttempts || exit) {
						if (nextTask == null) {
							nextTask = defaultNextTask;
						}
						logger.debug(StringUtil.mergeStrings(logClass, ": User entered input: ", inputBufferKey.toString(), ", for ", IvrUtil.getWsParamForLogging(dataMap)));
						dataMap.put(IvrConstant.USER_INPUT, inputBuffer.toString());
						break;
					}
					inputBuffer.delete(0, inputBuffer.length());
					cKey = playSoundFileList(dataMap, parentAgi, soundFiles);
					isFirstChar = false;
				}
				if (cKey == '\0') {
					cKey = waitForInputWithoutPlaying(parentAgi/*, blank*/, inputBuffer);
				}
				if (cKey == '\0') {
					isFirstChar = true;
					if (inputBuffer.length() != 0) {
						if (validLengthMap.isEmpty() || validLengthMap.get(inputBuffer.length()) != null) {
							nextTask = validLengthNextTask;
							exit = true;
						} else {
							playValidLengthMap(parentAgi);
						}
					} else {
						if (++noResponseCount == this.noResponse) {
							if (noResponseNextTask != null) {
								nextTask = noResponseNextTask;
								exit = true;
							}
							if (noResponseSoundFile.isEmpty() == false) {
								parentAgi.streamFile(noResponseSoundFile);
							}
						}
					}
				} else {
					if (inputBuffer.length() == 0 && ((nextTask = exitOnFirstMap.get(cKey)) != null) || this.isMenu == true) {
						if (nextTask == null) {
							if (invalidOptionSoundFile.isEmpty() == false) {
								parentAgi.streamFile(invalidOptionSoundFile);
							}
						} else {
							inputBuffer.append(cKey);
							exit = true;
							isFirstChar = true;
						}
					} else if (exitPattern.indexOf(cKey) != -1) {
						if (validLengthMap.isEmpty() == false && validLengthMap.get(inputBuffer.length()) == null) {
							playValidLengthMap(parentAgi);
						} else if(inputBuffer.length() > 0)
						{
							nextTask = validLengthNextTask;
							exit = true;
						}
						isFirstChar = true;
					} else {
						inputBuffer.append(cKey);
						if (validLengthList.isEmpty() == false && inputBuffer.length() > validLengthList.get(validLengthList.size() - 1)) {
							playValidLengthMap(parentAgi);
							isFirstChar = true;
						}
					}
				}
			}
		} catch (Exception e) {
			nextTask = "";
			inputBuffer.delete(0, inputBuffer.length());
			if (unknownErrorSoundFile.isEmpty() == false) {
				try {
					parentAgi.streamFile(unknownErrorSoundFile);
				} catch (Exception ee) {
				}
			}
			throw new BaseException(e);
		} finally {
			logger.info("Exiting input task: ",IvrUtil.getWsParamForLogging(dataMap));
		}
		dataMap.put(TaskConstant.NEXT_TASK_ID, nextTask);
	}


	public char playSoundFileList(Map<String, Object> dataMap, JavaAgiScript parentAgi, String[] soundFiles) throws AgiException
	{
		char cKey = '\0';

//		parentAgi.speechCreate();

		for (String sound : soundFiles) {
			String path = sound;
			if (sound.isEmpty() == false) {
				if (sound.startsWith("{") && sound.endsWith("}")) {
					sound = path = sound.substring(1, sound.length() - 1);
					int start = (path.charAt(0) == '#' || path.charAt(0) == '$') ? 1 : 0;
					path = (String) dataMap.get(path.substring(start));
				}
				if (sound.charAt(0) == '#') {
					cKey = sayDigits(parentAgi, path);
				} else if (sound.charAt(0) == '$') {
					cKey = sayDecimal(parentAgi, path); //sayNumber(parentAgi, path);
				} else {
					if(sound.charAt(0) == '*')
					{
						cKey = parentAgi.getOption(getSoundFilePath(sound.substring(1)), acceptablePattern, firstKeyTimeout);
//						parentAgi.speechRecognize(getSoundFilePath(sound.substring(1)), firstKeyTimeout);
					}
					else
					{
						cKey = parentAgi.getOption(getSoundFilePathWithLang(sound), acceptablePattern, firstKeyTimeout);
//						parentAgi.speechRecognize(getSoundFilePathWithLang(sound), firstKeyTimeout);
					}
				}
				if (cKey != '\0') {
					break;
				}
			}
		}

//		parentAgi.speechDestroy();
		return cKey;
	}


	private char waitForInputWithoutPlaying(JavaAgiScript parentAgi/*, String blank*/, StringBuilder inputBuffer) throws AgiException
	{
		char cKey = '\0';
		if (validLengthMap.isEmpty() || validLengthMap.get(inputBuffer.length()) == null) {
//			cKey = parentAgi.waitForDigit(interKeyTimeout);
			cKey = parentAgi.getOption(getSoundFilePath("common/blank"), acceptablePattern, interKeyTimeout);
		} else {
//			cKey = parentAgi.waitForDigit(lastKeyTimeout);
			cKey = parentAgi.getOption(getSoundFilePath("common/blank"), acceptablePattern, lastKeyTimeout);
		}
		return cKey;
	}
	
	private void playValidLengthMap(JavaAgiScript parentAgi) throws AgiException
	{
		try {
			if (invalidLengthSoundFile.isEmpty()) {
				if (validLengthList.isEmpty() == false) {
					parentAgi.streamFile("/var/lib/asterisk/sounds/IVRU/English/9000");
					for (int j = 0;; j++) {
						//						commonUtil.sayNumber(agiManager, validLengthList.get(j) + "", false, null);
						parentAgi.streamFile(getNumericSoundFilePathWithLang(validLengthList.get(j)+""));
						if ((j + 1) < validLengthList.size()) {
							parentAgi.streamFile("/var/lib/asterisk/sounds/IVRU/English/4000");//plays "or"
						} else {
							break;
						}
					}
					parentAgi.streamFile("/var/lib/asterisk/sounds/IVRU/English/9001");
				}
			} else {
				parentAgi.streamFile(invalidLengthSoundFile);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	
	public char sayDecimal(JavaAgiScript parentAgi, String number) throws BaseException
	{
		char ch = '\0';
		
		try
		{
			String []amount = number.split("\\.");
			
			List<String> list = getNumberParser(langId).parseNumber(amount[0]);
			
			for(String soundFile : list) {
				ch = parentAgi.getOption(getNumericSoundFilePathWithLang(soundFile), acceptablePattern, firstKeyTimeout);
				if(ch != '\0') break;
			}
			
//			ch = sayNumber(parentAgi, amount[0]);
			
			if(ch == '\0' && amount.length == 2 && amount[1].matches("[0]*") == false && amount[1].length() > 0)
			{
				ch = parentAgi.getOption(getSoundFilePathWithLang(decimalSoundFilePath), acceptablePattern, firstKeyTimeout);
				sayDigits(parentAgi, amount[1]);
				
	//			ch = parentAgi.getOption(getSoundFilePathWithLang(soundFile), acceptablePattern, firstKeyTimeout);
			}
		}
		catch(AgiException e)
		{
			logger.error("", e);
			throw new BaseException(e);
		}
		
		return ch;
	}
	

	public char sayNumber(JavaAgiScript parentAgi, String number)
	{
		char ch = '\0';
		try {
			int len = number.length();
			if(len > 0) {
				String units[] = {"", "", "100", "1000", "1000", "100", "million", "million", "hundred"};
				int pLen = len, idx = 0, ctrl = 0, sub = 0;
				for(; idx < len && ch == '\0'; pLen -= sub, idx += sub) {
					sub = pLen % 3;
					if(sub == 0) {
						sub = 1;
						if(number.charAt(idx) != '0') {
							ctrl = 1;
						}
					}
					if(number.charAt(idx) != '0' || number.charAt(idx + sub - 1) != '0') {
						ch = parentAgi.getOption(getNumericSoundFilePathWithLang(StringUtil.trimFromStart(number.substring(idx, idx + sub), '0')), acceptablePattern, firstKeyTimeout);
						if(ch == '\0' && units[pLen - 1].length() > 0) {
							ch = parentAgi.getOption(getNumericSoundFilePathWithLang(units[pLen - 1]), acceptablePattern, firstKeyTimeout);
						}
					} else if(ctrl == 1 && pLen > 3 && ch == '\0') {
						ctrl = 0;
						ch = parentAgi.getOption(getNumericSoundFilePathWithLang(units[pLen - 1]), acceptablePattern, firstKeyTimeout);
					}
				}
			}
			
		} catch (Exception ex) {
			logger.error("", ex);
		}
		return ch;
	}

	/*public char sayNumber(JavaAgiScript parentAgi, String number)
	{
		char ch = '\0';
		try {
			if(number.length() > 0) {
//number=number.substring(0,number.indexOf('.'));
				List<String> sayNum = new ArrayList<String>();
				String units[] = {"", "100", "1000", "100", "million", "hundred"};
				int idx = number.length(), ctrl = 1, sub = 2, unitsIdx = 0;
				for(; idx > 0; unitsIdx++) {
					idx -= sub;
					if(idx < 0) {
						sub += idx;
						idx = 0;						
					}
					if(number.charAt(idx) != '0' || number.charAt(idx + sub - 1) != '0') {
						if(units[unitsIdx].length() > 0) {
							sayNum.add(units[unitsIdx]);
						}
						sayNum.add(number.substring(idx, idx + sub));
					} else {
						if(idx > 0 && number.charAt(idx - 1) != '0' && unitsIdx > 1 && sub == 2) {
							sayNum.add(units[unitsIdx]);
						}
					}
					ctrl = (ctrl + 1) % 2;
					sub = ctrl + 1;
				}

				for(int i = sayNum.size() - 1; i > -1 && ch == '\0'; i--) {
					String path = getNumericSoundFilePathWithLang(sayNum.get(i));
					ch = parentAgi.getOption(path, acceptablePattern, firstKeyTimeout);
				}
			}
			
		} catch (Exception ex) {
		}
		return ch;
	}*/



	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 * 
	 * @param dataMap
	 * @param parentAgi
	 * @param substring - 
	 * </pre>
	 */
	private char sayDigits(JavaAgiScript parentAgi, String number)
	{
		char ch = '\0';
		try {
			for(int i=0;i<number.length() && ch == '\0' ;i++)
			{
				ch = parentAgi.getOption(getNumericSoundFilePathWithLang(number.substring(i, i+1)), acceptablePattern, firstKeyTimeout);
			}
		} catch (Exception ex) {
			logger.error("", ex);
		}
		return ch;
		
	}


	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @return - Returns the defaultSoundFilePath
	 * </pre>
	 */
	public String getDefaultSoundFilePath()
	{
		return soundFileBasePath;
	}


	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @param defaultSoundFilePath - the defaultSoundFilePath to set
	 * </pre>
	 */
	public void setSoundFileBasePath(String soundFileBasePath)
	{
		this.soundFileBasePath = soundFileBasePath;
	}
	
	private SoundFile _getSoundFilePath(String soundFile, Map<String, Object> dataMap)
	{
		SoundFile file = new SoundFile();
		boolean hasLang = true;

		if(soundFile.startsWith("/") == false) {
			if(soundFile.startsWith("*")) {
//				soundFile = soundFileBasePath + soundFile.substring(1);
				hasLang = false;
			}
			if(soundFile.startsWith("{") && soundFile.endsWith("}")) {
				soundFile = soundFile.substring(1, soundFile.length() - 1);
				if(soundFile.startsWith("#")) {
					file.isDigit = true;
				}
				else if(soundFile.startsWith("$")) {
					file.isNumber = true;
				}
				soundFile = soundFile.substring(1);
				soundFile = (String) dataMap.get(soundFile);
			}
		} else {
			file.filePath = soundFile;
		}
		return file;
	}
	
	private static class SoundFile {
		String filePath;
		boolean isDigit;
		boolean isNumber;
	}
	
	private String getSoundFilePath(String soundFile) {
		if(soundFile.startsWith("/") == false) {
			soundFile = soundFileBasePath + soundFile;
		}
		
		return soundFile;
	}
	
	private String getSoundFilePathWithLang(String soundFile) {
		if(soundFile.startsWith("/") == false) {
			soundFile = soundFileBasePath + langId + "/" + soundFile;
		}
		logger.debug(soundFile);
		return soundFile;		
	}


	private String getNumericSoundFilePathWithLang(String soundFile) {
		if(soundFile.startsWith("/") == false) {
			soundFile = soundFileBasePath + langId + "/numeric/" + soundFile;
		}
		
		return soundFile;		
	}

	
	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @return - Returns the langId
	 * </pre>
	 */
	public String getLangId()
	{
		return langId;
	}


	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @param langId - the langId to set
	 * </pre>
	 */
	public void setLangId(String langId)
	{
		this.langId = langId;
	}
	
	public void setNumberParser(String langId, NumberParser numberParser) {
		numberParserMap.put(langId, numberParser);
	}
	
	private NumberParser getNumberParser(String langId) {
		NumberParser numberParser = numberParserMap.get(langId);
		
		if(numberParser == null) {
			numberParser = numberParserMap.get(DEFAULT_NUMBER_PARSER);
		}

		return numberParser;
	}
}
