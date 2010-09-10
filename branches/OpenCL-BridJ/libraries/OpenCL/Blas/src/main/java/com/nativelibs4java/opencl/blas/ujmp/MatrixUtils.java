/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nativelibs4java.opencl.blas.ujmp;

import org.ujmp.core.floatmatrix.FloatMatrix2D;
import org.ujmp.core.doublematrix.DoubleMatrix2D;

import org.bridj.Pointer;
import static org.bridj.Pointer.*;

/**
 *
 * @author ochafik
 */
public class MatrixUtils {
    public static void write(double[] b, DoubleMatrix2D out) {
        write(pointerToDoubles(b), out);
    }
    public static void write(Pointer<Double> b, DoubleMatrix2D out) {
        long rows = out.getRowCount(), columns = out.getColumnCount();
        if (b.getRemainingElements() < rows * columns)
            throw new IllegalArgumentException("Not enough data in input buffer to write into " + rows + "x" + columns + " matrix (only has " + b.getRemainingElements() + ")");
        if (out instanceof CLDenseDoubleMatrix2D) {
            CLDenseDoubleMatrix2D mout = (CLDenseDoubleMatrix2D)out;
            mout.write(b);
        } else {
            for (long i = 0; i < rows; i++) {
            		long offset = i * columns;
                for (long j = 0; j < columns; j++)
                    out.setDouble(b.get(offset + j), i, j);
            }
        }
    }

    public static Pointer<Double> read(DoubleMatrix2D m) {
        Pointer<Double> buffer = allocateDoubles(m.getColumnCount() * m.getRowCount()).order(CLDenseDoubleMatrix2DFactory.getOpenCLUJMP().getContext().getKernelsDefaultByteOrder());
        read(m, buffer);
        return buffer;
    }
    public static void read(DoubleMatrix2D m, Pointer<Double> out) {
        long rows = m.getRowCount(), columns = m.getColumnCount();
        if (out.getRemainingElements() < rows * columns)
            throw new IllegalArgumentException("Not enough space in output buffer to read into " + rows + "x" + columns + " matrix (only has " + out.getRemainingElements() + ")");
        
        if (m instanceof CLDenseDoubleMatrix2D) {
            CLDenseDoubleMatrix2D mm = (CLDenseDoubleMatrix2D)m;
            mm.read(out);
        } else {
            for (long i = 0; i < rows; i++) {
            		long offset = i * columns;
            		for (long j = 0; j < columns; j++) {
                    out.set(offset + j, m.getDouble(i, j));
				}
			}
        }
    }

    public static Pointer<Float> read(FloatMatrix2D m) {
        Pointer<Float> buffer = allocateFloats(m.getColumnCount() * m.getRowCount()).order(CLDenseFloatMatrix2DFactory.getOpenCLUJMP().getContext().getKernelsDefaultByteOrder());
        read(m, buffer);
        return buffer;
    }
    public static void read(FloatMatrix2D m, Pointer<Float> out) {
        long rows = m.getRowCount(), columns = m.getColumnCount();
        if (out.getRemainingElements() < rows * columns)
            throw new IllegalArgumentException("Not enough space in output buffer to read into " + rows + "x" + columns + " matrix (only has " + out.getRemainingElements() + ")");

        if (m instanceof CLDenseFloatMatrix2D) {
            CLDenseFloatMatrix2D mm = (CLDenseFloatMatrix2D)m;
            mm.read(out);
        } else {
            for (long i = 0; i < rows; i++) {
            		long offset = i * columns;
            		for (long j = 0; j < columns; j++) {
                    out.set(offset + j, m.getFloat(i, j));
				}
			}
        }
    }
}
