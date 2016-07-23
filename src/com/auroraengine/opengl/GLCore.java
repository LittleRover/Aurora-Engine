/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl;

import com.auroraengine.client.ClientCore;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.threading.SynchroCore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The rendering core of the program. This should perform extremely limited
 * calculations and instead be solely focused on interacting with the GL Context
 * to boost performance. Note that the context has Keyboard and Mouse
 * interaction which may be ported to another thread in future.
 *
 * @author Arthur
 */
public class GLCore extends SynchroCore {

	private static final Logger LOG = AuroraLogs.getLogger(GLCore.class);

	/**
	 * The main constructor which prepares the core using the provided client.
	 * It is expected that interaction with other threads will happen through the
	 * ClientCore thread primarily.
	 * @param core 
	 */
	public GLCore(ClientCore core) {
		super("GL Core", core);
		this.core = core;
		this.window = new LWJGLWindow(core.getSession(), core.getProperties());
	}
	private final ClientCore core;
	private final GLWindow window;

	@Override
	protected void initialise() throws GLException {
		LOG.info("Initialising");
		window.create();
		LOG.info("Initialised");
	}

	@Override
	protected boolean isRunning() throws GLException {
		return !window.isCloseRequested();
	}

	@Override
	protected void update() throws GLException {
		window.update();
	}

	@Override
	protected void shutdown() {
		LOG.info("Shutting Down");
		if (window != null) {
			window.destroy();
		}
		LOG.info("Shut Down");
	}

	@Override
	protected void processException(AuroraException ex) {
		LOG.log(Level.SEVERE, "Fatal Exception: {0}", ex);
	}
}
