package uk.co.massimocarli.sqlitetodo.db

interface CrudOwner<T> {
  fun getCrud(): Crud<T>
}

interface Crud<T> {

  fun insert(item: T): Long

  fun update(item: T): Int

  fun findById(id: Long): T?

  fun list(): List<T>

  fun deleteById(id: Long): Int
}