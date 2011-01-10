package scalacl

package impl

import org.bridj.PointerIO
import org.bridj.SizeT

object PrefixSum {

  lazy val prefixSumCodeByte = new CLSimpleCode("""
    __kernel void prefSum(int size, __global const char* in, __global int* out) {
      int i = get_global_id(0);
      int j;
      if (i >= size)
        return;

      int total = 0;
      for (j = 0; j <= i; j++)
        total += in[j];

      out[i] = total;
    }
  """)
  lazy val prefixSumCodeInt = new CLSimpleCode("""
    __kernel void prefSum(int size, __global const int* in, __global int* out) {
      int i = get_global_id(0);
      int j;
      if (i >= size)
        return;

      int total = 0;
      for (j = 0; j <= i; j++)
        total += in[j];

      out[i] = total;
    }
  """)
  def prefixSumByte(bitmap: CLGuardedBuffer[Boolean], output: CLGuardedBuffer[Int])(implicit context: ScalaCLContext) = {
    val kernel = prefixSumCodeByte.getKernel(context)
    kernel.synchronized {
      kernel.setArgs(bitmap.size.toInt.asInstanceOf[Object], bitmap.buffer, output.buffer)
      CLEventBound.syncBlock(Array(bitmap), Array(output), evts => {
        kernel.enqueueNDRange(context.queue, Array(bitmap.size.toInt), evts:_*)
      })
    }
  }
  def prefixSumInt(bitmap: CLGuardedBuffer[Int], output: CLGuardedBuffer[Int])(implicit context: ScalaCLContext) = {
    /*try {
      CLEventBound.syncBlock(Array(bitmap), Array(output), evts => {
        GroupedPrefixSum[Int].prefixSum(bitmap.buffer, output.buffer, evts:_*)
        context.queue.finish // TODO
        null
      })
      println("Prefix sum bitmap = " + bitmap.toArray.toSeq)
      println("Prefix sum output = " + output.toArray.toSeq)
    } catch {
      case ex =>
        ex.printStackTrace*/
        val kernel = prefixSumCodeInt.getKernel(context)
        kernel.synchronized {
          kernel.setArgs(bitmap.size.toInt.asInstanceOf[Object], bitmap.buffer, output.buffer)
          CLEventBound.syncBlock(Array(bitmap), Array(output), evts => {
            kernel.enqueueNDRange(context.queue, Array(bitmap.size.toInt), evts:_*)
          })
        }
    //}
  }
  lazy val copyPrefixedCode = new CLSimpleCode("""
    __kernel void copyPrefixed(
        int size,
        __global const int* presencePrefix,
        __global const char* in,
        int elementSize,
        __global char* out
    ) {
      int i = get_global_id(0);
      if (i >= size)
        return;

      int prefix = presencePrefix[i];
      if (!i && prefix > 0 || i && prefix > presencePrefix[i - 1]) {
        int j, inOffset = i * elementSize, outOffset = (prefix - 1) * elementSize;
        for (j = 0; j < elementSize; j++) {
          out[outOffset + j] = in[inOffset + j];
        }
      }
    }
  """)
  def copyPrefixed[T](size: Int, presencePrefix: CLGuardedBuffer[Int], in: CLGuardedBuffer[T], out: CLGuardedBuffer[T])(implicit t: ClassManifest[T], context: ScalaCLContext) = {
    val kernel = copyPrefixedCode.getKernel(context)
    val pio = PointerIO.getInstance(t.erasure)
    assert(pio != null)
    kernel.synchronized {
      kernel.setArgs(in.size.toInt.asInstanceOf[Object], presencePrefix.buffer, in.buffer, pio.getTargetSize.toInt.asInstanceOf[Object], out.buffer)
      CLEventBound.syncBlock(Array(in, presencePrefix), Array(out), evts => {
        kernel.enqueueNDRange(context.queue, Array(in.size.toInt), evts:_*)
      })
    }
  }
}

