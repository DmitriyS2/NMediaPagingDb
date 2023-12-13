package ru.netology.nmedia.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.error.ApiError
import javax.inject.Inject

class PostPagingSource @Inject constructor(
  //  private val service: ApiService,
    private val postDao: PostDao
) : PagingSource<Long, Post>() {
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? {
        return null
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        try {
            val response = when (params) {
                is LoadParams.Refresh -> postDao.getLatest(postDao.getMaxId(), params.loadSize)
           //     is LoadParams.Refresh -> service.getLatest(params.loadSize)
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(),
                    prevKey = params.key,
                    nextKey = null
                )
                is LoadParams.Append -> postDao.getLatest(params.key, params.loadSize)
             //   is LoadParams.Append -> service.getBefore(params.key, params.loadSize)
            }

//            if (!response.isSuccessful) {
//                throw ApiError(response.code(), response.message())
//            }
//            val body = response.body() ?: throw ApiError(
//                response.code(),
//                response.message(),
//            )

            val nextKey = if (response.isEmpty()) null else response.last()?.id
            return LoadResult.Page(
                data = response,
                prevKey = params.key,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}