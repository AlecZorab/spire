package spire.algebra

import scala.{ specialized => spec }

trait VectorSpace[V, @spec(Int, Long, Float, Double) F] extends Module[V, F] {
  implicit def scalar: Field[F]

  def divr(v: V, f: F): V = timesl(scalar.reciprocal(f), v)
}

trait VectorSpace0 {
  implicit def NormedVectorSpaceIsVectorSpace[V, @spec(Int, Long, Float, Double) F](implicit
    vectorSpace: NormedVectorSpace[V, F]): VectorSpace[V, F] = vectorSpace
}

object VectorSpace extends VectorSpace0

final class VectorSpaceOps[V, @spec(Int,Long,Float,Double) F](lhs: V)(implicit ev: VectorSpace[V, F]) {
  def :/ (rhs:F): V = ev.divr(lhs, rhs)
}
