package models

final case class DuplicateKeyException(message: String) extends Exception(message)
final case class NotFoundException(message: String) extends Exception(message)
