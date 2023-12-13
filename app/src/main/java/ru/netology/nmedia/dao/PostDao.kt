package ru.netology.nmedia.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Query("SELECT MAX(id) FROM PostEntity")
    suspend fun getMaxId(): Long
    @Query("SELECT * FROM PostEntity WHERE id >= (:id - :count)")
    suspend fun getLatest(id:Long, count: Int): List<Post>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Query("SELECT COUNT(*) FROM PostEntity")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)
}