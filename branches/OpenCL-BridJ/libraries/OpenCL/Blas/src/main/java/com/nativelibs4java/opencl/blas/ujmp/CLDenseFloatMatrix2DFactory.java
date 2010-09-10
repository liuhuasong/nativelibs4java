/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nativelibs4java.opencl.blas.ujmp;

import com.nativelibs4java.opencl.util.LinearAlgebraUtils;
import com.nativelibs4java.opencl.util.Primitive;
import org.ujmp.core.floatmatrix.DenseFloatMatrix2D;
import org.ujmp.core.floatmatrix.factory.AbstractFloatMatrix2DFactory;
import org.ujmp.core.exceptions.MatrixException;

/**
 *
 * @author ochafik
 */
public class CLDenseFloatMatrix2DFactory extends AbstractFloatMatrix2DFactory {
	public static volatile OpenCLUJMP OpenCLUJMP;

    static synchronized OpenCLUJMP getOpenCLUJMP() {
        if (OpenCLUJMP == null) {
            try {
                OpenCLUJMP = new OpenCLUJMP();
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }
        return OpenCLUJMP;
    }

	public DenseFloatMatrix2D dense(long rows, long columns)
			throws MatrixException {
		return new CLDenseFloatMatrix2D(getOpenCLUJMP(), rows, columns);
	}

}