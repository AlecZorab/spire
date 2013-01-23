package spire.algebra

import scala.{specialized => spec}

import scala.{ specialized => spec }

/**
 * A group is a monoid where each element has an inverse.
 */
trait Group[@spec(Int,Long,Float,Double) A] extends Monoid[A] {
  def inverse(a: A): A
}

object Group extends Group0 {
  @inline final def apply[A](implicit ev: Group[A]) = ev
}

/**
 * An abelian group is a group whose operation is commutative.
 */
trait AbGroup[@spec(Int,Long,Float,Double) A] extends Group[A]

object AbGroup {
  @inline final def apply[A](implicit ev: AbGroup[A]) = ev
}

final class GroupOps[@spec(Int,Long,Float,Double) A](lhs:A)(implicit ev:Group[A]) {
  def inverse() = ev.inverse(lhs)
}

trait Group0 {
  implicit def abGroup[A: AbGroup]: Group[A] = AbGroup[A]
}
