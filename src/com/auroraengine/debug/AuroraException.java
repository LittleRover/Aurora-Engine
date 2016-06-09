/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.debug;


/**
 *
 * @author Arthur
 */
public abstract class AuroraException extends Exception {
	@SuppressWarnings("compatibility:2804439186761060564")
	private static final long serialVersionUID = -3519565822693877718L;

	public AuroraException() {}
	public AuroraException(String msg) {
		super(msg);
	}
	public AuroraException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public AuroraException(String msg, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
	}
	public AuroraException(Throwable cause) {
		super(cause);
	}
}