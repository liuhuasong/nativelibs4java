/*
	Copyright (c) 2009 Olivier Chafik (http://ochafik.free.fr/)
	
	This file is part of OpenCL4Java (http://code.google.com/p/nativelibs4java/wiki/OpenCL).
	
	OpenCL4Java is free software: you can redistribute it and/or modify
	it under the terms of the GNU Lesser General Public License as published by
	the Free Software Foundation, either version 2.1 of the License, or
	(at your option) any later version.
	
	OpenCL4Java is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Lesser General Public License for more details.
	
	You should have received a copy of the GNU Lesser General Public License
	along with OpenCL4Java.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.nativelibs4java.opencl;
import static com.nativelibs4java.opencl.CLException.error;
import static com.nativelibs4java.opencl.JavaCL.CL;
import static com.nativelibs4java.opencl.library.OpenCLLibrary.CL_FALSE;
import static com.nativelibs4java.opencl.library.OpenCLLibrary.CL_QUEUE_PROPERTIES;
import static com.nativelibs4java.opencl.library.OpenCLLibrary.CL_TRUE;

import java.util.EnumSet;

import com.nativelibs4java.opencl.library.OpenCLLibrary.cl_command_queue;
import com.nativelibs4java.opencl.library.OpenCLLibrary.cl_event;
import com.nativelibs4java.opencl.library.OpenCLLibrary.cl_mem;
import com.ochafik.lang.jnaerator.runtime.NativeSize;
import com.ochafik.lang.jnaerator.runtime.NativeSizeByReference;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

/**
 * OpenCL command queue.<br/>
 * OpenCL objects such as memory, program and kernel objects are created using a context. <br/>
 * Operations on these objects are performed using a command-queue. <br/>
 * The command-queue can be used to queue a set of operations (referred to as commands) in order. <br/>
 * Having multiple command-queues allows applications to queue multiple independent commands without requiring synchronization. <br/>
 * Note that this should work as long as these objects are not being shared. <br/>
 * Sharing of objects across multiple command-queues will require the application to perform appropriate synchronization.<br/>
 * <br/>
 * A queue is bound to a single device.
 * @see CLDevice#createQueue(com.nativelibs4java.opencl.CLContext, com.nativelibs4java.opencl.CLDevice.QueueProperties[]) 
 * @see CLDevice#createOutOfOrderQueue(com.nativelibs4java.opencl.CLContext)
 * @see CLDevice#createProfilingQueue(com.nativelibs4java.opencl.CLContext)
 * @see CLContext#createDefaultQueue(com.nativelibs4java.opencl.CLDevice.QueueProperties[])
 * @see CLContext#createDefaultOutOfOrderQueue()
 * @see CLContext#createDefaultProfilingQueue()
 * @author Olivier Chafik
 *
 */
public class CLQueue extends CLAbstractEntity<cl_command_queue> {

    private CLInfoGetter<cl_command_queue> infos = new CLInfoGetter<cl_command_queue>() {
		@Override
		protected int getInfo(cl_command_queue entity, int infoTypeEnum, NativeSize size, Pointer out, NativeSizeByReference sizeOut) {
			return CL.clGetCommandQueueInfo(getEntity(), infoTypeEnum, size, out, sizeOut);
		}
	};

	final CLContext context;
	final CLDevice device;

    CLQueue(CLContext context, cl_command_queue entity, CLDevice device) {
        super(entity);
        this.context = context;
		this.device = device;
    }

    public CLContext getContext() {
        return context;
    }
	public CLDevice getDevice() {
		return device;
	}

	@InfoName("CL_QUEUE_PROPERTIES")
	public EnumSet<CLDevice.QueueProperties> getProperties() {
		return CLDevice.QueueProperties.getEnumSet(infos.getIntOrLong(getEntity(), CL_QUEUE_PROPERTIES));
	}

	@SuppressWarnings("deprecation")
	public void setProperty(CLDevice.QueueProperties property, boolean enabled) {
		error(CL.clSetCommandQueueProperty(getEntity(), property.value(), enabled ? CL_TRUE : CL_FALSE, (LongByReference)null));
	}
	

    @Override
    protected void clear() {
        error(CL.clReleaseCommandQueue(getEntity()));
    }

    /**
	 * Blocks until all previously queued OpenCL commands in this queue are issued to the associated device and have completed. <br/>
	 * finish() does not return until all queued commands in this queue have been processed and completed. <br/>
	 * finish() is also a synchronization point.
	 */
    public void finish() {
        error(CL.clFinish(getEntity()));
    }

    /**
	 * Issues all previously queued OpenCL commands in this queue to the device associated with this queue. <br/>
	 * flush() only guarantees that all queued commands in this queue get issued to the appropriate device. <br/>
	 * There is no guarantee that they will be complete after flush() returns.
	 */
    public void flush() {
        error(CL.clFlush(getEntity()));
    }

	/**
	 * Enqueues a wait for a specific event or a list of events to complete before any future commands queued in the this queue are executed.
	 */
	public void enqueueWaitForEvents(CLEvent... events) {
        cl_event[] evts = CLEvent.to_cl_event_array(events);
        error(CL.clEnqueueWaitForEvents(getEntity(), evts == null ? 0 : evts.length, evts));
	}

	/**
	 * Enqueue a barrier operation.<br/>
	 * The enqueueBarrier() command ensures that all queued commands in command_queue have finished execution before the next batch of commands can begin execution. <br/>
	 * enqueueBarrier() is a synchronization point.
	 */
	public void enqueueBarrier() {
		error(CL.clEnqueueBarrier(getEntity()));
	}

	/**
	 * Enqueue a marker command to command_queue. <br/>
	 * The marker command returns an event which can be used by to queue a wait on this marker event i.e. wait for all commands queued before the marker command to complete.
	 * @return Event object that identifies this command and can be used to query or queue a wait for the command to complete.
	 */
	public CLEvent enqueueMarker() {
		cl_event[] eventOut = new cl_event[1];
		error(CL.clEnqueueMarker(getEntity(), eventOut));
		return CLEvent.createEvent(this, eventOut);
	}

	/**
	 * Used to acquire OpenCL memory objects that have been created from OpenGL objects. <br>
	 * These objects need to be acquired before they can be used by any OpenCL commands queued to a command-queue. <br>
	 * The OpenGL objects are acquired by the OpenCL context associated with this queue and can therefore be used by all command-queues associated with the OpenCL context.
	 * @param objects CL memory objects that correspond to GL objects.
	 * @param eventsToWaitFor Events that need to complete before this particular command can be executed.
	 * @return Event object that identifies this command and can be used to query or queue a wait for the command to complete.
	 */
	public CLEvent enqueueAcquireGLObjects(CLMem[] objects, CLEvent... eventsToWaitFor) {
        cl_event[] eventOut = CLEvent.new_event_out(eventsToWaitFor);
		cl_mem[] mems = new cl_mem[objects.length];
		for (int i = 0; i < objects.length; i++)
			mems[i] = objects[i].getEntity();
		cl_event[] evts = CLEvent.to_cl_event_array(eventsToWaitFor);
        error(CL.clEnqueueAcquireGLObjects(getEntity(), mems.length, mems, evts == null ? 0 : evts.length, evts, eventOut));
		return CLEvent.createEvent(this, eventOut);
	}

	/**
	 * Used to release OpenCL memory objects that have been created from OpenGL objects. <br>
	 * These objects need to be released before they can be used by OpenGL. <br>
	 * The OpenGL objects are released by the OpenCL context associated with this queue.
	 * @param objects CL memory objects that correpond to GL objects.
	 * @param eventsToWaitFor Events that need to complete before this particular command can be executed.
	 * @return Event object that identifies this command and can be used to query or queue a wait for the command to complete.
	 */
	public CLEvent enqueueReleaseGLObjects(CLMem[] objects, CLEvent... eventsToWaitFor) {
        cl_event[] eventOut = CLEvent.new_event_out(eventsToWaitFor);
		cl_mem[] mems = getEntities(objects, new cl_mem[objects.length]);
		cl_event[] evts = CLEvent.to_cl_event_array(eventsToWaitFor);
        error(CL.clEnqueueReleaseGLObjects(getEntity(), mems.length, mems, evts == null ? 0 : evts.length, evts, eventOut));
		return CLEvent.createEvent(this, eventOut);
	}
}