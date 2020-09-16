package com.thienbinh.apartmentsearch.db.dao

import androidx.room.*

@Dao
interface IBaseDAO<T> {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(obj: T)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(vararg obj: T)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  suspend fun update(obj: T)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  suspend fun update(vararg obj: T)

  @Delete
  suspend fun delete(obj: T)
}