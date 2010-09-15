/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scalacl



import org.bridj.StructObject

trait CLArgsProvider {
  def args: Seq[Any]
}

trait CLCol[T] extends CLEventBound with CLArgsProvider {
  protected type ThisCol[T] <: CLCol[T]

  @Deprecated
  def filter(f: CLFunction[T, Boolean]): CLCol[T]
  def filter(f: T => Boolean): CLCol[T]

  @Deprecated
  def map[V](f: CLFunction[T, V])(implicit v: ClassManifest[V]): CLCol[V]
  def map[V](f: T => V)(implicit v: ClassManifest[V]): CLCol[V]
  
  def toCLArray: CLArray[T]
  def toArray: Array[T]
  def toSeq = toArray.toSeq
  def toSet = toArray.toSet
  def toIndexedSeq = toArray.toIndexedSeq

  @Deprecated
  def toMap[K <: StructObject, V <: StructObject](implicit kvi: T =:= (K, V)) = error("Not implemented yet !")
  
  def view: CLView[T, ThisCol[T]]
  def slice(from: Long, to: Long): CLCol[T]
  def take(n: Long): CLCol[T]
  def drop(n: Long): CLCol[T]
  def zipWithIndex: CLCol[(T, Long)]
  def size: CLFuture[Long]
}

trait CLUpdatableCol[T] {
  this: CLCol[T] =>

  @Deprecated
  def update(f: CLFunction[T, T]): ThisCol[T]

  def update(f: T => T): ThisCol[T]
}
trait CLUpdatableFilteredCol[T] {
  this: CLCol[T] =>
  
  @Deprecated
  def refineFilter(f: CLFunction[T, Boolean]): ThisCol[T]

  def refineFilter(f: T => Boolean): ThisCol[T]
}