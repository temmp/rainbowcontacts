/*
 * Copyright (c) 2008-2009, Motorola, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - Neither the name of the Motorola, Inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package javax.obex;

import android.bluetooth.BluetoothSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.obex.ObexTransport;

public class BluetoothPbapRfcommTransport implements ObexTransport {
    private BluetoothSocket mSocket = null;

    public BluetoothPbapRfcommTransport(BluetoothSocket rfs) {
        super();
        this.mSocket = rfs;
    }

    @Override
	public void close() throws IOException {
        mSocket.close();
    }

    @Override
	public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    @Override
	public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    @Override
	public InputStream openInputStream() throws IOException {
        return mSocket.getInputStream();
    }

    @Override
	public OutputStream openOutputStream() throws IOException {
        return mSocket.getOutputStream();
    }

    @Override
	public void connect() throws IOException {
    }

    @Override
	public void create() throws IOException {
    }

    @Override
	public void disconnect() throws IOException {
    }

    @Override
	public void listen() throws IOException {
    }

    public boolean isConnected() throws IOException {
        return true;
    }

}
