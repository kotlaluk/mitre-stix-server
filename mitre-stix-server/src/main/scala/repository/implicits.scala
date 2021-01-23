package org.example.mitrestixserver
package repository

object implicits {

  implicit class ExtendOptionOps[+A](wrapped: Option[A]) {
    def toEither[E](error: => E): Either[E, A] =
      wrapped.fold[Either[E, A]](Left(error))(Right(_))
  }

  implicit class ExtendEitherOps[+E, +A](wrapped: Either[E, A]) {
    def invert: Either[A, E] = wrapped match {
      case Right(a) => Left(a)
      case Left(e) => Right(e)
    }
  }

}
