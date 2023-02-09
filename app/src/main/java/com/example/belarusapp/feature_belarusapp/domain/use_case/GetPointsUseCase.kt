package com.example.belarusapp.feature_belarusapp.domain.use_case

import com.example.belarusapp.feature_belarusapp.data.model.toCityDetail
import com.example.belarusapp.feature_belarusapp.domain.model.CityDetail
import com.example.belarusapp.feature_belarusapp.domain.repository.BoardGamesInfoRepository
import com.example.belarusapp.util.InputException
import com.example.belarusapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPointsUseCase @Inject constructor(
    private val repository: BoardGamesInfoRepository
) {
    operator fun invoke(id: Int): Flow<Resource<List<CityDetail>>> = flow {
        try {
            emit(Resource.Loading(emptyList()))
            val games =
                repository.getPoints(id)
                    .filter { it.lang == 1 }
                    .filter { it.city_id == id }
                    .map { it.toCityDetail() }
            if (games.isEmpty()) throw InputException()
            else emit(
                Resource.Success(
                    games
                )
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(
                Resource.Error(
                    data = emptyList(), message = "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    data = emptyList(),
                    message = "Couldn't reach server. Check your internet connection."
                )
            )
        } catch (e: InputException) {
            emit(
                Resource.Error(
                    data = emptyList(), message = "No data"
                )
            )
        }
    }.flowOn(Dispatchers.IO)

}