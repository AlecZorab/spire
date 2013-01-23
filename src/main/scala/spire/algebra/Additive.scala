package spire.algebra

import scala.{ specialized => spec }

trait AdditiveSemigroup[@spec(Int,Long,Float,Double) A] {
  def additive: Semigroup[A] = new Semigroup[A] {
    def op(x: A, y: A): A = plus(x, y)
  }

  def plus(x: A, y: A): A
}

trait AdditiveMonoid[@spec(Int,Long,Float,Double) A] extends AdditiveSemigroup[A] {
  override def additive: Monoid[A] = new Monoid[A] {
    def id = zero
    def op(x: A, y: A): A = plus(x, y)
  }

  def zero: A
}

trait AdditiveGroup[@spec(Int,Long,Float,Double) A] extends AdditiveMonoid[A] {
  override def additive: Group[A] = new Group[A] {
    def id = zero
    def op(x: A, y: A): A = plus(x, y)
    def inverse(x: A): A = negate(x)
  }

  def negate(x: A): A
  def minus(x: A, y: A): A = plus(x, negate(y))
}

trait AdditiveAbGroup[@spec(Int,Long,Float,Double) A] extends AdditiveGroup[A] {
  override def additive: AbGroup[A] = new AbGroup[A] {
    def id = zero
    def op(x: A, y: A): A = plus(x, y)
    def inverse(x: A): A = negate(x)
  }
}

trait AdditiveMonoid0 {
  implicit def moduleIsAdditiveMonoid[V, @spec(Int, Long, Float, Double) R](implicit
    m: Module[V, R]): AdditiveMonoid[V] = m
}

object AdditiveMonoid extends AdditiveMonoid0 {
  implicit def ringIsAdditiveMonoid[@spec(Int, Long, Float, Double) A](implicit
    r: Ring[A]): AdditiveMonoid[A] = r
}

import spire.math.{ConvertableTo, ConvertableFrom, Number}

final class AdditiveSemigroupOps[@spec(Int,Long,Float,Double) A](lhs:A)(implicit ev:AdditiveSemigroup[A]) {
  def +(rhs:A): A = ev.plus(lhs, rhs)
  def +(rhs:Int)(implicit c: Ring[A]): A = ev.plus(lhs, c.fromInt(rhs))
  def +(rhs:Double)(implicit c:ConvertableTo[A]): A = ev.plus(lhs, c.fromDouble(rhs))
  def +(rhs:Number)(implicit c:ConvertableFrom[A]): Number = c.toNumber(lhs) + rhs
}

final class AdditiveGroupOps[@spec(Int,Long,Float,Double) A](lhs:A)(implicit ev:AdditiveGroup[A]) {
  def unary_-() = ev.negate(lhs)
  def -(rhs:A): A = ev.minus(lhs, rhs)
  def -(rhs:Int)(implicit c: Ring[A]): A = ev.minus(lhs, c.fromInt(rhs))
  def -(rhs:Double)(implicit c:ConvertableTo[A]): A = ev.minus(lhs, c.fromDouble(rhs))
  def -(rhs:Number)(implicit c:ConvertableFrom[A]): Number = c.toNumber(lhs) - rhs
}
