/*
 * Copyright (C) 2017 LittleRover
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.auroraengine.opengl;

import com.auroraengine.client.ClientCore;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.opengl.viewport.FullScreenViewport;
import com.auroraengine.opengl.viewport.Viewport;
import com.auroraengine.threading.SynchroCore;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The rendering core of the program. This should perform extremely limited
 * calculations and instead be solely focused on interacting with the GL Context
 * to boost performance. Note that the context has Keyboard and Mouse
 * interaction which may be ported to another thread in future.
 *
 * @author LittleRover
 */
public class GLCore extends SynchroCore {

	private static final Logger LOG = AuroraLogs.getLogger(GLCore.class.getName());

	/**
	 * The main constructor which prepares the core using the provided client. It
	 * is expected that interaction with other threads will happen through the
	 * ClientCore thread primarily.
	 *
	 * @param core
	 */
	public GLCore(ClientCore core) {
		super("GL Core", core);
		this.core = core;
		this.window = new LWJGLWindow(core.getSession(), core.getProperties());
		this.viewports = new ArrayList<>(1);

		Viewport full = new FullScreenViewport(window);
		full.setActive(true);
		viewports.add(full);
	}
	private final ClientCore core;
	private final ArrayList<Viewport> viewports;
	private final GLWindow window;

	@Override
	protected void initialise()
					throws GLException {
		LOG.info("Initialising");
		window.create();
		LOG.info("Initialised");
	}

	@Override
	protected boolean isRunning()
					throws GLException {
		return !window.isCloseRequested();
	}

	@Override
	protected void processException(AuroraException ex) {
		LOG.log(Level.SEVERE, "Fatal Exception: {0}", ex);
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
	protected void update()
					throws GLException {
		// Required to reset the view
		window.update();

		// Then each viewport will be drawn to
		// TODO: Implement priority - e.g. texture viewports
		viewports.stream().filter((v) -> v.isActive()).forEach((v) -> {
			try {
				v.render();
			} catch (GLException ex) {
				LOG.log(Level.WARNING, "Failed to render the viewport: {0}", v
								.toString());
			}
		});
	}
}
