package spire.benchmark

import scala.annotation.tailrec
import scala.{specialized => spec}
import scala.util.Random
import Random._

import spire.algebra._
import spire.math._
import spire.implicits._
import fpf._

import com.google.caliper.Runner 
import com.google.caliper.SimpleBenchmark
import com.google.caliper.Param

import java.lang.Math
import java.math.BigInteger

/**
 * Extend this to create an actual benchmarking class.
 */
trait MyBenchmark extends SimpleBenchmark {

  /**
   * Sugar for building arrays using a per-cell init function.
   */
  def init[A:Manifest](size:Int)(f: => A) = {
    val data = Array.ofDim[A](size)
    for (i <- 0 until size) data(i) = f
    data
  }

  /**
   * Sugar for building arrays using a per-cell init function.
   */
  def mkarray[A:Manifest:Order](size:Int, layout:String)(f: => A): Array[A] = {
    val data = init(size)(f)
    val ct = implicitly[Manifest[A]]
    val order = Order[A]
    layout match {
      case "random" =>
      case "sorted" => spire.math.Sorting.sort(data)(order, ct)
      case "reversed" => spire.math.Sorting.sort(data)(order.reverse, ct)
      case _ => sys.error("unknown layout: %s" format layout)
    }
    data
  }

  def nextComplex = Complex(nextDouble, nextDouble)

  /**
   * Sugar to run 'f' for 'reps' number of times.
   */
  def run(reps:Int)(f: => Unit) = for(i <- 0 until reps)(f)
}

/**
 * Extend this to create a main object which will run 'cls' (a benchmark).
 */
abstract class MyRunner(val cls:java.lang.Class[_ <: com.google.caliper.Benchmark]) {
  def main(args:Array[String]): Unit = Runner.main(cls, args:_*)
}

trait BenchmarkData extends MyBenchmark {
  //val size = 10 * 1000
  //val size = 100 * 1000
  val size = 200 * 1000
  //val size = 1 * 1000 * 1000
  //val size = 4 * 1000 * 1000
  //val size = 20 * 1000 * 1000

  lazy val ints = init(size)(nextInt)
  lazy val longs = init(size)(nextLong)
  lazy val floats = init(size)(nextFloat)
  lazy val doubles = init(size)(nextDouble)
  lazy val maybeDoubles = init(size)(MaybeDouble(nextDouble))
  lazy val maybeFloats = init(size)(FastMaybeFloat(nextFloat))

  lazy val complexes = init(size)(nextComplex)
  lazy val fcomplexes = init(size)(FastComplex(nextFloat(), nextFloat()))
}
