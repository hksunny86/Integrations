package com.inov8.ivr.thread.config;


import io.task.context.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IvrConfigLoaderThread implements Runnable
{
	private static final Logger logger = LoggerFactory.getLogger(IvrConfigLoaderThread.class);
	private int reloadAfter = 3600;
	private int retryAfter = 10;
	private boolean isLoaded = false;
	private Context context;

	public IvrConfigLoaderThread(Context context)
	{
		this.context = context;
	}

	public boolean isLoaded()
	{
		return isLoaded;
	}

	public void setReloadAfter(int reloadAfter)
	{
		if(reloadAfter > 0) {
			this.reloadAfter = reloadAfter;
		}
	}


	public void setRetryAfter(int retryAfter)
	{
		if(retryAfter > 0) {
			this.retryAfter = retryAfter;
		}
	}

	@Override
	public void run()
	{
		if(logger.isInfoEnabled() == true) {
			logger.info("Started");
		}
		startupLoad();
		reload();
		if(logger.isInfoEnabled() == true) {
			logger.info("Stopped");
		}
	}


	private void startupLoad()
	{
		if(logger.isInfoEnabled() == true) {
			logger.info("Start");
		}
		int sleep = retryAfter * 1000;
		while(true) {
			try
			{
				context.start();
				isLoaded = true;
					synchronized(this) {
						notifyAll();
					}
					break;
			}
			catch(Exception ex)
			{
				logger.error("", ex);
				logger.warn("Unable to load config. Retrying after seconds: {}", retryAfter);
//			} else {
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						logger.warn("",e);
					}
			}
//			}
		}
		if(logger.isInfoEnabled() == true) {
			logger.info("End");
		}
	}


	private void reload()
	{
		if(logger.isInfoEnabled() == true) {
			logger.info("Start");
		}
		int sleep = reloadAfter * 1000;
		while(true) {
			try {
				if(logger.isInfoEnabled() == true) {
					logger.info("Going to sleep for {} seconds ", reloadAfter);
				}
				Thread.sleep(sleep);

				context.start();

				if(logger.isInfoEnabled() == true) {
					logger.info("Config reloading from DB successful");
				}
			} catch (InterruptedException e) {
				logger.warn("",e);
			} catch(Exception e) {
				logger.error("",e);
			}
		}
	}


	private void start(IvrConfigLoaderThread ivrConfigLoaderThread)
	{		
		if(logger.isInfoEnabled() == true) {
			logger.info("Start");
		}

		Thread thread = new Thread(ivrConfigLoaderThread);
		thread.setDaemon(true);
		thread.setName(IvrConfigLoaderThread.class.getName());
		thread.start();
		if(logger.isInfoEnabled() == true) {
			logger.info(thread.getName() + " started");
			logger.info("End");
		}
	}

	public void start()
	{
		if(logger.isInfoEnabled() == true) {
			logger.info("Started");
		}
		start(this);
		if(logger.isInfoEnabled() == true) {
			logger.info("End");
		}
	}

	public void startAndWait()
	{
		if(logger.isInfoEnabled() == true) {
			logger.info("Started");
		}
		start(this);
		synchronized(this) {
			while(this.isLoaded() == false) {
				try {
					if(logger.isInfoEnabled() == true) {
						logger.info("Waiting for the config to load for the first time");
					}
					this.wait(retryAfter * 1000);
					if(logger.isInfoEnabled() == true) {
						logger.info("Wait for the config to load for the first time over");
					}
				} catch (InterruptedException e) {
					logger.warn("",e);
				}
			}
		}
		if(logger.isInfoEnabled() == true) {
			logger.info("End");
		}
	}

}
