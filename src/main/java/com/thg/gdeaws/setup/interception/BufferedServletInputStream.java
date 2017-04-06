package com.thg.gdeaws.setup.interception;

import java.io.ByteArrayInputStream;

import javax.servlet.ServletInputStream;

public class BufferedServletInputStream extends ServletInputStream {

	private ByteArrayInputStream bais;

	public BufferedServletInputStream(ByteArrayInputStream bais) {
		this.bais = bais;
	}

	@Override
	public int available() {
		return this.bais.available();
	}

	@Override
	public int read() {
		return this.bais.read();
	}

	@Override
	public int read(byte[] buf, int off, int len) {
		return this.bais.read(buf, off, len);
	}

}